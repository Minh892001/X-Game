package com.morefun.XSanGo.notify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.XSanGo.Protocol.ChatCallbackPrx;
import com.XSanGo.Protocol.FormationCallbackPrx;
import com.XSanGo.Protocol.FormationCallbackPrxHelper;
import com.XSanGo.Protocol.HeroState;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.RoleCallbackPrx;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.faction.CopyNotifyEntity;
import com.morefun.XSanGo.formation.IFormation;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.net.GameSessionI;
import com.morefun.XSanGo.net.GameSessionManagerI;
import com.morefun.XSanGo.pushmsg.XsgPushMsgManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 数据通知管理器，统一管理后台数据推送
 * 
 * @author sulingyun
 * 
 */
public class NotifyControler implements INotifyControler {
	/**
	 * 新增活动 红点通过聊天协议发送给客户端
	 */
	private static List<MajorMenu> RedPointUseChatPrx;
	static {
		RedPointUseChatPrx = new ArrayList<MajorMenu>();
		RedPointUseChatPrx.add(MajorMenu.SeckillMenu);
		RedPointUseChatPrx.add(MajorMenu.DayChargeMenu);
		RedPointUseChatPrx.add(MajorMenu.DayConsumeMenu);
		RedPointUseChatPrx.add(MajorMenu.FundMenu);
		RedPointUseChatPrx.add(MajorMenu.LevelRewardMenu);
		RedPointUseChatPrx.add(MajorMenu.PowerRewardMenu);
		RedPointUseChatPrx.add(MajorMenu.VipGiftPacksMenu);
		RedPointUseChatPrx.add(MajorMenu.FirstJiaMenu);
		RedPointUseChatPrx.add(MajorMenu.LeijiLogin);
		RedPointUseChatPrx.add(MajorMenu.ForverLeijiLogin);
		RedPointUseChatPrx.add(MajorMenu.SendJunLing);
		RedPointUseChatPrx.add(MajorMenu.Share);
		RedPointUseChatPrx.add(MajorMenu.FortuneWheel);
		RedPointUseChatPrx.add(MajorMenu.LuckyBagMenu);
		RedPointUseChatPrx.add(MajorMenu.CornucopiaMenu);
		RedPointUseChatPrx.add(MajorMenu.ExchangItemMenu);
		RedPointUseChatPrx.add(MajorMenu.LevelWeal);
		RedPointUseChatPrx.add(MajorMenu.TreasureMenu);
		RedPointUseChatPrx.add(MajorMenu.SuperChargeMenu);
		RedPointUseChatPrx.add(MajorMenu.SuperRaffleMenu);
		RedPointUseChatPrx.add(MajorMenu.FriendsRecallTask);
		RedPointUseChatPrx.add(MajorMenu.Api);
		RedPointUseChatPrx.add(MajorMenu.AnnounceMenu);
		RedPointUseChatPrx.add(MajorMenu.Achieve);
		RedPointUseChatPrx.add(MajorMenu.OpenServerActive);
		RedPointUseChatPrx.add(MajorMenu.TournamentMenu);
		RedPointUseChatPrx.add(MajorMenu.ResourceBack);
		RedPointUseChatPrx.add(MajorMenu.Lottery);
		RedPointUseChatPrx.add(MajorMenu.MarksManMenu);
		RedPointUseChatPrx.add(MajorMenu.MakeWine);
		RedPointUseChatPrx.add(MajorMenu.MarksManShootFree);
		RedPointUseChatPrx.add(MajorMenu.Artifact);
	}

	private IRole roleRt;
	private boolean autoNotify = true;
	private Map<String, Number> rolePropertyMap;
	private Map<String, String> strPropertyMap;
	private Map<String, IItem> itemMap;
	private Map<String, IHero> heroMap;
	private Map<String, IFormation> formationMap;
	private Map<MajorMenu, MajorUIRedPointNote> redPointMap;
	private List<String> tips;
	private List<Integer> achieves;
	private String pushMsgs;
	private CopyNotifyEntity copyNotify;

	public NotifyControler(IRole rt) {
		this.roleRt = rt;
		this.rolePropertyMap = new HashMap<String, Number>();
		this.strPropertyMap = new HashMap<String, String>();
		this.itemMap = new HashMap<String, IItem>();
		this.heroMap = new ConcurrentHashMap<String, IHero>();
		this.formationMap = new HashMap<String, IFormation>();
		this.tips = new ArrayList<String>();
		this.achieves = new ArrayList<Integer>();
		this.redPointMap = new HashMap<MajorMenu, MajorUIRedPointNote>();
	}

	@Override
	public void setAutoNotify(boolean auto) {
		this.autoNotify = auto;
		if (auto) {
			this.notifyAllChange();
		}
	}

	private void notifyAllChange() {
		RoleCallbackPrx cb = this.getRoleCallback();
		if (cb == null) {
			clearAll();
			return;
		}

		// 属性变更通知
		for (String key : this.rolePropertyMap.keySet()) {
			cb.begin_rolePropertyChange(new Property(key, this.rolePropertyMap.get(key).intValue()));
		}
		// string 类型属性变更通知
		for (String key : this.strPropertyMap.keySet()) {
			cb.begin_strPropertyChange(key, this.strPropertyMap.get(key));
		}
		// 武将变更通知
		for (String key : this.heroMap.keySet()) {
			IHero hero = this.heroMap.get(key);
			cb.begin_heroChange(hero.getHeroView());

			// 武将变化多数情况伴随部队变化，因触发因素太多，所以直接在这里处理，即便是这样，也可能有通知包过多的情况
			IFormation formation = hero.getReferenceFormation();
			if (formation != null) {
				this.formationMap.put(formation.getId(), formation);
			}

			if (hero.getState().equals(HeroState.PartnerShip)) {
				this.formationMap.putAll(roleRt.getFormationControler().getFormation());
			}
		}
		// 物品变更通知
		for (String key : this.itemMap.keySet()) {
			cb.begin_itemChange(this.itemMap.get(key).getView());
		}
		// 部队变更通知
		for (String key : this.formationMap.keySet()) {
			FormationCallbackPrx fcb = FormationCallbackPrxHelper.uncheckedCast(cb, "formationCallback");
			if (fcb != null) {
				fcb.begin_formationChange(this.formationMap.get(key).getView());
			}
		}

		// 红点处理
		if (this.redPointMap.size() > 0) {
			this.handleRedPoint();
		}

		for (String s : tips) {
			cb.begin_showTips(s);
		}
		if (achieves.size() > 0) {
			int[] as = new int[achieves.size()];
			for (int i = 0; i < achieves.size(); i++)
				as[i] = achieves.get(i);
			cb.begin_showAchieves(as);
		}
		if (!TextUtil.isBlank(pushMsgs)) {
			cb.begin_pushMsgs(pushMsgs);
		}
		if (copyNotify != null) {
			cb.begin_factionCopyState(copyNotify.roleName, copyNotify.icon, copyNotify.vipLevel);
		}
		clearAll();
	}

	private void handleRedPoint() {
		MajorUIRedPointNote[] points = this.redPointMap.values().toArray(new MajorUIRedPointNote[0]);
		RoleCallbackPrx cb = this.getRoleCallback();
		if (cb != null) {
			cb.begin_showRedPointOnMajorUI(points);
		}

		ChatCallbackPrx chatPrx = this.roleRt.getChatControler().getChatCb();
		if (chatPrx == null) {
			return;
		}
		for (MajorUIRedPointNote note : points) {
			// 新增活动 红点通过聊天协议发送给客户端
			if (RedPointUseChatPrx.contains(note.menu)) {
				chatPrx.begin_redPointSmit(note.menu.value(), note.clearWhenOpen);
			}
		}
	}

	/**
	 * 清除所有通知对象
	 */
	private void clearAll() {
		this.rolePropertyMap.clear();
		this.strPropertyMap.clear();
		this.heroMap.clear();
		this.itemMap.clear();
		this.formationMap.clear();
		this.redPointMap.clear();
		this.tips.clear();
		this.achieves.clear();
		this.pushMsgs = null;
		this.copyNotify = null;
	}

	@Override
	public void onItemChange(IItem item) {
		this.itemMap.put(item.getId(), item);

		this.onDataChange();
	}

	@Override
	public RoleCallbackPrx getRoleCallback() {
		GameSessionI session = GameSessionManagerI.getInstance().findSession(this.roleRt.getAccount(),
				this.roleRt.getRoleId());
		return session == null ? null : session.getRoleCb();
	}

	@Override
	public void onHeroChanged(IHero hero) {
		this.heroMap.put(hero.getId(), hero);

		// 随从变更了，主人也刷新下数据，这个操作理论上应该写在上层逻辑里，
		// 考虑到多个地方调用和上层逻辑尽量简洁，所以暂时先放这里处理
		if (hero.isAttendant()) {
			IHero master = hero.getMaster();// 主人
			this.heroMap.put(master.getId(), master);
		}
		this.onDataChange();
	}

	private void onDataChange() {
		if (this.autoNotify) {
			this.notifyAllChange();
		}
	}

	@Override
	public void onFormationChange(IFormation formation) {
		this.formationMap.put(formation.getId(), formation);
		this.onDataChange();
	}

	@Override
	public void onYuanBaoChanged(int rmby) {
		this.rolePropertyMap.put(Const.PropertyName.RMBY, rmby);
		this.onDataChange();
	}

	@Override
	public void onPropertyChange(String propertyName, Number value) {
		this.rolePropertyMap.put(propertyName, value);
		this.onDataChange();
	}

	@Override
	public void onStrPropertyChange(String propertyName, String value) {
		this.strPropertyMap.put(propertyName, value);
		this.onDataChange();
	}

	@Override
	public void onMajorUIRedPointChange(MajorUIRedPointNote... redPoint) {
		if (redPoint == null) {
			return;
		}

		for (MajorUIRedPointNote p : redPoint) {
			if (p != null) {
				this.redPointMap.put(p.menu, p);
			}
		}
		this.onDataChange();
	}

	@Override
	public void showTips(String tips) {
		this.tips.add(tips);
		this.onDataChange();
	}

	@Override
	public void pushMsgs() {
		String msgs = XsgPushMsgManager.getInstance().getMsgs();
		if (!TextUtil.isBlank(msgs)) {
			this.pushMsgs = msgs;
			this.onDataChange();
		}
	}

	@Override
	public void factionCopyState(String roleName, String icon, int vipLevel) {
		this.copyNotify = new CopyNotifyEntity(roleName, icon, vipLevel);
		this.onDataChange();
	}

	@Override
	public void showAchieve(List<Integer> aids) {
		this.achieves.clear();
		this.achieves.addAll(aids);
		this.onDataChange();
	}
}
