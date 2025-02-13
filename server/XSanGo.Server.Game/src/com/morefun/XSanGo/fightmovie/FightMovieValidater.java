package com.morefun.XSanGo.fightmovie;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.FightMovieView;
import com.XSanGo.Protocol.GrowableProperty;
import com.XSanGo.Protocol.HeroView;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.XSanGo.Protocol.SceneDuelView;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.db.game.FightMovie;
import com.morefun.XSanGo.fightmovie.XsgFightMovieManager.ValidationStatus;
import com.morefun.XSanGo.util.HttpUtil;
import com.morefun.XSanGo.util.HttpUtil.ResponseContent;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 战报验证器
 * 
 * @author qinguofeng
 */
public class FightMovieValidater implements
		Callable<FightMovieValidater.ValidateResult> {
	private final static Log logger = LogFactory
			.getLog(FightMovieValidater.class);

	/** 战报序列化数据 */
	private FightMovie movie;
	private String roleId;
	private int serverId;
	private String roleAccount;

	public FightMovieValidater(String roleAccount, FightMovie movie) {
		this.roleId = movie.getSelfId();
		this.serverId = ServerLancher.getServerId();
		this.roleAccount = roleAccount;
		this.movie = movie;
	}

	/**
	 * 更新血量翻倍等
	 * */
	private void updateFormationView(FightMovie movie, PvpOpponentFormationView selfView, PvpOpponentFormationView opponentView) {
		FightLifeNumT numT = XsgFightMovieManager.getInstance().getFightLifeT(movie.getType());
		if (numT != null && numT.num != 1) {
			if (opponentView != null && opponentView.heros != null) {
				for (HeroView hv:opponentView.heros) {
					if (hv.properties != null) {
						for (GrowableProperty p:hv.properties) {
							if ("hp".equals(p.code)) {
								p.value = p.value * numT.num;
								p.grow = p.grow * numT.num;
							}
						}
					}
				}
			}
			if (selfView != null && selfView.heros != null) {
				for (HeroView hv:selfView.heros) {
					if (hv.properties != null) {
						for (GrowableProperty p:hv.properties) {
							if ("hp".equals(p.code)) {
								p.value = p.value * numT.num;
								p.grow = p.grow * numT.num;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 连接远程验证服务器
	 * */
	private ValidateResult validate() {
		ValidateResult result = new ValidateResult();
		// 反序列化战报数据
		FightMovieView movieView = null;
		// 自己阵容
		PvpOpponentFormationView selfFormationView = null;
		// 敌方阵容
		PvpOpponentFormationView opponentFormationView = null;
		try {
			// 反序列化战报数据
			String movieData = movie.getData();
			if (!TextUtil.isBlank(movieData)) {
				movieView = TextUtil.GSON.fromJson(TextUtil.ungzip(movieData),
						FightMovieView.class);
			}
			String selfFormation = movie.getSelfFormation();
			if (!TextUtil.isBlank(selfFormation)) {
				selfFormationView = TextUtil.GSON.fromJson(
						TextUtil.ungzip(selfFormation),
						PvpOpponentFormationView.class);
			}
			String opponentFormation = movie.getOpponentFormation();
			if (!TextUtil.isBlank(opponentFormation)) {
				opponentFormationView = TextUtil.GSON.fromJson(
						TextUtil.ungzip(opponentFormation),
						PvpOpponentFormationView.class);
			}
			if (movieView == null || selfFormationView == null) {
				String errStr = TextUtil.format(
						"战报格式错误,[{0}][{1}][{2}][{3}][{4}][{5}]",
						movieView == null, selfFormationView == null,
						opponentFormationView == null,
						TextUtil.isBlank(movieData),
						TextUtil.isBlank(selfFormation),
						TextUtil.isBlank(opponentFormation));
				logger.warn(errStr);
				result.result = ValidationStatus.Error;
				result.reason = errStr;
				return result;
			}
		} catch (Exception e) { // 战报解压缩失败, 战报格式错误
			logger.error("战报格式错误", e);
			result.result = ValidationStatus.Error;
			result.reason = "战报格式错误: " + e.getMessage();

			movieView = null;
			selfFormationView = null;
			opponentFormationView = null;
		}

		if (movieView != null && selfFormationView != null) {
			// 更新属性信息，生命翻倍等
			updateFormationView(movie, selfFormationView, opponentFormationView);
			// 构造验证服需要的参数格式
			ValidateParam param = new ValidateParam(selfFormationView,
					opponentFormationView, serverId, roleId, roleAccount,
					movieView.soloMovie);
			if (movie.getType() == XsgFightMovieManager.Type.Copy.ordinal()) {
				param.setType(1);// copy
				param.setExtra(movie.getOpponentId());
			}
			if (movie.getType() == XsgFightMovieManager.Type.TimeBattle
					.ordinal()) {
				param.setType(2); // time battle
				param.setExtra(movie.getOpponentId());
			}
			if (movie.getType() == XsgFightMovieManager.Type.WorldBoss.ordinal()) {
				param.setType(3); // World Boss
				param.setExtra(movie.getOpponentId());
			}
			byte[] parambytes = TextUtil.GSON.toJson(param).getBytes();
			// 参数总长度 = 阵容json数据长度 + 战报数据长度 + 4字节的阵容长度信息
			int length = parambytes.length + movieView.validateMovie.length + 4;
			// 格式: [总长度][阵容json数据长度][阵容json数据][战报数据]
			ByteBuffer buffer = ByteBuffer.allocate(length + 4);
			// 放入长度信息
			buffer.putInt(length).putInt(parambytes.length);
			// 放入具体数据
			buffer.put(parambytes).put(movieView.validateMovie);

			buffer.rewind();

			// 请求远程服务器验证
			ResponseContent checkResultContent = HttpUtil.doPost(
					TextUtil.format(XsgFightMovieManager.getInstance()
							.getValidateUrl(), movie.getId()), buffer.array());
			if (checkResultContent != null
					&& checkResultContent.getStatusCode() == 200) {
				// 战报验证结果
				if (!"false".equals(checkResultContent.getContent())) {
					result.result = XsgFightMovieManager.ValidationStatus.Normal;
					result.reason = "send success.";
				} else {
					result.result = XsgFightMovieManager.ValidationStatus.Error;
					result.reason = "remote return false." + checkResultContent.getContent();
				}
			} else { // 验证服务器返回值异常
//				logger.warn(TextUtil
//						.format("fightmovie validate server return error.status code:{0}",
//								checkResultContent == null ? "connect error"
//										: checkResultContent.getStatusCode()));
				result.result = ValidationStatus.Retry;
				result.reason = "server return null.";
			}
		}

		return result;
	}

	@Override
	public ValidateResult call() throws Exception {
		return validate();
	}

	/**
	 * 战报服务器需要的参数封装
	 * */
	public static class ValidateParam {
		public PvpOpponentFormationView selfFormation;
		public PvpOpponentFormationView opponentFormation;
		public SceneDuelView[] soloMovie;
		public int type;
		public String extra;

		public int serverId;
		public String roleId;
		public String roleAccount;

		public ValidateParam(PvpOpponentFormationView self,
				PvpOpponentFormationView opponent, int serverId, String roleId,
				String roleAccount, SceneDuelView[] soloMovie) {
			selfFormation = self;
			this.serverId = serverId;
			this.roleId = roleId;
			this.roleAccount = roleAccount;
			opponentFormation = opponent;
			this.soloMovie = soloMovie;
			type = 0;
			extra = "";
		}

		public void setType(int type) {
			this.type = type;
		}

		public void setExtra(String extra) {
			this.extra = extra;
		}

		public int getType() {
			return type;
		}

		public String getExtra() {
			return extra;
		}
	}

	/**
	 * 战报验证结果
	 * */
	public static class ValidateResult {
		public ValidateResult() {
		}

		public ValidateResult(int result, String reason) {
			this.result = result;
			this.reason = reason;
		}

		public int result; // XsgFightMovieManager.VALIDATION_STATUS, 验证结果码
		public String reason; // 原因
	}
}
