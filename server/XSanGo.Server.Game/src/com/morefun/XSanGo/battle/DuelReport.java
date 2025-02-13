/**
 * 
 */
package com.morefun.XSanGo.battle;

import java.util.ArrayList;
import java.util.List;

import com.XSanGo.Protocol.ActionReportView;
import com.XSanGo.Protocol.DuelBuffView;
import com.XSanGo.Protocol.DuelReportView;
import com.XSanGo.Protocol.DuelResult;
import com.XSanGo.Protocol.DuelRoundReportView;
import com.XSanGo.Protocol.DuelUnitView;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 单挑战报
 * 
 * @author sulingyun
 *
 */
public class DuelReport {
	private DuelResult result;
	private List<DuelRoundReport> roundList = new ArrayList<DuelRoundReport>();
	private DuelUnit first;
	private DuelUnit second;

	public void setResult(DuelResult res) {
		this.result = res;
	}

	public void appendRound(DuelRoundReport roundReport) {
		this.roundList.add(roundReport);
	}

	@Override
	public String toString() {
		String p1 = this.first.getName(), p2 = this.second.getName();
		StringBuilder sb = new StringBuilder();
		sb.append(p1 + "HP：" + this.first.getMaxHp());
		sb.append("\n");
		sb.append(p2 + "HP：" + this.second.getMaxHp());
		sb.append("\n");

		for (int i = 0; i < this.roundList.size(); i++) {
			sb.append("Round " + i);
			sb.append("\n");

			DuelRoundReport round = this.roundList.get(i);
			if (round.getActionList().size() == 0) {
				continue;
			}

			ActionReport act1 = round.getActionList().get(0);
			sb.append(this.formatAttackMsg(act1));
			sb.append("\n");

			if (round.getActionList().size() > 1) {
				ActionReport act2 = round.getActionList().get(1);
				sb.append(this.formatAttackMsg(act2));
				sb.append("\n");
			}
		}

		if (this.result == DuelResult.DuelResultWin) {
			sb.append(p1 + "获得最终胜利！");
		} else if (this.result == DuelResult.DuelResultFail) {
			sb.append(p2 + "获得最终胜利！");
		} else {
			sb.append("双方打成平手！");
		}

		return sb.toString();
	}

	private String formatAttackMsg(ActionReport act) {
		String player = act.getExecutorId() == this.first.getIdentity() ? this.first
				.getName() : this.second.getName();
		return TextUtil.format(
				"{0}使用技能{1}，造成{2}伤害{3}，攻击结果{4}，自身怒气为{5}，对方怒气为{6}。", player,
				act.getSkillId(), act.isCrit() ? "暴击" : "", act.getDamage(),
				act.getAttackResult(), act.getSp1(), act.getSp2());
	}

	public void setChallenger(DuelUnit unit) {
		this.first = unit;
	}

	public void setAcceptor(DuelUnit unit) {
		this.second = unit;
	}

	public DuelReportView generateView() {
		DuelReportView result = new DuelReportView();
		result.result = this.result;
		result.firsts = new DuelUnitView[] { DuelUtil
				.createDuelUnitView(this.first) };
		result.seconds = new DuelUnitView[] { DuelUtil
				.createDuelUnitView(this.second) };

		List<DuelRoundReportView> list = new ArrayList<DuelRoundReportView>();
		for (DuelRoundReport round : this.roundList) {
			List<ActionReportView> actList = new ArrayList<ActionReportView>();
			for (ActionReport act : round.getActionList()) {
				actList.add(new ActionReportView(act.getExecutorId(), act
						.getTargetId(), act.getSkillId(),
						act.getAttackResult(), act.getDamage(), act.isCrit(),
						act.getSp1(), act.getSp2(), act.getPower1(), act
								.getPower2(),
						act.getBuff() == null ? new DuelBuffView[0]
								: new DuelBuffView[] { act.getBuff() }));
			}

			list.add(new DuelRoundReportView(actList
					.toArray(new ActionReportView[0]), round
					.getExpireBuffList()));
		}

		result.rounds = list.toArray(new DuelRoundReportView[0]);
		// System.out.println(TextUtil.GSON.toJson(result));
		return result;
	}
}
