package com.morefun.XSanGo.formation;

import Ice.Current;

import com.XSanGo.Protocol.FormationView;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._FormationDisp;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.item.FormationBuffItem;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.role.IRole;

public class FormationI extends _FormationDisp {
	/** 序列化版本 */
	private static final long serialVersionUID = 1L;
	private IRole roleRt;

	public FormationI(IRole rt) {
		this.roleRt = rt;

	}

	@Override
	public FormationView[] getFormationList(Current __current) {
		return this.roleRt.getFormationControler().getFormationViewList();
	}

	@Override
	public void setFormationBuff(String formationId, String bookId,
			Current __current) {
		this.checkFormationIndex(formationId);

		IItem book = this.roleRt.getItemControler().getItem(bookId);
		if (book == null) {
			throw new IllegalArgumentException();
		}
		if (!(book instanceof FormationBuffItem)) {
			throw new IllegalArgumentException();
		}

		this.roleRt.getFormationControler().setFormationBuff(formationId,
				(FormationBuffItem) book);

	}

	private void checkFormationIndex(String formationId) {
		if (this.roleRt.getFormationControler().getFormation(formationId) == null) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void setFormationPosition(String formationId, byte postion,
			String heroId, Current __current) throws NoteException {
		this.checkFormationIndex(formationId);

		if (postion < 0 || postion > 11) {
			throw new NoteException(Messages.getString("FormationI.0")); //$NON-NLS-1$
		}

		IHero hero = this.roleRt.getHeroControler().getHero(heroId);
		try {
			this.roleRt.getNotifyControler().setAutoNotify(false);
			this.roleRt.getFormationControler().setFormationPosition(
					formationId, postion, hero, true);
		} finally {
			this.roleRt.getNotifyControler().setAutoNotify(true);
		}
	}

	@Override
	public void setFormationSkill(String formationId, byte pos, int skillId,
			Current __current) {
		throw new IllegalArgumentException();
		// if (pos < 0 || pos > Const.MaxSkillCountInFormation) {
		// throw new IllegalArgumentException();
		// }
		//
		// this.roleRt.getFormationControler().setFormationSkill(formationId,
		// pos,
		// skillId);
	}

	@Override
	public void clearFormation(String formationId, Current __current)
			throws NoteException {
		this.checkFormationIndex(formationId);
		this.roleRt.getFormationControler().clearFormation(formationId);
	}
}
