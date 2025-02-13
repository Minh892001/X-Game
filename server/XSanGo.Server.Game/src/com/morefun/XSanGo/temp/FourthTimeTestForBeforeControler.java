package com.morefun.XSanGo.temp;

import java.util.Calendar;

import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.MailSource;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleFourthTest;
import com.morefun.XSanGo.event.protocol.IRoleLevelup;
import com.morefun.XSanGo.mail.AbsMailAttachObject;
import com.morefun.XSanGo.mail.TemplateAttachObject;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;

/**
 * 
 * 二、所有3次测试充值总额折算当前成VIP发放，例（1000元宝=V7，玩家3次充值总额1200那么给玩家返还V7），如果玩家使用相同账号登陆不删档内测1服
 * ，等级达到10级即立马返还VIP并邮件通知玩家获得VIP原因，同时通过邮件形式给该玩家返还他3次充值总额×2的元宝；
 * 
 * 
 * 三、终极测试“火烧洛阳”所有玩家等级达到30级的，使用终极测试账号登陆不删档内测1服即立马通过邮件形式给玩家返还500元宝+随机紫将宝箱*1。
 * 
 * 201505271500和小美沟通确认，原本的创角发元宝改为10级时发
 * 
 * @author sulingyun
 *
 */
public class FourthTimeTestForBeforeControler implements
		IFourthTimeTestForBeforeControler, IRoleLevelup {

	private static final int Level_Limit = 13;
	private static final String Charge_Title = Messages.getString("FourthTimeTestForBeforeControler.0"); //$NON-NLS-1$
	private static final String Charge_Body = Messages.getString("FourthTimeTestForBeforeControler.1"); //$NON-NLS-1$
	private static final String Level_Title = Messages.getString("FourthTimeTestForBeforeControler.2"); //$NON-NLS-1$
	private static final String Level_Body = Messages.getString("FourthTimeTestForBeforeControler.3"); //$NON-NLS-1$
	private IRole rt;
	private Role db;

	public FourthTimeTestForBeforeControler(IRole rt, Role db) {
		this.rt = rt;
		this.db = db;

		this.rt.getEventControler().registerHandler(IRoleLevelup.class, this);
	}

	@Override
	public void onRoleLevelup() {
		if (!XsgFourthTimeTestManger.getInstance().isEnable()) {
			return;
		}

		this.handleChargeReturn();
	}

	/**
	 * 处理充值返利
	 */
	private void handleChargeReturn() {
		if (this.rt.getLevel() < Level_Limit) {
			return;
		}
		// 已经返还过或者不在封测充值帐号列表里的，不做处理
		ChargeHistoryT template = XsgFourthTimeTestManger.getInstance()
				.findChargeHistoryT(this.rt.getAccount());
		if (template == null) {
			return;
		}

		RoleFourthTest rft = this.getOrCreateDB();
		if (rft.isChargeReturn()) {
			return;
		}

		int vipExperience = template.rmb * 10;
		this.rt.getVipController().addExperience(vipExperience);
		AbsMailAttachObject[] attaches = new AbsMailAttachObject[] { new TemplateAttachObject(
				"rmby", vipExperience * 2) }; //$NON-NLS-1$
		XsgMailManager.getInstance().sendMail(
				new Mail(GlobalDataManager.getInstance().generatePrimaryKey(),
						MailSource.System.ordinal(), "", Messages.getString("FourthTimeTestForBeforeControler.6"), this.rt //$NON-NLS-1$ //$NON-NLS-2$
								.getRoleId(), Charge_Title, Charge_Body,
						XsgMailManager.getInstance().serializeMailAttach(
								attaches), Calendar.getInstance().getTime()));

		if (XsgFourthTimeTestManger.getInstance().isAccountInLevelRank(
				this.rt.getAccount())) {
			attaches = new AbsMailAttachObject[] {
					new TemplateAttachObject("rmby", 500), //$NON-NLS-1$
					new TemplateAttachObject("ch_hero", 1) }; //$NON-NLS-1$
			XsgMailManager.getInstance().sendMail(
					new Mail(GlobalDataManager.getInstance()
							.generatePrimaryKey(), MailSource.System.ordinal(),
							"", Messages.getString("FourthTimeTestForBeforeControler.10"), this.rt.getRoleId(), Level_Title, //$NON-NLS-1$ //$NON-NLS-2$
							Level_Body, XsgMailManager.getInstance()
									.serializeMailAttach(attaches), Calendar
									.getInstance().getTime()));
		}

		rft.setChargeReturn(true);
	}

	private RoleFourthTest getOrCreateDB() {
		if (this.db.getFourthTest() == null) {
			this.db.setFourthTest(new RoleFourthTest(db));
		}

		return this.db.getFourthTest();
	}
}
