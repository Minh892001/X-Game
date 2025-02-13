package com.morefun.XSanGo.hero;

import Ice.Current;

import com.XSanGo.Protocol.AttendantView;
import com.XSanGo.Protocol.HeroSkillPointView;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.NotEnoughMoneyException;
import com.XSanGo.Protocol.NotEnoughYuanBaoException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._HeroDisp;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.HeroSource;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.equip.EquipItem;
import com.morefun.XSanGo.item.IItem;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.TextUtil;

public class HeroI extends _HeroDisp {
	private static final long serialVersionUID = 1L;
	private IRole roleRt;

	public HeroI(IRole r) {
		this.roleRt = r;
	}

	@Override
	public void setHeroEquip(String heroId, String equipId, Current __current) throws NoteException {
		IItem item = this.roleRt.getItemControler().getItem(equipId);
		if (item == null || (!(item instanceof EquipItem))) {
			throw new NoteException(Messages.getString("HeroI.0")); //$NON-NLS-1$
		}

		EquipItem equip = (EquipItem) item;
		this.roleRt.getHeroControler().setHeroEquip(heroId, equip);
	}

	@Override
	public void removeHeroEquip(String heroId, String equipId, Current __current) throws NoteException {
		IItem item = this.roleRt.getItemControler().getItem(equipId);
		if (item == null || (!(item instanceof EquipItem))) {
			throw new NoteException(Messages.getString("HeroI.1")); //$NON-NLS-1$
		}

		EquipItem equip = (EquipItem) item;
		this.roleRt.getHeroControler().removeHeroEquip(heroId, equip);
	}

	@Override
	public void setHeroAttendant(String heroId, byte pos, String attendantId, Current __current) throws NoteException {
		if (pos < 0 || pos >= Const.Role.Max_Attendant_Count) {
			throw new NoteException(Messages.getString("HeroI.2") + pos); //$NON-NLS-1$
		}
		IHero attendant = this.roleRt.getHeroControler().getHero(attendantId);

		this.roleRt.getHeroControler().setHeroAttendant(heroId, pos, attendant);
	}

	@Override
	public void heroStarUp(String heroId, Current __current) throws NoteException, NotEnoughMoneyException {
		this.roleRt.getHeroControler().starUp(heroId);
	}

	@Override
	public ItemView[] heroColorUp(String heroId, Current __current) throws NoteException, NotEnoughMoneyException {
		return this.roleRt.getHeroControler().colorUp(heroId);
	}

	@Override
	public HeroView summonHero(int templateId, Current __current) throws NoteException, NotEnoughMoneyException {
		// 魂魄模板编号为小写字母s+武将模板
		String soulName = XsgHeroManager.getInstance().getSoulTemplateId(templateId);
		HeroT heroT = XsgHeroManager.getInstance().getHeroT(templateId);
		if (heroT == null) {
			throw new NoteException(Messages.getString("HeroI.3") + templateId); //$NON-NLS-1$
		}

		IHero hero = this.roleRt.getHeroControler().getHero(templateId);
		if (hero != null) {
			throw new NoteException(TextUtil.format(Messages.getString("HeroI.4"), //$NON-NLS-1$
					hero.getName()));
		}

		// 召唤武将需要魂魄数量
		int count = XsgHeroManager.getInstance().caculateSoulCountForSummon(heroT.color, heroT.star);
		int jinbi = XsgHeroManager.getInstance().caculateJinbiForSummon(heroT.color, heroT.star);
		if (this.roleRt.getItemControler().getItemCountInPackage(soulName) < count) {
			throw new NoteException(Messages.getString("HeroI.5")); //$NON-NLS-1$
		}
		if (this.roleRt.getJinbi() < jinbi) {
			throw new NotEnoughMoneyException();
		}

		try {
			this.roleRt.winJinbi(-jinbi);
		} catch (NotEnoughMoneyException e) {
			LogManager.error(e);
		}
		this.roleRt.getItemControler().changeItemByTemplateCode(soulName, -count);
		hero = this.roleRt.getHeroControler().addHero(heroT, HeroSource.Summon);

		return hero.getHeroView();
	}

	@Override
	public void heroSkillLevelUp(String heroId, int skillId, int upLevel, Current __current) throws NoteException,
			NotEnoughMoneyException {
		try {
			this.roleRt.getNotifyControler().setAutoNotify(false);
			this.roleRt.getHeroControler().skillLevelUp(heroId, skillId, upLevel);
		} finally {
			this.roleRt.getNotifyControler().setAutoNotify(true);
		}
	}

	@Override
	public HeroSkillPointView getHeroSkillView(Current __current) {
		return this.roleRt.getHeroSkillPointView();
	}

	@Override
	public void buyHeroSkillPoint(Current __current) throws NotEnoughYuanBaoException, NoteException {
		this.roleRt.buyHeroSkillPoint();
	}

	@Override
	public int getSkillPointInterval(Current __current) {
		return XsgGameParamManager.getInstance().getSkillPointInterval();
	}

	@Override
	public String getHeroPracticeList(String heroId, Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getHeroControler().getHeroPracticeList(heroId));
	}

	@Override
	public String resetPractice(String heroId, int id, Current __current) throws NoteException {
		return LuaSerializer.serialize(this.roleRt.getHeroControler().resetPractice(heroId, id));
	}

	@Override
	public void practice(String heroId, int id, String itemIds, Current __current) throws NoteException {
		this.roleRt.getHeroControler().practice(heroId, id, itemIds);
	}

	@Override
	public void activeHeroRelation(String heroId, int orignalRelationId, int level, Current __current)
			throws NotEnoughMoneyException, NoteException {
		this.roleRt.getHeroControler().setRelationLevel(heroId, orignalRelationId, level);
	}

	@Override
	public AttendantView resetAttendant(String heroId, byte pos, Current __current) throws NoteException {
		if (pos < 0 || pos >= Const.Role.Max_Attendant_Count) {
			throw new NoteException(Messages.getString("HeroI.2") + pos); //$NON-NLS-1$
		}
		AttendantView view = this.roleRt.getHeroControler().resetSpecialAttendant(heroId, pos);
		// return LuaSerializer.serialize(view);
		return view;
	}
}
