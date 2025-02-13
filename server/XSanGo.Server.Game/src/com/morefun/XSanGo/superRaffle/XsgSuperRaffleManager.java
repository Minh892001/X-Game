package com.morefun.XSanGo.superRaffle;

import java.util.ArrayList;
import java.util.List;

import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleSuperTurntable;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.superCharge.RaffleItemT;
import com.morefun.XSanGo.superCharge.RaffleNumT;
import com.morefun.XSanGo.superCharge.SuperChargeBaseParamT;

import edu.emory.mathcs.backport.java.util.LinkedList;

public class XsgSuperRaffleManager {

	private static XsgSuperRaffleManager instance = new XsgSuperRaffleManager();
	/**抽奖道具*/
	private List<RaffleItemT> rafflesList = new ArrayList<RaffleItemT>();
	/**抽奖次数*/
	private List<RaffleNumT> raffleNumList = new ArrayList<RaffleNumT>();
	/**超级充值配置说明文案*/
	private SuperChargeBaseParamT desc = null;
	private List<RoleSuperTurntable> recievedResults = new ArrayList<RoleSuperTurntable>();
	
	public XsgSuperRaffleManager() {
		
		this.loadScript();
		SimpleDAO simpleDao = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
		this.recievedResults = simpleDao.findByParam(RoleSuperTurntable.class,"announceFlag","1",
				"lastReceiveTime", desc.showNum);
	}

	private void loadScript() {
		rafflesList = ExcelParser.parse(RaffleItemT.class);
		raffleNumList = ExcelParser.parse(RaffleNumT.class);
		List<SuperChargeBaseParamT> list = ExcelParser.parse(SuperChargeBaseParamT.class);
		desc = list.get(0);
	}
	
	/**
	 * 创建超级转盘管理模块
	 * 
	 * @param roleRt
	 * @param roleDB
	 * @return
	 */
	public ISuperRaffleController createSuperChargeControler(IRole roleRt, Role roleDB) {
		return new SuperRaffleController(roleRt, roleDB);
	}
	
	public List<RaffleItemT> getRafflesList() {
		return rafflesList;
	}

	public List<RaffleNumT> getRaffleNumList() {
		return raffleNumList;
	}

	public SuperChargeBaseParamT getDesc() {
		return desc;
	}

	public static XsgSuperRaffleManager getInstance() {
		return instance;
	}

	public List<RoleSuperTurntable> getRecievedResults() {
		return recievedResults;
	}

	public void setRecievedResults(List<RoleSuperTurntable> recievedResults) {
		this.recievedResults = recievedResults;
	}

	public RaffleItemT getRaffleItemById(int scriptId) {
		for (RaffleItemT itemT : rafflesList) {
			if(itemT.id==scriptId){
				return itemT;
			}
		}
		return null;
	}

	public void addRaffleRecord(RoleSuperTurntable rst, RaffleItemT randomRaffle) {
		if(randomRaffle.announceFlag==1){
			LinkedList linkedList = new LinkedList(recievedResults);
			linkedList.addFirst(rst);
			this.recievedResults = new ArrayList<RoleSuperTurntable>(linkedList);
			if(recievedResults.size()>desc.showNum){//超过指定数量则移除尾部
				LinkedList list = new LinkedList(recievedResults);
				list.removeLast();
				this.recievedResults = new ArrayList<RoleSuperTurntable>(list);
			}
		}
	}
	
}
