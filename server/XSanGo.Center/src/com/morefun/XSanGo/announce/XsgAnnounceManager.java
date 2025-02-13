/**
 * 
 */
package com.morefun.XSanGo.announce;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

import com.XSanGo.Protocol.AnnounceView;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;

/**
 * 公告管理类
 * 
 * @author sulingyun
 *
 */
public class XsgAnnounceManager {

	private final static Log log = LogFactory.getLog(XsgAnnounceManager.class);
	private static XsgAnnounceManager instance = new XsgAnnounceManager();

	public static XsgAnnounceManager getInstance() {
		return instance;
	}

	private ConcurrentLinkedQueue<AnnounceT> announceList = new ConcurrentLinkedQueue<AnnounceT>();
	private long refreshInterval = TimeUnit.MINUTES.toMillis(1);

	private XsgAnnounceManager() {
		this.loadScriptFile();
		// CenterServer.scheduleTask(new DelayedTask(refreshInterval,
		// refreshInterval) {
		// @Override
		// public void run() {
		// loadScriptFile();
		// }
		// });
	}

	private void loadScriptFile() {
		List<AnnounceT> all = ExcelParser.parse(AnnounceT.class);
		CollectionUtils.filter(all, new Predicate() {
			@Override
			public boolean evaluate(Object arg0) {
				return ((AnnounceT) arg0).open == 1;
			}
		});

		this.announceList.clear();
		this.announceList.addAll(all);
	}

	/**
	 * 获取当前需要显示的公告信息
	 * 
	 * @return
	 */
	public AnnounceView[] getAnnounces() {
		List<AnnounceView> list = new ArrayList<AnnounceView>();
		Date now = Calendar.getInstance().getTime();
		for (AnnounceT template : this.announceList) {
			if (DateUtil.isBetween(now, template.beginTime, template.endTime)) {
				list.add(new AnnounceView(template.title, template.content));
			}
		}

		return list.toArray(new AnnounceView[0]);
	}

	public static void main(String[] args) {
		// List<Integer> list = new ArrayList<Integer>();
		// list.add(1);
		// list.add(2);
		// list.add(2);
		// list.add(3);
		// CollectionUtils.filter(list, new Predicate() {
		//
		// @Override
		// public boolean evaluate(Object arg0) {
		// return arg0.equals(2);
		// }
		// });
		// System.out.println();

	}

	public void uploadLoginAnnounce(byte[] file) throws FileNotFoundException,
			IOException {
		ClassPathResource cpr = new ClassPathResource("登录界面公告.xls");
		FileOutputStream fos = new FileOutputStream(cpr.getFile());
		log.info("Writing announce data...");
		fos.write(file);
		fos.close();
		log.info("Announce data closed.");
		this.loadScriptFile();
	}
}
