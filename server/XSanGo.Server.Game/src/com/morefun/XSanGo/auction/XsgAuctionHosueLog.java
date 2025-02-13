package com.morefun.XSanGo.auction;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.db.game.AuctionHouseDao;
import com.morefun.XSanGo.db.game.AuctionHouseItemLog;
import com.morefun.XSanGo.db.game.AuctionHouseLog;
import com.morefun.XSanGo.db.game.RoleItem;
import com.morefun.XSanGo.db.game.SimpleDAO;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 拍卖行,日志记录
 * 
 * @author qinguofeng
 * @date Apr 6, 2015
 */
public class XsgAuctionHosueLog {

	private final static Log logger = LogFactory
			.getLog(XsgAuctionHosueLog.class);

	public static final class LogType {
		/** 系统获得税收 */
		public static final int SYSTEM_RATE = 0;
		/** 上架 */
		public static final int SELL = 1;
		/** 普通叫价 */
		public static final int BUY = 2;
		/** 一口价 */
		public static final int FIXEDBUY = 3;
		/** 拍卖成功, 获得拍卖币 */
		public static final int SELL_SUCCESS = 4;
		/** 交易失败,退回物品 */
		public static final int SELL_FALURE = 5;
		/** 竞拍成功, 获得物品 */
		public static final int BUY_SUCCESS = 6;
		/** 竞价失败,退回拍卖币 */
		public static final int SEND_BACK = 7;
		/** 下架 */
		public static final int SELL_CANCEL = 8;
		/** 兑换拍卖币 */
		public static final int EXCHANGE = 9;
	}

	/**
	 * 税收
	 * 
	 * {otherId} 买了 {roleId} 的 {num} 个 {templateId} 系统收入 {money}
	 * */
	public static void logRevenue(String auctionId, String roleId,
			String otherId, int num, String templateId, long money) {
		AuctionHouseLog log = new AuctionHouseLog();
		log.setAuctionId(auctionId);
		log.setOperationType(LogType.SYSTEM_RATE);
		log.setRoleId(roleId);
		log.setOtherId(otherId);
		log.setNum(num);
		log.setTemplateId(templateId);
		log.setMoney(money);
		log.setUpdateTime(Calendar.getInstance().getTime());
		addAuctionLog(log);
	}

	/**
	 * 普通叫价
	 * 
	 * {roleId} 出价 {money} 购买 {num} 个 {templdateId}
	 * */
	public static void logBuy(String auctionId, String roleId, int num,
			String templateId, long money) {
		AuctionHouseLog log = new AuctionHouseLog();
		log.setAuctionId(auctionId);
		log.setOperationType(LogType.BUY);
		log.setRoleId(roleId);
		log.setNum(num);
		log.setTemplateId(templateId);
		log.setMoney(money);
		log.setUpdateTime(Calendar.getInstance().getTime());
		addAuctionLog(log);
	}

	/**
	 * 上架
	 * 
	 * {roleId} 上架了 {num} 个 {templateId} 起拍价 {money}
	 * */
	public static void logSell(String auctionId, String roleId, int num,
			String templateId, long money, RoleItem item) {
		AuctionHouseLog log = new AuctionHouseLog();
		log.setAuctionId(auctionId);
		log.setOperationType(LogType.SELL);
		log.setRoleId(roleId);
		log.setNum(num);
		log.setTemplateId(templateId);
		log.setMoney(money);
		log.setUpdateTime(Calendar.getInstance().getTime());

		AuctionHouseItemLog itemLog = new AuctionHouseItemLog();
		itemLog.setAuctionId(auctionId);
		itemLog.setItemId(item.getId());
		itemLog.setData(TextUtil.GSON.toJson(item));
		itemLog.setUpdateTime(Calendar.getInstance().getTime());

		addAuctionLog(log, itemLog);
	}

	/**
	 * 一口价买下
	 * 
	 * {roleId} 从 {otherId} 那里一口价 {money} 买下了 {num} 个 {templateId}
	 * */
	public static void logFixedBuySuccess(String auctionId, String roleId,
			String otherId, String templateId, int num, long money) {
		AuctionHouseLog log = new AuctionHouseLog();
		log.setAuctionId(auctionId);
		log.setRoleId(roleId);
		log.setTemplateId(templateId);
		log.setOperationType(LogType.FIXEDBUY);
		log.setUpdateTime(Calendar.getInstance().getTime());
		log.setOtherId(otherId);
		log.setMoney(money);
		log.setNum(num);
		addAuctionLog(log);
	}

	/**
	 * 竞拍成功, 获得物品
	 * 
	 * {roleId} 花了 {money} 竞拍成功了 {num} 个 {templateId}, 卖家是 {otherId}
	 * */
	public static void logNormalBuySuccess(String auctionId, String roleId,
			String otherId, String templateId, int num, long money) {
		AuctionHouseLog log = new AuctionHouseLog();
		log.setAuctionId(auctionId);
		log.setRoleId(roleId);
		log.setTemplateId(templateId);
		log.setNum(num);
		log.setOtherId(otherId);
		log.setOperationType(LogType.BUY_SUCCESS);
		log.setMoney(money);
		log.setUpdateTime(Calendar.getInstance().getTime());
		addAuctionLog(log);
	}

	/**
	 * 竞拍失败, 退回拍卖币
	 * 
	 * {roleId} 竞价 {num} 个 {templateId} 被 {otherId} 以 {money} 超过
	 * */
	public static void logBuyFailure(String auctionId, String roleId,
			String otherId, long money, int num, String templateId) {
		AuctionHouseLog log = new AuctionHouseLog();
		log.setAuctionId(auctionId);
		log.setOperationType(LogType.SEND_BACK);
		log.setRoleId(roleId);
		log.setOtherId(otherId);
		log.setMoney(money);
		log.setNum(num);
		log.setTemplateId(templateId);
		log.setUpdateTime(Calendar.getInstance().getTime());
		addAuctionLog(log);
	}

	/**
	 * 拍卖成功, 获得拍卖币
	 * 
	 * {roleId} 拍卖 {num} 个 {templateId} 被 {otherId} 已 {money} 拍卖币买走, 拍买成功
	 * */
	public static void logSellSuccess(String auctionId, String roleId,
			String otherId, long money, String templateId, int num) {
		AuctionHouseLog log = new AuctionHouseLog();
		log.setAuctionId(auctionId);
		log.setOperationType(LogType.SELL_SUCCESS);
		log.setRoleId(roleId);
		log.setOtherId(otherId);
		log.setMoney(money);
		log.setTemplateId(templateId);
		log.setNum(num);
		log.setUpdateTime(Calendar.getInstance().getTime());
		addAuctionLog(log);
	}

	/**
	 * 物品下架
	 * 
	 * {roleId} 下架了拍卖品 {num} 个 {templateId}
	 * */
	public static void logCancelAuction(String auctionId, String roleId,
			String templateId, int num) {
		AuctionHouseLog log = new AuctionHouseLog();
		log.setAuctionId(auctionId);
		log.setRoleId(roleId);
		log.setTemplateId(templateId);
		log.setNum(num);
		log.setOperationType(LogType.SELL_CANCEL);
		log.setUpdateTime(Calendar.getInstance().getTime());
		addAuctionLog(log);
	}

	/**
	 * 拍卖结束, 无人叫价
	 * 
	 * {roldId} 拍卖 {num} 个 {templateId} 无人购买, 价格 {price}
	 * */
	public static void logNoOneBuy(String auctionId, String roleId,
			String templateId, int num, long price) {
		AuctionHouseLog log = new AuctionHouseLog();
		log.setAuctionId(auctionId);
		log.setRoleId(roleId);
		log.setTemplateId(templateId);
		log.setNum(num);
		log.setMoney(price);
		log.setOperationType(LogType.SELL_FALURE);
		log.setUpdateTime(Calendar.getInstance().getTime());
		addAuctionLog(log);
	}

	/**
	 * 兑换拍卖币
	 * 
	 * {roleId} 用 {price} 兑换了{num} 拍卖币
	 * */
	public static void logExchange(String roleId, int price, long num) {
		AuctionHouseLog log = new AuctionHouseLog();
		log.setOperationType(LogType.EXCHANGE);
		log.setTemplateId(Const.PropertyName.AUCTION_COIN);
		log.setRoleId(roleId);
		log.setNum(price);
		log.setMoney(num);
		log.setUpdateTime(Calendar.getInstance().getTime());
		addAuctionLog(log);
	}

	public static void addAuctionLog(final AuctionHouseLog log) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				SimpleDAO dao = SimpleDAO
						.getFromApplicationContext(ServerLancher.getAc());
				dao.save(log);
			}
		});
	}
	
	public static void addAuctionLog(final AuctionHouseLog log, final AuctionHouseItemLog itemLog) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				SimpleDAO dao = SimpleDAO
						.getFromApplicationContext(ServerLancher.getAc());
				dao.save(log);
				dao.save(itemLog);
			}
		});
	}

	/**
	 * 删除老的日志数据, 数据库线程执行, 表示 {monthCount} 个月 {dayCount} 天之前的数据全都清理掉
	 * 
	 * @param monthCount
	 *            月份
	 * @param dayCount
	 *            天数
	 * */
	public static void removeOldLogs(final int monthCount, final int dayCount) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				Calendar cal = Calendar.getInstance();
				cal = DateUtil.addMonth(cal, -monthCount);
				cal = DateUtil.addDays(cal, -dayCount);
				AuctionHouseDao dao = AuctionHouseDao
						.getFromApplicationContext(ServerLancher.getAc());
				Date date = cal.getTime();
				int count = dao.removeLogOlderThen(date);
				if (count > 0) {
					logger.info("remove auction log count: " + count);
				}
				count = dao.removeItemLogOlderThen(date);
				if (count > 0) {
					logger.info("remove auction item log count: " + count);
				}
			}
		});
	}
}
