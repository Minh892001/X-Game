/**
 * 
 */
package com.morefun.XSanGo.gm;

import com.XSanGo.Protocol.GuideConfig;
import com.XSanGo.Protocol.WhiteList;
import com.XSanGo.Protocol.WhiteListType;

/**
 * 包含白名单，推荐服务器等动态参数
 * 
 * @author sulingyun
 *
 */
public class DynamicConfig {
	private int recommendServerId = -1;
	private WhiteList[] whiteListArray = new WhiteList[0];
	private GuideConfig guideConfig;

	public boolean hasRecommendServer() {
		return this.recommendServerId != -1;
	}

	public WhiteList findWhiteList(WhiteListType type) {
		for (WhiteList wl : this.whiteListArray) {
			if (wl.type == type) {
				return wl;
			}
		}

		return null;
	}

	/**
	 * 设置白名单
	 * 
	 * @param wl
	 */
	public void setWhiteList(WhiteList wl) {
		WhiteList current = this.findWhiteList(wl.type);
		if (current == null) {
			WhiteList[] oldArray = this.whiteListArray;
			this.whiteListArray = new WhiteList[oldArray.length + 1];
			System.arraycopy(oldArray, 0, this.whiteListArray, 0,
					oldArray.length);
			this.whiteListArray[oldArray.length] = wl;
		} else {
			current.enable = wl.enable;
			current.tip = wl.tip;
			current.whiteAccountList = wl.whiteAccountList;
			current.whiteIpList = wl.whiteIpList;
		}
	}

	public void setRecommendServer(int serverId) {
		this.recommendServerId = serverId;
	}

	public int getRecommendServer() {
		return this.recommendServerId;
	}

	public GuideConfig getGuideConfig() {
		return guideConfig;
	}

	public void setGuideConfig(GuideConfig guideConfig) {
		this.guideConfig = guideConfig;
	}
}
