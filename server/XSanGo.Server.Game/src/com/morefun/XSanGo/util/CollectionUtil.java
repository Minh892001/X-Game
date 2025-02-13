/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BruceSu
 * 
 */
public class CollectionUtil {
	/**
	 * 从集合中移除符合指定谓词的元素，内部遍历实现，因此对于元素数量较多的集合不推荐使用
	 * 
	 * @param <T>
	 * @param coll
	 * @param pre
	 * @return
	 */
	public static <T> Collection<T> removeEqual(Collection<T> coll,
			final T reItem) {

		return removeWhere(coll, new IPredicate<T>() {

			@Override
			public boolean check(T item) {
				return reItem == item;
			}
		});

	}

	public static <T> Collection<T> removeWhere(Collection<T> coll,
			IPredicate<T> pre) {
		if (coll == null) {
			return null;
		}
		Collection<T> deletes = where(coll, pre);

		coll.removeAll(deletes);

		return coll;
	}

	/**
	 * 返回符合筛选条件的子集,内部遍历实现，因此对于元素数量较多的集合不推荐使用
	 * 
	 * @param <T>
	 * @param coll
	 * @param pre
	 * @return
	 */
	public static <T> Collection<T> where(Collection<T> coll, IPredicate<T> pre) {

		List<T> list = new ArrayList<T>();
		if (coll != null) {
			for (T t : coll) {
				if (pre.check(t)) {
					list.add(t);
				}
			}
		}

		return list;
	}

	/**
	 * 获取符合指定条件的第一个元素
	 * 
	 * @param <T>
	 * @param coll
	 * @param pre
	 * @return
	 */
	public static <T> T first(Collection<T> coll, IPredicate<T> pre) {
		if (coll != null) {
			for (T t : coll) {
				if (pre.check(t)) {
					return t;
				}
			}
		}
		return null;
	}

	public static <T> boolean exists(Collection<T> coll, IPredicate<T> pre) {
		return first(coll, pre) != null;
	}

	public static <T> int indexOf(T[] array, T t) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(t)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 用指定的KEY将集合转化成MAP
	 * 
	 * @param coll
	 *            数据集
	 * @param fieldOrFun
	 *            字段或者方法名，仅接受不带参数的方法
	 * @return
	 */
	public static <K, V> Map<K, V> toMap(Collection<V> coll, String fieldOrFun) {
		AccessibleObject ao = null;
		Map<K, V> map = new HashMap<K, V>();
		for (V item : coll) {
			try {
				if (ao == null) {
					ao = item.getClass().getField(fieldOrFun);
					if (ao == null) {
						ao = item.getClass().getMethod(fieldOrFun);
					}
					if (ao == null) {
						throw new IllegalArgumentException(fieldOrFun);
					}

					ao.setAccessible(true);
				}

				K key = null;
				if (ao instanceof Field) {
					key = (K) ((Field) ao).get(item);
				} else if (ao instanceof Method) {
					key = (K) ((Method) ao).invoke(item, new Object[0]);
				}

				map.put(key, item);
			} catch (Exception e) {
				LogManager.error(e);
			}
		}

		return map;
	}

}
