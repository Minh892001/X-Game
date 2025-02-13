/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.util;

/**
 * 谓词对象
 * 
 * @author BruceSu
 * 
 */
public interface IPredicate<T> {
	boolean check(T item);
}
