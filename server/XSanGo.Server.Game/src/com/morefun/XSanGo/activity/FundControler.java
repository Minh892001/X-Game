package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.XSanGo.Protocol.CurrencyType;
import com.XSanGo.Protocol.FundRewardView;
import com.XSanGo.Protocol.FundView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.Money;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.activity.XsgActivityManage.ActivityTemplate;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleFund;
import com.morefun.XSanGo.event.protocol.IAcceptFundReward;
import com.morefun.XSanGo.event.protocol.IBuyFund;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 成长基金controler
 * 
 * @author qinguofeng
 */
@RedPoint(isTimer = true)
public class FundControler implements IFundControler {
	/** 已购买基金 */
	private static final int HAS_BUY = 1;

	private IRole roleRt;
	private Role roleDB;

	/** DB对象 */
	private RoleFund roleFund;
	// 已经领取过的等级
	private Set<String> alreadyReceivedLevel = new HashSet<String>();

	private IAcceptFundReward acceptFundReward;
	private IBuyFund buyFund;
	
	public FundControler(IRole rt, Role db) {
		this.roleRt = rt;
		this.roleDB = db;

		acceptFundReward = roleRt.getEventControler().registerEvent(
				IAcceptFundReward.class);
		buyFund = roleRt.getEventControler().registerEvent(IBuyFund.class);

		initRoleFund(db.getFund());
	}

	/**
	 * 初始化,如果db对象为空,则初始化一个
	 * 
	 * @param roleFund
	 *            db对象
	 * */
	private void initRoleFund(RoleFund roleFund) {
		if (roleFund == null) {
			roleFund = new RoleFund(roleDB);
			saveToDb(roleFund);
			this.roleFund = roleDB.getFund();
		} else {
			this.roleFund = roleFund;
			alreadyReceivedLevel = TextUtil.GSON.fromJson(
					roleFund.getAcceptedRewards(), Set.class);
		}
	}

	private void saveToDb(RoleFund roleFund) {
		roleFund.setAcceptedRewards(TextUtil.GSON.toJson(alreadyReceivedLevel));
		roleFund.setLastUpdateTime(Calendar.getInstance().getTime());
		roleDB.setFund(roleFund);
	}

	/**
	 * 获取下一个可以领取的等级奖励
	 * */
	private int getNextRewardLevel() {
		List<FundT> funds = XsgActivityManage.getInstance().getFundRewardList();
		// 按照奖励等级排序
		Collections.sort(funds, new Comparator<FundT>() {
			@Override
			public int compare(FundT o1, FundT o2) {
				return o1.level - o2.level;
			}
		});
		if (funds != null) {
			for (FundT fund : funds) {
				if (!isReceivedReward(fund.level)) {
					return fund.level;
				}
			}
		}
		return 0;
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.Fund)) {
			return null;
		}
		// 聊天回调接口未注册则不发送
		if(roleRt.getChatControler().getChatCb() == null){
			return null;
		}
		
		// 满足购买基金条件
		if (roleFund.getBuyFund() != HAS_BUY) {
			XsgActivityManage manager = XsgActivityManage.getInstance();
			FundConfigT config = manager.getFundConfig();
			
			if (roleRt.getVipLevel() >= config.minVip
					&& roleRt.getTotalYuanbao() >= config.price) {
				return new MajorUIRedPointNote(MajorMenu.FundMenu, false);
			}
		}
		else { // 已经购买过基金
			int level = roleRt.getLevel();
			// 根据当前等级获取可以领取的最大等级的奖励
			int nextLevel = getNextRewardLevel();
			if (nextLevel > 0 && level >= nextLevel) {
				return new MajorUIRedPointNote(MajorMenu.FundMenu, false);
			}
		}
		return null;
	}

	private boolean isReceivedReward(int checkId) {
		return alreadyReceivedLevel.contains(checkId + ""); //$NON-NLS-1$
	}

	@Override
	public FundView getFundView() throws NoteException {
		XsgActivityManage manager = XsgActivityManage.getInstance();
		FundConfigT config = manager.getFundConfig(); // 基金金额配置
		List<FundT> fundList = manager.getFundRewardList(); // 基金奖励列表
		List<FundRewardView> fundRewardView = new ArrayList<FundRewardView>();
		if (fundList != null) {
			for (FundT fund : fundList) {
				// 初始化基金奖励的View
				fundRewardView.add(new FundRewardView(fund.level, fund.rmby,
						isReceivedReward(fund.level)));
			}
		}
		FundRewardView viewArray[] = fundRewardView
				.toArray(new FundRewardView[0]);

		return new FundView(config.price, roleFund.getBuyFund() == HAS_BUY,
				viewArray);
	}

	/**
	 * 增加一个已经领取过的等级, 并持久化到数据库
	 * 
	 * @param level
	 *            等级
	 * */
	private void addReceivedLevel(int level) {
		alreadyReceivedLevel.add(level + ""); //$NON-NLS-1$
		saveToDb(roleFund);
	}

	@Override
	public void acceptFundReward(int level) throws NoteException {
		if (roleFund.getBuyFund() != HAS_BUY) {
			throw new NoteException(Messages.getString("FundControler.2")); //$NON-NLS-1$
		}
		if (roleRt.getLevel() < level) {
			throw new NoteException(Messages.getString("FundControler.3")); //$NON-NLS-1$
		}
		if (isReceivedReward(level)) {
			throw new NoteException(Messages.getString("FundControler.4")); //$NON-NLS-1$
		}
		XsgActivityManage manager = XsgActivityManage.getInstance();
		FundT fund = manager.getFundReward(level);
		if (fund == null) {
			throw new NoteException(Messages.getString("FundControler.5")); //$NON-NLS-1$
		}

		// 领奖励
		roleRt.getRewardControler().acceptReward(Const.PropertyName.RMBY,
				fund.rmby);
		// 增加到已经领取的集合
		addReceivedLevel(level);
		// 事件通知
		acceptFundReward.onAcceptFundReward(level, fund.rmby);
	}

	@Override
	public void buyFund() throws NoteException, NotEnoughMoneyException,
			NotEnoughYuanBaoException {
		// 是否已经购买
		if (roleFund.getBuyFund() == HAS_BUY) {
			throw new NoteException(Messages.getString("FundControler.6")); //$NON-NLS-1$
		}
		XsgActivityManage manager = XsgActivityManage.getInstance();
		FundConfigT config = manager.getFundConfig();
		if (roleRt.getVipLevel() < config.minVip) {
			throw new NoteException(Messages.getString("FundControler.7")); //$NON-NLS-1$
		}
		// 元宝是否足够
		if (roleRt.getTotalYuanbao() < config.price) {
			throw new NotEnoughYuanBaoException();
		}
		// 减少玩家身上的元宝
		roleRt.reduceCurrency(new Money(CurrencyType.Yuanbao, config.price));
		// 设置已经购买的标记
		roleFund.setBuyFund(HAS_BUY);
		// 持久化
		saveToDb(roleFund);
		// 事件通知
		buyFund.onBuyFund(config.price);
	}

	/** 是否已经把全部奖励领完了 */
	@Override
	public boolean hasAcceptAll() {
		if (alreadyReceivedLevel != null) {
			List<FundT> funds = XsgActivityManage.getInstance().getFundRewardList();
			return alreadyReceivedLevel.size() >= funds.size();
		}
		return false;
	}
	
}
