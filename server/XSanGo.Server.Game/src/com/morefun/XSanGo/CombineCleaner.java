package com.morefun.XSanGo;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.morefun.XSanGo.db.game.RoleDAO;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 合服出错的数据清理
 * 
 * @Uasge java -jar -cp lib-path com.morefun.XSanGo.CombineCleaner configFile
 *        serverId (java -Xmn256m -Xms1024m -Xmx3072m -cp game-lib\*;config
 *        com.morefun.XSanGo.CombineCleaner applicationContextMain.xml 15)
 * 
 * @author guofeng.qin
 */
public class CombineCleaner {
	private final static Log logger = LogFactory.getLog(CombineCleaner.class);

	private static ApplicationContext ctx;
	private static String cfgPath = "applicationContextMain.xml";
	private static int serverId = 16;

	public static void main(String[] args) {
		if (args != null && args.length > 0) {
			cfgPath = args[0];
			serverId = Integer.parseInt(args[1]);
		}

		ctx = new ClassPathXmlApplicationContext(cfgPath);

		logger.warn(TextUtil.format("{0}服玩家数据准备清理", serverId));

		final CountDownLatch cdl = new CountDownLatch(1);

		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				try {
					cleanRoleData(serverId);
				} catch (Exception e) {
					logger.error(e);
				} finally {
					cdl.countDown();
				}
			}
		});

		try {
			cdl.await();
		} catch (InterruptedException e) {
			logger.error("Interrupted Exception:", e);
			System.exit(1);
		}
		logger.warn(TextUtil.format("{0}服玩家数据清理完成", serverId));
		System.exit(0);
	}

	public static void cleanRoleData(int serverId) {
		logger.warn(TextUtil.format("{0}服玩家数据清理开始", serverId));
		final RoleDAO dao = RoleDAO.getFromApplicationContext(ctx);
		final SimpleDAO deleteDao = SimpleDAO.getFromApplicationContext(ctx);

		List<String> roleList = dao.findAllIdByServerId(serverId);

		int size = roleList.size();
		final CountDownLatch latch = new CountDownLatch(size);

		final AtomicInteger count = new AtomicInteger(0);
		for (final String roleId : roleList) {
			DBThreads.execute(new Runnable() {
				@Override
				public void run() {
					try {
						deleteDao.delete(dao.findById(roleId));
						logger.warn(TextUtil.format("已处理{0}个", count.incrementAndGet()));
					} catch (Exception e) {
						logger.error(TextUtil.format("role {0} delete error.", roleId), e);
					} finally {
						latch.countDown();
					}
				}
			});
		}
		logger.warn(TextUtil.format("{0}个符合条件的玩家加入处理队列", size));
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.warn(TextUtil.format("{0}服玩家数据清理结束,size:{1}", serverId, size));
	}
}
