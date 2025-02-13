package com.morefun.XSanGo.equip;

import com.XSanGo.Protocol.ArtifactView;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 神器
 * 
 * @author xiongming.li
 *
 */
public interface IArtifactControler extends IRedPointNotable {

	ArtifactView[] getAllArtifact() throws NoteException;

	void upgradeArtifact(String dbId) throws NoteException;

	void useArtifact(String dbId, String heroId) throws NoteException;

	/**
	 * 获取武将装备的神器
	 * 
	 * @param heroId
	 * @return
	 */
	ArtifactLevelT getHeroArtifactId(String heroId);
}
