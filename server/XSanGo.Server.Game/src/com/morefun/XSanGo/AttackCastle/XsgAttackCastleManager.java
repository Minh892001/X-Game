package com.morefun.XSanGo.AttackCastle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;

/**
 * 北伐
 * 
 * @author qinguofeng
 * @date Jan 27, 2015
 */
public class XsgAttackCastleManager {
	private final static Log logger = LogFactory
			.getLog(XsgAttackCastleManager.class);

	private static XsgAttackCastleManager instance = new XsgAttackCastleManager();

	public static XsgAttackCastleManager getInstance() {
		return instance;
	}

	private AttackCastleParamSettingT paramSetting;
	private Map<Integer, AttackCastleNodeSettingT> nodeSetting = new HashMap<Integer, AttackCastleNodeSettingT>();
	private Map<Integer, AttackCastleNodeSettingT> rewardsSetting = new HashMap<Integer, AttackCastleNodeSettingT>();
	private Map<Integer, AttackCastleShopRewardT> shopRewards = new HashMap<Integer, AttackCastleShopRewardT>();
	private Map<Integer, AttackCastleExchangeSettingT> exchangeConsumes = new HashMap<Integer, AttackCastleExchangeSettingT>(); 
	private List<AttackCastleRefreshCostT> refreshCostList = new ArrayList<AttackCastleRefreshCostT>();
	private List<AttackCastleExtraHeroT> extraHeroList = new ArrayList<AttackCastleExtraHeroT>();
	
	private XsgAttackCastleManager() {
		// 读取参数配置
		List<AttackCastleParamSettingT> paramSettingList = ExcelParser
				.parse(AttackCastleParamSettingT.class);
		if (paramSettingList == null || paramSettingList.size() <= 0) {
			logger.error("北伐配置文件'参数配置'错误");
			return;
		}
		paramSetting = paramSettingList.get(0);

		// 读取关卡设置
		List<AttackCastleNodeSettingT> nodeSettingList = ExcelParser
				.parse(AttackCastleNodeSettingT.class);
		if (nodeSettingList == null || nodeSettingList.size() <= 0) {
			logger.error("北伐配置文件'关卡设置'错误");
			return;
		}
		final int TNODE = 0, TBOX = 1, TSUPPLY = 2; // 关卡(0), 宝箱(1), 补给(2)
		for (AttackCastleNodeSettingT node : nodeSettingList) {
			int type = node.stageType;
			// 关卡
			if (TNODE == type) {
				nodeSetting.put(node.nodeId, node);
			}
			// 宝箱或者补给
			if (TBOX == type || TSUPPLY == type) {
				rewardsSetting.put(node.nodeId, node);
			}
		}
		
		// 读取商城配置
		List<AttackCastleExchangeSettingT> exchangeSettingList = ExcelParser.parse(AttackCastleExchangeSettingT.class);
		if (exchangeSettingList == null || exchangeSettingList.size() <= 0) {
			logger.error("北伐商城配置'兑换商城'错误");
			return;
		}
		for(AttackCastleExchangeSettingT exchange:exchangeSettingList){
			exchangeConsumes.put(exchange.num, exchange);
		}

		// 读取商城商品配置
		List<AttackCastleShopRewardT> shopList = ExcelParser.parse(AttackCastleShopRewardT.class);
		if (shopList == null || shopList.size() <= 0) {
			logger.error("北伐商城配置'商城随机刷新'错误");
			return;
		}
		for (AttackCastleShopRewardT shop:shopList) {
			shopRewards.put(shop.id, shop);
		}
		
		// 读取刷新配置
		List<AttackCastleRefreshCostT> refreshCost = ExcelParser.parse(AttackCastleRefreshCostT.class);
		refreshCostList = refreshCost;
		
		// 读取整卡武将配置
		List<AttackCastleExtraHeroT> extraHeros = ExcelParser.parse(AttackCastleExtraHeroT.class);
		extraHeroList = extraHeros;
	}

	public AttackCastleNodeSettingT getNodeSetting(int nodeId) {
		return nodeSetting.get(nodeId);
	}

	public AttackCastleParamSettingT getParamSetting() {
		return paramSetting;
	}

	public AttackCastleNodeSettingT getRewardSetting(int nodeId) {
		return rewardsSetting.get(nodeId);
	}

	public Map<Integer, AttackCastleShopRewardT> getShopRewardsMap(){
		return shopRewards;
	}

	public AttackCastleExchangeSettingT getExchangeSetting(int count) {
		return exchangeConsumes.get(count);
	}
	
	public  AttackCastleShopRewardT getExchangeItem(int id) {
		return shopRewards.get(id);
	}
	
	public AttackCastleController createAttackCastleController(IRole role,
			Role roleDB) {
		return new AttackCastleController(role, roleDB);
	}
	
	public AttackCastleRefreshCostT getRefreshCostT(int num) {
		if (refreshCostList != null) {
			for (AttackCastleRefreshCostT t : refreshCostList) {
				if (t.num == num) {
					return t;
				}
			}
		}
		return null;
	}
	
	public boolean canMatchSelf(int index){
		// 暂时程序写死前三关不能遇到自己, 配置脚本中的索引为6
		if (index > 6) {
			return true;
		}
		return false;
	}
	
	public List<AttackCastleExtraHeroT> getFirstExtraHeroT() {
		List<AttackCastleExtraHeroT> heroTList = new ArrayList<AttackCastleExtraHeroT>();
		for (AttackCastleExtraHeroT heroT : extraHeroList) {
			if (heroT.type == 1) {
				heroTList.add(heroT);
			}
		}
		return heroTList;
	}
	
	public List<AttackCastleExtraHeroT> getNormalExtraHeroT() {
		return extraHeroList;
	}
}
