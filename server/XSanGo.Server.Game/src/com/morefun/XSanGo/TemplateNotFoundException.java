/**
 * 
 */
package com.morefun.XSanGo;

/**
 * 模板未找到异常
 * 
 * @author sulingyun
 *
 */
public class TemplateNotFoundException extends Exception {

	private String templateCode;

	public TemplateNotFoundException(String templateCode) {
		this.templateCode = templateCode;
	}

}
