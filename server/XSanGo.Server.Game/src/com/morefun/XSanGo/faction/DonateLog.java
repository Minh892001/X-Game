package com.morefun.XSanGo.faction;

/**
 * 科技捐赠日志
 * 
 * @author xiongming.li
 *
 */
public class DonateLog {
	public String datetime;
	public int donateWeizhang;
	public int donateYuanbao;

	public DonateLog(){
		
	}
	
	public DonateLog(String datetime, int donateWeizhang, int donateYuanbao) {
		this.datetime = datetime;
		this.donateWeizhang = donateWeizhang;
		this.donateYuanbao = donateYuanbao;
	}

}
