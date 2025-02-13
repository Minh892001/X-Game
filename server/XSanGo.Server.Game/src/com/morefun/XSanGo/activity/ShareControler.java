package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.ShareSub;
import com.XSanGo.Protocol.ShareView;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.activity.XsgShareManage.ShareTemplate;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleShare;
import com.morefun.XSanGo.event.IEventControler;
import com.morefun.XSanGo.event.protocol.ICombatPowerChange;
import com.morefun.XSanGo.event.protocol.IGetShareAward;
import com.morefun.XSanGo.event.protocol.IRoleLevelup;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 
 * @author sunjie
 * 
 */
@RedPoint
public class ShareControler implements IShareControler, ICombatPowerChange, IRoleLevelup {
	private IRole rt;
	private Role db;
	
	private IGetShareAward getShareAwardEvent;

	// 未达到任务开始条件
	private static int CAN_NOT_REC = 0;
	// 未完成不可分享
	private static int UNFINISH = 1;
	// 可分享
	private static int FINISH = 2;
	// 已分享
	private static int SHARED = 3;
	// 已領取
	private static int RECEIVED = 4;

	public ShareControler(IRole rt, Role db) {
		super();
		this.rt = rt;
		this.db = db;
		IEventControler eventControler = rt.getEventControler();
		getShareAwardEvent = this.rt.getEventControler().registerEvent(IGetShareAward.class);
		
		eventControler.registerHandler(IRoleLevelup.class, this);
		eventControler.registerHandler(ICombatPowerChange.class, this);

	}

	@Override
	public ShareView sharePageView() throws NoteException {
		if (!XsgShareManage.getInstance().isOpen()) {
			return null;
		}
		Map<Integer, ShareBaseTaskT> taskBaseCfg4Id = XsgShareManage.getInstance().getTaskBaseCfg4Id();
		Map<Integer, ShareConfigT> autoBaseCfg4Id = XsgShareManage.getInstance().getAutoBaseCfg4Id();
		List<ShareSub> list = new ArrayList<ShareSub>();
		for (ShareConfigT t : autoBaseCfg4Id.values()) {
			ShareBaseTaskT bt = taskBaseCfg4Id.get(t.taskId);
			if (bt == null)
				continue;
			boolean isBetween = DateUtil.isBetween(new Date(), DateUtil.parseDate(t.openTime),
					DateUtil.parseDate(t.closeTime));
			if (!isBetween)
				continue;
			if (t.isValid != 1)
				continue;
			if (t.groupId != 0 && t.groupId != ServerLancher.getServerId())
				continue;
			ShareSub sub = getShareSub(t.taskId);
			list.add(sub);
		}
		return new ShareView(list.toArray(new ShareSub[0]));
	}

	/**
	 * 获取视图
	 * 
	 * @param id
	 * @return
	 */
	private ShareSub getShareSub(int id) {
		ShareBaseTaskT bt = XsgShareManage.getInstance().getTaskBaseCfg4Id().get(id);
		ShareConfigT t = XsgShareManage.getInstance().getAutoBaseCfg4Id().get(id);
		ShareSub sub = new ShareSub();
		sub.taskId = t.taskId;
		sub.target = bt.target;
		sub.status = getShareStatus(id, bt.target);
		sub.awards = wrapRewardItem(t.itemMap);
		sub.title = TextUtil.isBlank(t.resetTaskTitle) ? bt.title : t.resetTaskTitle;
		sub.titleDesc = TextUtil.isBlank(t.resetTaskContent) ? bt.content : t.resetTaskContent;
		sub.taskIcon = bt.icon;
		sub.order = t.order;
		sub.bannerTitle = t.bannerTitle;
		sub.bannerType = t.bannerType;
		sub.shareContent = t.shareContent;
		sub.shareImg = t.shareImg;
		sub.shareIcon = t.shareIcon;
		sub.shareTitle = t.shareTitle;
		sub.shareLink = t.shareLink;
		return sub;
	}

	@Override
	public ShareSub share(int id) throws NoteException {
		Map<Integer, ShareConfigT> autoBaseCfg4Id = XsgShareManage.getInstance().getAutoBaseCfg4Id();
		ShareConfigT t = autoBaseCfg4Id.get(id);
		if (t == null)
			throw new NoteException(Messages.getString("ShareControler.0"));
		ShareBaseTaskT tb = XsgShareManage.getInstance().getTaskBaseCfg4Id().get(id);
		if (tb == null)
			throw new NoteException(Messages.getString("ShareControler.0"));
		int status = getShareStatus(id, tb.target);
		if (status != FINISH)
			throw new NoteException(Messages.getString("ShareControler.1"));
		Map<Integer, RoleShare> roleShares = db.getRoleShares();
		RoleShare share = roleShares.get(id);
		share.setShareDate(new Date());
		
		if (t.itemMap != null && t.itemMap.size() > 0) {
			Map<String, String> replaceMap = new HashMap<String, String>();
			// 发送邮件
			XsgMailManager.getInstance().sendTemplate(rt.getRoleId(), MailTemplate.ShareAward,
					t.itemMap, replaceMap);
			getShareAwardEvent.onGetAward(id);
		}
		share.setRecDate(new Date());
		return getShareSub(id);
	}

//	public ShareSub recShareAward(int taskId) throws NoteException {
//		Map<Integer, ShareConfigT> autoBaseCfg4Id = XsgShareManage.getInstance().getAutoBaseCfg4Id();
//		ShareConfigT t = autoBaseCfg4Id.get(taskId);
//		if (t == null)
//			throw new NoteException(Messages.getString("ShareControler.0"));
//		ShareBaseTaskT tb = XsgShareManage.getInstance().getTaskBaseCfg4Id().get(taskId);
//		if (tb == null)
//			throw new NoteException(Messages.getString("ShareControler.0"));
//		int status = getShareStatus(taskId, tb.target);
//		if (status != SHARED)
//			throw new NoteException(Messages.getString("ShareControler.1"));
//		Map<Integer, RoleShare> roleShares = db.getRoleShares();
//		RoleShare share = roleShares.get(taskId);
//		if (t.itemMap != null && t.itemMap.size() > 0) {
//			for (String item : t.itemMap.keySet()) {
//				this.rt.getRewardControler().acceptReward(item, t.itemMap.get(item));
//			}
//		}
//		share.setRecDate(new Date());
//		return getShareSub(taskId);
//	}

	/**
	 * 更新进度
	 * 
	 * @param type
	 * @param para
	 */
	private void updateShareProgress(String type, String para) {
		boolean isOpen = XsgShareManage.getInstance().isOpen();
		if (!isOpen) {
			return;
		}
		Map<Integer, RoleShare> roleShares = db.getRoleShares();
		Map<Integer, ShareBaseTaskT> typeMap = XsgShareManage.getInstance().getTaskBaseCfg4Target().get(type);
		// 热加载脚本
		Map<Integer, ShareConfigT> autoBaseCfg4Id = XsgShareManage.getInstance().getAutoBaseCfg4Id();
		for (ShareBaseTaskT t : typeMap.values()) {
			if (t.needLvl > rt.getLevel())
				continue;
			ShareConfigT autoT = autoBaseCfg4Id.get(t.id);
			if (autoT == null)
				continue;
			boolean isBetween = DateUtil.isBetween(new Date(), DateUtil.parseDate(autoT.openTime),
					DateUtil.parseDate(autoT.closeTime));
			if (!isBetween)
				continue;
			if (autoT.isValid != 1)
				continue;
			if (autoT.groupId != 0 && autoT.groupId != ServerLancher.getServerId())
				continue;
			int status = getShareStatus(t.id, t.target);
			if (status != UNFINISH)
				continue;
			RoleShare share = roleShares.get(t.id);
			if (share == null) {
				share = new RoleShare(GlobalDataManager.getInstance().generatePrimaryKey(), db, t.id, "", new Date());
				roleShares.put(t.id, share);
			}
			switch (ShareTemplate.getShareTemplate(type)) {
			case KingLv:
				share.setProgress(rt.getLevel() + "");
				break;
			case Power:
				if (TextUtil.isBlank(share.getProgress())) {
					share.setProgress(para + "");
				} else {
					if (Integer.valueOf(share.getProgress()) < Integer.valueOf(para)) {
						share.setProgress(para + "");
					}
				}
				break;
			default:
				return;
			}
			if (getShareStatus(t.id, t.target) == SHARED || getShareStatus(t.id, t.target) == FINISH) {
				notifyRedPoint();
			}
		}
	}

	/**
	 * 获取分享状态
	 * 
	 * @param taskId
	 * @return
	 */
	private int getShareStatus(int taskId, String type) {
		ShareBaseTaskT t = XsgShareManage.getInstance().getTaskBaseCfg4Id().get(taskId);
		if (t == null)
			return CAN_NOT_REC;
		if (rt.getLevel() < t.needLvl)
			return CAN_NOT_REC;
		Map<Integer, RoleShare> roleShares = db.getRoleShares();
		if (roleShares == null)
			return UNFINISH;
		RoleShare share = roleShares.get(taskId);
		if (share == null)
			return UNFINISH;
		if (share.getRecDate() != null)
			return RECEIVED;
		if (share.getShareDate() != null)
			return SHARED;

		switch (ShareTemplate.getShareTemplate(type)) {
		case KingLv:
		case Power:
			if (Integer.valueOf(share.getProgress()) >= Integer.valueOf(t.demand)) {
				return FINISH;
			}
			break;
		default:
			return CAN_NOT_REC;
		}
		return UNFINISH;
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		boolean flag = false;
		Map<Integer, ShareBaseTaskT> taskBaseCfg4Id = XsgShareManage.getInstance().getTaskBaseCfg4Id();
		Map<Integer, ShareConfigT> autoBaseCfg4Id = XsgShareManage.getInstance().getAutoBaseCfg4Id();
		for (ShareConfigT t : autoBaseCfg4Id.values()) {
			ShareBaseTaskT bt = taskBaseCfg4Id.get(t.taskId);
			if (bt == null)
				continue;
			boolean isBetween = DateUtil.isBetween(new Date(), DateUtil.parseDate(t.openTime),
					DateUtil.parseDate(t.closeTime));
			if (!isBetween)
				continue;
			if (t.isValid != 1)
				continue;
			if (t.groupId != 0 && t.groupId != ServerLancher.getServerId())
				continue;
			if (getShareStatus(bt.id, bt.target) == SHARED || getShareStatus(bt.id, bt.target) == FINISH)
			{
				flag = true;
				break;
			}
		}
		if (flag) {
			return new MajorUIRedPointNote(MajorMenu.Share, false);
		}
		return null;
	}

	/**
	 * 红点提示
	 */
	private void notifyRedPoint() {
		this.rt.getNotifyControler().onMajorUIRedPointChange(new MajorUIRedPointNote(MajorMenu.Share, false));
	}

	/**
	 * 生成物品数组
	 * 
	 * @param itemsMap
	 *            奖励配置表
	 * @return 物品数组
	 */
	private IntString[] wrapRewardItem(Map<String, Integer> itemsMap) {
		if (itemsMap.size() == 0) {
			return null;
		}
		IntString[] items = new IntString[itemsMap.size()];
		Iterator<String> it = itemsMap.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			String itemId = it.next();
			int itemNum = itemsMap.get(itemId);
			items[i++] = new IntString(itemNum, itemId);
		}
		return items;
	}

	/**
	 * 登录处理
	 */
	public void update4Login() {
		updateShareProgress(ShareTemplate.KingLv.name(), "");
	}

	@Override
	public void onRoleLevelup() {
		updateShareProgress(ShareTemplate.KingLv.name(), "");
	}

	@Override
	public void onCombatPowerChange(int old, int newValue) {
		updateShareProgress(ShareTemplate.Power.name(), newValue + "");
	}

}
