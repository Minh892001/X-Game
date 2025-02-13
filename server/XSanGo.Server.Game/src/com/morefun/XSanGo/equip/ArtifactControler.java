package com.morefun.XSanGo.equip;

import java.util.ArrayList;
import java.util.List;

import com.XSanGo.Protocol.ArtifactView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleArtifact;
import com.morefun.XSanGo.event.protocol.IHeroArtifactChange;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.TextUtil;

@RedPoint
public class ArtifactControler implements IArtifactControler {
	private IRole iRole;
	private Role role;
	private IHeroArtifactChange artifactChangeEvent;

	public ArtifactControler(IRole iRole, Role role) {
		this.iRole = iRole;
		this.role = role;

		artifactChangeEvent = iRole.getEventControler().registerEvent(IHeroArtifactChange.class);
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		for (RoleArtifact a : role.getArtifact().values()) {
			ArtifactLevelT levelT = XsgEquipManager.getInstance()
					.getArtifactLevelT(a.getArtifactId(), a.getLevel() + 1);
			if (levelT == null) {// 满级了
				continue;
			}
			boolean redPointNote = true;
			for (String its : levelT.upgradeItems.split(",")) {
				String code = its.split(":")[0];
				int num = Integer.parseInt(its.split(":")[1]);
				if (iRole.getItemControler().getItemCountInPackage(code) < num) {
					redPointNote = false;
					break;
				}
			}
			if (redPointNote) {
				return new MajorUIRedPointNote(MajorMenu.Artifact, true);
			}
		}
		return null;
	}

	@Override
	public ArtifactView[] getAllArtifact() throws NoteException {
		int openLevel = XsgGameParamManager.getInstance().getArtifactOpenLevel();
		if (iRole.getLevel() < openLevel) {
			throw new NoteException(TextUtil.format("Level {0} Open.", openLevel));
		}
		initArtifact();
		List<ArtifactView> views = new ArrayList<ArtifactView>();
		for (RoleArtifact r : role.getArtifact().values()) {
			views.add(new ArtifactView(r.getId(), r.getArtifactId(), r.getLevel(), r.getUseHeroId()));
		}
		return views.toArray(new ArtifactView[0]);
	}

	private void initArtifact() {
		for (ArtifactT t : XsgEquipManager.getInstance().getAllArtifactT()) {
			if (!role.getArtifact().containsKey(t.id)) {
				role.getArtifact().put(t.id,
						new RoleArtifact(GlobalDataManager.getInstance().generatePrimaryKey(), role, t.id, 0));
			}
		}
	}

	@Override
	public void upgradeArtifact(String dbId) throws NoteException {
		RoleArtifact artifact = null;
		for (RoleArtifact a : role.getArtifact().values()) {
			if (a.getId().equals(dbId)) {
				artifact = a;
			}
		}

		if (artifact == null) {
			throw new NoteException(Messages.getString("ArtifactControler.artifactNotExist"));
		}
		ArtifactLevelT levelT = XsgEquipManager.getInstance().getArtifactLevelT(artifact.getArtifactId(),
				artifact.getLevel() + 1);
		if (levelT == null) {
			throw new NoteException(Messages.getString("ArtifactControler.artifactLevelFull"));
		}
		if (iRole.getJinbi() < levelT.upgradeGold) {
			throw new NoteException(Messages.getString("ArtifactControler.artifactNotItem"));
		}
		for (String its : levelT.upgradeItems.split(",")) {
			String code = its.split(":")[0];
			int num = Integer.parseInt(its.split(":")[1]);
			if (iRole.getItemControler().getItemCountInPackage(code) < num) {
				throw new NoteException(Messages.getString("ArtifactControler.artifactNotItem"));
			}
		}

		try {
			iRole.winJinbi(-levelT.upgradeGold);
			for (String its : levelT.upgradeItems.split(",")) {
				String code = its.split(":")[0];
				int num = Integer.parseInt(its.split(":")[1]);
				iRole.getItemControler().changeItemByTemplateCode(code, -num);
			}
		} catch (Exception e) {
			throw new NoteException(Messages.getString("ArtifactControler.artifactNotItem"));
		}
		artifact.setLevel(artifact.getLevel() + 1);

		IHero hero = iRole.getHeroControler().getHero(artifact.getUseHeroId() == null ? "" : artifact.getUseHeroId());
		if (hero != null) {
			iRole.getNotifyControler().onHeroChanged(hero);
		}

		artifactChangeEvent.onHeroArtifactChange(artifact.getUseHeroId(), "", artifact.getArtifactId(),
				artifact.getLevel());
	}

	@Override
	public void useArtifact(String dbId, String heroId) throws NoteException {
		RoleArtifact artifact = null;
		for (RoleArtifact a : role.getArtifact().values()) {
			if (a.getId().equals(dbId)) {
				artifact = a;
			}
			if (heroId.equals(a.getUseHeroId()) && !a.getId().equals(dbId) && TextUtil.isNotBlank(heroId)) {
				throw new NoteException(Messages.getString("ArtifactControler.artifactExistHero"));
			}
		}

		if (artifact == null) {
			throw new NoteException(Messages.getString("ArtifactControler.artifactNotExist"));
		}
		IHero hero = null;
		if (TextUtil.isNotBlank(heroId)) {
			hero = iRole.getHeroControler().getHero(heroId);
			if (hero == null) {
				throw new NoteException(Messages.getString("ChatControler.79"));
			}
		}
		IHero oldHero = null;
		if (TextUtil.isNotBlank(artifact.getUseHeroId())) {
			oldHero = iRole.getHeroControler().getHero(artifact.getUseHeroId());
		}
		artifact.setUseHeroId(heroId);

		if (hero != null) {
			iRole.getNotifyControler().onHeroChanged(hero);
		}
		if (oldHero != null) {
			iRole.getNotifyControler().onHeroChanged(oldHero);
		}

		artifactChangeEvent.onHeroArtifactChange(oldHero == null ? "" : oldHero.getId(), artifact.getUseHeroId(),
				artifact.getArtifactId(), artifact.getLevel());
	}

	@Override
	public ArtifactLevelT getHeroArtifactId(String heroId) {
		for (RoleArtifact a : role.getArtifact().values()) {
			if (heroId.equals(a.getUseHeroId())) {
				return XsgEquipManager.getInstance().getArtifactLevelT(a.getArtifactId(), a.getLevel());
			}
		}
		return null;
	}

}
