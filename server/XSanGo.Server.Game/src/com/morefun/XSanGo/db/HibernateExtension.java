/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.IPredicate;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 自定义Hibernate扩展，提供无状态检测机制的数据覆盖操作
 * 
 * @author BruceSu
 * 
 */
public class HibernateExtension {
	private static Map<Class, EntityMeta> reflactionMap = new ConcurrentHashMap<Class, EntityMeta>();

	public static EntityMeta getEntityMeta(Class<?> c) {
		if (!reflactionMap.containsKey(c)) {
			reflactionMap.put(c, new EntityMeta(c));
		}

		return reflactionMap.get(c);
	}

	// public static EmbedMeta getEmbedMeta(Class<?> c) {
	// if (!embedMetaMap.containsKey(c)) {
	// embedMetaMap.put(c, new EmbedMeta(c));
	// }
	//
	// return embedMetaMap.get(c);
	// }

	/**
	 * 获取父对象主键值
	 * 
	 * @param method
	 *            获取父对象方法
	 * @param instance
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static Object getParentKey(Method m, Object instance) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Object parent = m.invoke(instance);
		if (parent == null) {
			throw new IllegalStateException(TextUtil.format("Could not get the forign key for the class {0}",
					instance.getClass()));
		}

		return getPrimaryKey(parent);
	}

	private static Object getPrimaryKey(Object instance) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		EntityMeta meta = getEntityMeta(instance.getClass());
		if (meta == null) {
			LogManager.warn(instance.getClass().toString());
			return null;
		}

		if (meta.isGetPrimaryKeyFromParent()) {
			return getParentKey(meta.getParentMethod(), instance);
		} else {
			return meta.getIdGetMethod().invoke(instance);
		}
	}

	private static void insert(Session session, final Object instance) throws Exception {
		final EntityMeta reflaction = getEntityMeta(instance.getClass());

		Map<String, Object> map = new HashMap<String, Object>();// 插入数据库中的列和值
		if (!reflaction.isInsertExcludePrimaryKey()) {// 是否需要插入主键
			Object value = null;
			if (reflaction.isGetPrimaryKeyFromParent()) {
				value = getParentKey(reflaction.getParentMethod(), instance);
			} else {
				value = reflaction.getIdGetMethod().invoke(instance);
			}

			map.put(reflaction.getPrimaryColumnName(), value);
		}

		// 设置外键
		for (String col : reflaction.getJoinColumnMap().keySet()) {
			map.put(col, getParentKey(reflaction.getJoinColumnMap().get(col), instance));
		}

		// 设置普通列
		for (String col : reflaction.getNomalColumnMap().keySet()) {
			map.put(col, reflaction.getNomalColumnMap().get(col).invoke(instance));
		}

		List<String> colList = new ArrayList<String>();
		final List<Object> valueList = new ArrayList<Object>();
		List<String> paramList = new ArrayList<String>();
		for (String col : map.keySet()) {
			colList.add(col);
			paramList.add("?");
			valueList.add(map.get(col));
		}
		final String sql = TextUtil.format("insert into {0} ({1}) values ({2})", reflaction.getTableName(),
				TextUtil.join(colList, ","), TextUtil.join(paramList, ","));

		// 如果插入时候不需要主键，则需要在插入完成后获取并赋值到对象上
		if (reflaction.isInsertExcludePrimaryKey()) {
			// 从数据库生成主键
			session.doWork(new Work() {
				@Override
				public void execute(Connection con) throws SQLException {
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try {
						pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
						int index = 1;
						for (Object p : valueList) {
							pstmt.setObject(index, p);
							index++;
						}

						pstmt.executeUpdate();
						rs = pstmt.getGeneratedKeys();
						if (rs.next()) {
							String idSetName = "set" + reflaction.getIdGetMethod().getName().substring(3);

							reflaction.getSetMethodMap().get(idSetName).invoke(instance, rs.getInt(1));

						}
					} catch (Exception e) {
						LogManager.error(e);
					} finally {
						if (rs != null) {
							rs.close();
						}
						if (pstmt != null) {
							pstmt.close();
						}
					}
				}
			});
		} else {
			executeSQL(session, sql, valueList.toArray());
		}

		// 级联
		for (Method m : reflaction.getOneToOneChildren()) {
			Object child = m.invoke(instance);
			if (child != null) {
				insert(session, child);
			}
		}

		for (Method m : reflaction.getOneToManyChildren()) {
			Object children = m.invoke(instance);

			// 目前只有Set,Map，Set默认实现了Collection接口
			if (children instanceof Map) {
				children = ((Map) children).values();
			}

			Collection collection = (Collection) children;
			for (Object item : collection) {
				insert(session, item);
			}
		}
	}

	/**
	 * 比较新老实体，以及级联对象，并把新实体状态用session更新至数据库 使用本方法，需要满足以下前提 1、更新操作仅针对普通列，主外键列不应被修改
	 * ; 2、一对多关系接口声明除MAP接口外，必须支持Collection接口;3、一个对象必须最多只能有一个父对象
	 * 
	 * @param session
	 * @param old
	 * @param instance
	 * @throws Exception
	 */
	public static <T> void update(Session session, T old, T instance) throws Exception {
		if (old == null && instance == null) {
			throw new IllegalArgumentException();
		}

		if (old == null) {
			insert(session, instance);
			return;
		}
		if (instance == null) {
			delete(session, old);
			return;
		}

		EntityMeta reflaction = getEntityMeta(instance.getClass());
		List<String> colList = new ArrayList<String>();
		List<Object> valueList = new ArrayList<Object>();
		// 只扫描普通列，主外键列不变更
		for (String col : reflaction.getNomalColumnMap().keySet()) {
			Method m = reflaction.getNomalColumnMap().get(col);
			Object oldVal = m.invoke(old);
			Object newVal = m.invoke(instance);
			// 判断是否需要更新
			if ((oldVal == null && newVal != null) || (oldVal != null && newVal == null)
					|| (oldVal != null && !oldVal.equals(newVal)) || (newVal != null && !newVal.equals(oldVal))
					|| m.getReturnType().equals(byte[].class)) {
				colList.add(col + " = ?");
				valueList.add(newVal);
			}
		}
		// 最后的条件语句，添加主键作为值
		valueList.add(getPrimaryKey(instance));

		if (colList.size() > 0) {
			String sql = TextUtil.format("update {0} set {1} where {2} = ?", reflaction.getTableName(),
					TextUtil.join(colList, ","), reflaction.getPrimaryColumnName());
			executeSQL(session, sql, valueList.toArray());
		}

		// 级联更新一对一子对象，这里用的递归
		for (Method m : reflaction.getOneToOneChildren()) {
			Object oldChild = m.invoke(old);
			Object newChild = m.invoke(instance);
			if (oldChild == null) {
				if (newChild != null) {
					insert(session, newChild);
				}
			} else {
				if (newChild != null) {
					update(session, oldChild, newChild);
				} else {
					delete(session, oldChild);
				}
			}
		}

		// 级联更新一对多子对象，这里用的递归
		for (Method m : reflaction.getOneToManyChildren()) {
			Object oldChildren = m.invoke(old);
			Object newChildren = m.invoke(instance);
			if (newChildren == null) {
				throw new IllegalStateException(m.toGenericString() + "return null value.");
			}

			// 目前只有Set,Map，Set默认实现了Collection接口
			if (oldChildren instanceof Map) {
				oldChildren = ((Map) oldChildren).values();
				newChildren = ((Map) newChildren).values();
			}

			Collection oldCollection = (Collection) oldChildren;
			Collection newCollection = (Collection) newChildren;
			// 处理更新和增加的数据
			for (Object item : newCollection) {
				// final EntityMeta childInfo =
				// getReflactionInfo(item.getClass());
				final Object primaryKey = getPrimaryKey(item);
				Object oldItem = CollectionUtil.first(oldCollection, new IPredicate() {
					@Override
					public boolean check(Object i) {
						try {
							return getPrimaryKey(i).equals(primaryKey);
						} catch (Exception e) {
							LogManager.error(e);
							return false;
						}
					}
				});

				if (oldItem != null) {
					// update
					update(session, oldItem, item);
				} else {
					// insert
					insert(session, item);
				}
			}

			// 处理删除的数据
			for (Object object : oldCollection) {
				// final EntityMeta childInfo = getReflactionInfo(object
				// .getClass());
				final Object primaryKey = getPrimaryKey(object);
				Object newItem = CollectionUtil.first(newCollection, new IPredicate() {
					@Override
					public boolean check(Object i) {
						try {
							return getPrimaryKey(i).equals(primaryKey);
						} catch (Exception e) {
							LogManager.error(e);
							return false;
						}
					}
				});

				if (newItem == null) {
					// delete
					delete(session, object);
				}
			}
		}

	}

	/**
	 * 级联删除，先删除子对象，最后才删除自己
	 * 
	 * @param session
	 * @param object
	 * @throws Exception
	 */
	private static void delete(Session session, Object object) throws Exception {
		EntityMeta reflaction = getEntityMeta(object.getClass());
		for (Method m : reflaction.getOneToOneChildren()) {
			Object child = m.invoke(object);
			if (child != null) {
				delete(session, child);
			}
		}

		for (Method m : reflaction.getOneToManyChildren()) {
			// delete from tb where fk = ?
			Object children = m.invoke(object);
			// 目前只有Set,Map，Set默认实现了Collection接口
			if (children instanceof Map) {
				children = ((Map) children).values();
			}

			for (Object item : (Collection) children) {
				// 由于可能存在删除对象中存在级联对象，因此不能直接根据外键删除，而必须逐条删除
				delete(session, item);
				// EntityReflactor childInfo =
				// getReflactionInfo(item.getClass());
				// for (String col : childInfo.joinColumnMap.keySet()) {
				// if (childInfo.joinColumnMap.get(col).getReturnType()
				// .equals(object.getClass())) {
				// String sql = TextUtil.format(
				// "delete from {0} where {1} = ?",
				// childInfo.tableName, col);
				// executeSQL(session, sql,
				// reflaction.getPrimaryKey(object));
				// break;
				// }
				// }
				// // 只需执行一次
				// break;
			}
		}

		String sql = TextUtil.format("delete from {0} where {1} = ?", reflaction.getTableName(),
				reflaction.getPrimaryColumnName());
		executeSQL(session, sql, getPrimaryKey(object));
	}

	private static void executeSQL(Session session, String sql, Object... params) throws Exception {
		SQLQuery query = session.createSQLQuery(sql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}

		try {
			query.executeUpdate();
		} catch (Exception e) {
			LogManager.warn("Error sql:" + sql + ";Params:" + TextUtil.GSON.toJson(params));
			// 这里必须再次抛出，否则忽略了异常会有坏档风险
			throw e;
		}
	}
}
