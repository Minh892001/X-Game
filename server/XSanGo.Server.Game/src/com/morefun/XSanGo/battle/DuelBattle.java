/**
 * 
 */
package com.morefun.XSanGo.battle;

import com.XSanGo.Protocol.DuelResult;
import com.morefun.XSanGo.util.NumberUtil;

/**
 * 单挑战斗
 * 
 * @author sulingyun
 *
 */
public class DuelBattle {
	private static int MaxRound = 30;
	private static int FirstUnitIdentity = 4444;
	private static int SecondUnitIdentity = 5555;
	private DuelUnit first;
	private DuelUnit second;
	private DuelUnit escaper;

	public DuelBattle(DuelUnit first, DuelUnit second) {
		this.first = first;
		this.second = second;
		this.first.setIdentity(FirstUnitIdentity);
		this.second.setIdentity(SecondUnitIdentity);
	}

	public DuelReport fuckEachOther() {
		DuelReport report = new DuelReport();
		report.setChallenger(this.first);
		report.setAcceptor(this.second);

		// 谁也秒杀不了谁，进入持久战
		int round = 0;
		while (!this.anyDead() && round <= MaxRound) {
			DuelRoundReport roundReport = new DuelRoundReport();
			if (round == 0) {// 冷箭和秒杀判断
				DuelUnit powerLower = this.first.getPower() > this.second
						.getPower() ? this.second : this.first;
				DuelUnit powerHigher = powerLower.equals(this.first) ? this.second
						: this.first;
				ActionReport actionA = powerLower.tryArrow(powerHigher);
				if (actionA != null) {
					roundReport.appendAction(actionA);
				}
				if (!this.anyDead()) {
					ActionReport actionB = powerHigher.trySeckill(powerLower);
					if (actionB != null) {
						roundReport.appendAction(actionB);
					}
				}

				report.appendRound(roundReport);
				round++;
				continue;
			}

			// 逃跑
			this.checkEscape();
			if (this.escaper != null) {
				DuelUnit other = this.getOther(this.escaper);
				roundReport
						.appendAction(this.escaper.createEscapeAction(other));

				ActionReport handle = other.handleEscape(this.escaper);
				if (handle != null) {
					roundReport.appendAction(handle);
				}
				report.appendRound(roundReport);
				round++;
				break;
			}

			ActionReport actionA = first.attack(second);
			roundReport.appendAction(actionA);

			if (!this.anyDead()) {
				ActionReport actionB = second.attack(first);
				roundReport.appendAction(actionB);
			}

			roundReport.appendExpireBuff(this.first.checkBuffExpire());
			roundReport.appendExpireBuff(this.second.checkBuffExpire());

			report.appendRound(roundReport);
			round++;
		}

		// 正式结算
		if (this.first.isDead()) {
			report.setResult(DuelResult.DuelResultFail);
		} else if (this.second.isDead()) {
			report.setResult(DuelResult.DuelResultWin);
		} else {
			if (this.escaper != null) {
				if (this.escaper.getIdentity() == this.first.getIdentity()) {
					report.setResult(DuelResult.DuelResultFail);
				} else {
					report.setResult(DuelResult.DuelResultWin);
				}
			} else {
				report.setResult(DuelResult.DuelResultNoWinNoFail);
			}
		}

		return report;
	}

	private void checkEscape() {
		if (this.escaper != null) {
			return;
		}

		if (this.isEscape(second)) {
			this.escaper = this.second;
			return;
		}
		if (this.isEscape(first)) {
			this.escaper = this.first;
			return;
		}
	}

	private boolean isEscape(DuelUnit unit) {
		if (!unit.alreadyCheckEscape() && unit.getMaxHp() / unit.getHp() >= 4) {
			unit.setEscapeCheck(true);
			return !NumberUtil.isHit(unit.getBrave(), 100);
		}
		return false;
	}

	private DuelUnit getOther(DuelUnit one) {
		return this.first.getIdentity() == one.getIdentity() ? this.second
				: this.first;
	}

	private boolean anyDead() {
		return this.first.isDead() || this.second.isDead();
	}
}
