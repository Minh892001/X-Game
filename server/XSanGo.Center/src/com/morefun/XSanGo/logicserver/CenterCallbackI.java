/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.logicserver;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Ice.Current;

import com.XSanGo.Protocol.AMD_CenterCallback_beginUseCDK;
import com.XSanGo.Protocol.AMD_CenterCallback_createOrderForAppleAppStore;
import com.XSanGo.Protocol.AMD_CenterCallback_endUseCDK;
import com.XSanGo.Protocol.AMD_CenterCallback_getChannelOrderIdFromPayCenter;
import com.XSanGo.Protocol.AlarmType;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.Property;
import com.XSanGo.Protocol._CenterCallbackDisp;
import com.morefun.XSanGo.CenterServer;
import com.morefun.XSanGo.LoginDatabase;
import com.morefun.XSanGo.SMSManager;
import com.morefun.XSanGo.XsgAccountManager;
import com.morefun.XSanGo.db.Account;
import com.morefun.XSanGo.db.CDKDao;
import com.morefun.XSanGo.db.CDKDetail;
import com.morefun.XSanGo.db.CDKGroup;
import com.morefun.XSanGo.db.Channel;
import com.morefun.XSanGo.db.stat.AccountOperateLog;
import com.morefun.XSanGo.db.stat.StatSimpleDAO;
import com.morefun.XSanGo.http.HttpUtil;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

/**
 * @author WFY
 *
 */
@SuppressWarnings("serial")
public class CenterCallbackI extends _CenterCallbackDisp {
	private static Log log = LogFactory.getLog(CenterCallbackI.class);
	
	private int serverId;
	private ILogicServerControler logicServer;

	public CenterCallbackI(int serverId) {
		super();
		this.serverId = serverId;
		this.logicServer = new LogicServerControler(serverId);
	}

	@Override
	public void addRole(String account, String roleId, String roleName, Current __current) {

		this.logicServer.createRole(account, roleId, roleName);

	}

	@Override
	public void newMaxLevel(String account, String roleId, String roleName, int newLevel, Current __current) {
		this.logicServer.newMaxLevel(account, roleId, roleName, newLevel);
	}

	@Override
	public void beginUseCDK_async(final AMD_CenterCallback_beginUseCDK __cb, final String account, final String cdk,
			final int roleLevel, final String factionName, Current __current) throws NoteException {
		// 查找是否存在，是否已经被使用，是否有使用次数，服务器账号角色有效期等限制
		LoginDatabase.execute(new Runnable() {

			@Override
			public void run() {
				// 查询数据库
				CDKDetail detail = CDKDao.getFromApplicationContext(LoginDatabase.instance().getAc()).findDetailByCDK(
						cdk);
				try {
					checkCDK(account, serverId, roleLevel, factionName, detail);
				} catch (NoteException e) {
					__cb.ice_exception(e);
					return;
				}

				__cb.ice_response(TextUtil.GSON.fromJson(detail.getGroup().getContent(), Property[].class));
			}

		});

	}

	/**
	 * @param __cb
	 * @param account
	 * @param roleLevel
	 * @param factionName
	 * @param detail
	 * @return
	 * @throws NoteException
	 */
	public static void checkCDK(String account, int serverId, final int roleLevel, final String factionName,
			CDKDetail detail) throws NoteException {
		if (detail == null) {
			throw new NoteException("兑换码错误。");
		}
		if (detail.getMaxUseTime() > 0 && detail.getUseTime() >= detail.getMaxUseTime()) {
			throw new NoteException("Sorry，该兑换码已被使用。");
		}

		CDKGroup group = detail.getGroup();
		Integer[] channels = TextUtil.GSON.fromJson(group.getChannels(), Integer[].class);
		Integer[] servers = TextUtil.GSON.fromJson(group.getServers(), Integer[].class);
		if (servers.length > 0 && servers[0] != -1) {// -1表示不做限制
			if (CollectionUtil.indexOf(servers, serverId) < 0) {
				throw new NoteException("Sorry，该兑换码只能在指定服务器使用。");
			}
		}

		if (System.currentTimeMillis() > group.getEndTime().getTime()) {
			throw new NoteException("Sorry，兑换码已过期。");
		}
		if (System.currentTimeMillis() < group.getBeginTime().getTime()) {
			throw new NoteException("Sorry，兑换码尚未启用。");
		}
		if (roleLevel > group.getMaxLevel()) {
			throw new NoteException("Sorry，使用等级上限为" + group.getMaxLevel());
		}
		if (roleLevel < group.getMinLevel()) {
			throw new NoteException("Sorry，使用等级下限为" + group.getMinLevel());
		}

		if (!TextUtil.isNullOrEmpty(group.getFactionName())) {
			if (group.getFactionName().equals(factionName)) {
				throw new NoteException("Sorry，该兑换码有公会限制。");
			}
		}
		if (LoginDatabase.instance().getAc().getBean("CheckCdkChannel", Boolean.class)) {
			if (channels.length > 0 && channels[0] != -1) {// -1表示不做限制
				Account db = XsgAccountManager.getInstance().findAccount(account);
				if (db == null) {
					throw new NoteException("Sorry，帐号不存在。");
				}
				if (CollectionUtil.indexOf(channels, db.getChannelId()) < 0) {
					throw new NoteException("Sorry，该兑换码有渠道限制。");
				}
			}
		}
	}

	@Override
	public void endUseCDK_async(final AMD_CenterCallback_endUseCDK __cb, final String cdk, Current __current) {
		LoginDatabase.execute(new Runnable() {

			@Override
			public void run() {
				// 查询数据库
				CDKDao dao = CDKDao.getFromApplicationContext(LoginDatabase.instance().getAc());
				CDKDetail detail = dao.findDetailByCDK(cdk);
				detail.setUseTime(detail.getUseTime() + 1);
				// 保存数据库
				dao.attachDirty(detail);
				__cb.ice_response();
			}
		});
	}

	/* (non-Javadoc)
	 * 游戏逻辑服向登录中心服请求充值订单号，登录中心服向paycenter获取订单号后返回给游戏逻辑服
	 * @see com.XSanGo.Protocol._CenterCallbackOperations#getChannelOrderIdFromPayCenter_async(com.XSanGo.Protocol.AMD_CenterCallback_getChannelOrderIdFromPayCenter, int, int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, Ice.Current)
	 */
	@Override
	public void getChannelOrderIdFromPayCenter_async(final AMD_CenterCallback_getChannelOrderIdFromPayCenter __cb,
			final int channel, final int appId, final int money, final String mac, final String username,
			final String roleid, final String params, Current __current) throws NoteException {
		final Channel ch = CenterServer.instance().findChannel(channel);
		if (ch == null) {
			__cb.ice_exception(new NoteException("非法的渠道来源"));
			return;
		}

		LoginDatabase.execute(new Runnable() {
			@Override
			public void run() {
				String content = TextUtil.format(
						"appid={0}&channel={1}&money={2}&username={3}&serverid={4}&roleid={5}&mac={6}&expand={7}",
						appId, channel, money, username, serverId, roleid, mac, TextUtil.isNullOrEmpty(params) ? "{}"
								: params);
				// LogManager.warn(ch.getOrderUrl());
				// LogManager.warn(content);
				// content = encodeForPayCenter(content);
				try {
					__cb.ice_response(HttpUtil.doPost(ch.getOrderUrl(), content).trim());
				} catch (IOException e) {
					__cb.ice_exception(new NoteException("无法获取渠道订单号", e));
					LogManager.error(e);
				} catch (Exception e) {
					__cb.ice_exception(new NoteException("未知异常", e));
					LogManager.error(e);
				}
			}
		});
	}

	public static void main(String[] args) {

		// String res = HttpUtil
		// .doPost("http://192.168.1.59/paycenter/to/uc.jsp",
		//
		// encodeForPayCenter("appid=45&channel=503&money=1998&username=cj111115mf&serverid=11&roleid=dev119435&mac=none&expand=x3"));
		// System.out.println(res);
	}

	// /**
	// * demo
	// *
	// * @return
	// */
	// public static String demo() {
	// String postData =
	// "appid=45&channel=503&money=1998&username=cj111115@mf&serverid=11&roleid=dev-11-9435&mac=none&expand=";
	// // String appid = "45";// 游戏id，由一号通分配
	// // String channel = "503";// 渠道编号，由商务分配
	// // String money = "1";// 订单金额，单位元
	// // String username = "test01";// 游戏帐号
	// // String serverid = "1";// 服务器id
	// // String roleid = "12";// 角色id
	// // String expand = "abc";// 游戏自定义参数
	// // String mac = "aaaa";// mac地址
	// String reqURL = "http://192.168.1.59/paycenter/to/uc.jsp";//
	// // 请求渠道的下单地址
	// String res = null;
	// try {
	// String charset = "UTF-8";
	//
	// // 开始连接服务器
	// PrintWriter writer = null;
	// HttpURLConnection httpConn = null;
	// try {
	// byte[] data = encode(postData);
	// URL url = new URL(reqURL);
	// httpConn = (HttpURLConnection) url.openConnection();
	// httpConn.setRequestMethod("POST");
	// httpConn.setRequestProperty("Content-Type", "text/html");
	// httpConn.setRequestProperty("Content-Length",
	// String.valueOf(data.length));
	// httpConn.setDoInput(true);
	// httpConn.setDoOutput(true);
	// httpConn.setConnectTimeout(30000);
	// httpConn.setReadTimeout(30000);
	// httpConn.connect();
	// OutputStream os = httpConn.getOutputStream();
	// os.write(data);
	// os.flush();
	// os.close();
	// int responseCode = httpConn.getResponseCode();
	// if (HttpURLConnection.HTTP_OK == responseCode) {
	// byte[] buffer = new byte[1024];
	// int len = -1;
	// InputStream is = httpConn.getInputStream();
	// ByteArrayOutputStream bos = new ByteArrayOutputStream();
	// while ((len = is.read(buffer)) != -1) {
	// bos.write(buffer, 0, len);
	// }
	// res = bos.toString(charset);
	// is.close();
	// } else {
	// System.out.println(responseCode);
	// res = "您好,由于系统繁忙,请您稍后提交...";
	// }
	// } catch (Exception ex) {
	// res = "服务器繁忙，请稍后再试...";
	// ex.printStackTrace();
	// } finally {
	// if (null != writer) {
	// writer.close();
	// }
	// if (null != httpConn) {
	// httpConn.disconnect();
	// }
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// System.out.println("res=" + res);
	// return res;
	// }

	// public static byte[] encode(String sourceStr) {
	// if (sourceStr == null || sourceStr.equals("")) {
	// return null;
	// }
	// byte[] data = sourceStr.getBytes();
	// byte[] encrypt = new byte[data.length];
	// int index = 0;
	// for (int i = data.length - 1; i >= 0; i--) {
	// byte temp = data[i];
	// encrypt[index] = (byte) (127 - temp);
	// index++;
	// }
	// return encrypt;
	// }

	/**
	 * 支付中心脱裤子放屁的编码规则
	 * 
	 * @param sourceStr
	 * @return
	 */
	private static String encodeForPayCenter(String sourceStr) {
		if (sourceStr == null || sourceStr.equals("")) {
			return null;
		}
		byte[] data = sourceStr.getBytes();
		byte[] encrypt = new byte[data.length];
		int index = 0;
		for (int i = data.length - 1; i >= 0; i--) {
			byte temp = data[i];
			encrypt[index] = (byte) (127 - temp);
			index++;
		}
		return new String(encrypt, 0, encrypt.length);
	}

	@Override
	public void createOrderForAppleAppStore_async(final AMD_CenterCallback_createOrderForAppleAppStore __cb,
			final String appStoreOrderId, final int channel, final int appId, final int money, String itemId,
			final String mac, final String username, final String roleid, final String params, Current __current)
			throws NoteException {
		final Channel ch = CenterServer.instance().findChannel(channel);
		if (ch == null) {
			__cb.ice_exception(new NoteException("非法的渠道来源"));
			return;
		}

		LoginDatabase.execute(new Runnable() {
			@Override
			public void run() {
				String content = TextUtil
						.format("money={0}&username={1}&channel={2}&appid={3}&serverid={4}&roleid={5}&expand={6}&mac={7}&transaction={8}",
								money, username, channel, appId, serverId, roleid,
								TextUtil.isNullOrEmpty(params) ? "{}" : params, mac, appStoreOrderId);
				// System.out.println(content);
				try {
					content = encodeForPayCenter(content);
					HttpUtil.doPost(LoginDatabase.instance().getAc().getBean("IOS_AppStore_Order_Url", String.class),
							content).trim();
					__cb.ice_response();
				} catch (IOException e) {
					__cb.ice_exception(new NoteException("充值中心处理异常", e));
				} catch (Exception e) {
					__cb.ice_exception(new NoteException("未知异常", e));
				}
			}
		});

	}

	@Override
	public void frozenAccount(final String account, final String remark, Current __current) {
		LoginDatabase.execute(new Runnable() {

			@Override
			public void run() {
				Account acc = XsgAccountManager.getInstance().findAccount(account);
				if (acc != null) {
					Date release = DateUtil.addDays(Calendar.getInstance(), 30).getTime();
					acc.setFrozenExpireTime(release);
					XsgAccountManager.getInstance().updateAccount(acc);
					StatSimpleDAO.getForStat(LoginDatabase.instance().getAc()).save(
							new AccountOperateLog(account, Calendar.getInstance().getTime(), remark));
				}
			}
		});
	}

	@Override
	public void sendAlarmSMS(final AlarmType type, final String smsText, Current __current) {
		SMSManager.getInstance().sendAlarmSMS(type, smsText);
	}
}