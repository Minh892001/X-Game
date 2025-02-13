/**
 * 
 */
package com.morefun.XSanGo.temp;

import java.util.Calendar;
import java.util.Date;

import com.XSanGo.Protocol.CustomChargeParams;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.common.MailSource;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.event.protocol.ICharge;
import com.morefun.XSanGo.mail.AbsMailAttachObject;
import com.morefun.XSanGo.mail.TemplateAttachObject;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.TextUtil;

/**
 * @author sulingyun
 * 
 */
public class ChargeWrapper extends AbsTemporaryOperationWrapper implements ICharge {
	private ChargeWrapperParams paramObj;

	public ChargeWrapper(IRole rt, String params, Date begin, Date end) {
		super(rt, params, begin, end);
		this.paramObj = TextUtil.GSON.fromJson(params, ChargeWrapperParams.class);
		this.rt.getEventControler().registerHandler(ICharge.class, this);
	}

	@Override
	public void onCharge(CustomChargeParams chargeParams, int returnYuanbao, String orderId, String currency) {
		if (!this.checkPeriod()) {
			return;
		}
		String rewardItem = "";
		for (ChargeReward config : this.paramObj.rewardConfig) {
			if (config.chargeId == chargeParams.item) {
				rewardItem = config.rewardItem;
				break;
			}
		}
		if (TextUtil.isBlank(rewardItem)) {// 无匹配项，不做任何处理
			return;
		}

		String attachments = XsgMailManager.getInstance().serializeMailAttach(
				new AbsMailAttachObject[] { new TemplateAttachObject(rewardItem, 1) });
		XsgMailManager.getInstance().sendMail(
				new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), MailSource.System.ordinal(), "",
						"X三国运营团队", this.rt.getRoleId(), this.paramObj.mailTitle, this.paramObj.mailContent,
						attachments, Calendar.getInstance().getTime()));
	}
}

class ChargeWrapperParams {
	public ChargeReward[] rewardConfig;
	public String mailTitle;
	public String mailContent;
}

class ChargeReward {
	public int chargeId;
	public String rewardItem;
}
