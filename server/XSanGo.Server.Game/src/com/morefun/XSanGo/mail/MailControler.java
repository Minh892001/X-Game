/**
 * 
 */
package com.morefun.XSanGo.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.MailState;
import com.XSanGo.Protocol.MailType;
import com.XSanGo.Protocol.MailView;
import com.XSanGo.Protocol.MajorMenu;
import com.XSanGo.Protocol.MajorUIRedPointNote;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol.ServerMailParam;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleMail;
import com.morefun.XSanGo.db.game.RoleMailHistory;
import com.morefun.XSanGo.event.protocol.IMailReward;
import com.morefun.XSanGo.reward.XsgRewardManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.TextUtil;

/**
 * @author lvmingtao
 * 
 **/
class MailControler implements IMailControler {
	private IRole roleRt;
	private Role roleDb;

	private IMailReward eventReward; // 邮件奖励

	public MailControler(IRole roleRt, Role roleDB) {
		this.roleRt = roleRt;
		this.roleDb = roleDB;

		this.eventReward = this.roleRt.getEventControler().registerEvent(IMailReward.class);

		// 邮件历史记录模板数据
		this.initMailTempl();
	}

	/**
	 * 邮件列表
	 */
	@Override
	public List<MailView> selectMailViewList() {
		// 添加 模板邮件
		this.autoAddTemplMail();
		// 游戏中心内 发送邮件
		this.receiveMail();
		// 删除 到期的 公告 邮件
		this.deleteMail();

		List<MailView> resMailList = new ArrayList<MailView>();
		for (RoleMail mail : this.setToList(this.roleDb.getRoleMailMap().values())) {
			if (mail.getState() != MailState.delete.ordinal()) {
				resMailList.add(this.setMailView(mail));
			}
		}

		return resMailList;
	}

	/**
	 * 查询 单一邮件
	 * 
	 * @throws NoteException
	 */
	@Override
	public MailView selectMailView(String mailId) throws NoteException {
		RoleMail mail = this.roleDb.getRoleMailMap().get(mailId);
		if (mail == null) {
			throw new NoteException(Messages.getString("MailControler.0")); //$NON-NLS-1$
		}

		return this.setMailView(mail);
	}

	/**
	 * 标记 邮件状态
	 * 
	 * @param mailId
	 * @param state
	 *            0：已删除，1:未读，2：已读
	 * @throws NoteException
	 */
	@Override
	public void markMail(String mailId, int state) throws NoteException {
		RoleMail mail = this.roleDb.getRoleMailMap().get(mailId);
		if (mail == null) {
			throw new NoteException(Messages.getString("MailControler.1")); //$NON-NLS-1$
		}

		// 状态 设置
		mail.setState(state);

		// 提取附件 ,状态 设置:已读
		if (state == MailState.extract.ordinal()) {
			AbsMailAttachObject[] attaches = XsgMailManager.getInstance().deserializeMailAttach(mail.getAttachments());
			List<AbsMailAttachObject> remainList = new ArrayList<AbsMailAttachObject>(Arrays.asList(attaches));
			List<AbsMailAttachObject> successList = new ArrayList<AbsMailAttachObject>();
			try {
				for (AbsMailAttachObject attach : attaches) {
					this.extractAttach(attach);
					remainList.remove(attach);
					successList.add(attach);
				}
				mail.setState(MailState.read.ordinal());
			} finally {// 无论是否领取，领取多少，最后都要更新附件数据
				mail.setAttachments(XsgMailManager.getInstance().serializeMailAttach(
						remainList.toArray(new AbsMailAttachObject[0])));

				// 添加 邮件 标记事件
				this.eventReward.onReward(LuaSerializer.removeSpecialLuaString(mail.getTitle()), successList.toArray(new AbsMailAttachObject[0]));
			}

			// 系统邮件，直接删除
			if (mail.getType() == MailType.System.value()) {
				this.roleDb.getRoleMailMap().remove(mail.getId());
				return;
			}
		}
	}

	@Override
	public void receiveRoleMail(MailTemplate awardId, Map<String, Integer> rewardMap) {
		this.receiveRoleMail(awardId, rewardMap, null);
	}

	@Override
	public void receiveRoleMail(MailTemplate awardId, Map<String, Integer> rewardMap, Map<String, String> replaceMap) {

		// 邮件奖励模板
		MailRewardT rewardT = XsgMailManager.getInstance().getMailRewardTList().get(awardId.value());

		// 替换模板字符
		String mailContent = rewardT.body;
		if (replaceMap != null) {
			for (String key : replaceMap.keySet()) {
				mailContent = mailContent.replace(key, replaceMap.get(key));
			}
		}

		this.receiveRoleMail(rewardT.title, mailContent, rewardMap, MailType.valueOf(rewardT.type), rewardT.sendName,
				String.valueOf(rewardT.id));
	}

	/**
	 * 邮件中心的 发送操作
	 */
	@Override
	public void receiveMail() {
		// 放到自己的收件箱，且状态为未读
		List<Mail> mailList = XsgMailManager.getInstance().receiveMail(this.roleRt.getRoleId());
		for (Mail mail : mailList) {
			if (!this.getMailTempl().contains(mail.getId())) {
				this.receiveMail(mail);
			}
		}

		// 系统邮件，全服发放
		for (Mail mail : XsgMailManager.getInstance().getMailSystem().values()) {
			if (!this.getMailTempl().contains(mail.getId())) {
				if (checkMailCondition(mail.getParams())) {
					this.receiveMail(mail);
				}
			}
		}
	}

	/**
	 * 校验邮件接收条件
	 * 
	 * @param condition
	 * @return
	 */
	private boolean checkMailCondition(String condition) {
		if (TextUtil.isNotBlank(condition)) {
			ServerMailParam param = TextUtil.GSON.fromJson(condition, ServerMailParam.class);
			if (param == null) {
				return true;
			}

			if (param.serverId != 0) {// 来源服务器ID
				if (this.roleRt.getServerId() != param.serverId) {
					return false;
				}
			}
			if (param.minLevel != 0) {// 最小等级
				if (this.roleRt.getLevel() < param.minLevel) {
					return false;
				}
			}
			if (param.maxLevel != 0) {
				if (this.roleRt.getLevel() > param.maxLevel) {
					return false;
				}
			}
			if (param.maxRoleCreateTime != 0) {// 最晚创建角色时间
				if (this.roleRt.getCreateTime().getTime() > param.maxRoleCreateTime) {
					return false;
				}
			}
			if (param.lastLoginTime != 0) {// lastLoginTime之后登陆的才能收到
				if (this.roleRt.getLoginTime().getTime() < param.lastLoginTime) {
					return false;
				}
			}
			if (param.minVip != 0) {
				if (this.roleRt.getVipLevel() < param.minVip) {
					return false;
				}
			}
			if (param.maxVip != 0) {
				if (this.roleRt.getVipLevel() > param.maxVip) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 邮件中心 发送邮件
	 * 
	 * @param mail
	 */
	private void receiveMail(Mail mail) {
		// 记录邮件历史记录 模板ID
		this.setMailTempl(String.valueOf(mail.getId()));
		// 具体发送邮件
		AbsMailAttachObject[] attaches = XsgMailManager.getInstance().deserializeMailAttach(mail.getAttachments());
		Map<String, Integer> rewardMap = new HashMap<String, Integer>();
		for (AbsMailAttachObject item : attaches) {
			rewardMap.put(item.getTemplateId(), item.getNum());
		}
		this.receiveRoleMail(mail.getTitle(), mail.getBody(), rewardMap, MailType.System, mail.getSenderName(),
				mail.getId());
	}

	/** 初始化 邮件历史记录模板数据 */
	private RoleMailHistory initMailTempl() {
		// 邮件模板 历史记录
		RoleMailHistory mailHistory = this.roleDb.getMailHistory();
		if (mailHistory == null) {
			mailHistory = new RoleMailHistory(GlobalDataManager.getInstance().generatePrimaryKey(), this.roleDb, ""); //$NON-NLS-1$

			this.roleDb.setMailHistory(mailHistory);
		}

		return mailHistory;
	}

	// 查询 邮件历史记录模板数据
	private List<String> getMailTempl() {
		return TextUtil.stringToList(this.initMailTempl().getTempleId());
	}

	/** 保存邮件模板数据 */
	private void setMailTempl(String templId) {
		RoleMailHistory mailHistory = this.initMailTempl();

		List<String> templeIdList = TextUtil.stringToList(this.initMailTempl().getTempleId());
		/*
		 * 20150402 edit by Bruce
		 * 从吕明涛手中接手，改为只记录原来没记的，另外竞技场首胜，每日定时发奖等记录下来无意义，新版本不再记录
		 */
		// TODO S理论上还应该删除全局或模板中不存在的编号，但由于无法区分ID是来源邮局中心还是模板配置，因此暂时无法处理
		if (!templeIdList.contains(templId)) {
			templeIdList.add(templId);
		}
		mailHistory.setTempleId(TextUtil.join(templeIdList, ",")); //$NON-NLS-1$

		this.roleDb.setMailHistory(mailHistory);
	}

	/**
	 * 对角色用户邮件 排序
	 * 
	 * @param collection
	 * @return
	 */
	private List<RoleMail> setToList(Collection<RoleMail> collection) {
		List<RoleMail> mailList = new ArrayList<RoleMail>();
		for (RoleMail mail : collection) {
			if (mail.getState() != MailState.delete.ordinal()) {
				mailList.add(mail);
			}
		}

		// 邮件排序
		this.mailSort(mailList);

		return mailList;
	}

	/**
	 * 邮件列表排序，按照邮件ID降序排列
	 */
	private void mailSort(List<RoleMail> mailList) {
		Comparator<RoleMail> comparator = new Comparator<RoleMail>() {
			public int compare(RoleMail s1, RoleMail s2) {
				int time = s2.getCreateTime().compareTo(s1.getCreateTime());
				if (time == 0) {
					return s2.getId().compareTo(s1.getId());
				} else {
					return time;
				}
			}
		};

		Collections.sort(mailList, comparator);
	}

	// 填充返回客户端的邮件数据
	private MailView setMailView(RoleMail mail) {
		MailView mailView = new MailView();
		mailView.id = mail.getId();
		mailView.type = mail.getType();
		mailView.senderId = mail.getSenderId();
		mailView.senderName = mail.getSenderName();
		mailView.title = LuaSerializer.removeSpecialLuaString(mail.getTitle());
		mailView.body = LuaSerializer.removeSpecialLuaString(mail.getBody());
		mailView.state = mail.getState();
		AbsMailAttachObject[] attaches = XsgMailManager.getInstance().deserializeMailAttach(mail.getAttachments());
		List<Property> list = new ArrayList<Property>();
		for (AbsMailAttachObject ato : attaches) {
			list.add(new Property(ato.getTemplateId(), ato.getNum()));
		}
		mailView.attach = list.toArray(new Property[0]);
		mailView.createTime = DateUtil.toString(mail.getCreateTime().getTime(), "yyyy-MM-dd"); //$NON-NLS-1$

		return mailView;
	}

	/** 提取附件 */
	private void extractAttach(AbsMailAttachObject attach) {
		if (attach == null) {
			return;
		}

		if (attach instanceof InstanceAttachObject) { // 获取实例物品
			this.roleRt.getItemControler().addItemFromDb(((InstanceAttachObject) attach).getInstanceData());
		} else {
			this.roleRt.getRewardControler().acceptReward(attach.getTemplateId(), attach.getNum());
		}
	}

	// 根据配置文件，自动添加邮件
	private void autoAddTemplMail() {
		for (StaticMailT mailT : XsgMailManager.getInstance().getMailTList()) {
			// 自动添加邮件
			if (mailT.sendObj.equals("0") //$NON-NLS-1$
					&& !this.getMailTempl().contains(String.valueOf(mailT.id))) {
				// 有效日期内，等级和VIP等级、渠道符合条件下，发送邮件
				List<String> channls = TextUtil.stringToList(mailT.channels);
				if (DateUtil.isBetween(DateUtil.parseDate(mailT.starDate), DateUtil.parseDate(mailT.endDate))
						&& roleRt.getLevel() >= mailT.level && roleRt.getVipLevel() >= mailT.vipLevel
						&& (channls.isEmpty() || channls.contains(String.valueOf(roleDb.getRegChannel())))) {
					// 附件
					Property[] attachArr = null;
					if (!mailT.attachStr.equals("")) { //$NON-NLS-1$
						// 附件TC中的随机数据
						ItemView[] ItemViewArr = XsgRewardManager.getInstance()
								.doTcToItem(this.roleRt, mailT.attachStr);

						attachArr = new Property[ItemViewArr.length];
						for (int i = 0; i < ItemViewArr.length; i++) {
							Property pro = new Property();
							pro.code = ItemViewArr[i].templateId;
							pro.value = ItemViewArr[i].num;
							attachArr[i] = pro;
						}
					}

					RoleMail addMail = new RoleMail(GlobalDataManager.getInstance().generatePrimaryKey(), this.roleDb,
							mailT.type, MailState.unRead.ordinal(), "", //$NON-NLS-1$
							mailT.sendName, "", mailT.title, mailT.body, //$NON-NLS-1$
							XsgMailManager.getInstance().serializeMailAttach(attachArr), DateUtil.parseDate(
									"yyyy-MM-dd", mailT.sendDate), //$NON-NLS-1$
							String.valueOf(mailT.id));
					// 发送邮件
					this.roleDb.getRoleMailMap().put(addMail.getId(), addMail);
					// 记录邮件历史记录 模板ID
					this.setMailTempl(String.valueOf(mailT.id));
				}
			}
		}
	}

	/**
	 * 删除 到期的 公告 邮件
	 */
	public void deleteMail() {
		Date passDate = DateUtil.addDays(DateUtil.getFirstSecondOfToday(), -XsgMailManager.ExpireDay).getTime();

		// 查询需要删除的模板ID
		List<String> deleteIdList = new ArrayList<String>();
		for (StaticMailT mailT : XsgMailManager.getInstance().getMailTList()) {
			if (mailT.type == MailType.Announce.ordinal()) {
				if (DateUtil.parseDate(mailT.deleteDate).before(new Date())) {
					deleteIdList.add(String.valueOf(mailT.id));
				}
			}
		}

		Iterator<Entry<String, RoleMail>> it = this.roleDb.getRoleMailMap().entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, RoleMail> entry = it.next();

			// 对比 角色邮件的模板ID数据,需要清理的
			// 日期到了，需要清理的
			if (deleteIdList.contains(entry.getValue().getTemplId())) {
				it.remove();
			} else if (entry.getValue().getCreateTime().before(passDate)) {
				it.remove();
			}
		}
	}

	/**
	 * 客户端红点显示 邮件中有附件的，无论已读未读，显示红点
	 */
	@Override
	public MajorUIRedPointNote getRedPointNote() {
		boolean note = false;
		List<MailView> mailViewList = this.selectMailViewList();
		for (MailView mail : mailViewList) {
			// 邮件未读，或者有附件未提取，显示红点
			if (mail.type == MailState.unRead.ordinal() || mail.attach != null) {
				note = true;
				break;
			}
		}
		return note ? new MajorUIRedPointNote(MajorMenu.MailMenu, false) : null;
	}

	@Override
	public void receiveRoleMail(String title, String content, Map<String, Integer> rewardMap, MailType type,
			String senderName, String templateId) {
		RoleMail addMail = new RoleMail(GlobalDataManager.getInstance().generatePrimaryKey(), this.roleDb,
				type.ordinal(), MailState.unRead.ordinal(), "", senderName, "", //$NON-NLS-1$ //$NON-NLS-2$
				title, content, XsgMailManager.getInstance().serializeMailAttach(rewardMap), new Date(), templateId);
		// 发送邮件
		this.roleDb.getRoleMailMap().put(addMail.getId(), addMail);
		this.notifyRedPoint();
	}

	@Override
	public void notifyRedPoint() {
		this.roleRt.getNotifyControler().onMajorUIRedPointChange(new MajorUIRedPointNote(MajorMenu.MailMenu, false));
	}
}
