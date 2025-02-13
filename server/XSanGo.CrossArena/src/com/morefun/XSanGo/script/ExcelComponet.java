/**
 * 
 */
package com.morefun.XSanGo.script;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 组件描述类
 * 
 * @author sulingyun
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelComponet {

	int index();

	int columnCount();

	int size();

}
