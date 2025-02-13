package com.morefun.XSanGo.colorfulEgg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.XSanGo.Protocol.ChatCallbackPrx;
import com.XSanGo.Protocol.ColorfullEggView;
import com.XSanGo.Protocol.EggInfo;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.chat.ChatAdT;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleColorfulEgg;
import com.morefun.XSanGo.event.protocol.IColorfullEggBroken;
import com.morefun.XSanGo.event.protocol.IColorfullEggRcerive;
import com.morefun.XSanGo.event.protocol.IRoleLevelup;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.IRandomHitable;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.RandomRange;
import com.morefun.XSanGo.util.TextUtil;

public class ColorfullEggController implements IColorfullEggController, IRoleLevelup {

	private IRole roleRt;
	private Role roleDB;
	private IColorfullEggBroken brokenEvent;
	private IColorfullEggRcerive receiveEvent;

	public ColorfullEggController(IRole roleRt, Role roleDB) {
		this.roleRt = roleRt;
		this.roleDB = roleDB;

		init();
		//注册升级事件
		roleRt.getEventControler().registerHandler(IRoleLevelup.class, this);
		brokenEvent = roleRt.getEventControler().registerEvent(IColorfullEggBroken.class);
		receiveEvent = roleRt.getEventControler().registerEvent(IColorfullEggRcerive.class);
	}

	/** 彩蛋数据初始化 */
	private void init() {
		if(!XsgColorfullEggManager.getInstance().isOpen()){
			return;
		}
		RoleColorfulEgg roleColorfullEgg = roleDB.getRoleColorfullEgg();
		EggPeriodsT eggPeriodsT = XsgColorfullEggManager.getInstance().getPeriodList().get(0);
		if (roleColorfullEgg == null) {
			roleColorfullEgg = new RoleColorfulEgg(roleDB, null, null, null, null, 0, eggPeriodsT.beginTime);
			roleDB.setRoleColorfullEgg(roleColorfullEgg);
		} else if (eggPeriodsT.beginTime.getTime() != roleColorfullEgg.getStartTime().getTime()) {
			// 另一个活动时间开始的时候，重置所有数据
			roleColorfullEgg.setAcceptScriptId(null);
			roleColorfullEgg.setBrokenTime(null);
			roleColorfullEgg.setReceiveTime(null);
			roleColorfullEgg.setRecord(null);
			roleColorfullEgg.setAcceptNum(0);
			roleColorfullEgg.setStartTime(eggPeriodsT.beginTime);
		}else{
			//领奖cd时间已过，重置砸蛋数据 但是不重置参与的领奖次数
			EggBasisConfT eggConf = XsgColorfullEggManager.getInstance().getEggConf();
			Date refreshTime = DateUtil.joinTime(eggConf.refreshTime);
			Date receiveTime = roleColorfullEgg.getReceiveTime();
			if(receiveTime!=null &&
					DateUtil.checkTime(receiveTime, refreshTime)){
				roleColorfullEgg.setAcceptScriptId(null);
				roleColorfullEgg.setBrokenTime(null);
				roleColorfullEgg.setReceiveTime(null);
				roleColorfullEgg.setRecord(null);
			}
		}
	}

	@Override
	public ColorfullEggView getView() throws NoteException {

		boolean isOpen = XsgColorfullEggManager.getInstance().isOpen();
		if (!isOpen) {
			throw new NoteException(Messages.getString("FriendsRecallController.notOpen"));
		}
		init();

		RoleColorfulEgg roleColorfullEgg = roleDB.getRoleColorfullEgg();
		/*boolean isReceived = XsgColorfullEggManager.getInstance().isCurrBetween(roleColorfullEgg.getReceiveTime());
		if (isReceived) {// 活动期间已经领取过的不再参与
			throw new NoteException(Messages.getString("CollectHeroSoulController.3"));
		}*/

		EggBasisConfT eggConf = XsgColorfullEggManager.getInstance().getEggConf();

		if (eggConf == null) {
			throw new NoteException(Messages.getString("FriendsRecallController.notOpen"));
		} else if (eggConf.limitLevel > roleRt.getLevel()) {
			throw new NoteException(Messages.getString("TournamentControler.levelLimit"));
		}

		//CD时间内的不在参与      脚本配置的CD是秒
		Date receiveTime = roleColorfullEgg.getReceiveTime();
		Date refreshTime = DateUtil.joinTime(eggConf.refreshTime);
		if(receiveTime!=null &&
				!DateUtil.checkTime(receiveTime, refreshTime)){
			throw new NoteException(Messages.getString("CollectHeroSoulController.3"));
		}
		
		if(roleColorfullEgg.getAcceptNum() >= eggConf.limitNum){
			throw new NoteException(Messages.getString("CollectHeroSoulController.3"));
		}
		
		EggPeriodsT timeT = XsgColorfullEggManager.getInstance().getPeriodList().get(0);
		String beginTime = DateUtil.format(timeT.beginTime, "yyyy年MM月dd日");
		String endTime = DateUtil.format(timeT.endTime, "yyyy年MM月dd日");
		
		ColorfullEggView view = new ColorfullEggView();
		view.entry = eggConf.entryFlag;
		view.reqLevel = eggConf.limitLevel;
		view.opentime = beginTime+" - "+endTime;

		boolean brokenTime = XsgColorfullEggManager.getInstance().isCurrBetween(roleColorfullEgg.getBrokenTime());
		EggInfo[] eggInfoList = null;
		// 活动期间已经砸蛋的数据
		if (brokenTime) {
			eggInfoList = TextUtil.GSON.fromJson(roleColorfullEgg.getRecord(),
					new EggInfo[XsgColorfullEggManager.EGG_NUM].getClass());
		}
		
		view.eggInfoList = eggInfoList;
		view.joinTimes = eggConf.limitNum;
		
		int[] colors = new int[XsgColorfullEggManager.EGG_NUM];
		colors[0]=eggConf.firstEggRewardFlag;
		colors[1]=eggConf.secondEggRewardFlag;
		colors[2]=eggConf.thirdEggRewardFlag;
		
		view.colors = colors ;
		return view;
	}

	@Override
	public ColorfullEggView brokenEgg(byte eggFlag) throws NoteException {

		boolean isOpen = XsgColorfullEggManager.getInstance().isOpen();
		if (!isOpen) {
			throw new NoteException(Messages.getString("FriendsRecallController.notOpen"));
		}
		
		EggBasisConfT eggConf = XsgColorfullEggManager.getInstance().getEggConf();
		
		if(roleRt.getLevel() < eggConf.limitLevel){
			throw new NoteException(Messages.getString("AttackCastleController.levelNotEnough"));
		}
		
		init();
		// 获取最新砸蛋数据
		RoleColorfulEgg roleColorfullEgg = roleDB.getRoleColorfullEgg();
		/*boolean isReceived = XsgColorfullEggManager.getInstance().isCurrBetween(roleColorfullEgg.getReceiveTime());
		if (isReceived) {// 活动期间已经领取过的不再参与
			throw new NoteException(Messages.getString("CollectHeroSoulController.3"));
		}*/

		//CD时间内的不在参与      脚本配置的CD是秒
		Date receiveTime = roleColorfullEgg.getReceiveTime();
		Date refreshTime = DateUtil.joinTime(eggConf.refreshTime);
		if(receiveTime!=null &&
				!DateUtil.checkTime(receiveTime, refreshTime)){
			throw new NoteException(Messages.getString("CollectHeroSoulController.3"));
		}
		
		if(roleColorfullEgg.getAcceptNum() >= eggConf.limitNum){
			throw new NoteException(Messages.getString("CollectHeroSoulController.3"));
		}
		// 活动期间是否有砸蛋数据
		EggInfo[] eggInfoList = TextUtil.GSON.fromJson(roleColorfullEgg.getRecord(),
				new EggInfo[XsgColorfullEggManager.EGG_NUM].getClass());
		List<EggInfo> eggs = new ArrayList<EggInfo>();
		if (TextUtil.isNotBlank(roleColorfullEgg.getRecord())) {// 该彩蛋是否已经砸过
			for (int i = 0; i < eggInfoList.length; i++) {
				if (eggInfoList[i].id == eggFlag) {
					throw new NoteException(Messages.getString("CornucopiaControler.notDoubleGet"));
				}
				eggs.add(eggInfoList[i]);
			}
			if(eggInfoList.length > XsgColorfullEggManager.EGG_NUM){
				throw new NoteException(Messages.getString("CornucopiaControler.notDoubleGet"));
			}
		}
		
		
		EggInfo egg = getRandomEggInfo(roleColorfullEgg.getRecord(),eggFlag);
		egg.id = eggFlag;
		eggs.add(egg);
		roleColorfullEgg.setBrokenTime(new Date());
		roleColorfullEgg.setRecord(TextUtil.GSON.toJson(eggs.toArray(new EggInfo[0])));

		roleDB.setRoleColorfullEgg(roleColorfullEgg);
		
		brokenEvent.onBroken(egg.id, eggs.size(), egg.itemId);
		return getView();
	}

	/**
	 * 随机一个彩蛋奖励配置
	 * 
	 * @param record 已经砸过的彩蛋数据
	 * @param eggFlag 彩蛋id   彩蛋1,2,3
	 * @return
	 */
	private EggInfo getRandomEggInfo(String record, byte eggFlag) {
		List<EggRewardConfT> rewardConf = XsgColorfullEggManager.getInstance().getRewardConf();
		Map<Integer, List<EggRewardsT>> rewardMap = XsgColorfullEggManager.getInstance().getRewardMap();
		
		//获取对应彩蛋的彩池
		int rewardType = getRewardType(eggFlag);
		List<EggRewardsT> list = rewardMap.get(rewardType);
		
		EggInfo egg = new EggInfo();
		EggRewardsT randomReward = null;
		if (TextUtil.isBlank(record)) {
			//本次砸蛋为第一次
			randomReward = getRandomProp(list);
			egg.itemId = randomReward.itemId;
			egg.num = NumberUtil.randomContain(randomReward.minNum,randomReward.maxNum);
		}else{//根据活动期间已经产生的领取记录生成本次砸蛋记录
			EggInfo[] eggInfoList = TextUtil.GSON.fromJson(record,
					new EggInfo[XsgColorfullEggManager.EGG_NUM].getClass());
			//统计砸蛋记录里面各个物品的砸出次数
			Map<String,Integer> eggBrokenStat = getBrokenItmeCounts(eggInfoList); 
			boolean flag = true;
			if(eggInfoList.length==1){
				//本次砸蛋为第二次
				for (EggRewardConfT confT : rewardConf) {
					if(confT.itemId.equals(eggInfoList[0].itemId)){
						int random = NumberUtil.randomContain(1, 100);
						if(random <= confT.pro2){
							egg.itemId = confT.itemId;
							egg.num = NumberUtil.randomContain(confT.minNum, confT.maxNum);
							flag = false;
							break;
						}
					}
				}
			}
			//本次砸蛋为第三次 且前两次奖励有重复
			if(eggInfoList.length==2 && eggBrokenStat.size() == 1){
				for (EggRewardConfT confT : rewardConf) {
					if(confT.itemId.equals(eggInfoList[0].itemId)){
						int random = NumberUtil.randomContain(1, 100);
						if(random <= confT.pro3){
							egg.itemId = confT.itemId;
							egg.num = NumberUtil.randomContain(confT.minNum, confT.maxNum);
							flag = false;
							break;
						}
					}
				}
			}
			
			if(flag){
				//前几次砸出物品各不相同或者没有从奖励控制表单中随机出奖励道具
				randomReward = getRandomProp(list);
				egg.itemId = randomReward.itemId;
				egg.num = NumberUtil.randomContain(randomReward.minNum,randomReward.maxNum);
			}
			
		}
		
		return egg;
	}

	private int getRewardType(byte eggFlag) {
		int value = 1;
		EggBasisConfT eggConf = XsgColorfullEggManager.getInstance().getEggConf();
		
		switch (eggFlag) {
		case 1:
			value = eggConf.firstEggRewardFlag;
			break;
		case 2:
			value = eggConf.secondEggRewardFlag;
			break;
		case 3:
			value = eggConf.thirdEggRewardFlag;
			break;
		default:
			value = 1;
		}
		
		return value;
	}

	@Override
	public void acceptReward(String itemId, int num) throws NoteException {
		boolean isOpen = XsgColorfullEggManager.getInstance().isOpen();
		if (!isOpen) {
			throw new NoteException(Messages.getString("FriendsRecallController.notOpen"));
		}

		EggBasisConfT eggConf = XsgColorfullEggManager.getInstance().getEggConf();

		if (roleRt.getLevel() < eggConf.limitLevel) {
			throw new NoteException(Messages.getString("AttackCastleController.levelNotEnough"));
		}

		init();
		// 获取最新砸蛋数据
		RoleColorfulEgg roleColorfullEgg = roleDB.getRoleColorfullEgg();
		/*boolean isReceived = XsgColorfullEggManager.getInstance().isCurrBetween(roleColorfullEgg.getReceiveTime());
		if (isReceived) {// 活动期间已经领取过的不再参与
			throw new NoteException(Messages.getString("CollectHeroSoulController.3"));
		}*/
		
		if(roleColorfullEgg.getAcceptNum() >= eggConf.limitNum){
			throw new NoteException(Messages.getString("CollectHeroSoulController.3"));
		}
		
		//当天更新时间
		Date receiveTime = roleColorfullEgg.getReceiveTime();
		Date refreshTime = DateUtil.joinTime(eggConf.refreshTime);
		if(receiveTime!=null &&
				!DateUtil.checkTime(receiveTime, refreshTime)){
			throw new NoteException(Messages.getString("CollectHeroSoulController.3"));
		}
		
		EggInfo[] eggInfoList = TextUtil.GSON.fromJson(roleColorfullEgg.getRecord(),
				new EggInfo[XsgColorfullEggManager.EGG_NUM].getClass());

		Map<String, Integer> eggBrokenStat = getBrokenItmeCounts(eggInfoList);

		if (eggInfoList.length != 3) {
			throw new NoteException(Messages.getString("TaskControler.8"));
		}
		// 奖励道具互不相同
		if (eggBrokenStat.size() == 3 && eggConf.rewardFlag == 0) {// 用户三选一领取
			boolean flag = false;
			for (EggInfo eggInfo : eggInfoList) {
				if (eggInfo.itemId.equals(itemId) && eggInfo.num == num) {
					num = eggInfo.num;
					flag = true;
				}
			}

			if (null == itemId) {// 当有三个不同奖励时，若用户未选择
				flag = true;
				int index = NumberUtil.random(0, XsgColorfullEggManager.EGG_NUM);
				itemId = eggInfoList[index].itemId;
				num = eggInfoList[index].num;
			}

			if (flag) {
				IItem item = roleRt.getRewardControler().acceptReward(itemId, num);
				roleColorfullEgg.setReceiveTime(new Date());
				roleColorfullEgg.setAcceptScriptId(itemId);
				roleColorfullEgg.setAcceptNum(roleColorfullEgg.getAcceptNum()+1);
				//sendAdnnounce(itemId,num,1,item);

				receiveEvent.onReceive(itemId, num);
			} else {
				throw new NoteException(Messages.getString("AttackCastleController.14"));
			}
			// 奖励道具有重复 itemId
		} else {
			boolean flag = true;
			if (eggBrokenStat.size() <= 0 || eggBrokenStat.size() >= 3) {// 检测是否真的有重复
				flag = false;
				throw new NoteException(Messages.getString("AttackCastleController.14"));
			}
			
			//砸出的三个道具如果有两个重复的，且玩家选择了不重复的奖励
			Integer val = eggBrokenStat.get(itemId);
			if(val!=null && val==1){
				for (EggInfo eggInfo : eggInfoList) {
					if (eggInfo.itemId.equals(itemId) && eggInfo.num == num) {
						num = eggInfo.num;
						flag = false;
					}
				}
				
				if (!flag) {
					IItem item = roleRt.getRewardControler().acceptReward(itemId, num);
					roleColorfullEgg.setReceiveTime(new Date());
					roleColorfullEgg.setAcceptScriptId(itemId);
					roleColorfullEgg.setAcceptNum(roleColorfullEgg.getAcceptNum()+1);
					//sendAdnnounce(itemId,num,1,item);

					receiveEvent.onReceive(itemId, num);
				} else {
					throw new NoteException(Messages.getString("AttackCastleController.14"));
				}
				
				return;
			}
			
			acceptRewardBy3(roleColorfullEgg, eggInfoList, eggBrokenStat, flag);

			acceptRewardBy2(roleColorfullEgg, eggInfoList, eggBrokenStat);
		}

		roleDB.setRoleColorfullEgg(roleColorfullEgg);

	}

	/**
	 * 领奖公告
	 * @param itemCode 领取物品
	 * @param num 领取数量
	 * @param i 领取类型 1-正常领取/2-2倍领取/5-5倍领取
	 * @param item 奖励道具
	 */
	private void sendAdnnounce(String itemID, int num, int i, IItem item) {
		
		List<ChatAdT> adTList = XsgChatManager.getInstance()
				.getAdContentMap(XsgChatManager.AdContentType.ColorfullEggAD);
		
		if (adTList != null && adTList.size() > 0) {
			XsgChatManager chat = XsgChatManager.getInstance();
			ChatAdT adT = null;
			
			if(i==1){
				adT = adTList.get(0);
			}
			
			if(i==2){
				adT = adTList.get(1);
			}
			if(i==5){
				adT = adTList.get(2);
			}
			
			if(adT == null){//跑马灯脚本配置出错
				return;
			}
			
			Map<String, String> replaceMap = new HashMap<String, String>();
			replaceMap.put("~role_id~", this.roleRt.getRoleId());
			replaceMap.put("~role_name~", this.roleRt.getName());
			replaceMap.put("~role_vip~", this.roleRt.getVipLevel() + "");
			if(item!=null){
				replaceMap.put("~item~", TextUtil.format("{0}|{1}|{2}", item.getTemplate().getItemType().ordinal(), itemID, ""));
			}else{
				AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(itemID);
				if(itemT==null){
					return;
				}
				replaceMap.put("~item~", TextUtil.format("{0}|{1}|{2}", itemT.getItemType().ordinal(), itemID, ""));
			}
			XsgChatManager.getInstance().sendAnnouncementItem(item , this.roleRt.getChatControler()
					.parseAdConent(chat.replaceRoleContent(adT.content, this.roleRt), replaceMap));
		}
		
	}

	/**
	 * 砸出2个相同的道具，2倍的道具奖励
	 * */
	private void acceptRewardBy2(RoleColorfulEgg roleColorfullEgg, EggInfo[] eggInfoList,
			Map<String, Integer> eggBrokenStat) throws NoteException {
		//砸出2个一样的道具，可获得相同道具2倍的奖励
		if(eggBrokenStat.size()==2){
			//获取砸出两次的道具
			String item = "";
			int rewardNum = 0;
			int temp = 0;
			for (Entry<String, Integer> entry : eggBrokenStat.entrySet()) {
				if(entry.getValue()==2){
					item = entry.getKey();
					break;
				}
			}
			
			//计算奖励道具数量
			for (EggInfo eggInfo : eggInfoList) {
				if(item.equals(eggInfo.itemId)){
					temp++;
					rewardNum += eggInfo.num;
				}
			}
			
			if(rewardNum>0 && temp==2){
				IItem acceptReward = roleRt.getRewardControler().acceptReward(item, rewardNum*2);
				roleColorfullEgg.setReceiveTime(new Date());
				roleColorfullEgg.setAcceptScriptId(item);
				roleColorfullEgg.setAcceptNum(roleColorfullEgg.getAcceptNum()+1);
				sendAdnnounce(item,rewardNum*2,2,acceptReward);
				
				receiveEvent.onReceive(item, rewardNum*2);
			}else{
				throw new NoteException(Messages.getString("AttackCastleController.14"));
			}
			
		}
	}
	
	/**
	 * 砸出三个相同的道具，5倍的道具奖励
	 * */
	private void acceptRewardBy3(RoleColorfulEgg roleColorfullEgg, EggInfo[] eggInfoList,
			Map<String, Integer> eggBrokenStat, boolean flag) throws NoteException {
		//三个道具奖励都是同一个道具
		if(eggBrokenStat.size()==1){
			String item = eggInfoList[0].itemId;
			int rewardNum = 0;
			for (EggInfo eggInfo : eggInfoList) {
				if(!item.equals(eggInfo.itemId)){
					flag = false;
					break;
				}else{
					rewardNum += eggInfo.num;
				}
			}
			
			if(flag){
				IItem acceptReward = roleRt.getRewardControler().acceptReward(item, rewardNum*5);
				roleColorfullEgg.setReceiveTime(new Date());
				roleColorfullEgg.setAcceptScriptId(item);
				roleColorfullEgg.setAcceptNum(roleColorfullEgg.getAcceptNum()+1);
				sendAdnnounce(item,rewardNum*5,5,acceptReward);
				
				receiveEvent.onReceive(item, rewardNum*5);
			}else{
				throw new NoteException(Messages.getString("AttackCastleController.14"));
			}
			
		}
	}

	/**
	 * 按照脚本权重，随机获取一个砸蛋奖励
	 * 
	 * @param list
	 *            彩蛋奖励集合
	 * @return
	 */
	private EggRewardsT getRandomProp(List<EggRewardsT> list) {

		Map<Integer, EggRewardsT> rewardPool = XsgColorfullEggManager.getInstance().getRewardPool();

		List<ColorfullEggController.RandomProp> randomProps = new ArrayList<ColorfullEggController.RandomProp>();
		for (EggRewardsT p : list) {
			randomProps.add(new RandomProp(p.id, p.weight));
		}

		if (randomProps.size() <= 0) {
			return null;
		}

		RandomRange<RandomProp> randomRewardGen = new RandomRange<RandomProp>(randomProps);
		RandomProp randomReward = randomRewardGen.random();
		return rewardPool.get(randomReward.id);

	}
	
	static class RandomProp implements IRandomHitable {
		public int id;
		public int rank;

		public RandomProp(int id, int rank) {
			this.id = id;
			this.rank = rank;
		}

		@Override
		public int getRank() {
			return rank;
		}
	}

	/**
	 * 统计指定集合中彩蛋奖励道具的数量
	 * @param eggInfoList 玩家砸蛋获取的奖励道具信息
	 * @return
	 */
	private Map<String, Integer> getBrokenItmeCounts(EggInfo[] eggInfoList) {

		Map<String, Integer> map= new HashMap<String, Integer>();
		for (EggInfo eggInfo : eggInfoList) {
			Integer value = map.get(eggInfo.itemId);
			if(value == null || value == 0){
				map.put(eggInfo.itemId, 1);
			}else{
				map.put(eggInfo.itemId, value+1);
			}
		}
		
		return map;
	}

	@Override
	public void onRoleLevelup() {

		boolean isOpen = XsgColorfullEggManager.getInstance().isOpen();
		if (!isOpen) {
			return;
		}

		EggBasisConfT eggConf = XsgColorfullEggManager.getInstance().getEggConf();

		if (eggConf == null) {
			return;
		} else if (eggConf.limitLevel > roleRt.getLevel() || eggConf.entryFlag != 1) {
			return;
		}

		ChatCallbackPrx prx = this.roleRt.getChatControler().getChatCb();
		if (prx != null) {
			try {
				prx.begin_getColorfulEggView(LuaSerializer.serialize(this.getView()));
			} catch (NoteException e) {
			}
		}

	}
}
