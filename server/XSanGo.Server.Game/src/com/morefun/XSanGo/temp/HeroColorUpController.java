package com.morefun.XSanGo.temp;

import java.util.Calendar;
import java.util.Date;

import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.common.MailSource;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.event.protocol.IHeroQualityUp;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.mail.AbsMailAttachObject;
import com.morefun.XSanGo.mail.TemplateAttachObject;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.TextUtil;

/**
 * @author guofeng.qin
 */
public class HeroColorUpController extends AbsTemporaryOperationWrapper implements IHeroColorUpController,
		IHeroQualityUp {

	private ColorUpParam param;

	public HeroColorUpController(IRole rt, String params, Date begin, Date end) {
		super(rt, params, begin, end);
		this.param = TextUtil.GSON.fromJson(params, ColorUpParam.class);

		this.rt.getEventControler().registerHandler(IHeroQualityUp.class, this);
	}

	@Override
	public void onHeroQualityUp(IHero hero, int beforeQualityLeve) {
		// 确认在活动期间内
		if (!checkPeriod()) {
			return;
		}
		ColorUpParamItem item = getRewardItem(hero.getQualityLevel());
		if (item != null && !TextUtil.isBlank(item.rewardTC)) {
			// 生成奖励
			String attachments = XsgMailManager.getInstance().serializeMailAttach(
					new AbsMailAttachObject[] { new TemplateAttachObject(item.rewardTC, 1) });
			String content = TextUtil.isBlank(item.content) ? param.mailContent : item.content;
			// 发送奖励
			XsgMailManager.getInstance().sendMail(
					new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), MailSource.System.ordinal(), "",
							"X三国运营团队", this.rt.getRoleId(), this.param.mailTitle, content, attachments, Calendar
									.getInstance().getTime()));
		}
	}

	private ColorUpParamItem getRewardItem(int lvl) {
		for (ColorUpParamItem item : param.items) {
			if (item.upLevel == lvl) {
				return item;
			}
		}
		return null;
	}

	public static class ColorUpParam {
		public ColorUpParamItem[] items;
		public String mailTitle;
		public String mailContent;
	}

	public static class ColorUpParamItem {
		public int upLevel;
		public String rewardTC;
		public String content;
	}
}
