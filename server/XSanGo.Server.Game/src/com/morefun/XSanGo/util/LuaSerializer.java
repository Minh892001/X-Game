/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * lua table格式的序列化器
 * 
 * @author BruceSu
 * 
 */
public class LuaSerializer {

	/**
	 * Returns a string holding the contents of the passed object,
	 * 
	 * @param scope
	 *            String
	 * @param parentObject
	 *            Object
	 * @param visitedObjs
	 *            List
	 * @return String
	 */
	private static String complexTypeToString(String scope,
			Object parentObject, List visitedObjs) {

		StringBuffer buffer = new StringBuffer(scope + "{");

		try {
			//
			// Ok, now we need to reflect into the object and add its child
			// nodes...
			//

			Class cl = parentObject.getClass();
			while (cl != null) {

				processFields(cl.getDeclaredFields(), scope, parentObject,
						buffer, visitedObjs);

				cl = cl.getSuperclass();
			}
		} catch (IllegalAccessException iae) {
			buffer.append(iae.toString());
		}

		return (buffer.toString()) + "}";
	}

	/**
	 * Method processFields
	 * 
	 * @param fields
	 *            Field[]
	 * @param scope
	 *            String
	 * @param parentObject
	 *            Object
	 * @param buffer
	 *            StringBuffer
	 * @param visitedObjs
	 *            List
	 * @throws IllegalAccessException
	 */
	private static void processFields(Field[] fields, String scope,
			Object parentObject, StringBuffer buffer, List visitedObjs)
			throws IllegalAccessException {

		for (int i = 0; i < fields.length; i++) {

			//
			// Disregard certain fields for IDL structures
			//
			if (fields[i].getName().equals("__discriminator")
					|| fields[i].getName().equals("__uninitialized")) {
				continue;
			}

			//
			// This allows us to see non-public fields. We might need to deal
			// with some
			// SecurityManager issues here once it is outside of VAJ...
			//
			fields[i].setAccessible(true);

			if (Modifier.isStatic(fields[i].getModifiers())) {
				//
				// Ignore all static members. The classes that this dehydrator
				// is
				// meant to handle are simple data objects, so static members
				// have no
				// bearing....
				//
			} else {
				buffer.append(typeToString(fields[i].getName(),
						fields[i].get(parentObject), visitedObjs));
			}
		}

	}

	/**
	 * Method isCollectionType
	 * 
	 * @param obj
	 *            Object
	 * @return boolean
	 */
	private static boolean isCollectionType(Object obj) {

		return (obj.getClass().isArray() || (obj instanceof Collection)
				|| (obj instanceof Hashtable) || (obj instanceof HashMap)
				|| (obj instanceof HashSet) || (obj instanceof List) || (obj instanceof AbstractMap));
	}

	/**
	 * Method isComplexType
	 * 
	 * @param obj
	 *            Object
	 * @return boolean
	 */
	private static boolean isComplexType(Object obj) {

		if (obj instanceof Boolean || obj instanceof Short
				|| obj instanceof Byte || obj instanceof Integer
				|| obj instanceof Long || obj instanceof Float
				|| obj instanceof Character || obj instanceof Double
				|| obj instanceof String || obj instanceof Enum) {

			return false;
		} else {

			Class objectClass = obj.getClass();

			if (objectClass == boolean.class || objectClass == Boolean.class
					|| objectClass == short.class || objectClass == Short.class
					|| objectClass == byte.class || objectClass == Byte.class
					|| objectClass == int.class || objectClass == Integer.class
					|| objectClass == long.class || objectClass == Long.class
					|| objectClass == float.class || objectClass == Float.class
					|| objectClass == char.class
					|| objectClass == Character.class
					|| objectClass == double.class
					|| objectClass == Double.class
					|| objectClass == String.class) {

				return false;

			}

			else {
				return true;
			}
		}
	}

	/**
	 * Returns a string holding the contents of the passed object,
	 * 
	 * @param scope
	 *            String
	 * @param obj
	 *            Object
	 * @param visitedObjs
	 *            List
	 * @return String
	 */

	private static String collectionTypeToString(String scope, Object obj,
			List visitedObjs) {

		StringBuffer buffer = new StringBuffer(scope + "{\n");

		// if (obj == null) {
		// return buffer.append(scope + "[]: empty\n").toString();
		// }
		if (obj.getClass().isArray()) {
			if (Array.getLength(obj) > 0) {

				for (int j = 0; j < Array.getLength(obj); j++) {

					Object x = Array.get(obj, j);

					buffer.append(typeToString("", x, visitedObjs));
				}

			} else {
				// buffer.append(scope + "[]: empty\n");
			}
		} else {
			boolean isCollection = (obj instanceof Collection);
			boolean isHashTable = (obj instanceof Hashtable);
			boolean isHashMap = (obj instanceof HashMap);
			boolean isHashSet = (obj instanceof HashSet);
			boolean isAbstractMap = (obj instanceof AbstractMap);
			boolean isMap = isAbstractMap || isHashMap || isHashTable;

			if (isMap) {
				Set keySet = ((Map) obj).keySet();
				Iterator iterator = keySet.iterator();
				int size = keySet.size();

				if (size > 0) {

					for (int j = 0; iterator.hasNext(); j++) {

						Object key = iterator.next();
						Object x = ((Map) obj).get(key);

						buffer.append(typeToString("", x, visitedObjs));
					}
				} else {
					// buffer.append(scope + "[]: empty\n");
				}
			} else if (/* isHashTable || */
			isCollection || isHashSet /* || isHashMap */
			) {

				Iterator iterator = null;
				int size = 0;

				if (obj != null) {

					if (isCollection) {
						iterator = ((Collection) obj).iterator();
						size = ((Collection) obj).size();
					} else if (isHashTable) {
						iterator = ((Hashtable) obj).values().iterator();
						size = ((Hashtable) obj).size();
					} else if (isHashSet) {
						iterator = ((HashSet) obj).iterator();
						size = ((HashSet) obj).size();
					} else if (isHashMap) {
						iterator = ((HashMap) obj).values().iterator();
						size = ((HashMap) obj).size();
					}

					if (size > 0) {

						for (int j = 0; iterator.hasNext(); j++) {

							Object x = iterator.next();
							buffer.append(typeToString("", x, visitedObjs));
						}
					} else {
						// buffer.append(scope + "[]: empty\n");
					}
				} else {
					//
					// theObject is null
					// buffer.append(scope + "= nil\n");
				}
			}
		}

		return (buffer.toString()) + "}\n";

	}

	/**
	 * Method typeToString
	 * 
	 * @param scope
	 *            String
	 * @param obj
	 *            Object
	 * @param visitedObjs
	 *            List
	 * @return String
	 */
	private static String typeToString(String scope, Object obj,
			List visitedObjs) {

		if (!scope.isEmpty()) {
			scope += " = ";
		}

		if (obj == null) {
			return "";
			// return (scope + "nil,");
		} else if (isCollectionType(obj)) {
			return collectionTypeToString(scope, obj, visitedObjs) + ",";
		} else if (isComplexType(obj)) {
			return complexTypeToString(scope, obj, visitedObjs) + ",";
		} else if (isEnum(obj)) {
			return (scope + ((Enum) obj).ordinal() + ",");
		} else if (obj instanceof String || isLong(obj)) {
			return (scope + " [[" + obj.toString() + "]],");
		} else {
			return (scope + obj.toString() + ",");
		}
	}

	/**
	 * @param obj
	 * @return
	 */
	private static boolean isEnum(Object obj) {

		return obj.getClass().isEnum();
	}

	private static boolean isLong(Object obj) {
		Class objectClass = obj.getClass();
		return objectClass == long.class || objectClass == Long.class;
	}

	/**
	 * 将指定对象序列化成lua的数据格式
	 * 
	 * @param scope
	 * @param obj
	 * 
	 * @return String
	 * 
	 */

	public static String serialize(Object obj) {

		if (obj == null) {
			return ("nil\n");
		} else if (isCollectionType(obj)) {
			return collectionTypeToString("", obj, new ArrayList());
		} else if (isComplexType(obj)) {
			return complexTypeToString("", obj, new ArrayList());
		} else if (obj instanceof String || isLong(obj)) {
			return ("{ [[" + obj.toString() + "]]}");
		} else if (isEnum(obj)) {
			return ("{" + ((Enum) obj).ordinal() + "}");
		} else {
			return ("{" + obj.toString() + "}\n");
		}
	}

	/**
	 * 移除LUA中特殊的字符串：( ) . % + - * ? [ ^ $
	 * 
	 * 
	 * @param input
	 * @return
	 */
	public static String removeSpecialLuaString(String input) {
		if(input == null) {
			return input;
		}
		// Lua中的特殊字符如下： ( ) . % + - * ? [ ^ $ ]
		// 参考链接
		// http://www.cnblogs.com/whiteyun/archive/2009/09/02/1541043.html
		return input.replace("(", "").replace(")", "").replace(".", "")
				.replace("%", "").replace("+", "").replace("-", "")
				.replace("*", "").replace("?", "").replace("[", "")
				.replace("]", "").replace("]", "").replace("^", "")
				.replace("$", "");
	}

	public static void main(String[] args) {
		String test = "a(b)c.d%e+f-g(*h?i[j^k$lmn";
		System.out.println(test);
		System.out.println(removeSpecialLuaString(test));
	}

}
