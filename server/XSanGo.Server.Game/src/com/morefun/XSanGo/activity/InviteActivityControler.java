package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.List;

import com.XSanGo.Protocol.InviteActivityView;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.SummationReward;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.InviteCode;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleInviteActivity;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.reward.TcResult;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 邀请好友
 * 
 * @author lixiongming
 *
 */
@RedPoint(isTimer = true)
public class InviteActivityControler implements IInviteActivityControler {
	private Role db;
	private IRole rt;

	public InviteActivityControler(IRole rt, Role db) {
		this.db = db;
		this.rt = rt;
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		SummationReward[] summationRewards = getSummationReward();
		for (SummationReward s : summationRewards) {
			if (this.db.getInviteNum() >= s.threshold && !s.received) {
				return new MajorUIRedPointNote(MajorMenu.InviteFriendMenu,
						false);
			}
		}
		return null;
	}

	@Override
	public InviteActivityView getInviteActivityView() throws NoteException {
		InviteConfT conf = XsgActivityManage.getInstance().inviteConf;
		if (this.rt.getLevel() >= conf.minLevel) {
			if (TextUtil.isBlank(this.db.getInviteCode())) {
				InviteCode inviteCode = XsgActivityManage.getInstance()
						.getAvailableCode();
				this.db.setInviteCode(inviteCode.getCode());
				inviteCode.setUseRoleId(this.rt.getRoleId());
				saveInviteCode(inviteCode);
			}
		}
		InviteActivityView view = new InviteActivityView(getSummationReward(),
				this.db.getInviteNum(), this.db.getInviteCode());
		return view;
	}

	private void saveInviteCode(final InviteCode inviteCode) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				SimpleDAO simpleDAO = SimpleDAO
						.getFromApplicationContext(ServerLancher.getAc());
				simpleDAO.save(inviteCode);
			}
		});
	}

	/**
	 * 获取奖励列表
	 * 
	 * @return
	 */
	private SummationReward[] getSummationReward() {
		List<SummationReward> list = new ArrayList<SummationReward>();
		for (InviteActivityT it : XsgActivityManage.getInstance().inviteActivitys) {
			boolean received = this.db.getInviteActivity().get(it.num) != null;
			TcResult tcResult = XsgRewardManager.getInstance().doTc(this.rt,
					it.tc);
			ItemView[] itemViews = XsgRewardManager.getInstance()
					.generateItemView(tcResult);
			list.add(new SummationReward(it.num, received, itemViews));
		}
		return list.toArray(new SummationReward[0]);
	}

	@Override
	public void receiveRewardForInvite(int threshoId) throws NoteException {
		InviteActivityT inviteActivityT = XsgActivityManage.getInstance()
				.getInviteActivityT(threshoId);
		if (inviteActivityT == null) {
			throw new NoteException(Messages.getString("InviteActivityControler.0")); //$NON-NLS-1$
		}
		if (this.db.getInviteNum() < threshoId) {
			throw new NoteException(Messages.getString("InviteActivityControler.1")); //$NON-NLS-1$
		}
		RoleInviteActivity inviteActivity = this.db.getInviteActivity().get(
				threshoId);
		if (inviteActivity != null) {
			throw new NoteException(Messages.getString("InviteActivityControler.2")); //$NON-NLS-1$
		}
		// 发放奖励
		TcResult tcResult = XsgRewardManager.getInstance().doTc(this.rt,
				inviteActivityT.tc);
		this.rt.getRewardControler().acceptReward(tcResult);
		inviteActivity = new RoleInviteActivity(GlobalDataManager.getInstance()
				.generatePrimaryKey(), this.db, threshoId);
		this.db.getInviteActivity().put(threshoId, inviteActivity);
	}

}
