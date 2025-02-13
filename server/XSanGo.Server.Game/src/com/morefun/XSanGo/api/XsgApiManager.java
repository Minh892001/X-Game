/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: XsgApiManager
 * 功能描述：
 * 文件名：XsgApiManager.java
 **************************************************
 */
package com.morefun.XSanGo.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.morefun.XSanGo.activity.ActivityT;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 活动api
 * 
 * @author zhangwei02.zhang
 * @since 2015年11月11日
 * @version 1.0
 */
public class XsgApiManager {

	private static final Logger log = LoggerFactory.getLogger(XsgApiManager.class);

	private static XsgApiManager _instance = new XsgApiManager();

	public static XsgApiManager getInstance() {
		return _instance;
	}

	private XsgApiManager() {
	}

	/** API活动列表 活动id */
	private Map<Integer, ActivityT> apiActivitys = new HashMap<Integer, ActivityT>();

	/** 按API分组的活动列表，便于计算用，api_id */
	private Map<Integer, List<ActivityT>> apiActiveityGroups = new HashMap<Integer, List<ActivityT>>();

	/** API类型参数，key:apiid */
	private Map<Integer, ApiTypeT> apiIdsTypeMap = new HashMap<Integer, ApiTypeT>();

	/** API接口参数，key:apiinterface */
	private Map<String, List<ApiTypeT>> apiInterFaceTypeMap = new HashMap<String, List<ApiTypeT>>();

	/**
	 * API脚本初始化
	 * 
	 * @param atlist
	 */
	public void loadScript(List<ActivityT> atlist) {
		apiActivitys.clear();
		apiActiveityGroups.clear();
		apiIdsTypeMap.clear();
		apiInterFaceTypeMap.clear();

		List<ApiRewardT> rewardList = ExcelParser.parse(ApiRewardT.class);
		if (rewardList == null || rewardList.size() == 0) {
			log.error("API奖励配置错误");
		}
		List<ApiTypeT> apiTypeList = ExcelParser.parse(ApiTypeT.class);
		if (apiTypeList == null || apiTypeList.size() == 0) {
			log.error("API类型配置错误");
		}
		List<ApiFunctionTypeT> apiFuncList = ExcelParser.parse(ApiFunctionTypeT.class);
		if (apiFuncList == null || apiFuncList.size() == 0) {
			log.error("API功能配置错误");
		}

		// 初始化功能参数
		Map<Integer, ApiFunctionTypeT> map = new HashMap<Integer, ApiFunctionTypeT>();
		for (ApiFunctionTypeT ft : apiFuncList) {
			if (TextUtil.isNotBlank(ft.apiInterface)) {
				map.put(ft.functionId, ft);
			}
		}
		// 初始化API配置数据
		List<ApiTypeT> aList = null;
		for (ApiTypeT tt : apiTypeList) {
			if (tt.apiId > 0 && map.containsKey(tt.funcId)) {
				tt.setFuncInterface(map.get(tt.funcId).apiInterface);
				apiIdsTypeMap.put(tt.apiId, tt);

				aList = apiInterFaceTypeMap.get(tt.getFuncInterface());
				if (aList == null) {
					aList = new ArrayList<ApiTypeT>();
					apiInterFaceTypeMap.put(tt.getFuncInterface(), aList);
				}
				aList.add(tt);
			} else {
				log.warn("api config error,api_id:" + tt.apiId);
			}
		}
		ApiTypeT tt = null;
		for (ApiRewardT rt : rewardList) {
			if (apiIdsTypeMap.containsKey(rt.apiId) && TextUtil.isNotBlank(rt.rewardItem)) {// 验证APIId和奖励必填，并且正确
				rt.parseItems();
				tt = apiIdsTypeMap.get(rt.apiId);
				tt.addRewards(rt);
				if (tt.getMaxApiRewardT() == null || rt.targetCount > tt.getMaxApiRewardT().targetCount) {
					tt.setMaxApiRewardT(rt);
				}
			}
		}

		// 初始化API活动列表
		List<ActivityT> list = null;
		for (Iterator<ActivityT> it = atlist.iterator(); it.hasNext();) {
			ActivityT at = it.next();
			if (at.apiId > 0) {
				if (apiIdsTypeMap.containsKey(at.apiId) && at.type != 1) {// API只能是2、3类型
					apiActivitys.put(at.id, at);
					list = apiActiveityGroups.get(at.apiId);
					if (list == null) {
						list = new ArrayList<ActivityT>();
						apiActiveityGroups.put(at.apiId, list);
					}
					list.add(at);
				} else {// 不符合API活动要求 移除
					it.remove();
				}
			}
		}
	}

	/**
	 * 创建API处理器实例
	 * 
	 * @param r
	 * @param db
	 * @return
	 */
	public ApiController createApiController(IRole r, Role db) {
		return new ApiController(r, db);
	}

	/**
	 * @return Returns the apiActivitys.
	 */
	public Map<Integer, ActivityT> getApiActivitys() {
		return apiActivitys;
	}

	/**
	 * @return Returns the apiActiveityGroups.
	 */
	public Map<Integer, List<ActivityT>> getApiActiveityGroups() {
		return apiActiveityGroups;
	}

	/**
	 * @return Returns the apiIdsTypeMap.
	 */
	public Map<Integer, ApiTypeT> getApiIdsTypeMap() {
		return apiIdsTypeMap;
	}

	/**
	 * @param apiId
	 * @return Returns the ApiTypeT.
	 */
	public ApiTypeT getApiIdsTypeMap(int apiId) {
		return apiIdsTypeMap.get(apiId);
	}

	/**
	 * @return Returns the apiInterFaceTypeMap.
	 */
	public Map<String, List<ApiTypeT>> getApiInterFaceTypeMap() {
		return apiInterFaceTypeMap;
	}
	
	/**
	 * API类型
	 * 
	 * @author zwy
	 * @since 2015-11-26
	 * @version 1.0
	 */
	enum Api {
		/** 武将升星 */
		HeroStarUp(1, "StarUp"),
		/** 武将突破 */
		HeroBreakUp(1, "BrokeUp"),
		/** 武将进阶 */
		HeroQualityUp(1, "Advance"),
		/** 装备升星 */
		EquipStarUp(1, "EquipStar"),
		/** 元宝单抽 */
		BuyWineByYuanbao(2, "onermby"),
		/** 元宝十连抽 */
		Buy10WineByYuanbao(2, "tenrmby"),
		/** 聚贤庄宴请 */
		JuXianZhang(2, "dinner"),
		/** 消耗道具 */
		ItemConsumption(2, "useitem");

		Api(int _type, String _value) {
			type = _type;
			value = _value;
		}

		public String getValue() {
			return value;
		}

		public int getType() {
			return type;
		}

		/**
		 * API类型 1返回类 2计数类
		 * 
		 * @param _value
		 * @return
		 */
		public static int getType(String _value) {
			for (Api api : values()) {
				if (api.value.equals(_value)) {
					return api.type;
				}
			}
			return 0;
		}

		/**
		 * API
		 * 
		 * @param _value
		 * @return
		 */
		public static Api getApi(String _value) {
			for (Api api : values()) {
				if (api.value.equals(_value)) {
					return api;
				}
			}
			return null;
		}

		/** 枚举值 */
		private String value;

		/** API类型 1返回类 2计数类 */
		private int type;
	}
}
