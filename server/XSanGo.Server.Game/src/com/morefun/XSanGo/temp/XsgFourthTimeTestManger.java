/**
 * 
 */
package com.morefun.XSanGo.temp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.SensitiveWord;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.CollectionUtil;

/**
 * 四测活动管理，处理前面几次测试的充值返利以及3测冲级活动
 * 
 * @author sulingyun
 *
 */
public class XsgFourthTimeTestManger {

	private static XsgFourthTimeTestManger instance = new XsgFourthTimeTestManger();

	public static XsgFourthTimeTestManger getInstance() {
		return instance;
	}

	private boolean enable = ServerLancher.getAc().getBean("FourthTestEnable",
			Boolean.class);
	private Map<String, ChargeHistoryT> chargeHistoryTMap;
	private List<String> levelAccountList;

	private XsgFourthTimeTestManger() {
		this.chargeHistoryTMap = CollectionUtil.toMap(
				ExcelParser.parse(ChargeHistoryT.class), "account");

		this.levelAccountList = new ArrayList<String>();
		for (LevelAccountT lat : ExcelParser.parse(LevelAccountT.class)) {
			this.levelAccountList.add(lat.account);
		}
	}

	public IFourthTimeTestForBeforeControler createControler(IRole rt, Role db) {
		return new FourthTimeTestForBeforeControler(rt, db);
	}

	public boolean isEnable() {
		return this.enable;
	}

	public ChargeHistoryT findChargeHistoryT(String account) {
		return this.chargeHistoryTMap.get(account);
	}

	public boolean isAccountInLevelRank(String account) {
		return this.levelAccountList.contains(account);
	}

}
