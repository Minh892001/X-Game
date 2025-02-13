package com.morefun.XSanGo.activity;

import java.util.Date;

import com.XSanGo.Protocol.ActivityAnnounceView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.morefun.XSanGo.common.RedPoint;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;

@RedPoint(isTimer = true)
public class AnnounceControler implements IAnnounceControler {
	private IRole iRole;

	// private Role role;

	public AnnounceControler(IRole rt, Role db) {
		this.iRole = rt;
		// this.role = db;
	}

	@Override
	public MajorUIRedPointNote getRedPointNote() {
		if (!iRole.isOnline()) {
			return null;
		}
		if (iRole.getRoleOpenedMenu().getOpenAnnounceDate() != null
				&& DateUtil.isSameDay(new Date(), iRole.getRoleOpenedMenu().getOpenAnnounceDate())) {
			return null;
		}
		ActivityAnnounceView[] views = iRole.generateAnnounceViewList();
		for (ActivityAnnounceView v : views) {
			if (!v.read) {
				return new MajorUIRedPointNote(MajorMenu.AnnounceMenu, true);
			}
		}
		return null;
	}

}
