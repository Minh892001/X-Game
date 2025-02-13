/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.net;

import Glacier2._PermissionsVerifierDisp;
import Ice.Current;
import Ice.StringHolder;

import com.XSanGo.Protocol.DeviceInfo;
import com.morefun.XSanGo.center.XsgCenterManager;

@SuppressWarnings("serial")
public class PermissionsVerifierI extends _PermissionsVerifierDisp {

	private static PermissionsVerifierI instance = new PermissionsVerifierI();
	private boolean fuse = false;

	public static PermissionsVerifierI getInstance() {
		return instance;
	}

	private PermissionsVerifierI() {
	}

	@Override
	public boolean checkPermissions(String userId, String password, StringHolder reason, Current __current) {
		if (fuse) {// 已经熔断了
			return false;
		}
		DeviceInfo device = XsgCenterManager.instance().isVerify(userId, password);

		if (device == null) {
			device = GameSessionManagerI.getInstance().reconnectVerify(userId, password);
		}

		boolean result = device != null;
		if (result) {// 无论是首次登录还是重连，只要验证通过，都必须提交设备信息
			GameSessionManagerI.getInstance().addConnectingDevice(userId, device);
		}

		return result;
	}

	/**
	 * 熔断，拒绝所有连接
	 */
	public void fuse() {
		this.fuse = true;
	}

	/**
	 * 恢复连接，逻辑代码不应该直接调用此方法
	 */
	public void restore() {
		this.fuse = false;
	}

}
