package com.morefun.XSanGo.copy;

import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.game.WorshipRank;
import com.morefun.XSanGo.db.game.WorshipRankDAO;
import com.morefun.XSanGo.event.protocol.IWorship;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;

public class WorshipControler implements IWorshipRankControler {
	private IWorship worshipEvent;
	private IRole iRole;

	public WorshipControler(IRole iRole) {
		this.iRole = iRole;
		this.worshipEvent = this.iRole.getEventControler().registerEvent(
				IWorship.class);
	}

	@Override
	public void addWorshipRank(final String roleId) {
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				WorshipRankDAO dao = WorshipRankDAO
						.getFromApplicationContext(ServerLancher.getAc());
				WorshipRank worshipRank = dao.findByRoleId(roleId);
				if (worshipRank == null) {
					IRole iRole = XsgRoleManager.getInstance().findRoleById(
							roleId);
					worshipRank = new WorshipRank(GlobalDataManager
							.getInstance().generatePrimaryKey(), roleId, 1,
							iRole.getName());
					dao.save(worshipRank);
				} else {
					worshipRank.setWorshipCount(worshipRank.getWorshipCount() + 1);
					dao.update(worshipRank);
				}
				final int count = worshipRank.getWorshipCount();
				LogicThread.execute(new Runnable() {

					@Override
					public void run() {
						worshipEvent.onWorship(roleId, count);
					}
				});
			}
		});
	}
}
