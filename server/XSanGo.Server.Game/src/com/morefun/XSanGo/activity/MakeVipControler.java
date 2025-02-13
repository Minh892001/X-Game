package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.event.protocol.IVipEndAnswer;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

@RedPoint(isTimer = true)
public class MakeVipControler implements IMakeVipControler {
	private IRole iRole;
	private Role role;
	private List<AnswerT> answerTs = new ArrayList<AnswerT>();// 需要回答的题目
	private IVipEndAnswer vipEndAnswerEvent;

	public MakeVipControler(IRole iRole, Role role) {
		this.iRole = iRole;
		this.role = role;
		this.vipEndAnswerEvent = iRole.getEventControler().registerEvent(IVipEndAnswer.class);
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!iRole.isOnline()) {
			return null;
		}
		if (iRole.getRoleOpenedMenu().getOpenMakeVipDate() != null
				&& DateUtil.isSameDay(new Date(), iRole.getRoleOpenedMenu().getOpenMakeVipDate())) {
			return null;
		}
		MakeVipT makeVipT = XsgActivityManage.getInstance().makeVipT;
		if (isAnswer() && this.iRole.getLevel() >= makeVipT.level) {
			return new MajorUIRedPointNote(MajorMenu.AnswerMenu, true);
		}
		return null;
	}

	@Override
	public boolean isAnswer() {
		// 今天已经答过题了
		if (role.getLastAnswerDate() != null && DateUtil.isSameDay(role.getLastAnswerDate(), new Date())) {
			return false;
		}
		MakeVipT makeVipT = XsgActivityManage.getInstance().makeVipT;
		if (makeVipT.isOpen == XsgActivityManage.OPEN) {
			return DateUtil.checkTimeRange(new Date(), makeVipT.beginTime, makeVipT.endTime);
		}
		return false;
	}

	@Override
	public String beginAnswer() throws NoteException {
		if (!isAnswer()) {
			throw new NoteException(Messages.getString("MakeVipControler.0")); //$NON-NLS-1$
		}
		MakeVipT makeVipT = XsgActivityManage.getInstance().makeVipT;
		if (this.role.getLevel() < makeVipT.level) {
			throw new NoteException(TextUtil.format(Messages.getString("MakeVipControler.1"), //$NON-NLS-1$
					makeVipT.level));
		}
		this.role.setLastAnswerDate(new Date());
		this.answerTs = XsgActivityManage.getInstance().getSubjectList(makeVipT.subjectNum);
		StringBuilder sb = new StringBuilder();
		for (AnswerT at : this.answerTs) {
			if (sb.length() == 0) {
				sb.append(at.id);
			} else {
				sb.append("," + at.id);
			}
		}
		return sb.toString();
	}

	@Override
	public int endAnswer(String str) throws NoteException {
		if (this.answerTs == null) {
			throw new NoteException(Messages.getString("MakeVipControler.3"));
		}
		int sumVipExp = 0;
		String[] content = str.split(";");
		for (String s : content) {
			String[] idAndAnswer = s.split(",");
			int id = Integer.valueOf(idAndAnswer[0]);
			String answer = idAndAnswer[1];
			for (AnswerT a : this.answerTs) {
				// 不知道得一半分
				if (id == a.id && "D".equals(answer)) {
					sumVipExp += a.vipExp / 2;
				} else if (id == a.id && a.rightAnswer.equals(answer)) {// 回答正确
					sumVipExp += a.vipExp;
				}
			}
		}
		this.iRole.getVipController().addExperience(sumVipExp);
		// 答题结束事件
		vipEndAnswerEvent.onVipEndAnswer(sumVipExp);
		this.answerTs = null;
		return sumVipExp;
	}

}
