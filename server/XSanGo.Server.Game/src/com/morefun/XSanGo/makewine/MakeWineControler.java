package com.morefun.XSanGo.makewine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.AMD_MakeWineInfo_receiveShare;
import com.XSanGo.Protocol.AMD_MakeWineInfo_shareView;
import com.XSanGo.Protocol.AMD_MakeWineInfo_topUp;
import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.MakeResult;
import com.XSanGo.Protocol.MakeWineAwardInfo;
import com.XSanGo.Protocol.MakeWineExchangeItem;
import com.XSanGo.Protocol.MakeWineExchangeView;
import com.XSanGo.Protocol.MakeWineMaterialView;
import com.XSanGo.Protocol.MakeWineScoreAwardView;
import com.XSanGo.Protocol.MakeWineScoreRank;
import com.XSanGo.Protocol.MakeWineScoreRankAward;
import com.XSanGo.Protocol.MakeWineScoreRankAwardItem;
import com.XSanGo.Protocol.MakeWineScoreRankItem;
import com.XSanGo.Protocol.MakeWineShareRecord;
import com.XSanGo.Protocol.MakeWineShareTarget;
import com.XSanGo.Protocol.MakeWineShareView;
import com.XSanGo.Protocol.MakeWineView;
import com.XSanGo.Protocol.NotEnoughException;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.OneWineItem;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.MakeWineShareRecordDao;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleMakeWine;
import com.morefun.XSanGo.db.game.RoleMakeWineShareRecord;
import com.morefun.XSanGo.event.IEventControler;
import com.morefun.XSanGo.event.protocol.IRoleHeadAndBorderChange;
import com.morefun.XSanGo.event.protocol.IRoleLevelup;
import com.morefun.XSanGo.event.protocol.IRoleNameChange;
import com.morefun.XSanGo.event.protocol.IVipLevelUp;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.TextUtil;


/**
 * 酿酒
 * @author zhuzhi.yang
 *
 */
@RedPoint(isTimer = true)
public class MakeWineControler implements IMakeWineControler, IRoleNameChange, IRoleHeadAndBorderChange, IRoleLevelup, IVipLevelUp {

	private IRole roleRt;
	private RoleMakeWine roleMakeWine;
	private MakeWineShareRecordDao shareRecordDao;
	private int topedQuality = 4; //最高品质
	
	public MakeWineControler(IRole rt, Role db) {
		this.roleRt = rt;
		
		IEventControler eventControler = rt.getEventControler();
		eventControler.registerHandler(IRoleHeadAndBorderChange.class, this);
		
		shareRecordDao = MakeWineShareRecordDao.getFromApplicationContext(ServerLancher.getAc());
		roleMakeWine = XsgMakeWineManager.getInstance().getMapRoleInfos().get(this.roleRt.getRoleId());
	}
	
	/**
	 * 检测数据是否需要重置
	 * (因为登录要给玩家推送红点的原因, 在登录的时候就重置了数据)
	 */
	@Override
	public void checkUpdate(){
		boolean needUpdate = false; // 是否需要更新数据库
		if(this.roleMakeWine == null) {
			this.roleMakeWine = new RoleMakeWine(this.roleRt.getRoleId(), this.roleRt.getName(), roleRt.getVipLevel(), roleRt.getLevel(), roleRt.getHeadImage());
			this.roleMakeWine.setItemComposedCount(TextUtil.GSON.toJson(XsgMakeWineManager.getInstance().DEFAULT_COMPOSED_COUNT));
			
			XsgMakeWineManager.getInstance().getMapRoleInfos().put(this.roleRt.getRoleId(), this.roleMakeWine);
		}
		
		// 登录重置 领取材料时间，当日已合成的次数，领取分享的时间，
		Date now = new Date();
		if(this.roleMakeWine.getReceiveMaterialDate() != null
				&& DateUtil.diffDate(now, this.roleMakeWine.getReceiveMaterialDate()) < 0) {
			roleMakeWine.setReceiveMaterialDate(null);
			needUpdate = true;
		}
		if(this.roleMakeWine.getResetDate() == null 
				|| DateUtil.diffDate(now, this.roleMakeWine.getResetDate()) > 0) {
			this.roleMakeWine.setItemComposedCount(TextUtil.GSON.toJson(XsgMakeWineManager.getInstance().DEFAULT_COMPOSED_COUNT));
			this.roleMakeWine.setTopedTimes(0);
			this.roleMakeWine.setResetDate(now);
			needUpdate = true;
		}
		
		if(needUpdate) {
			XsgMakeWineManager.getInstance().saveRoleMakeWine2DB(this.roleMakeWine);
		}
	}
	
	/**
	 * 酿酒界面 
	 */
	@Override
	public String makeWineView() throws NoteException {
		MakeWineConfigT configT = XsgMakeWineManager.getInstance().makeWineConfig;
		Map<Integer, MakeWineShareT> map_make_share = XsgMakeWineManager.getInstance().map_make_share;
		List<MaterialReceiveT> list_material_receive = XsgMakeWineManager.getInstance().list_material_receive;
		List<MakeWineScoreTargetT> list_score_target = XsgMakeWineManager.getInstance().list_score_target;
		
		// 检测数据是否需要重置
		checkUpdate();
		
		// 酿酒目标和材料集合
		List<OneWineItem> list_wine = new ArrayList<OneWineItem>();
		int[] itemComposedCount = TextUtil.GSON.fromJson(this.roleMakeWine.getItemComposedCount(), int[].class);
		Iterator<Integer> iter = map_make_share.keySet().iterator();
		while(iter.hasNext()) {
			int id = iter.next();
			MakeWineShareT ws = map_make_share.get(id);
			OneWineItem wine = new OneWineItem(ws.id, ws.item, ws.list_needItem.toArray(new IntString[0]), ws.compoundNum - itemComposedCount[id - 1]);
			list_wine.add(wine);
		}

		Calendar now = Calendar.getInstance();
		// 下次可领取的材料
		MaterialReceiveT materialReceive = list_material_receive.get(0); 
		// 下次领取时间默认第一个
		Calendar c_receiveTime = Calendar.getInstance();
		c_receiveTime.setTime(materialReceive.receiveTime);
		c_receiveTime.set(Calendar.DATE, now.get(Calendar.DATE));
		c_receiveTime.set(Calendar.MONTH, now.get(Calendar.MONTH));
		c_receiveTime.set(Calendar.YEAR, now.get(Calendar.YEAR));
		
		int receiveMaterialState = 0; // 领材料的状态
		// 全部已领光的状态
		MaterialReceiveT mr_last = list_material_receive.get(list_material_receive.size() - 1);
		Calendar c_last = Calendar.getInstance();
		c_last.setTime(mr_last.receiveTime);
		c_last.set(Calendar.DATE, now.get(Calendar.DATE));
		c_last.set(Calendar.MONTH, now.get(Calendar.MONTH));
		c_last.set(Calendar.YEAR, now.get(Calendar.YEAR));
		if(this.roleMakeWine.getReceiveMaterialDate() != null
				&& this.roleMakeWine.getReceiveMaterialDate().getTime() > c_last.getTimeInMillis()) {
			receiveMaterialState = 2;
			materialReceive = mr_last;
			c_receiveTime = c_last;
		} else { 
			// 可领的状态
			int receiveIndex = -1;
			for(int i = 0; i < list_material_receive.size(); i++){
				MaterialReceiveT mr = list_material_receive.get(i);
				Calendar c_temp = Calendar.getInstance();
				c_temp.setTime(mr.receiveTime);
				c_temp.set(Calendar.DATE, now.get(Calendar.DATE));
				c_temp.set(Calendar.MONTH, now.get(Calendar.MONTH));
				c_temp.set(Calendar.YEAR, now.get(Calendar.YEAR));
				
				if(c_temp.getTimeInMillis() < now.getTimeInMillis()
						&& (this.roleMakeWine.getReceiveMaterialDate() == null 
								|| c_temp.getTimeInMillis() > this.roleMakeWine.getReceiveMaterialDate().getTime())) {
					receiveMaterialState = 1;
					materialReceive = mr;
					c_receiveTime = c_temp;
					receiveIndex = i;
				}
			}
			// 不可领状态
			if(receiveMaterialState == 1 
					&& receiveIndex < list_material_receive.size() - 1
					&& receiveIndex > -1){
				MaterialReceiveT mr_next = list_material_receive.get(receiveIndex + 1);
				Calendar c_temp_next = Calendar.getInstance();
				c_temp_next.setTime(mr_next.receiveTime);
				c_temp_next.set(Calendar.DATE, now.get(Calendar.DATE));
				c_temp_next.set(Calendar.MONTH, now.get(Calendar.MONTH));
				c_temp_next.set(Calendar.YEAR, now.get(Calendar.YEAR));
				if(c_temp_next.getTimeInMillis() < now.getTimeInMillis()) {
					receiveMaterialState = 0;
				}
			}
			if(receiveMaterialState == 0) {	
				for(MaterialReceiveT mr : list_material_receive){
					Calendar c_temp = Calendar.getInstance();
					c_temp.setTime(mr.receiveTime);
					c_temp.set(Calendar.DATE, now.get(Calendar.DATE));
					c_temp.set(Calendar.MONTH, now.get(Calendar.MONTH));
					c_temp.set(Calendar.YEAR, now.get(Calendar.YEAR));
					
					if(c_temp.getTimeInMillis() > now.getTimeInMillis()) {
						receiveMaterialState = 0;
						materialReceive = mr;
						c_receiveTime = c_temp;
						break;
					}
				}
			}
		}
		
		// 下次可领取的积分奖励
		int receiveScoreState = 0;
		MakeWineScoreTargetT makeWineScoreTarget = null;
		for(MakeWineScoreTargetT mst : list_score_target){
			// 未领取
			if(mst.compoundScoreGoal > this.roleMakeWine.getReceiveSocre()) {
				makeWineScoreTarget = mst;
				// 可领取
				if(mst.compoundScoreGoal <= this.roleMakeWine.getComposeScore()){
					receiveScoreState = 1;
				}
				break;
			}
		}
		// 全部都领取了,则返回最后一个
		if(makeWineScoreTarget == null){
			makeWineScoreTarget = list_score_target.get(list_score_target.size() - 1);
			receiveScoreState = 2;
		}
		
		MakeWineView view = new MakeWineView(
				list_wine.toArray(new OneWineItem[0]), 
				(configT.endTime.getTime() - System.currentTimeMillis()) / 1000, 
				new IntString(materialReceive.num, materialReceive.itemID),
				(c_receiveTime.getTimeInMillis() - now.getTimeInMillis()) / 1000,
				receiveMaterialState,
				this.roleMakeWine.getComposeScore(),
				new IntString(makeWineScoreTarget.itemNum, makeWineScoreTarget.itemID), 
				makeWineScoreTarget.compoundScoreGoal, 
				receiveScoreState
				);
		return LuaSerializer.serialize(view);
	}

	/**
	 * 定时领取材料
	 * @throws NoteException
	 */
	@Override
	public String receiveMaterial() throws NoteException {
		List<MaterialReceiveT> list_material_receive = XsgMakeWineManager.getInstance().list_material_receive;
		MaterialReceiveT materialReceive = null; 
		Calendar c_receiveTime = null;
		Calendar now = Calendar.getInstance();
		int receivedIndex = 0; // 领取的索引
		for(int i = 0; i < list_material_receive.size(); i ++){
			MaterialReceiveT mr = list_material_receive.get(i);
			Calendar c = Calendar.getInstance();
			c.setTime(mr.receiveTime);
			c.set(Calendar.DATE, now.get(Calendar.DATE));
			c.set(Calendar.MONTH, now.get(Calendar.MONTH));
			c.set(Calendar.YEAR, now.get(Calendar.YEAR));
			
			// 获取当前可领的时间段
			if(roleMakeWine.getReceiveMaterialDate() == null 
					|| (c.getTime().getTime() <= now.getTimeInMillis()
						&& c.getTimeInMillis() > roleMakeWine.getReceiveMaterialDate().getTime())) {
				materialReceive = mr;
				c_receiveTime = c;
				receivedIndex = i;
			}
		}
		// 时间未到
		if(materialReceive == null) {
			throw new NoteException(Messages.getString("MakeWine.cdstatus"));
		}
		
		// 已领取
		if(this.roleMakeWine.getReceiveMaterialDate() != null
				&& c_receiveTime.getTime().getTime() < this.roleMakeWine.getReceiveMaterialDate().getTime()) {
			throw new NoteException(Messages.getString("ApiController.4"));
		}
		
		this.roleMakeWine.setReceiveMaterialDate(now.getTime());
		
		XsgMakeWineManager.getInstance().saveRoleMakeWine2DB(this.roleMakeWine);
		
		// 领奖
		this.roleRt.getRewardControler().acceptReward(materialReceive.itemID, materialReceive.num);
		
		// 返回下一个材料和领取时间
		int nextReceiveState = 2; // 默认最后一个
		MaterialReceiveT nextMaterial = materialReceive;
		if(receivedIndex < list_material_receive.size() - 1) {
			nextMaterial = list_material_receive.get(receivedIndex + 1);
			nextReceiveState = 0;
		}
		// 下次领取的时间
		Calendar c_next = Calendar.getInstance();
		c_next.setTime(nextMaterial.receiveTime);
		c_next.set(Calendar.DATE, now.get(Calendar.DATE));
		c_next.set(Calendar.MONTH, now.get(Calendar.MONTH));
		c_next.set(Calendar.YEAR, now.get(Calendar.YEAR));
		
		return LuaSerializer.serialize(new MakeWineMaterialView(
				new IntString(nextMaterial.num, nextMaterial.itemID), 
				new IntString(this.roleRt.getItemControler().getItemCountInPackage(materialReceive.itemID), materialReceive.itemID),
				(c_next.getTimeInMillis() - System.currentTimeMillis()) / 1000, 
				nextReceiveState));
	}

	/**
	 * 领取积分奖励
	 * @throws NoteException
	 */
	@Override
	public String receiveScoreAward() throws NoteException {
		List<MakeWineScoreTargetT> list_score_target = XsgMakeWineManager.getInstance().list_score_target;
		MakeWineScoreTargetT makeWineScoreTarget = null;
		int receivedScoreAwardIndex = -1; // 已领取的积分奖励索引
		for(int i = 0; i < list_score_target.size(); i++){
			MakeWineScoreTargetT mst = list_score_target.get(i);
			// 未领取
			if(mst.compoundScoreGoal > this.roleMakeWine.getReceiveSocre()
					&& mst.compoundScoreGoal <= this.roleMakeWine.getComposeScore()) {
				makeWineScoreTarget = mst;
				receivedScoreAwardIndex = i;
				break;
			}
		}
		// 全部都领取了,显示的是最后一个,但不可领取
		if(makeWineScoreTarget == null){
			throw new NoteException(Messages.getString("OpenServerActiveControler.0"));
		}
		
		this.roleMakeWine.setReceiveSocre(makeWineScoreTarget.compoundScoreGoal);
		
		XsgMakeWineManager.getInstance().saveRoleMakeWine2DB(this.roleMakeWine);
		
		// 领奖
		this.roleRt.getRewardControler().acceptReward(makeWineScoreTarget.itemID, makeWineScoreTarget.itemNum);
		
		// 返回下一档次的积分奖励
		int nextReceiveScoreState = 2; // 默认已领取全部 
		MakeWineScoreTargetT nextScoreTarget = makeWineScoreTarget;
		if(receivedScoreAwardIndex < list_score_target.size() - 1) {
			nextScoreTarget = list_score_target.get(receivedScoreAwardIndex + 1);
			nextReceiveScoreState = 0;
			// 可领取
			if(nextScoreTarget.compoundScoreGoal <= this.roleMakeWine.getComposeScore()){
				nextReceiveScoreState = 1;
			}
		}
		
		return LuaSerializer.serialize(new MakeWineScoreAwardView(
				new IntString(nextScoreTarget.itemNum, nextScoreTarget.itemID), 
				nextScoreTarget.compoundScoreGoal, 
				nextReceiveScoreState));
	}

	/**
	 * 酿酒
	 * @param id合成目标,0:全部酿酒
	 * @param type: 0:酿酒一瓶, 1:全部酿酒,酿完该种类的酒 
	 * @return 生成的目标和扣除材料的数量
	 * @throws NoteException
	 */
	@Override
	public String make(int targetId, int type) throws NotEnoughMoneyException, NotEnoughYuanBaoException, NotEnoughException, NoteException {
		Map<Integer, MakeWineShareT> map_make_share = XsgMakeWineManager.getInstance().map_make_share;
		List<MakeWineScoreTargetT> list_score_target = XsgMakeWineManager.getInstance().list_score_target;
		MakeWineShareT ws = map_make_share.get(targetId);
		// 非法targetId参数
		if(ws == null) {
			throw new NoteException(Messages.getString("ResourceBackControler.invalidParam"));
		}
		for(IntString is : ws.list_needItem) {
			// 校验物品数量
			if(this.roleRt.getItemControler().getItemCountInPackage(is.strValue) < is.intValue){
				throw new NoteException(Messages.getString("HeroAdmireControler.7"));
			}
		}
		
		// 已酿造的次数
		int[] itemComposedCount = TextUtil.GSON.fromJson(this.roleMakeWine.getItemComposedCount(), int[].class);
		// 每日酿酒数量已达上限
		if(ws.compoundNum != -1 
				&& itemComposedCount[targetId - 1] >= ws.compoundNum) {
			throw new NoteException(Messages.getString("CollectHeroSoulController.3"));
		}
		
		int composeCount = 1; // 可合成的数量
		if(type == 1){ // 全部酿造
			composeCount = Integer.MAX_VALUE;
			for(IntString is : ws.list_needItem) {
				// 拥有的材料数
				int hasCount = this.roleRt.getItemControler().getItemCountInPackage(is.strValue);
				if(hasCount / is.intValue < composeCount) {
					composeCount = hasCount / is.intValue; // 最多可合成多少个
				}
			}
		}
		// 可酿造次数不能超过上限
		if(ws.compoundNum != -1
				&& itemComposedCount[targetId - 1] + composeCount > ws.compoundNum) {
			composeCount = ws.compoundNum - itemComposedCount[targetId - 1];
		}
		
		List<IntString> list_material_last = new ArrayList<IntString>(); // 剩余材料数量
		// 扣除材料
		for(IntString is : ws.list_needItem) {
			this.roleRt.getRewardControler().acceptReward(is.strValue, -is.intValue * composeCount);
			
			list_material_last.add(new IntString(this.roleRt.getItemControler().getItemCountInPackage(is.strValue), is.strValue));
		}
		
		// 更新已酿造的次数
		itemComposedCount[targetId - 1] += composeCount;
		this.roleMakeWine.setItemComposedCount(TextUtil.GSON.toJson(itemComposedCount));
		
		// 获得酿酒积分
		int gotScore = ws.compoundScore * composeCount;
		this.roleMakeWine.setComposeScore(this.roleMakeWine.getComposeScore() + gotScore);
		
		// 保存数据库
		XsgMakeWineManager.getInstance().saveRoleMakeWine2DB(roleMakeWine);
		
		// 生成目标			
		this.roleRt.getRewardControler().acceptReward(ws.item, composeCount);
		
		// 下次可领取的积分奖励,默认未领取
		int receiveScoreState = 0;
		MakeWineScoreTargetT makeWineScoreTarget = null;
		for(MakeWineScoreTargetT mst : list_score_target){
			// 未领取
			if(mst.compoundScoreGoal > this.roleMakeWine.getReceiveSocre()) {
				makeWineScoreTarget = mst;
				// 可领取
				if(mst.compoundScoreGoal <= this.roleMakeWine.getComposeScore()){
					receiveScoreState = 1;
				}
				break;
			}
		}
		// 全部都领取了
		if(makeWineScoreTarget == null){
			receiveScoreState = 2;
		}
		
		// 记录酿酒日志 //【05-06 17：30：59】,酿造【果酒×10】，【酿造积分+10】，【兑换积分+10】
		List<String> list_log = XsgMakeWineManager.getInstance().map_make_log.get(this.roleRt.getRoleId());
		if(list_log == null){
			list_log = new ArrayList<String>();
		}
		if(list_log.size() >= XsgMakeWineManager.getInstance().RANK_LIMIT) {
			list_log.remove(0);
		}
		AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(ws.item);
		list_log.add(String.format(Messages.getString("MakeWine.MakeLog"), 
				DateUtil.format(new Date(), "MM-dd HH:mm:ss"), 
				"<font color='ff" + roleRt.getItemControler().getItemColor(itemT.getColor().value()) + "'>" + itemT.getName() + "</font>", 
				composeCount, 
				gotScore,
				gotScore));
		XsgMakeWineManager.getInstance().map_make_log.put(this.roleRt.getRoleId(), list_log);
		
		// 返回当前的目标数量和材料数量
		return LuaSerializer.serialize(new MakeResult(this.roleRt.getItemControler().getItemCountInPackage(ws.item),
				list_material_last.toArray(new IntString[0]), 
				composeCount, 
				this.roleMakeWine.getComposeScore(), 
				receiveScoreState, 
				gotScore));
	}
	
	/**
	 * 分享界面
	 * onlyFriends：0:全部  1:只看好友 2:自己的分享
	 */
	@Override
	public void shareView(final AMD_MakeWineInfo_shareView __cb, int condition, final int startIndex) throws NoteException {
		getShareView(new ShareViewCallBack() {

			@Override
			public void onGetShareView(MakeWineShareView shareview) {
				__cb.ice_response(LuaSerializer.serialize(shareview));
			}
			
		}, condition, startIndex);
	}
	
	/**
	 * 获取MakeWineShareView
	 */
	private void getShareView(final ShareViewCallBack callback, int condition, final int startIndex) {
		final MakeWineConfigT configT = XsgMakeWineManager.getInstance().makeWineConfig;
		Map<Integer, MakeWineShareT> map_make_share = XsgMakeWineManager.getInstance().map_make_share;
		
		// 分享目标
		final List<MakeWineShareTarget> list_share = new ArrayList<MakeWineShareTarget>();
		Iterator<Integer> iter = map_make_share.keySet().iterator();
		while(iter.hasNext()) {
			int id = iter.next();
			MakeWineShareT ws = map_make_share.get(id);
			list_share.add(new MakeWineShareTarget(ws.id, ws.item, ws.shareNum));
		}
		
		//分享记录
		final StringBuffer sql = new StringBuffer("");
		if(condition == 1) { // 只看好友
			List<String> list_friends = new ArrayList<String>(this.roleRt.getSnsController().getFriends());
			for(int i = 0; i < list_friends.size(); i ++){
				sql.append("'").append(list_friends.get(i)).append("'");
				if(i < list_friends.size() - 1) {
					sql.append(",");
				}
			}
		} else if(condition == 2) { // 自己的分享
			sql.append("'").append(this.roleRt.getRoleId()).append("'");
		}
		
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				List<RoleMakeWineShareRecord> list_role_record = shareRecordDao.findRecords(sql.toString(), startIndex);
				
				List<MakeWineShareRecord> list_record = new ArrayList<MakeWineShareRecord>();
				for(RoleMakeWineShareRecord roleMakeWineShareReocrd : list_role_record) {
					int state = 0;
					List<String> list_received_player = null;
					String[] receivedPlayers = TextUtil.GSON.fromJson(roleMakeWineShareReocrd.getReceivedPlayers(), String[].class);
					if(receivedPlayers != null) {
						list_received_player = new ArrayList<String>(Arrays.asList(receivedPlayers));						
					}
					if(list_received_player != null 
							&& list_received_player.size() > 0 
							&& list_received_player.contains(roleRt.getRoleId())) { // 已领取
						state = 2;
					} else {
						state = roleMakeWineShareReocrd.getLastCount() > 0 ? 1 : 0;
					}
					list_record.add(new MakeWineShareRecord(
							roleMakeWineShareReocrd.getId(), 
							XsgMakeWineManager.getInstance().map_make_share.get(roleMakeWineShareReocrd.getConfigID()).item, 
							roleMakeWineShareReocrd.getRoleId(),
							roleMakeWineShareReocrd.getRoleName(), 
							roleMakeWineShareReocrd.getLastCount(), 
							roleRt.getSnsController().getFriends().contains(roleMakeWineShareReocrd.getRoleId()) ? 1 : 0,
							state, 
							roleMakeWineShareReocrd.getConfigID() < topedQuality ? 2 :roleMakeWineShareReocrd.getTop(), 
							configT.upPrice, 
							roleMakeWine.getTopedTimes(),
							DateUtil.format(roleMakeWineShareReocrd.getTopTime(), "MM-dd HH:mm:ss"), 
							XsgMakeWineManager.getInstance().map_make_share.get(roleMakeWineShareReocrd.getConfigID()).shareScore));
				}
				
				MakeWineShareView shareview = new MakeWineShareView(
						roleMakeWine.getReceiveShareDate() == null ? 0 
								: (roleMakeWine.getReceiveShareDate().getTime() + configT.shareInterval * 60 * 1000 - System.currentTimeMillis()) / 1000, 
						list_share.toArray(new MakeWineShareTarget[0]),
						list_record.toArray(new MakeWineShareRecord[0]), 
						shareRecordDao.findTotalRecords(sql.toString()), 
						roleMakeWine.getShareScore());
				
				callback.onGetShareView(shareview);
			}
		});
	}
	
	public static interface ShareViewCallBack {
		void onGetShareView(MakeWineShareView shareview);
	}

	/**
	 * 分享
	 * id:目标
	 * count:多少组
	 */
	@Override
	public void share(int id, final int count) throws NoteException {
		Map<Integer, MakeWineShareT> map_make_share = XsgMakeWineManager.getInstance().map_make_share;
		final MakeWineShareT ws = map_make_share.get(id);
		
		// 负数非法参数
		if(count <= 0) {
			throw new NoteException(Messages.getString("ResourceBackControler.invalidParam"));
		}
		
		// 校验是否可分享
		if(ws.isShare == 0) {
			throw new NoteException(Messages.getString("MakeWine.canShare"));
		}
		
		// 校验道具是否足够
		int needCount = count * ws.shareNum;
		if(this.roleRt.getItemControler().getItemCountInPackage(ws.item) < needCount) {
			throw new NoteException(Messages.getString("HeroAdmireControler.7"));
		}
		
		// 扣除道具
		this.roleRt.getItemControler().changeItemByTemplateCode(ws.item, -count * ws.shareNum);
		
		// 分享获得道具
		this.roleRt.getItemControler().changeItemByTemplateCode(ws.shareItem, count * ws.selfNum);
		
		// 保存分享记录
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				// id, roleId, itemId, roleName, roleLevel, roleVip, roleHeadImg, lastCount
				Date now = new Date();
				shareRecordDao.save(new RoleMakeWineShareRecord(
						GlobalDataManager.getInstance().generatePrimaryKey(),
						roleRt.getRoleId(), 
						ws.id, 
						roleRt.getName(), 
						count * ws.lootAmount, // 分享的数量=组*其他玩家可抢瓶数
						now, 
						now));
			}
		});
	}
	
	/**
	 * 领取分享奖励
	 */
	@Override
	public void receiveShare(final AMD_MakeWineInfo_receiveShare __cb, final String recordId, final int condition, final int startIndex) throws NoteException {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				final MakeWineConfigT configT = XsgMakeWineManager.getInstance().makeWineConfig;
				final Map<Integer, MakeWineShareT> map_make_share = XsgMakeWineManager.getInstance().map_make_share;
				
				// 不存在的记录
				final RoleMakeWineShareRecord roleMakeWineShareRecord = shareRecordDao.findById(recordId);
				if(roleMakeWineShareRecord == null) {
					__cb.ice_exception(new NoteException(Messages.getString("ShareControler.0")));
					return;
				}
				
				// 不能领取自己分享的
				if(roleMakeWineShareRecord.getRoleId().equalsIgnoreCase(roleRt.getRoleId())) {
					__cb.ice_exception(new NoteException(Messages.getString("MakeWine.canReceiveMine")));
					return;
				}
				
				// 当前配置
				MakeWineShareT ws = map_make_share.get(roleMakeWineShareRecord.getConfigID());
				
				// CD 冷却中
				if(roleMakeWine.getReceiveShareDate() != null 
						&& roleMakeWine.getReceiveShareDate().getTime() + configT.shareInterval * 60 * 1000 >  System.currentTimeMillis()) {
					__cb.ice_exception(new NoteException(Messages.getString("MakeWine.cdstatus")));
					return;
				}
				
				// 剩余数量不足
				if(roleMakeWineShareRecord.getLastCount() < ws.lootNum) {
					__cb.ice_exception(new NoteException(Messages.getString("SeckillControler.9")));
					return;
				}
				
				// 已经抢过了
				String[] recevedPlayers = TextUtil.GSON.fromJson(roleMakeWineShareRecord.getReceivedPlayers(), String[].class);
				List<String> list_received_players = new ArrayList<String>();
				if(recevedPlayers != null){
					list_received_players = new ArrayList<String>(Arrays.asList(recevedPlayers));
				}
				if(list_received_players.contains(roleRt.getRoleId())) {
					__cb.ice_exception(new NoteException(Messages.getString("MakeWine.hasReceived")));
					return;
				}

				// 更新XX抢到了该记录
				list_received_players.add(roleRt.getRoleId());
				roleMakeWineShareRecord.setReceivedPlayers(TextUtil.GSON.toJson(list_received_players.toArray(new String[0])));
				
				// 领取分享的时间
				roleMakeWine.setReceiveShareDate(new Date());
				XsgMakeWineManager.getInstance().saveRoleMakeWine2DB(roleMakeWine);
				
				// 扣除被抢的瓶数
				roleMakeWineShareRecord.setLastCount(roleMakeWineShareRecord.getLastCount() - ws.lootNum);

				// 【【分享的人】】获得分享积分
				RoleMakeWine shareRoleMakeWine = XsgMakeWineManager.getInstance().getMapRoleInfos().get(roleMakeWineShareRecord.getRoleId());
				shareRoleMakeWine.setShareScore(shareRoleMakeWine.getShareScore() + ws.shareScore * ws.lootNum);
				XsgMakeWineManager.getInstance().saveRoleMakeWine2DB(shareRoleMakeWine);
				
				// 获得抢到的道具
//				roleRt.getItemControler().changeItemByTemplateCode(ws.item, ws.lootNum);
				
				// 领取分享获得奖励道具
				roleRt.getItemControler().changeItemByTemplateCode(ws.shareReceiveItem, ws.getNum);
				
				// 保存数据库
				XsgMakeWineManager.getInstance().saveShareRecord2DB(roleMakeWineShareRecord);
				
				// 记录 被领取 日志 //【05-06 17：30：59】,玩家【玩家ID】领取你分享的【葡萄酒X2】，【分享积分+10】
				List<String> list_log = XsgMakeWineManager.getInstance().map_receive_log.get(shareRoleMakeWine.getRoleId());
				if(list_log == null){
					list_log = new ArrayList<String>();
				}
				if(list_log.size() >= XsgMakeWineManager.getInstance().RANK_LIMIT) {
					list_log.remove(0);
				}
				AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(ws.item);
				list_log.add(String.format(Messages.getString("MakeWine.ReceiveLog"), 
						DateUtil.format(new Date(), "MM-dd HH:mm:ss"), 
						"<font color='ff" + roleRt.getVipController().getVipColor() + "'>" + roleRt.getName() +  "</font>",
						"<font color='ff" + roleRt.getItemControler().getItemColor(itemT.getColor().value()) + "'>"+itemT.getName()+"</font>", 
						ws.lootNum, 
						ws.shareScore * ws.lootNum));
				XsgMakeWineManager.getInstance().map_receive_log.put(shareRoleMakeWine.getRoleId(), list_log);
				
				// 返回分享界面数据
				getShareView(new ShareViewCallBack() {

					@Override
					public void onGetShareView(MakeWineShareView shareview) {
						__cb.ice_response(LuaSerializer.serialize(shareview));
					}
					
				}, condition, startIndex);
			}
		});
	}
	
	/**
	 * 置顶
	 */
	@Override
	public void topUp(final AMD_MakeWineInfo_topUp __cb, final String id) throws NoteException, NotEnoughYuanBaoException {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				MakeWineConfigT configT = XsgMakeWineManager.getInstance().makeWineConfig;
				final RoleMakeWineShareRecord roleMakeWineShareRecord = shareRecordDao.findById(id);
				if(roleMakeWineShareRecord == null) {
					__cb.ice_exception(new NoteException(Messages.getString("ShareControler.0")));
					return;
				}
				
				// 21	置顶功能只有最高品质的才有，其他的没有
				if(roleMakeWineShareRecord.getConfigID() < topedQuality) {
					__cb.ice_exception(new NoteException(Messages.getString("MakeWine.TopLimit")));
					return;
				}
				
				// 检查当日置顶次数
//				int count = shareRecordDao.findTodayTopCount(roleRt.getRoleId(), DateUtil.toString(Calendar.getInstance().getTimeInMillis(), "yyyy-MM-dd"));
				int count = roleMakeWine.getTopedTimes();
				if(count >= configT.upNum) {
					__cb.ice_exception(new NoteException(Messages.getString("CollectHeroSoulController.3")));
					return;
				}
				
				// 扣除置顶花费(元宝)
				try {
					roleRt.winYuanbao(-configT.upPrice, false);
				}catch(Exception e){
					__cb.ice_exception(e);
					return;
				}
				
				// 增加今日已置顶次数
				roleMakeWine.setTopedTimes(roleMakeWine.getTopedTimes() + 1);
				XsgMakeWineManager.getInstance().saveRoleMakeWine2DB(roleMakeWine);
				
				// 设置 置顶
				roleMakeWineShareRecord.setTop(1);
				roleMakeWineShareRecord.setTopTime(new Date());
				// 保存数据库
				XsgMakeWineManager.getInstance().saveShareRecord2DB(roleMakeWineShareRecord);
				
				__cb.ice_response();
			}
		});
	}
	
	/**
	 * 兑换界面
	 */
	@Override
	public String exchangeView() throws NoteException {
		List<MakeWineExchangeItem> list_item = new ArrayList<MakeWineExchangeItem>();
		List<MakeWineScoreExchangeT> list_score_exchange = XsgMakeWineManager.getInstance().list_score_exchange;
		for(MakeWineScoreExchangeT mse : list_score_exchange) {
			list_item.add(new MakeWineExchangeItem(
					mse.id, 
					new IntString(mse.num, mse.item), 
					mse.needCompoundScore));
		}
		return LuaSerializer.serialize(new MakeWineExchangeView(
				list_item.toArray(new MakeWineExchangeItem[0]), 
				this.roleMakeWine.getComposeScore() - this.roleMakeWine.getExchangeUsedScore()));
	}

	/**
	 * 兑换
	 */
	@Override
	public void exchange(int id, int num) throws NoteException {
		if(num < 1) {
			throw new NoteException(Messages.getString("ShareControler.0"));
		}
		List<MakeWineScoreExchangeT> list_score_exchange = XsgMakeWineManager.getInstance().list_score_exchange;
		for(MakeWineScoreExchangeT mse : list_score_exchange) {
			if(mse.id == id) {
				// 可用的酿酒积分
				int lastComposeScore = this.roleMakeWine.getComposeScore() - this.roleMakeWine.getExchangeUsedScore(); 
				if(lastComposeScore < mse.needCompoundScore * num) {
					throw new NoteException(Messages.getString("MakeWine.notEnoughScore"));
				}
				
				// 设置兑换用掉的积分
				this.roleMakeWine.setExchangeUsedScore(this.roleMakeWine.getExchangeUsedScore() + mse.needCompoundScore * num);
//				this.roleMakeWine.setComposeScore(this.roleMakeWine.getComposeScore() - mse.needCompoundScore);
				
				// 发放道具
				this.roleRt.getItemControler().changeItemByTemplateCode(mse.item, mse.num * num);
				
				// 记兑换日志 // 【05-06 17：30：59】,兑换【紫武器宝箱X1】，花费【100兑换积分】
				List<String> list_log = XsgMakeWineManager.getInstance().map_exchange_log.get(this.roleRt.getRoleId());
				if(list_log == null){
					list_log = new ArrayList<String>();
				}
				if(list_log.size() >= XsgMakeWineManager.getInstance().RANK_LIMIT) {
					list_log.remove(0);
				}
				AbsItemT itemT = XsgItemManager.getInstance().findAbsItemT(mse.item);
				list_log.add(String.format(Messages.getString("MakeWine.ShareLog"), 
						DateUtil.format(new Date(), "MM-dd HH:mm:ss"), 
						"<font color='ff" + roleRt.getItemControler().getItemColor(itemT.getColor().value()) + "'>" + itemT.getName() + "</font>", 
						mse.num * num, 
						mse.needCompoundScore));
				XsgMakeWineManager.getInstance().map_exchange_log.put(this.roleRt.getRoleId(), list_log);
				
				break;
			}
		}
	}
	
	/**
	 * 积分排名
	 */
	@Override
	public String scoreRank() throws NoteException{
		MakeWineConfigT configT = XsgMakeWineManager.getInstance().makeWineConfig;
		List<MakeWineScoreRankItem> list_rank = new ArrayList<MakeWineScoreRankItem>();
		List<RoleMakeWine> list_makewine = XsgMakeWineManager.getInstance().getRankList();
		for (int i = 0; i < list_makewine.size(); i++) {
			RoleMakeWine rmw = list_makewine.get(i);
			list_rank.add(new MakeWineScoreRankItem(i + 1, rmw.getComposeScore() + rmw.getShareScore(), rmw.getLevel(), rmw.getVip(), rmw.getName(), rmw.getHeadImg()));
		}
			
		MakeWineScoreRank scoreRank = new MakeWineScoreRank(
				configT.mixScore,
				DateUtil.format(configT.endTime), // 发奖时间==活动结束时间
				list_rank.toArray(new MakeWineScoreRankItem[0]), 
				XsgMakeWineManager.getInstance().getRoleRank(this.roleRt.getRoleId()), 
				this.roleMakeWine.getComposeScore() + this.roleMakeWine.getShareScore(), 
				this.roleRt.getLevel(), 
				this.roleRt.getVipLevel(), 
				this.roleRt.getName(), 
				this.roleRt.getHeadImage()
				);
		
		return LuaSerializer.serialize(scoreRank);
	}
	
	/**
	 * 积分排名奖励列表
	 */
	@Override
	public String scoreRankAward() throws NoteException {
		MakeWineConfigT configT = XsgMakeWineManager.getInstance().makeWineConfig;
		List<MakeWineScoreRankAwardT> list_score_rank_award = XsgMakeWineManager.getInstance().list_score_rank_award;

		List<MakeWineScoreRankAwardItem> list_award = new ArrayList<MakeWineScoreRankAwardItem>();
		for(MakeWineScoreRankAwardT sra : list_score_rank_award) {
			list_award.add(new MakeWineScoreRankAwardItem((sra.startRank == sra.stopRank) ? 
					String.valueOf(sra.startRank) : (sra.startRank + "-" + sra.stopRank),
					sra.item_array.toArray(new IntString[0]), 
					sra.needScore));
		}
		
		MakeWineScoreRankAward rankAward = new MakeWineScoreRankAward(
				System.currentTimeMillis() - configT.endTime.getTime(), // 发奖时间==活动结束时间
				list_award.toArray(new MakeWineScoreRankAwardItem[0]),
				this.roleMakeWine.getComposeScore() + this.roleMakeWine.getShareScore()
			);
		return LuaSerializer.serialize(rankAward);
	}
	
	/**
	 * 查看酒的详情和奖励信息
	 */
	@Override
	public String wineInfoView() throws NoteException{
		Map<Integer, MakeWineShareT> map_make_share = XsgMakeWineManager.getInstance().map_make_share;
		List<MakeWineAwardInfo> list_award_info = new ArrayList<MakeWineAwardInfo>();
		for (MakeWineShareT ws : map_make_share.values()){
			list_award_info.add(new MakeWineAwardInfo(
					ws.item,
					new IntString(ws.selfNum, ws.shareItem), 
					new IntString(ws.getNum, ws.shareReceiveItem), 
					ws.compoundScore, 
					ws.shareScore));
		}
		
		return LuaSerializer.serialize(list_award_info.toArray(new MakeWineAwardInfo[0]));
	}
	
	/**
	 * 查看日志
	 * @param type:1:酿酒 2:领取 3:兑换
	 */
	@Override
	public String seeLog(int type) throws NoteException{
		List<String> list = null;
		switch(type){
		case 1:
			list = XsgMakeWineManager.getInstance().map_make_log.get(roleRt.getRoleId());
			break;
		case 2:
			list = XsgMakeWineManager.getInstance().map_receive_log.get(roleRt.getRoleId());
			break;
		case 3:
			list = XsgMakeWineManager.getInstance().map_exchange_log.get(roleRt.getRoleId());
			break;
		}
		if(list == null) {
			list = new ArrayList<String>();
		}
		return LuaSerializer.serialize(list);
	}
	
	
	
	@Override
	public void onVipLevelUp(int newLevel) {
		this.roleMakeWine.setVip(newLevel);
		XsgMakeWineManager.getInstance().saveRoleMakeWine2DB(this.roleMakeWine);
	}
	
	@Override
	public void onRoleLevelup() {
		this.roleMakeWine.setLevel(this.roleRt.getLevel());
		XsgMakeWineManager.getInstance().saveRoleMakeWine2DB(this.roleMakeWine);
	}
	
	@Override
	public void onRoleNameChange(String old, String name) {
		this.roleMakeWine.setName(name);
		XsgMakeWineManager.getInstance().saveRoleMakeWine2DB(this.roleMakeWine);
	}

	@Override
	public void onRoleHeadChange(String old, String headAndBorder) {
		this.roleMakeWine.setHeadImg(headAndBorder);
		XsgMakeWineManager.getInstance().saveRoleMakeWine2DB(this.roleMakeWine);
	}
	
	/**
	 * 红点推送
	 */
	@Override
	public MajorUIRedPointNote getRedPointNote() {
		// 开放时间和等级不符合
		try {
			XsgMakeWineManager.getInstance().checkTimeLevel(this.roleRt.getLevel());
		} catch (NoteException e) {
			return null;
		}
		
		if(this.roleMakeWine == null)	return null;
		
		boolean showRedPoint = false;
		// 有定时领取的材料////////////////////////////////////////////////////////////////////////////////////
		MaterialReceiveT materialReceive = null; 
		Calendar c_receiveTime = null;
		Calendar now = Calendar.getInstance();
		for(MaterialReceiveT mr : XsgMakeWineManager.getInstance().list_material_receive){
			Calendar c = Calendar.getInstance();
			c.setTime(mr.receiveTime);
			c.set(Calendar.DATE, now.get(Calendar.DATE));
			c.set(Calendar.MONTH, now.get(Calendar.MONTH));
			c.set(Calendar.YEAR, now.get(Calendar.YEAR));
			
			// 获取当前可领的时间段
			if(c.getTime().getTime() < now.getTimeInMillis()) {
				materialReceive = mr;
				c_receiveTime = c;
			}
		}
		
		if(materialReceive != null && (this.roleMakeWine.getReceiveMaterialDate() == null
				|| c_receiveTime.getTimeInMillis() > this.roleMakeWine.getReceiveMaterialDate().getTime())) {
			showRedPoint = true;
		}

		// 有积分奖励////////////////////////////////////////////////////////////////////////////////////
		if(!showRedPoint) {
			MakeWineScoreTargetT makeWineScoreTarget = null;
			for(MakeWineScoreTargetT mst : XsgMakeWineManager.getInstance().list_score_target){
				// 未领取
				if(mst.compoundScoreGoal > this.roleMakeWine.getReceiveSocre()
						&& mst.compoundScoreGoal <= this.roleMakeWine.getComposeScore()) {
					makeWineScoreTarget = mst;
					break;
				}
			}
			if(makeWineScoreTarget != null){
				showRedPoint = true;
			}
		}
		
		// 可合成////////////////////////////////////////////////////////////////////////////////////
		if(!showRedPoint) {
			boolean canCompose = true;
			Map<Integer, MakeWineShareT> map_make_share = XsgMakeWineManager.getInstance().map_make_share;
			for(MakeWineShareT ws : map_make_share.values()){
				for(IntString is : ws.list_needItem) {
					// 校验物品数量
					if(this.roleRt.getItemControler().getItemCountInPackage(is.strValue) < is.intValue){
						canCompose = false;
						break;
					}
				}
				if(!canCompose) {
					break;
				}
			}
			if(canCompose){
				showRedPoint = true;
			}
		}
		
		// 有分享奖励////////////////////////////////////////////////////////////////////////////////////
		// 需要实时去查一次列表数据，影响效率
		
		if(showRedPoint) {
			return new MajorUIRedPointNote(MajorMenu.MakeWine, false);
		}
		return null;
	}

}
