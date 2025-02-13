/**
 * 
 */
package com.morefun.XSanGo.temp;

import java.util.Calendar;
import java.util.List;

import com.XSanGo.Protocol.BuyHeroResult;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.common.MailSource;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.event.protocol.IBuy10WineByYuanbao;
import com.morefun.XSanGo.mail.AbsMailAttachObject;
import com.morefun.XSanGo.mail.TemplateAttachObject;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;

/**
 * 元宝十连抽必出物品，通过邮件发送
 * 
 * @author sulingyun
 *
 */
public class FixRewardForBuy10WineByYuanbao implements IBuy10WineByYuanbao {
	public static FixRewardForBuy10WineT config = ServerLancher.getAc()
			.containsBean("Yuanbao10WineFixReward") ? ServerLancher.getAc()
			.getBean("Yuanbao10WineFixReward", FixRewardForBuy10WineT.class)
			: new FixRewardForBuy10WineT();
	private IRole rt;

	public FixRewardForBuy10WineByYuanbao(IRole rt) {
		this.rt = rt;
		this.rt.getEventControler().registerHandler(IBuy10WineByYuanbao.class,
				this);
	}

	@Override
	public void onBuy10WineByYuanbao(List<BuyHeroResult> list) {
		if (!config.isEnable()) {
			return;
		}

		String attachments = XsgMailManager.getInstance().serializeMailAttach(
				new AbsMailAttachObject[] { new TemplateAttachObject(config
						.getItemTemplateId(), 1) });
		XsgMailManager.getInstance().sendMail(
				new Mail(GlobalDataManager.getInstance().generatePrimaryKey(),
						MailSource.System.ordinal(), "", "X三国运营团队", this.rt
								.getRoleId(), "主公，您是会玩的！", "这个宝箱最适合您了！",
						attachments, Calendar.getInstance().getTime()));
	}
}
