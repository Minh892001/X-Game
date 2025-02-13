/**
 * 
 */
package com.morefun.XSanGo.hero;

/**
 * 武将所处位置描述信息
 * 
 * @author sulingyun
 * 
 */
public class PositionData {
	/** 被引用的阵法ID */
	public String formationId;
	/** 被引用的武将ID */
	public String heroId;
	/** 阵位或随从位 */
	public int position;
	/** 被引用的伙伴ID，实际上是roleId */
	public String partnerId;

}
