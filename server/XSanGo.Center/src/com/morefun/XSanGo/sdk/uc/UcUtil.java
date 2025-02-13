package com.morefun.XSanGo.sdk.uc;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;

import com.morefun.XSanGo.LoginDatabase;
import com.morefun.XSanGo.http.HttpUtil;
import com.morefun.XSanGo.util.EncryptUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * UC九游接口操作类
 * 
 * @author BruceSu
 * 
 */
public class UcUtil {
	/**
	 * sdk server的接口地址 #端游服务端测试环境访问地址：http://sdk.test4.g.uc.cn/ss
	 * #端游服务端生产环境访问地址：http://sdk.g.uc.cn/ss
	 */
	private static String serverUrl = LoginDatabase.instance().getAc()
			.getBean("UcLoginUrl", String.class);
	// 游戏合作商编号
	private static int cpId = 593;
	// 游戏编号
	private static int gameId = 544156;
	// 游戏发行渠道编号
	private static String channelId = "2";
	// 游戏服务器编号
	private static int serverId = 2459;
	// 分配给游戏合作商的接入密钥,请做好安全保密
	private static String apiKey = "7f768353ed0854f58a8d7f3972949aac";

	private static ObjectMapper objectMapper = new ObjectMapper();
	static {
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		objectMapper
				.setDeserializationConfig(objectMapper
						.getDeserializationConfig()
						.without(
								DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
	}

	/**
	 * 程序入口。
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		// String s =
		// "{\"id\":1383637031656,\"state\":{\"code\":10,\"msg\":\"游戏信息配置错误\"},\"data\":{}}";
		// System.out.println(s);
		// SidInfoResponse test = (SidInfoResponse) TextUtil.GSON.fromJson(s,
		// SidInfoResponse.class);
		System.out.println("serverUrl=" + serverUrl);
		System.out.println("cpId=" + cpId);
		System.out.println("gameId=" + gameId);
		System.out.println("channelId=" + channelId);
		System.out.println("serverId=" + serverId);
		System.out.println("apiKey=" + apiKey);

		/************************************************************/
		//
		String sid = "70b37f00-5f59-4819-99ad-8bde027ade00144882";//
		// request.Params["sid"];从游戏客户端的请求中获取的sid值
		// String sid = "abcdefg123456";
		sidInfo(sid);// 调用sid用户会话验证
		/************************************************************/

		/************************************************************/
		//
		String gameUser = "abcd";// request.Params["gameUser"];从游戏客户端的请求中获取的gameUser值
		// ucid_bind_create(gameUser);//调用ucid和游戏官方帐号绑定接口
		/************************************************************/

		/************************************************************/

		// return payCallback(request);支付回调通知
		// payCallback(null);
		/************************************************************/
	}

	/**
	 * sid用户会话验证。
	 * 
	 * @param sid
	 *            从游戏客户端的请求中获取的sid值
	 * @throws Exception
	 */
	public static SidInfoResponse sidInfo(String sid) {
		// System.out.println("开始调用sidInfo接口");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", System.currentTimeMillis());// 当前系统时间
		params.put("service", "ucid.user.sidInfo");

		Game game = new Game();
		game.setCpId(cpId);// cpid是在游戏接入时由UC平台分配，同时分配相对应cpId的apiKey
		game.setGameId(gameId);// gameid是在游戏接入时由UC平台分配
		game.setChannelId(channelId);// channelid是在游戏接入时由UC平台分配
		// serverid是在游戏接入时由UC平台分配，
		// 若存在多个serverid情况，则根据用户选择进入的某一个游戏区而确定。
		// 若在调用该接口时未能获取到具体哪一个serverid，则此时默认serverid=0
		game.setServerId(serverId);

		params.put("game", game);

		Map data = new HashMap();
		data.put("sid", sid);// 在uc sdk登录成功时，游戏客户端通过uc
								// sdk的api获取到sid，再游戏客户端由传到游戏服务器
		params.put("data", data);

		params.put("encrypt", "md5");
		/*
		 * 签名规则=cpId+签名内容+apiKey
		 * 假定cpId=109,apiKey=202cb962234w4ers2aaa,sid=abcdefg123456
		 * 那么签名原文109sid=abcdefg123456202cb962234w4ers2aaa
		 * 签名结果6e9c3c1e7d99293dfc0c81442f9a9984
		 */
		String signSource = cpId + "sid=" + sid + apiKey;// 组装签名原文
		String sign = null;
		SidInfoResponse rsp = null;
		try {
			// MD5加密签名
			sign = EncryptUtil.bytesToHexString(EncryptUtil.getMD5(signSource
					.getBytes("UTF-8")));
			// System.out.println("[签名原文]" + signSource);
			// System.out.println("[签名结果]" + sign);

			params.put("sign", sign);

			String body = TextUtil.GSON.toJson(params);// Util.encodeJson(params);//
														// 把参数序列化成一个json字符串
														// System.out.println("[请求参数]"
														// + body);

			String result = HttpUtil.doPost(serverUrl, body).trim();// http
			// post方式调用服务器接口,请求的body内容是参数json格式字符串
			// System.out.println("[响应结果]"
			// + result);//
			// 结果也是一个json格式字符串

			// 这里只能用UC提供的反序列化方式，因为当data为空时未按标准格式传输
			rsp = (SidInfoResponse) decodeJson(result, SidInfoResponse.class);// 反序列化
			if (rsp != null) {// 反序列化成功，输出其对象内容
				// System.out.println("[id]" + rsp.getId());
				// System.out.println("[code]" + rsp.getState().getCode());
				// System.out.println("[msg]" + rsp.getState().getMsg());
				// System.out.println("[ucid]" + rsp.getData().getUcid());
				// System.out.println("[nickName]" +
				// rsp.getData().getNickName());
			} else {
				System.out.println("接口返回异常");
			}
			// System.out.println("调用sidInfo接口结束");
		} catch (NoSuchAlgorithmException e1) {
			LoggerFactory.getLogger(UcUtil.class).error(e1.toString());
		} catch (UnsupportedEncodingException e1) {
			LoggerFactory.getLogger(UcUtil.class).error(e1.toString());
		} catch (Exception e) {
			LoggerFactory.getLogger(UcUtil.class).error(e.toString());
		}

		return rsp;

	}

	private static Object decodeJson(String json, Class pojoClass)
			throws Exception {
		return objectMapper.readValue(json, pojoClass);
	}
}
