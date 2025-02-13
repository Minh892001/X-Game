/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.db;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.morefun.XSanGo.util.TextUtil;

/**
 * ORM元数据信息描述类
 * 
 * @author BruceSu
 * 
 */
public class EntityMeta {

	/** 表名 */
	private String tableName;
	/** 外键列 */
	private Map<String, Method> joinColumnMap;
	/** 普通列，不含主键 */
	private Map<String, Method> nomalColumnMap;
	/** 一对一子表 */
	private List<Method> oneToOneChildren;
	/** 一对多子表 */
	private List<Method> oneToManyChildren;
	/** 主键列 */
	private String primaryColumnName;
	/** 插入数据时是否排除主键 */
	private boolean insertExcludePrimaryKey;
	/** 是否从引用父亲对象获取主键 */
	private boolean getPrimaryKeyFromParent;
	/** 主键获取方法 */
	private Method idGetMethod;
	/** 设置方法 */
	private Map<String, Method> setMethodMap;
	/** 父对象获取方法 */
	private Method parentMethod;

	public EntityMeta(Class c) {
		Table table = (Table) c.getAnnotation(Table.class);
		if (table == null) {
			throw new IllegalStateException(TextUtil.format("{0} is not a table mapping.", c));
		}
		this.tableName = table.name();
		this.joinColumnMap = new HashMap<String, Method>();
		this.nomalColumnMap = new HashMap<String, Method>();
		this.oneToManyChildren = new ArrayList<Method>();
		this.oneToOneChildren = new ArrayList<Method>();
		this.setMethodMap = new HashMap<String, Method>();

		Method[] allMethods = c.getDeclaredMethods();
		for (Method method : allMethods) {
			method.setAccessible(true);
			if (method.getName().substring(0, 3).equalsIgnoreCase("set")) {
				this.setMethodMap.put(method.getName(), method);
				continue;
			}

			Id idCol = method.getAnnotation(Id.class);
			JoinColumn joinCol = method.getAnnotation(JoinColumn.class);
			Column col = method.getAnnotation(Column.class);
			PrimaryKeyJoinColumn pkjCol = method.getAnnotation(PrimaryKeyJoinColumn.class);
			OneToOne oto = method.getAnnotation(OneToOne.class);
			OneToMany otm = method.getAnnotation(OneToMany.class);

			if (idCol != null) {
				this.primaryColumnName = col.name();
				this.idGetMethod = method;

				// native foreign
				GenericGenerator gg = method.getAnnotation(GenericGenerator.class);
				GeneratedValue gv = method.getAnnotation(GeneratedValue.class);
				if (gv == null) {
					this.insertExcludePrimaryKey = false;
				} else {
					this.insertExcludePrimaryKey = gg == null || gg.strategy().equalsIgnoreCase("native")
							|| gg.strategy().equalsIgnoreCase("identity");
				}

			}
			if (joinCol != null) {
				this.joinColumnMap.put(joinCol.name(), method);
			}
			if (col != null) {
				this.nomalColumnMap.put(col.name(), method);
			}

			if (pkjCol != null) {
				this.getPrimaryKeyFromParent = true;
				this.parentMethod = method;
			} else if (oto != null) {
				this.oneToOneChildren.add(method);
			}

			if (otm != null) {
				this.oneToManyChildren.add(method);
			}
		}

		this.nomalColumnMap.remove(this.primaryColumnName);
	}

	public String getTableName() {
		return tableName;
	}

	public Map<String, Method> getJoinColumnMap() {
		return joinColumnMap;
	}

	public Map<String, Method> getNomalColumnMap() {
		return nomalColumnMap;
	}

	public List<Method> getOneToOneChildren() {
		return oneToOneChildren;
	}

	public List<Method> getOneToManyChildren() {
		return oneToManyChildren;
	}

	public String getPrimaryColumnName() {
		return primaryColumnName;
	}

	public boolean isInsertExcludePrimaryKey() {
		return insertExcludePrimaryKey;
	}

	public boolean isGetPrimaryKeyFromParent() {
		return getPrimaryKeyFromParent;
	}

	public Method getIdGetMethod() {
		return idGetMethod;
	}

	public Map<String, Method> getSetMethodMap() {
		return setMethodMap;
	}

	public Method getParentMethod() {
		return parentMethod;
	}

}
