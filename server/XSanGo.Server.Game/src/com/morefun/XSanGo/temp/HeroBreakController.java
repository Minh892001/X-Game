package com.morefun.XSanGo.temp;

import java.util.Calendar;
import java.util.Date;

import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.common.MailSource;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.event.protocol.IHeroBreakUp;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.mail.AbsMailAttachObject;
import com.morefun.XSanGo.mail.TemplateAttachObject;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 武将突破送大礼
 * 
 * @author guofeng.qin
 */
public class HeroBreakController extends AbsTemporaryOperationWrapper implements
		IHeroBreakController, IHeroBreakUp {

	private BreakUpParam param;

	public HeroBreakController(IRole rt, String params, Date begin, Date end) {
		super(rt, params, begin, end);

		this.param = TextUtil.GSON.fromJson(params, BreakUpParam.class);

		this.rt.getEventControler().registerHandler(IHeroBreakUp.class, this);
	}

	@Override
	public void onHeroBreakUp(IHero hero, int beforeBreakLevel) {
		// 确认在活动期间内
		if (!checkPeriod()) {
			return;
		}
		BreakUpParamItem item = getRewardItem(hero.getBreakLevel());
		if (item != null && !TextUtil.isBlank(item.rewardTC)) {
			// 生成奖励
			String attachments = XsgMailManager
					.getInstance()
					.serializeMailAttach(
							new AbsMailAttachObject[] { new TemplateAttachObject(
									item.rewardTC, 1) });
			String content = TextUtil.isBlank(item.content) ? param.mailContent
					: item.content;
			// 发送奖励
			XsgMailManager.getInstance().sendMail(
					new Mail(GlobalDataManager.getInstance()
							.generatePrimaryKey(), MailSource.System.ordinal(),
							"", "X三国运营团队", this.rt.getRoleId(),
							this.param.mailTitle, content, attachments,
							Calendar.getInstance().getTime()));
		}
	}

	private BreakUpParamItem getRewardItem(int lvl) {
		for (BreakUpParamItem item : param.items) {
			if (item.upLevel == lvl) {
				return item;
			}
		}
		return null;
	}

	public static class BreakUpParam {
		public BreakUpParamItem[] items;
		public String mailTitle;
		public String mailContent;
	}

	public static class BreakUpParamItem {
		public int upLevel;
		public String rewardTC;
		public String content;
	}
}
