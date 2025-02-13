/**
 * 
 */
package com.morefun.XSanGo.role;

import com.XSanGo.Protocol.MajorUIRedPointNote;

/**
 * 支持红点提示的接口
 * 
 * @author sulingyun
 *
 */
public interface IRedPointNotable {

	/**
	 * 获取该功能在主界面上的红点提示
	 * 
	 * @return
	 */
	MajorUIRedPointNote getRedPointNote();

}
