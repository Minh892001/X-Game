package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

/**
 * 武将神器变化
 * 
 * @author xiongming.li
 *
 */
@signalslot
public interface IHeroArtifactChange {
	void onHeroArtifactChange(String oldHero, String newHero, int artifactId, int artifactLevel);
}
