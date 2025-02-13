/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.event.protocol;


import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 碎片合成后 炫耀
 * 
 * @author lvmingtao
 */

@signalslot
public interface IItemChipStrut {
	void onStrut(String itemId);
}
