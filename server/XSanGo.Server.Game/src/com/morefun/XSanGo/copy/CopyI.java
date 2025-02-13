/**
 * 
 */
package com.morefun.XSanGo.copy;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Ice.Current;

import com.XSanGo.Protocol.AMD_Copy_autoChallengeTa;
import com.XSanGo.Protocol.AMD_Copy_beginChallengeTa;
import com.XSanGo.Protocol.AMD_Copy_endChallenge;
import com.XSanGo.Protocol.AMD_Copy_getBigCopyView;
import com.XSanGo.Protocol.AMD_Copy_getSmallCopyView;
import com.XSanGo.Protocol.AMD_Copy_getSmallCopyViewWithWarmup;
import com.XSanGo.Protocol.AMD_Copy_hallOfFameList;
import com.XSanGo.Protocol.BuyMilitaryOrderView;
import com.XSanGo.Protocol.ChallengeTaResult;
import com.XSanGo.Protocol.ChallengeTaView;
import com.XSanGo.Protocol.ChapterRewardView;
import com.XSanGo.Protocol.CopyChallengeResultView;
import com.XSanGo.Protocol.CopyClearResultView;
import com.XSanGo.Protocol.CopySummaryView;
import com.XSanGo.Protocol.HallOfFameView;
import com.XSanGo.Protocol.IntIntPair;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NotEnoughException;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.SmallCopyView;
import com.XSanGo.Protocol._CopyDisp;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.db.game.ServerCopy;
import com.morefun.XSanGo.fightmovie.FightLifeNumT;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;
import com.morefun.XSanGo.vip.VipT;
import com.morefun.XSanGo.vip.XsgVipManager;

/**
 * 副本网络协议入口
 * 
 * @author sulingyun
 * 
 */
public class CopyI extends _CopyDisp {
	private static final long serialVersionUID = -7274502409414601978L;
	private static final Log log = LogFactory.getLog(CopyI.class);
	private IRole roleRt;
	// private int copyId;// 挑战TA的关卡ID
	// private String challengeRoleId; // 挑战TA的roleId
	/** 战报上下文ID */
	private String fightMovieIdContext = null;

	public CopyI(IRole role) {
		this.roleRt = role;
	}

	@Override
	public void getBigCopyView_async(AMD_Copy_getBigCopyView __cb, Ice.Current __current) {
		this.roleRt.getCopyControler().getBigCopyView(__cb);
	}

	@Override
	public void getSmallCopyView_async(final AMD_Copy_getSmallCopyView __cb, final int copyId, Current __current) {
		if (!this.roleRt.getCopyControler().canChallenge(copyId)) {
			__cb.ice_exception(new IllegalStateException());
			return;
		}

		final int tokenCount = this.roleRt.getItemControler().getItemCountInPackage(
				XsgCopyManager.getInstance().getClearItemTemplate());
		final short maxChallengeTime = XsgCopyManager.getInstance().getMaxChallengeTime(copyId);
		final short remainTime = (short) this.roleRt.getCopyControler().getRemainTime(copyId);
		final int resetCount = this.roleRt.getCopyControler().getResetCountToday(copyId);

		final ServerCopy sc = GlobalDataManager.getInstance().getServerCopy(copyId);
		final String resetTime = XsgCopyManager.getInstance().getNextResetTime();
		final int junLing = XsgCopyManager.getInstance().getJunLingConsume(copyId);
		if (sc == null) {
			__cb.ice_response(new SmallCopyView(remainTime, maxChallengeTime, tokenCount, false, Messages
					.getString("CopyI.DefaultCopyChampionName"), 0, resetCount, resetTime, //$NON-NLS-1$
					junLing));
			return;
		}

		XsgRoleManager.getInstance().loadRoleByIdAsync(sc.getChampionId(), new Runnable() {
			@Override
			public void run() {
				IRole championRole = XsgRoleManager.getInstance().findRoleById(sc.getChampionId());
				__cb.ice_response(new SmallCopyView(remainTime, maxChallengeTime, tokenCount, true, championRole
						.getName(), championRole.getVipLevel(), resetCount, resetTime, junLing));
			}
		}, new Runnable() {
			@Override
			public void run() {
				__cb.ice_response(new SmallCopyView(remainTime, maxChallengeTime, tokenCount, false, Messages
						.getString("CopyI.DefaultCopyChampionName"), 0, //$NON-NLS-1$
						resetCount, resetTime, junLing));
			}
		});
	}

	@Override
	public String clear(int copyTemplateId, int count, Current __current) throws NoteException, NotEnoughException {
		if (count != 1) {
			throw new IllegalStateException();
		}

		// 检查是否通过该难度关卡，同时检查是否有足够令牌
		CopyClearResultView result = this.roleRt.getCopyControler().clearCopy(copyTemplateId);
		return LuaSerializer.serialize(result);
	}

	@Override
	public CopyChallengeResultView beginChallenge(String formationId, int copyId, Current __current)
			throws NoteException, NotEnoughException {
		return this.roleRt.getCopyControler().beginChallenge(formationId, copyId, true);
	}

	@Override
	public void endChallenge_async(AMD_Copy_endChallenge __cb, Ice.Current __current) throws NoteException {
		this.roleRt.getCopyControler().endChallenge(__cb);
	}

	@Override
	public int getYuanbaoPrice(ItemView[] items, Current __current) throws NoteException {
		throw new NoteException("expire info.");
	}

	@Override
	public void buySuccess(ItemView[] items, Current __current) throws NotEnoughYuanBaoException, NoteException {
		// this.roleRt.getCopyControler().buySuccess(items);
		throw new NoteException(Messages.getString("CopyI.2")); //$NON-NLS-1$
	}

	@Override
	public ChapterRewardView getChapterRewardView(int chapterId, Current __current) {
		ChapterRewardView crv = this.roleRt.getCopyControler().getChapterRewardView(chapterId);
		return crv;
	}

	@Override
	public void receiveChapterReward(int chapterId, int level, Current __current) throws NoteException {
		this.roleRt.getCopyControler().receiveChapterReward(chapterId, level);
	}

	@Override
	public void buyChallengeChance(int copyId, Current __current) throws NotEnoughYuanBaoException, NoteException {
		this.roleRt.getCopyControler().buyChallengeChance(copyId);
	}

	@Override
	public IntIntPair[] buyChapterChallengeChance(int chapterId, Current __current) throws NotEnoughYuanBaoException,
			NoteException {
		return this.roleRt.getCopyControler().buyChapterChallengeChance(chapterId);
	};

	@Override
	public void releaseCaptured(Current __current) {
		this.roleRt.getCopyControler().releaseCaptured();
	}

	@Override
	public int killCaptured(Current __current) {
		return this.roleRt.getCopyControler().killCaptured();
	}

	@Override
	public String employCaptured(Current __current) throws NotEnoughMoneyException {
		return LuaSerializer.serialize(this.roleRt.getCopyControler().employCaptured());
	}

	@Override
	public String getCopyChallengeInfo(String idStr, Current __current) {
		String[] strArray = idStr.split(","); //$NON-NLS-1$
		int[] idArray = new int[strArray.length];
		for (int i = 0; i < idArray.length; i++) {
			idArray[i] = NumberUtil.parseInt(strArray[i]);
		}

		CopySummaryView[] copyInfoArray = this.roleRt.getCopyControler().getCopyChallengeInfo(idArray);
		return LuaSerializer.serialize(copyInfoArray);
	}

	@Override
	public void hallOfFameList_async(final AMD_Copy_hallOfFameList __cb, Current __current) {
		final SortedSet<Property> propertyList = GlobalDataManager.getInstance().getHallOfFameRoleIdListOrderByCount();
		final List<String> roleIdList = new ArrayList<String>();
		for (Property p : propertyList) {
			roleIdList.add(p.code);
		}
		XsgRoleManager.getInstance().loadRoleAsync(roleIdList, new Runnable() {

			@Override
			public void run() {
				List<HallOfFameView> list = new ArrayList<HallOfFameView>();
				for (Property p : propertyList) {
					IRole role = XsgRoleManager.getInstance().findRoleById(p.code);
					if (role != null) {
						list.add(new HallOfFameView(p.code, role.getName(), p.value, role.getHeadImage(), (short) role
								.getLevel(), role.getVipLevel()));
					}
				}

				__cb.ice_response(LuaSerializer.serialize(list.toArray(new HallOfFameView[0])));
			}
		});

	}

	@Override
	public int calculateStar(byte remainHero, byte killNum, float minTime, float maxTime, Current __current)
			throws NoteException {
		return this.roleRt.getCopyControler().calculateStar(remainHero, killNum, minTime, maxTime);
	}

	@Override
	public String getHuDongView(int copyId, Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getCopyControler().getHuDongView(copyId));
	}

	@Override
	public ChallengeTaResult endChallengeTa(int resFlag, Current __current) throws NoteException {
		this.roleRt.getCopyControler().endChallengeTa(resFlag);
		// TODO ... 次数少一个剩余武将的参数, 下个版本协议中加上
		String tempMovieId = XsgFightMovieManager.getInstance().endFightMovie(roleRt.getRoleId(), fightMovieIdContext,
				resFlag, (byte) 1);
		if (TextUtil.isBlank(tempMovieId)) {
			log.error(TextUtil.format(Messages.getString("CopyI.4"), roleRt.getRoleId(), "", resFlag)); //$NON-NLS-1$
		}
		// fightMovieIdContext = null;
		// this.copyId = -1;
		return new ChallengeTaResult(resFlag, tempMovieId);
	}

	@Override
	public int worshipTa(int copyId, Current __current) throws NoteException {
		return this.roleRt.getCopyControler().worshipTa(copyId);
	}

	@Override
	public void buyHuDong(int copyId, Current __current) throws NoteException, NotEnoughYuanBaoException {
		this.roleRt.getCopyControler().buyHudong(copyId);
	}

	@Override
	public void beginChallengeTa_async(final AMD_Copy_beginChallengeTa __cb, final int copyId, Current __current)
			throws NoteException {
		final ServerCopy sc = GlobalDataManager.getInstance().getServerCopy(copyId);
		if (sc == null) {
			__cb.ice_exception(new NoteException(Messages.getString("CopyControler.37")));
			return;
		}
		if (sc.getChampionId().equals(this.roleRt.getRoleId())) {
			__cb.ice_exception(new NoteException(Messages.getString("CopyI.5")));
			return;
		}
		this.roleRt.getCopyControler().resetHudongCount(copyId);
		if (roleRt.getCopyControler().getChallengeTaCount() <= 0) {
			__cb.ice_exception(new NoteException(Messages.getString("CopyI.6")));
			return;
		}
		FightLifeNumT numT = XsgFightMovieManager.getInstance().getFightLifeT(
				XsgFightMovieManager.Type.Challenge.ordinal());
		if (numT.newBattle == 1) { // 采用新的战斗机制，不应该请求这个接口
			__cb.ice_exception(new NoteException(Messages.getString("ResourceBackControler.invalidParam")));
			return;
		}
		XsgRoleManager.getInstance().loadRoleByIdAsync(sc.getChampionId(), new Runnable() {
			@Override
			public void run() {
				IRole iRole = XsgRoleManager.getInstance().findRoleById(sc.getChampionId());
				ChallengeTaView challengeTaView = roleRt.getCopyControler().getChallengeTaView(iRole, copyId);
				// CopyI.this.copyId = copyId;
				// CopyI.this.challengeRoleId = sc.getChampionId();
				// 存储战报上下文
				fightMovieIdContext = XsgFightMovieManager.getInstance().generateMovieId(
						XsgFightMovieManager.Type.Challenge, roleRt, iRole);
				__cb.ice_response(challengeTaView);
			}
		}, new Runnable() {
			@Override
			public void run() {
				__cb.ice_exception(new NoteException(Messages.getString("CopyI.7"))); //$NON-NLS-1$
			}
		});
	}

	// @Override
	// public String clearChapter(int chapterId, Current __current)
	// throws NoteException {
	// // 检查是否通过该难度关卡，同时检查是否有足够令牌
	// CopyClearResultView[] result = this.roleRt.getCopyControler()
	// .clearChapter(chapterId);
	// return LuaSerializer.serialize(result);
	// }

	@Override
	public IntIntPair getMyOccupy(Current __current) throws NoteException {
		SortedSet<Property> set = GlobalDataManager.getInstance().getHallOfFameRoleIdListOrderByCount();
		int current = 0;
		for (Property p : set) {
			if (p.code.equals(this.roleRt.getRoleId())) {
				current = p.value;
				break;
			}
		}
		VipT vipT = XsgVipManager.getInstance().findVipT(this.roleRt.getVipLevel());

		return new IntIntPair(current, vipT.maxServerCopy);
	}

	@Override
	public void buyMilitaryOrder(Current __current) throws NotEnoughYuanBaoException, NoteException {
		try {
			this.roleRt.getCopyControler().buyMilitaryOrder();
		} catch (NotEnoughMoneyException e) {
			LogManager.error(e);
		}
	}

	@Override
	public BuyMilitaryOrderView getBuyMilitaryOrderView(Current __current) throws NoteException {
		return this.roleRt.getCopyControler().getBuyMilitaryOrderView();
	}

	@Override
	public void failChallenge(Current __current) throws NoteException {
		roleRt.getCopyControler().failChallenge();
	}

	@Override
	public void getSmallCopyViewWithWarmup_async(AMD_Copy_getSmallCopyViewWithWarmup __cb, int copyId, Current __current)
			throws NoteException {
		this.roleRt.getCopyControler().getSmallCopyViewWithWarmup_async(__cb, copyId);
	}

	@Override
	public String beginWarmup(Current __current) throws NoteException {
		return this.roleRt.getCopyControler().beginWarmup();
	}

	@Override
	public byte endWarmup(byte remainHero, Current __current) throws NoteException {
		return this.roleRt.getCopyControler().endWarmup(remainHero);
	}

	@Override
	public String cancelWarmup(boolean first, Current __current) throws NoteException {
		return this.roleRt.getCopyControler().cancelWarmup(first);
	}

	@Override
	public int levyCopy(int copyId, Current __current) throws NoteException {
		return roleRt.getCopyControler().levyCopy(copyId);
	}

	@Override
	public String myOccupyList(Current __current) throws NoteException {
		return LuaSerializer.serialize(roleRt.getCopyControler().myOccupyList());
	}

	@Override
	public void giveCopy(int copyId, Current __current) throws NoteException {
		roleRt.getCopyControler().giveCopy(copyId);
	}

	@Override
	public void autoChallengeTa_async(AMD_Copy_autoChallengeTa __cb, int copyId, Current __current)
			throws NoteException {
		roleRt.getCopyControler().autoChallengeTa(__cb, copyId);
	}

}