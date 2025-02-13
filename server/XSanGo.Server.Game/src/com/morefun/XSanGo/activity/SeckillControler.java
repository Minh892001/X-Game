package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.SeckillActivityView;
import com.XSanGo.Protocol.SeckillItemView;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.activity.XsgActivityManage.ActivityTemplate;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleSeckill;
import com.morefun.XSanGo.db.game.SeckillItem;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.event.protocol.ISeckill;
import com.morefun.XSanGo.event.protocol.ISeckillOver;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

@RedPoint(isTimer = true)
public class SeckillControler implements ISeckillControler {
	private IRole rt;
	private Role db;
	private ISeckill seckillEvent;
	private ISeckillOver seckillOverEvent;
	// 限时活动登录都显示红点
	private boolean firstOpen = false;

	public SeckillControler(IRole rt, Role db) {
		this.rt = rt;
		this.db = db;
		this.seckillEvent = this.rt.getEventControler().registerEvent(
				ISeckill.class);
		this.seckillOverEvent = this.rt.getEventControler().registerEvent(
				ISeckillOver.class);
		this.firstOpen = true;
	}

	@Override
	public SeckillActivityView getSeckillActivityView() throws NoteException {
		List<SecKillConfigT> secKillConfigTs = XsgActivityManage.getInstance()
				.getAllSeckillConfig();
		List<SeckillItemView> itemViews = new ArrayList<SeckillItemView>();
		Date seckillDate = null;
		for (SecKillConfigT sc : secKillConfigTs) {
			// 关闭的或者过期的
			if (sc.isOpen == 0 || sc.endDate.before(new Date())) {
				continue;
			}
			if (DateUtil.isSameDay(sc.startDate, new Date())) {
				seckillDate = sc.startDate;
				
				int state = 0;
				for (SecKillItemT si : sc.secKillItemTs) {
					SeckillItem seckillItem = XsgActivityManage.getInstance()
							.getSeckillItem(si.id, sc);
					int remainNum = si.maxNum;
					if (seckillItem != null) {
						remainNum = si.maxNum - seckillItem.getBuyNum();
					}
					if (sc.startDate.after(new Date())) {//未开放
						state = 2;
					} else if (isSeckill(si.id)) {// 已抢过了
						state = 1;
					} else {
						state = 0;
					}
					itemViews.add(new SeckillItemView(si.id, si.itemId,
							si.price, si.maxNum, remainNum, state, "")); //$NON-NLS-1$
				}
			}
//			else {
//				int diffDay = DateUtil.diffDate(sc.startDate, new Date());
//				Calendar calendar = Calendar.getInstance();
//				calendar.setTime(sc.startDate);
//				int hour = calendar.get(Calendar.HOUR_OF_DAY);
//				String minute = DateUtil.toString(calendar.getTimeInMillis(),
//						"mm"); //$NON-NLS-1$
//				String dateDesc = null;
//				if (diffDay <= 1) {
//					dateDesc = TextUtil.format(Messages.getString("SeckillControler.4"), hour, minute); //$NON-NLS-1$
//				} else if (diffDay == 2) {
//					dateDesc = TextUtil.format(Messages.getString("SeckillControler.5"), hour, minute); //$NON-NLS-1$
//				} else {
//					dateDesc = TextUtil.format(Messages.getString("SeckillControler.6"), diffDay, hour, //$NON-NLS-1$
//							minute);
//				}
//				for (SecKillItemT si : sc.secKillItemTs) {
//					itemViews.add(new SeckillItemView(si.id, si.itemId,
//							si.price, si.maxNum, 0, 2, dateDesc));
//				}
//			}
		}
		ActivityT activityT = XsgActivityManage.getInstance().getActivityT(
				ActivityTemplate.Seckill);
		// 秒杀活动倒计时
		int remainSecond = (int) ((DateUtil.parseDate(activityT.endTime)
				.getTime() - System.currentTimeMillis()) / 1000);
		// 秒杀开始时间倒计时
		int hour = 0;
		int startRemainSecond = 0;
		String minute = "";
		if(seckillDate != null){
			// 秒杀开始时间
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(seckillDate);
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			minute = DateUtil.toString(calendar.getTimeInMillis(),
					"mm"); //$NON-NLS-1$
			
			startRemainSecond = (int)(DateUtil.compareTime(seckillDate, new Date()) / 1000);
		}
		SeckillActivityView view = new SeckillActivityView(TextUtil.format("{0}:{1}", hour, minute),
				remainSecond, startRemainSecond > 0 ? startRemainSecond : 0, 
						itemViews.toArray(new SeckillItemView[0]));
		return view;
	}

	@Override
	public void seckillItem(int id) throws NotEnoughYuanBaoException,
			NoteException {
		SecKillItemT secKillItemT = XsgActivityManage.getInstance()
				.getSecKillItemT(id);
		if (secKillItemT == null) {
			throw new NoteException(Messages.getString("SeckillControler.7")); //$NON-NLS-1$
		}
		SecKillConfigT secKillConfigT = XsgActivityManage.getInstance()
				.getSecKillConfigT(secKillItemT.type);
		if (!DateUtil.isBetween(new Date(), secKillConfigT.startDate,
				secKillConfigT.endDate)) {
			throw new NoteException(Messages.getString("SeckillControler.8")); //$NON-NLS-1$
		}
		SeckillItem seckillItem = XsgActivityManage.getInstance()
				.getSeckillItem(id, secKillConfigT);
		if (seckillItem != null
				&& seckillItem.getBuyNum() >= secKillItemT.maxNum) {
			throw new NoteException(Messages.getString("SeckillControler.9")); //$NON-NLS-1$
		}
		if (isSeckill(id)) {
			throw new NoteException(Messages.getString("SeckillControler.10")); //$NON-NLS-1$
		}
		if (rt.getTotalYuanbao() < secKillItemT.price) {
			throw new NotEnoughYuanBaoException();
		}
		if (seckillItem == null) {
			seckillItem = new SeckillItem(GlobalDataManager.getInstance()
					.generatePrimaryKey(), id, 0, new Date());
			XsgActivityManage.getInstance().addSeckillItem(seckillItem);
		}
		// 扣除元宝
		rt.winYuanbao(-secKillItemT.price, false);
		// 发放物品
		rt.getRewardControler().acceptReward(secKillItemT.itemId, 1);
		seckillItem.setBuyNum(seckillItem.getBuyNum() + 1);
		RoleSeckill data = db.getRoleSeckills().get(id);
		if (data == null) {
			data = new RoleSeckill(GlobalDataManager.getInstance()
					.generatePrimaryKey(), db, id, new Date());
			db.getRoleSeckills().put(id, data);
		} else {
			data.setSeckillDate(new Date());
		}
		saveSeckillItem(seckillItem);
		seckillEvent.onSeckill(id, secKillItemT.price);
		// 秒杀完事件
		if (secKillItemT.maxNum == seckillItem.getBuyNum()) {
			seckillOverEvent.onSeckillOver(id, secKillItemT.itemId);
		}
	}

	private void saveSeckillItem(final SeckillItem seckillItem) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				SimpleDAO simpleDAO = SimpleDAO
						.getFromApplicationContext(ServerLancher.getAc());
				simpleDAO.attachDirty(seckillItem);
			}
		});
	}

	/**
	 * 是否秒杀过物品
	 * 
	 * @param id
	 * @return
	 */
	private boolean isSeckill(int id) {
		RoleSeckill data = this.db.getRoleSeckills().get(id);
		SecKillItemT itemT = XsgActivityManage.getInstance()
				.getSecKillItemT(id);
		SecKillConfigT configT = XsgActivityManage.getInstance()
				.getSecKillConfigT(itemT.type);
		return data != null
				&& DateUtil.isBetween(data.getSeckillDate(), configT.startDate,
						configT.endDate);
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if(!XsgActivityManage.getInstance().isActivityOpen(ActivityTemplate.Seckill)){
			return null;
		}
		// 聊天回调接口未注册则不发送
		if(rt.getChatControler().getChatCb() == null){
			return null;
		}
		
		if(firstOpen) {
			firstOpen = false;
			return new MajorUIRedPointNote(MajorMenu.SeckillMenu, true);
		}
		
		SeckillActivityView view = null;
		try {
			view = getSeckillActivityView();
		} catch (NoteException e) {
			return null;
		}
		for (SeckillItemView sv : view.items) {
			if (sv.buyable == 0 && sv.remainNum > 0) {
				return new MajorUIRedPointNote(MajorMenu.SeckillMenu, true);
			}
		}
		return null;
	}
	
	@Override
	public void setFirstOpen(boolean value) {
		firstOpen = value;
	}
}
