/**
 * 
 */
package com.morefun.XSanGo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.db.game.CombineFactionDAO;
import com.morefun.XSanGo.db.game.CombineRoleDAO;
import com.morefun.XSanGo.db.game.CombineSimpleDAO;
import com.morefun.XSanGo.db.game.Faction;
import com.morefun.XSanGo.db.game.FactionDAO;
import com.morefun.XSanGo.db.game.Mail;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleDAO;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 合服工具
 * 
 * @author linyun.su
 * 
 */
public class ServerCombiner {
	private static String appProperties = "app.properties";
	private static ApplicationContext ac;
	private final static Log logger = LogFactory.getLog(ServerCombiner.class);
	private static int CombineServerId;
	private static String iceConfigPath;
	private static int MajorServerId;

	public static void main(String[] args) {
		initIceConfigFilePath(args);
		try {
			parseServerId();
		} catch (Exception e) {
			logger.error("", e);
			System.exit(1);
		}

		ac = new ClassPathXmlApplicationContext("applicationContextForCombine.xml");
		CombineServerId = ac.getBean("CombineServerId", Integer.class);

		final CountDownLatch cdl = new CountDownLatch(4);
		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				logger.info("正在转移邮件中心数据。。。");
				ServerCombiner.combineMailData();
				cdl.countDown();
				logger.info("邮件数据合并完成。");
			}
		});

		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				logger.info("正在转移角色数据。。。");
				ServerCombiner.combineRoleData();
				cdl.countDown();
				logger.info("角色数据合并完成。");
			}
		});

		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				logger.info("正在清理机器人数据。。。");
				ServerCombiner.clearRobotData();
				cdl.countDown();
				logger.info("机器人清理完成。");
			}
		});

		DBThreads.execute(new Runnable() {

			@Override
			public void run() {
				logger.info("正在转移公会数据。。。");
				ServerCombiner.combineFactionData();
				cdl.countDown();
				logger.info("公会数据合并完成。");
			}
		});

		int returnCode = 0;
		try {
			cdl.await();
			logger.info("SUCCESS.");
		} catch (InterruptedException e) {
			logger.error("Unknow Error!", e);
			returnCode = 1;
		}

		System.exit(returnCode);
	}

	/**
	 * 初始化ICE配置文件路径
	 * 
	 * @param args
	 */
	private static void initIceConfigFilePath(String[] args) {
		iceConfigPath = appProperties;
		for (String arg : args) {
			if (arg.startsWith("--Ice.Config=")) {
				iceConfigPath = arg.substring(arg.lastIndexOf("=") + 1, arg.length());
			}
		}
	}

	private static void parseServerId() throws FileNotFoundException, IOException {
		Properties sysConfig = new Properties();
		InputStream iStream = new FileInputStream(iceConfigPath);
		sysConfig.load(iStream);
		MajorServerId = Integer.parseInt(sysConfig.getProperty("Ice.Admin.ServerId"));
	}

	/**
	 * 合并公会数据，重名只改被合并服务器的公会名
	 */
	private static void combineFactionData() {
		FactionDAO dao = FactionDAO.getFromApplicationContext(ac);
		List<String> nameList = dao.findAllName();
		CombineFactionDAO subDao = CombineFactionDAO.getFromApplicationContext(ac);
		List subList = subDao.findAll();
		for (Object obj : subList) {
			Faction subFaction = (Faction) obj;
			String name = subFaction.getName();

			try {
				if (ServerCombiner.CollectContainsIgnoreCase(nameList, name)) {// 记录重复名字并直接更改被
					subFaction.setName(generateNameForDuplicate(CombineServerId, name));
				}
				dao.customMerge(subFaction);
			} catch (Exception e) {
				logger.error(name, e);
				System.exit(1);
			}
		}
	}

	/**
	 * 清理机器人数据
	 */
	private static void clearRobotData() {
		final RoleDAO readDao = RoleDAO.getFromApplicationContext(ac);
		final SimpleDAO deleteDao = SimpleDAO.getFromApplicationContext(ac);
		List<String> robotList = readDao.findAllRobot();
		final CountDownLatch cdl = new CountDownLatch(robotList.size());
		for (final String id : robotList) {
			DBThreads.execute(new Runnable() {

				@Override
				public void run() {
					deleteDao.delete(readDao.findById(id));
					cdl.countDown();
				}
			});
		}

		try {
			cdl.await();
		} catch (InterruptedException e) {
			logger.error("Unknow Error!", e);
			System.exit(1);
		}
	}

	/**
	 * 合并全局邮件
	 * 
	 * @throws InterruptedException
	 */
	private static void combineMailData() {
		final SimpleDAO dao = SimpleDAO.getFromApplicationContext(ac);
		CombineSimpleDAO subDao = CombineSimpleDAO.getFromApplicationContext(ac);
		List<Mail> mailList = subDao.findAll(Mail.class);
		final CountDownLatch cdl = new CountDownLatch(mailList.size());
		for (final Mail mail : mailList) {
			DBThreads.execute(new Runnable() {

				@Override
				public void run() {
					dao.save(mail);
					cdl.countDown();
				}
			});
		}

		try {
			cdl.await();
		} catch (InterruptedException e) {
			logger.error("Unknow Error!", e);
			System.exit(1);
		}
	}

	/**
	 * 合并角色数据，重名只改被合并服务器的角色名
	 * 
	 * @throws InterruptedException
	 */
	private static void combineRoleData() {
		final RoleDAO dao = RoleDAO.getFromApplicationContext(ac);
		final List<String> nameList = dao.findByNameAll();
		final CombineRoleDAO subDao = CombineRoleDAO.getFromApplicationContext(ac);
		List<String> subList = subDao.findAllNotRobot();
		final CountDownLatch cdl = new CountDownLatch(subList.size());
		for (final String id : subList) {
			DBThreads.execute(new Runnable() {

				@Override
				public void run() {
					try {
						Role subRole = subDao.findById(id);
						String name = subRole.getName();
						if (ServerCombiner.CollectContainsIgnoreCase(nameList, name)) {// 记录重复名字玩家并直接更改被
							Role majorRole = dao.findByName(name);
							if (majorRole != null) {// 由于合服指令都是异步操作，可能重名的是机器人，而此时机器人可能已经被清理
								// majorRole.setName(generateNameForDuplicate(MajorServerId,
								// name));
								// dao.customMerge(majorRole);

								subRole.setName(generateNameForDuplicate(CombineServerId, name));
							}
						}
						dao.customMerge(subRole);
						cdl.countDown();
					} catch (Exception e) {
						logger.error(id, e);
						System.exit(1);
					}
				}
			});
		}

		try {
			cdl.await();
		} catch (InterruptedException e) {
			logger.error("Unknow Error!", e);
			System.exit(1);
		}

	}

	/**
	 * 字符串集合中是否包含指定字符串，忽略大小写
	 * 
	 * @param list
	 * @param input
	 * @return
	 */
	private static boolean CollectContainsIgnoreCase(Collection<String> list, String input) {
		for (String item : list) {
			if (item.equalsIgnoreCase(input)) {
				return true;
			}
		}

		return false;
	}

	public static String generateNameForDuplicate(int serverId, String name) {
		return TextUtil.format("s{0}.{1}", serverId % Const.Ten_Thousand, name);
	}
}
