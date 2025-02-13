package com.morefun.XSanGo.buyJinbi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;

/**
 * 点金手
 * 
 * @author 吕明涛
 * 
 */
public class XsgBuyJInbiManager {
	private static XsgBuyJInbiManager instance = new XsgBuyJInbiManager();

	private Map<Integer, BuyJInbiT> buyJinbis = new HashMap<Integer, BuyJInbiT>();

	public static XsgBuyJInbiManager getInstance() {
		return instance;
	}

	public IBuyJInbiControler createBuyJInbiControler(IRole roleRt, Role roleDB) {
		return new BuyJInbiControler(roleRt, roleDB);
	}

	public XsgBuyJInbiManager() {
		loadBuyJinbiScript();
	}
	
	/**
	 * 加载购买金币脚本
	 */
	public void loadBuyJinbiScript(){
		List<BuyJInbiT> buyJInbiTs = ExcelParser.parse(BuyJInbiT.class);
		this.buyJinbis.clear();
		for (BuyJInbiT b : buyJInbiTs) {
			this.buyJinbis.put(b.times, b);
		}
	}

	public BuyJInbiT getBuyJInbiT(int times) {
		return this.buyJinbis.get(times);
	}

}
