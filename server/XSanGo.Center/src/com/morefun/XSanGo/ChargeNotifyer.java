/**
 * 
 */
package com.morefun.XSanGo;

import com.morefun.XSanGo.charge.ChargeManager;

/**
 * 充值订单通知线程
 * 
 * @author sulingyun
 *
 */
public class ChargeNotifyer extends Thread {
	private static ChargeNotifyer instance = new ChargeNotifyer();

	public static ChargeNotifyer getInstance() {
		return instance;
	}

	private ChargeNotifyer() {

	}

	@Override
	public void run() {
		while (true) {
			try {
				ChargeManager.handleOrder();

				Thread.sleep(30 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
