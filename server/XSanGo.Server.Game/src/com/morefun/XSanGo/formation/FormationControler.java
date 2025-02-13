package com.morefun.XSanGo.formation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.FormationPosView;
import com.XSanGo.Protocol.FormationView;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleFormation;
import com.morefun.XSanGo.event.protocol.IFormationBuffChange;
import com.morefun.XSanGo.event.protocol.IFormationClear;
import com.morefun.XSanGo.event.protocol.IFormationPosChange;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.PositionData;
import com.morefun.XSanGo.item.FormationBuffItem;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

public class FormationControler implements IFormationControler {
	private IRole roleRt;
	private Role roleDb;
	private Map<String, IFormation> formationMap;
	private IFormationClear formationClearEvent;
	private IFormationBuffChange formationBuffChangeEvent;
	private IFormationPosChange formationPosChangeEvent;

	public FormationControler(IRole rt, Role db) {
		this.roleRt = rt;
		this.roleDb = db;

		this.formationMap = new HashMap<String, IFormation>();
		for (RoleFormation rf : this.roleDb.getRoleFormations()) {
			this.formationMap.put(rf.getId(), new XsgFormation(rt, rf));
		}

		this.formationClearEvent = this.roleRt.getEventControler().registerEvent(IFormationClear.class);
		this.formationBuffChangeEvent = this.roleRt.getEventControler().registerEvent(IFormationBuffChange.class);
		this.formationPosChangeEvent = this.roleRt.getEventControler().registerEvent(IFormationPosChange.class);
	}

	@Override
	public void checkData() {
		for (IFormation formation : this.formationMap.values()) {
			String fid = formation.getId();
			FormationBuffItem buff = formation.getBuff();
			if (buff != null && !fid.equals(buff.getRefereneFormationId())) {
				LogManager.warn(TextUtil.format(Messages.getString("FormationControler.0"), fid)); //$NON-NLS-1$
			}

			for (FormationPosView fpv : formation.getView().postions) {
				PositionData pd = this.roleRt.getHeroControler().getHero(fpv.heroId).getHeroPositionData();
				if (pd == null || !fid.equals(pd.formationId)) {
					LogManager.warn(TextUtil.format(Messages.getString("FormationControler.1"), //$NON-NLS-1$
							fid, fpv.heroId));
				}
			}

		}
	}

	@Override
	public void setFormationBuff(String formationIndex, FormationBuffItem book) {
		IFormation formation = this.getFormation(formationIndex);
		FormationBuffItem old = formation.getBuff();
		if (old != null) {
			old.setRefereneFormationIndex2Empty();
		}
		if (!TextUtil.isBlank(book.getRefereneFormationId())) {
			this.getFormation(book.getRefereneFormationId()).setBuff(null);
		}

		formation.setBuff(book);
		book.setRefereneFormationIndex(formationIndex);
		this.formationChange(formation);

		this.formationBuffChangeEvent.onFormationBuffChange(formation, book);
	}

	@Override
	public IFormation getFormation(String formationIndex) {
		return this.formationMap.get(formationIndex);
	}

	@Override
	public void setFormationPosition(String formationId, int position, IHero hero, boolean checkLastHero)
			throws NoteException {
		if (XsgFormationManager.getInstance().getFormationPosOpenLevel(position) > this.roleRt.getLevel()) {
			throw new NoteException(Messages.getString("FormationControler.2")); //$NON-NLS-1$
		}
		IFormation formation = this.getFormation(formationId);
		IHero oldHero = formation.getHeroByPos(position);
		int heroCountInFormation = formation.getHeroCountExcludeSupport();
		if (hero == oldHero) {
			return;
		}

		// 同一部队，两人互换的情况
		if (hero != null && oldHero != null && formation.equals(hero.getReferenceFormation())) {
			this.exchangePostion(formation, position, hero.getHeroPositionData().position);
			return;
		}

		if (hero == null && checkLastHero && heroCountInFormation < 2
				&& XsgFormationManager.getInstance().isBattlePosition(position)) {
			throw new NoteException(Messages.getString("FormationControler.3")); //$NON-NLS-1$
		}
		// 检查人数限制
		if (oldHero == null// 这里hero不可能为null，否则在方法刚被执行就return了
				&& XsgFormationManager.getInstance().isBattlePosition(position)
				// hero本来不在首发位置
				&& (!formation.containsHero(hero.getTemplateId()) || !XsgFormationManager.getInstance()
						.isBattlePosition(hero.getHeroPositionData().position))) {
			if (heroCountInFormation >= Const.MaxHeroCountInFormationExcludeSupport) {
				throw new NoteException(Messages.getString("FormationControler.4")); //$NON-NLS-1$
			}
		}
		if (oldHero != null) {
			this.roleRt.getHeroControler().removeReference(oldHero, false, false);
			heroCountInFormation--;
		}

		if (hero != null) {
			PositionData pd = hero.getHeroPositionData();
			// 最后一名上阵武将，且同一部队，给开绿灯
			if (pd != null && formationId.equals(pd.formationId) && heroCountInFormation == 1
					&& XsgFormationManager.getInstance().isBattlePosition((byte) pd.position)
					&& XsgFormationManager.getInstance().isBattlePosition(position)) {
				formation.setHeroPosition(pd.position, null);
			} else {
				this.roleRt.getHeroControler().removeReference(hero, true, true);
				pd = new PositionData();
			}

			pd.formationId = formationId;
			pd.position = position;
			hero.setHeroPositionData(pd);
		}
		formation.setHeroPosition(position, hero);

		this.formationChange(formation);
		// 有可能发生公会科技属性变更，所以都要重新获取数据
		if (oldHero != null) {
			roleRt.getNotifyControler().onHeroChanged(oldHero);
		}
		if (hero != null) {
			roleRt.getNotifyControler().onHeroChanged(hero);
		}
		this.formationPosChangeEvent.onFormationPositionChange(formation, position, hero);
	}

	/**
	 * 阵型中武将位置互换
	 * 
	 * @param formation
	 * @param pos1
	 * @param pos2
	 * @throws NoteException
	 */
	private void exchangePostion(IFormation formation, int pos1, int pos2) throws NoteException {
		IHero hero1 = formation.getHeroByPos(pos1);
		IHero hero2 = formation.getHeroByPos(pos2);
		if (hero1 == null || hero2 == null) {
			throw new NoteException(Messages.getString("FormationControler.5")); //$NON-NLS-1$
		}

		// 直接在互换位置
		formation.setHeroPosition(pos1, hero2);
		formation.setHeroPosition(pos2, hero1);
		// 各自状态数据直接重用
		PositionData temp = hero1.getHeroPositionData();
		hero1.setHeroPositionData(hero2.getHeroPositionData());
		hero2.setHeroPositionData(temp);

		this.formationChange(formation);

		// 需要触发两个事件
		this.formationPosChangeEvent.onFormationPositionChange(formation, pos1, hero2);
		this.formationPosChangeEvent.onFormationPositionChange(formation, pos2, hero1);
	}

	@Override
	public void formationChange(IFormation formation) {
		for (IHero hero : formation.getHeros()) {
			this.roleRt.getNotifyControler().onHeroChanged(hero);
		}

		this.roleRt.getNotifyControler().onFormationChange(formation);
	}

	@Override
	public Map<String, IFormation> getFormation() {
		return this.formationMap;
	}

	public FormationView[] getFormationViewList() {
		List<FormationView> list = new ArrayList<FormationView>();
		for (String id : this.formationMap.keySet()) {
			list.add(this.formationMap.get(id).getView());
		}

		return list.toArray(new FormationView[0]);
	}

	@Override
	public IFormation getDefaultFormation() {
		for (IFormation f : this.formationMap.values()) {
			if (f.getIndex() == 0) {
				return f;
			}
		}

		throw new IllegalStateException(TextUtil.format("[{0}] has no default formation!", this.roleRt.getRoleId())); //$NON-NLS-1$
	}

	@Override
	public PvpOpponentFormationView getPvpOpponentFormationView(String formationId) {
		// 获取作为PVP对手时候的部队配置数据
		return getPvpOpponentFormationView(formationId, false);
	}
	
	@Override
	public PvpOpponentFormationView getPvpOpponentFormationView(String formationId, boolean isCalculationTempProp) {
		List<HeroView> heroViewList = new ArrayList<HeroView>();
		IFormation formation = this.getFormation(formationId);
		for (IHero hero : formation.getHeros()) {
			heroViewList.add(hero.getHeroView(isCalculationTempProp));
		}
		return new PvpOpponentFormationView(formation.getView(), heroViewList.toArray(new HeroView[0]));
	}

	@Override
	public void clearFormation(String formationId) {
		IFormation formation = this.getFormation(formationId);
		if (formation.getHeroCountIncludeSupport() < 2) {
			return;
		}
		// 清空部队配置，当小于2名武将时直接返回，否则会保留1名武将
		try {
			this.roleRt.getNotifyControler().setAutoNotify(false);
			for (int i = 11; i >= 9; i--) {
				IHero hero = formation.getHeroByPos(i);
				if (hero != null) {
					this.roleRt.getHeroControler().removeReference(hero, false, true);
				}
			}
			for (int i = 8; i >= 0; i--) {
				if (formation.getHeroCountExcludeSupport() < 2) {
					break;
				}
				IHero hero = formation.getHeroByPos(i);
				if (hero != null) {
					this.roleRt.getHeroControler().removeReference(hero, false, true);
				}
			}

			this.formationChange(formation);
			this.formationClearEvent.onFormationClear(formation);
		} catch (NoteException e) {
			LogManager.error(e);
		} finally {
			this.roleRt.getNotifyControler().setAutoNotify(true);
		}
	}
}
