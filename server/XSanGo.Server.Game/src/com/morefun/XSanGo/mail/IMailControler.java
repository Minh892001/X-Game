/**
 * 
 */
package com.morefun.XSanGo.mail;

import java.util.List;
import java.util.Map;

import com.XSanGo.Protocol.MailType;
import com.XSanGo.Protocol.MailView;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.role.IRedPointNotable;

/**
 * 玩家邮件控制器接口
 * 
 * @author BruceSu
 * 
 */
public interface IMailControler extends IRedPointNotable {

	/**
	 * 从邮件中心接收邮件并根据是否有未读邮件发送通知
	 */
	void receiveMail();

	/**
	 * 查询 邮件列表
	 * 
	 * @return
	 */
	List<MailView> selectMailViewList();

	/**
	 * 查询 单一邮件
	 * 
	 * @return
	 * @throws NoteException
	 */
	MailView selectMailView(String mailId) throws NoteException;

	/**
	 * 标记 邮件状态
	 * 
	 * @param mailId
	 * @param state
	 *            0：已删除，1:未读，2：已读
	 * @throws NoteException
	 */
	void markMail(String mailId, int state) throws NoteException;

	/**
	 * 角色发送 模板奖励邮件
	 * 
	 * @param awardId
	 * @param awardStr
	 */
	void receiveRoleMail(MailTemplate awardId, Map<String, Integer> rewardMap);

	/**
	 * 角色发送 模板奖励邮件
	 * 
	 * @param awardId
	 *            模板Id
	 * @param awardStr
	 *            奖励附件，奖励的内容，发放附件中，格式：模板ID：数量，模板ID：数量，……
	 * @param replaceMap
	 *            模板替换字符
	 */
	void receiveRoleMail(MailTemplate awardId, Map<String, Integer> rewardMap, Map<String, String> replaceMap);

	/**
	 * 接收邮件
	 * 
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 * @param rewardMap
	 *            附件
	 * @param type
	 * @param senderName
	 * @param templateId
	 */
	void receiveRoleMail(String title, String content, Map<String, Integer> rewardMap, MailType type,
			String senderName, String templateId);

	/**
	 * 推送红点，通知领邮件
	 */
	void notifyRedPoint();
}
