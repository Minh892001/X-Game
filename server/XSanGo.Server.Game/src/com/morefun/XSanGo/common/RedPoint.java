/**
 * 
 */
package com.morefun.XSanGo.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 红点控制标记，必须与IRedPointNotable接口结合使用
 * 
 * @author linyun.su
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedPoint {
	/**
	 * 是否定时检测
	 * 
	 * @return
	 */
	boolean isTimer() default false;
}
