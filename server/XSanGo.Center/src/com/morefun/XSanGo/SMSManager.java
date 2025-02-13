/**
 * 
 */
package com.morefun.XSanGo;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;

import com.XSanGo.Protocol.AlarmType;
import com.morefun.XSanGo.http.HttpUtil;
import com.morefun.XSanGo.logicserver.AlarmSMSConfig;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 短信管理
 * 
 * @author linyun.su
 * 
 */
public class SMSManager {
	private static Logger log = LogManager.getLogger(SMSManager.class);
	private static SMSManager instance = new SMSManager();
	private static Map<AlarmType, Date> smsUpdateMap = new ConcurrentHashMap<AlarmType, Date>();

	public static SMSManager getInstance() {
		return instance;
	}

	private SMSManager() {
	}

	public void sendAlarmSMS(final AlarmType type, final String smsText) {
		log.error(TextUtil.format("[{0}],{1}", type, smsText));
		final AlarmSMSConfig config = LoginDatabase.instance().getAc().getBean("AlarmSMSConfig", AlarmSMSConfig.class);
		if (config.isEnable() && filterSMS(type, config.getHours())) {
			LoginDatabase.execute(new Runnable() {

				@Override
				public void run() {
					try {
						String url = TextUtil.format("{0}?desNum={1}&message={2}", config.getUrl(),
								config.getMobileNumber(), smsText);
//						TODO 短信服务器暂不搭建，临时注释
//						HttpUtil.doPost(url, "").trim();
//						smsUpdateMap.put(type, new Date());
					} catch (Exception e) {
						log.error("SMS channel error!!!");
					}
				}
			});
		}
	}

	/**
	 * 短信过滤策略，允许发送返回true，否则为false
	 * 
	 * @param type
	 *            警告类型
	 * @param hours
	 *            间隔时间标准
	 * @return
	 */
	private boolean filterSMS(AlarmType type, int hours) {
		Date lastTime = smsUpdateMap.get(type);
		Date allowTime = DateUtil.addHours(Calendar.getInstance(), -hours).getTime();
		return !(lastTime != null && lastTime.after(allowTime));
	}

//	public static void main(String[] args) throws IOException {
//		String url = TextUtil.format("{0}?desNum={1}&message={2}", "http://101.251.206.230:34043/putsms/sms",
//				"18516059801", "hi");
//		HttpUtil.doPost(url, "");
//	}
}
