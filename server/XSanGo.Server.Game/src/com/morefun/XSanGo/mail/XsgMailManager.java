/**
 * 
 */
package com.morefun.XSanGo.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.codec.Base64;

import com.XSanGo.Protocol.Property;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.cache.XsgCacheManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.db.game.MailDAO;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.TextUtil;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Query;

/**
 * 邮件全局管理类
 * 
 * @author BruceSu
 * 
 */
public class XsgMailManager {

	private static final Log log = LogFactory.getLog(XsgMailManager.class);

	private static XsgMailManager instance = new XsgMailManager();

	// 邮件的脚本数据
	private List<StaticMailT> mailTList = new ArrayList<StaticMailT>();

	/** 指定邮件格式的邮件 */
	private Map<Integer, MailRewardT> mailRewardTList = new HashMap<Integer, MailRewardT>();

	private Ehcache mailCache; // 指定发送的邮件

	private Map<String, Mail> mailSystem = new HashMap<String, Mail>(); // 系统全服邮件

	/** 邮件过期时限，单位：天 */
	public static int ExpireDay = 7;

	public static XsgMailManager getInstance() {
		return instance;
	}

	/**
	 * 创建玩家邮件管理模块
	 * 
	 * @param roleRt
	 * @param roleDB
	 * @return
	 */
	public IMailControler createMailControler(IRole roleRt, Role roleDB) {
		return new MailControler(roleRt, roleDB);
	}

	/**
	 * 构造函数
	 */
	private XsgMailManager() {
		// 初始化 邮件中心的缓存数据
		this.initMainCache();
		this.loadMailScript();

		// 每天检查清除数据
		LogicThread.scheduleTask(new DelayedTask(0, TimeUnit.DAYS.toMillis(1)) {
			@Override
			public void run() {
				passMailProcess();
			}
		});
	}

	/**
	 * 加载邮件配置数据
	 */
	public void loadMailScript() {
		// 邮件模板
		this.mailTList.clear();
		for (StaticMailT mail : ExcelParser.parse(StaticMailT.class)) {
			if (mail.id != 0) {
				this.mailTList.add(mail);
			} else {
				break;
			}
		}
		// 邮件 发奖励 模板
		this.mailRewardTList.clear();
		for (MailRewardT mailReward : ExcelParser.parse(MailRewardT.class)) {
			mailRewardTList.put(mailReward.id, mailReward);
		}
	}

	/**
	 * 初始化 邮件中心的缓存数据
	 */
	private void initMainCache() {
		SimpleDAO dao = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
		List<Mail> mailList = dao.findAll(Mail.class);

		this.mailCache = XsgCacheManager.getInstance().getCache(Const.Mail.CACHE_NAME_MAIL);
		for (Mail mail : mailList) {
			// 判断是否 全服 发送邮件
			if (!mail.getAcceptorId().equals(Const.Mail.CACHE_All_MAIL)) {
				this.mailCache.put(new Element(mail.getId(), mail));
			} else {
				mailSystem.put(mail.getId(), mail);
			}
		}
	}

	/**
	 * 投递邮件，玩家间发送邮件需经过邮件管理中心预处理
	 * 
	 * @param mail
	 */
	public void sendMail(final Mail mail) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				// 持久化邮件
				SimpleDAO dao = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
				dao.save(mail);
			}
		}, new Runnable() {
			@Override
			public void run() {
				// 判断是否 全服 发送邮件
				if (!mail.getAcceptorId().equals(Const.Mail.CACHE_All_MAIL)) {
					mailCache.put(new Element(mail.getId(), mail));
				} else {
					mailSystem.put(mail.getId(), mail);
				}
				IRole target = XsgRoleManager.getInstance().findRoleById(mail.getAcceptorId());
				if (target != null && target.isOnline()) {
					target.getMailControler().notifyRedPoint();
				}
			}
		});
	}

	/**
	 * 批量投递邮件，玩家间发送邮件需经过邮件管理中心预处理
	 * 
	 * @param mail
	 */
	public void sendMail(final List<Mail> mails) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				// 持久化邮件
				SimpleDAO dao = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
				dao.batchSave(mails);
			}
		}, new Runnable() {
			@Override
			public void run() {
				for (Mail mail : mails) {
					// 判断是否 全服 发送邮件
					if (!mail.getAcceptorId().equals(Const.Mail.CACHE_All_MAIL)) {
						mailCache.put(new Element(mail.getId(), mail));
					} else {
						mailSystem.put(mail.getId(), mail);
					}
					IRole target = XsgRoleManager.getInstance().findRoleById(mail.getAcceptorId());
					if (target != null && target.isOnline()) {
						target.getMailControler().notifyRedPoint();
					}
				}
			}
		});
	}

	/**
	 * 玩家从邮件中心收取新邮件，方法调用成功后，收取的邮件将无法通过再次调用获取
	 * 
	 * @param roleId
	 * @return
	 */
	public List<Mail> receiveMail(String roleId) {
		Query query = this.mailCache.createQuery().includeValues().addCriteria(this.mailCache.getSearchAttribute(Const.Mail.CACHE_INDEX_ACCEPTOR_ID).eq(roleId));
		List<Mail> list = XsgCacheManager.parseCacheValue(query.execute().all(), Mail.class);
		for (Mail mail : list) {
			this.removeMail(mail);
		}
		return list;
	}

	/**
	 * 删除邮件
	 * 
	 * @param mail
	 */
	protected void removeMail(final Mail mail) {
		// 判断是否 全服 发送邮件 缓存删除
		if (!mail.getAcceptorId().equals(Const.Mail.CACHE_All_MAIL)) {
			this.mailCache.remove(mail.getId());
		} else {
			mailSystem.remove(mail.getId());
		}

		// DB删除
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				// 删除中心邮件
				SimpleDAO dao = SimpleDAO.getFromApplicationContext(ServerLancher.getAc());
				dao.delete(mail);
			}
		});
	}

	/**
	 * 发送全服 奖励的邮件
	 * 
	 * @param awardId 奖励邮件模板ID
	 * @param awardStr 奖励的内容，发放附件中，格式：模板ID：数量，模板ID：数量，……
	 */
	public void sendAwardAll(MailTemplate awardId, Map<String, Integer> rewardMap) {
		this.sendTemplate(Const.Mail.CACHE_All_MAIL, awardId, rewardMap);
	}

	/**
	 * 发送模板的邮件
	 * 
	 * @param acceptId 收件人
	 * @param templateId 邮件模板ID
	 * @param rewardMap 邮件奖励
	 */
	public void sendTemplate(String acceptId, MailTemplate templateId, Map<String, Integer> rewardMap) {
		this.sendTemplate(acceptId, templateId, rewardMap, null);
	}

	/**
	 * 发送模板的邮件
	 * 
	 * @param acceptId 收件人
	 * @param templateId 邮件模板ID
	 * @param rewardMap 邮件奖励
	 * @param replaceMap 邮件参数
	 */
	public void sendTemplate(String acceptId, MailTemplate templateId, Map<String, Integer> rewardMap, Map<String, String> replaceMap) {
		Mail mail = getMailByTemplate(acceptId, templateId, rewardMap, replaceMap);
		if (mail != null) {
			this.sendMail(mail);
		}
	}

	/**
	 * 获得模版邮件对象
	 * 
	 * @param acceptId 收件人
	 * @param templateId 邮件模板ID
	 * @param rewardMap 邮件奖励
	 * @param replaceMap 邮件参数
	 * @return
	 */
	public Mail getMailByTemplate(String acceptId, MailTemplate templateId, Map<String, Integer> rewardMap, Map<String, String> replaceMap) {
		MailRewardT rewardT = this.mailRewardTList.get(templateId.value());
		if (rewardT != null) {
			// 替换模板字符
			String mailContent = rewardT.body;
			if (replaceMap != null) {
				for (String key : replaceMap.keySet()) {
					mailContent = mailContent.replace(key, replaceMap.get(key));
				}
			}
			Mail mail = new Mail(GlobalDataManager.getInstance().generatePrimaryKey(), 0, "", rewardT.sendName, acceptId, rewardT.title, mailContent, this.serializeMailAttach(rewardMap), new Date());
			return mail;
		} else {
			log.error("邮件模板id:" + templateId + ",不存在");
		}
		return null;
	}

	/**
	 * 序列化邮件附件
	 * 
	 * @param attach
	 * @return
	 */
	public String serializeMailAttach(Property[] attaches) {
		if (attaches == null || attaches.length == 0) {
			return "";
		}
		// 合并重复物品
		Map<String, Property> map = new HashMap<String, Property>();
		for (Property p : attaches) {
			Property t = map.get(p.code);
			if (t != null) {
				t.value += p.value;
			} else {
				map.put(p.code, new Property(p.code, p.value));
			}
		}
		List<AbsMailAttachObject> list = new ArrayList<AbsMailAttachObject>();
		for (Property p : map.values()) {
			list.add(new TemplateAttachObject(p.code, p.value));
		}
		return this.serializeMailAttach(list.toArray(new AbsMailAttachObject[0]));
	}

	/**
	 * 序列化邮件附件
	 * 
	 * @param rewardMap 可堆叠物品的KEY和数量集合
	 * @return
	 */
	public String serializeMailAttach(Map<String, Integer> rewardMap) {
		if (rewardMap == null || rewardMap.size() == 0) {
			return "";
		}

		List<AbsMailAttachObject> list = new ArrayList<AbsMailAttachObject>();
		for (String rewardKey : rewardMap.keySet()) {
			list.add(new TemplateAttachObject(rewardKey, rewardMap.get(rewardKey)));
		}
		return this.serializeMailAttach(list.toArray(new AbsMailAttachObject[0]));
	}

	/**
	 * 序列化邮件附件
	 * 
	 * @param attaches 附件列表
	 * @return
	 */
	public String serializeMailAttach(AbsMailAttachObject[] attaches) {
		if (attaches == null || attaches.length == 0) {
			return "";
		}
		try {
			// 先复制对象，然后进行base64编码，最后进行gzip压缩
			return TextUtil.gzip(new String(Base64.encode(TextUtil.objectToBytes(attaches))));
		} catch (IOException e) {
			log.error("附件序列化失败", e);
			return "";
		}
	}

	/**
	 * 反序列化邮件附件
	 * 
	 * @param attachStr
	 * @return
	 */
	public AbsMailAttachObject[] deserializeMailAttach(String attachStr) {
		if (TextUtil.isBlank(attachStr)) {
			return new AbsMailAttachObject[0];
		}

		try {
			return (AbsMailAttachObject[]) TextUtil.bytesToObject(Base64.decode(TextUtil.ungzip(attachStr).getBytes()));
		} catch (IOException e) {
			log.error("附件反序列化失败", e);
			return new AbsMailAttachObject[0];
		}
	}

	/**
	 * 过期邮件清理
	 */
	private void passMailProcess() {
		// 过期邮件删除
		final Date passDate = DateUtil.addDays(DateUtil.getFirstSecondOfToday(), -ExpireDay).getTime();

		// 删除邮件中心
		@SuppressWarnings("unchecked")
		List<String> keyList = this.mailCache.getKeysWithExpiryCheck();
		for (String key : keyList) {
			Mail mail = (Mail) this.mailCache.get(key).getObjectValue();
			if (mail.getCreateTime().before(passDate)) {
				this.mailCache.remove(mail.getId());
			}
		}

		// 删除邮件中心 全服数据
		Iterator<Entry<String, Mail>> it = mailSystem.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Mail> entry = it.next();
			if (entry.getValue().getCreateTime().before(passDate)) {
				it.remove();
			}
		}

		// DB删除
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				// 删除中心邮件
				MailDAO dao = MailDAO.getFromApplicationContext(ServerLancher.getAc());
				dao.deletePass(DateUtil.toString(passDate.getTime()));
			}
		});
	}

	/**
	 * 获取邮件数量描述
	 * 
	 * @return
	 */
	public String getMailSizeDesc() {
		return TextUtil.format("{0}-{1}", this.mailCache.getSize(), this.mailSystem.size());
	}

	public List<StaticMailT> getMailTList() {
		return mailTList;
	}

	public Map<Integer, MailRewardT> getMailRewardTList() {
		return mailRewardTList;
	}

	public Map<String, Mail> getMailSystem() {
		return mailSystem;
	}
}
