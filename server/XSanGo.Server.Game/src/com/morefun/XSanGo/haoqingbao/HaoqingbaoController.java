package com.morefun.XSanGo.haoqingbao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.AMD_Haoqingbao_getRedPacketDetail;
import com.XSanGo.Protocol.AMD_Haoqingbao_rankList;
import com.XSanGo.Protocol.ChatChannel;
import com.XSanGo.Protocol.ChatRole;
import com.XSanGo.Protocol.HaoqingbaoRecordView;
import com.XSanGo.Protocol.HaoqingbaoView;
import com.XSanGo.Protocol.MyRedPacketView;
import com.XSanGo.Protocol.NoFactionException;
import com.XSanGo.Protocol.NoGroupException;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol.PreRecvRedPacketResultView;
import com.XSanGo.Protocol.RankItemView;
import com.XSanGo.Protocol.RankView;
import com.XSanGo.Protocol.RecvRedPacketResultView;
import com.XSanGo.Protocol.RedPacketDetailItemView;
import com.XSanGo.Protocol.RedPacketDetailView;
import com.XSanGo.Protocol.TextMessage;
import com.morefun.XSanGo.GlobalDataManager;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.chat.IChatControler;
import com.morefun.XSanGo.chat.XsgChatManager;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.common.XsgGameParamManager;
import com.morefun.XSanGo.db.game.FactionMember;
import com.morefun.XSanGo.db.game.HaoqingbaoRedPacket;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.db.game.RoleClaimRedPacket;
import com.morefun.XSanGo.db.game.RoleHaoqingbao;
import com.morefun.XSanGo.db.game.RoleHaoqingbaoItem;
import com.morefun.XSanGo.db.game.RoleHaoqingbaoRecord;
import com.morefun.XSanGo.db.game.RoleHaoqingbaoRedPacketRecord;
import com.morefun.XSanGo.db.game.RoleSns;
import com.morefun.XSanGo.event.protocol.IHaoqingbaoCharge;
import com.morefun.XSanGo.event.protocol.IHaoqingbaoCheckout;
import com.morefun.XSanGo.event.protocol.IHaoqingbaoGiveBack;
import com.morefun.XSanGo.event.protocol.IHaoqingbaoRecvRedPacket;
import com.morefun.XSanGo.event.protocol.IHaoqingbaoSendRedPacket;
import com.morefun.XSanGo.event.protocol.ILuckyStar;
import com.morefun.XSanGo.faction.IFaction;
import com.morefun.XSanGo.faction.XsgFactionManager;
import com.morefun.XSanGo.haoqingbao.XsgHaoqingbaoManager.RankItem;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.role.XsgRoleManager;
import com.morefun.XSanGo.sns.SNSType;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.LuaSerializer;
import com.morefun.XSanGo.util.TextUtil;

/**
 * @author guofeng.qin
 */
public class HaoqingbaoController implements IHaoqingbaoController {
	private final static Log log = LogFactory.getLog(HaoqingbaoController.class);

	// 排行榜显示前多少名
	private static final int HeadRankNum = 50;

	private long lastChargeStatusTime = 0L;

	private IRole roleRt;
	private Role roleDB;

	private RoleHaoqingbao haoqingbao;

	private IHaoqingbaoCharge charge;
	private IHaoqingbaoCheckout checkout;
	private IHaoqingbaoRecvRedPacket recvRedPacket;
	private IHaoqingbaoSendRedPacket sendRedPacket;
	private ILuckyStar luckyStar;
	private IHaoqingbaoGiveBack giveBack;

	public HaoqingbaoController(IRole roleRt, Role roleDB) {
		this.roleRt = roleRt;
		this.roleDB = roleDB;

		haoqingbao = roleDB.getHaoqingbao();
		if (haoqingbao == null) {
			initDB();
		}

		charge = roleRt.getEventControler().registerEvent(IHaoqingbaoCharge.class);
		checkout = roleRt.getEventControler().registerEvent(IHaoqingbaoCheckout.class);
		recvRedPacket = roleRt.getEventControler().registerEvent(IHaoqingbaoRecvRedPacket.class);
		sendRedPacket = roleRt.getEventControler().registerEvent(IHaoqingbaoSendRedPacket.class);
		luckyStar = roleRt.getEventControler().registerEvent(ILuckyStar.class);
		giveBack = roleRt.getEventControler().registerEvent(IHaoqingbaoGiveBack.class);

		cleanClaimRecords();

		XsgHaoqingbaoManager.getInstance().updateSendRankList(roleRt.getRoleId(), haoqingbao.getTotalSendSum());
		XsgHaoqingbaoManager.getInstance().updateRecvRankList(roleRt.getRoleId(), haoqingbao.getTotalRecvNum());
	}

	/**
	 * 初始化豪情宝DB对象
	 * */
	private void initDB() {
		Date current = Calendar.getInstance().getTime();
		haoqingbao = new RoleHaoqingbao(roleRt.getRoleId(), roleDB, 0, 0, 0, 0, 0, null, current, 0, 0, 0);
		saveToRoleDB();
	}

	private void saveToRoleDB() {
		roleDB.setHaoqingbao(haoqingbao);
	}

	private boolean canSendFriendRedPacket(int friendPoint, int range) {
		List<RoleSns> friends = roleRt.getSnsController().getRoleSns(SNSType.FRIEND);
		if (friends != null && friends.size() > 0) {
			for (RoleSns rs : friends) {
				if (rs.getFriendPoint() >= friendPoint) {
					if (range == 0) { // 在线好友
						IRole target = XsgRoleManager.getInstance().findRoleById(rs.getTargetRoleId());
						if (target == null || !target.isOnline()) {
							continue;
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	private int getFriendsNum() {
		List<RoleSns> list = roleRt.getSnsController().getRoleSns(SNSType.FRIEND);
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	@Override
	public HaoqingbaoView openHaoqingbaoView() {
		HaoqingbaoCfgT cfg = XsgHaoqingbaoManager.getInstance().getHaoqingbaoCfgT();
		if (cfg.isOpen == 0) {
			return null;
		}
		// 清除过期的历史记录
		removeOldRecords();
		// 清理过期的索要记录
		cleanClaimRecords();
		// 构造历史操作记录
		List<RoleHaoqingbaoRecord> list = roleDB.getHaoqingbaoRecords();
		// 按时间倒序
		Collections.sort(list, new Comparator<RoleHaoqingbaoRecord>() {
			@Override
			public int compare(RoleHaoqingbaoRecord o1, RoleHaoqingbaoRecord o2) {
				return Long.valueOf(o2.getUpdateTime().getTime()).compareTo(o1.getUpdateTime().getTime());
			}
		});
		List<HaoqingbaoRecordView> records = new ArrayList<HaoqingbaoRecordView>();
		if (list != null && list.size() > 0) {
			for (RoleHaoqingbaoRecord record : list) {
				records.add(new HaoqingbaoRecordView(roleRt.getHeadImage(), roleRt.getLevel(), roleRt.getVipLevel(),
						DateUtil.format(record.getUpdateTime()), record.getDescription(), record.getYuanbaoNum()));
			}
		}
		// 构造返回view
		HaoqingbaoView view = new HaoqingbaoView(cfg.chargeStatusTime, haoqingbao.getYuanbaoNum(),
				records.toArray(new HaoqingbaoRecordView[0]), cfg.maxWords, cfg.maxTotalNum);

		return view;
	}

	/**
	 * 清除累计状态
	 * */
	private void clearStatus() {
		Date checkPoint = DateUtil.joinTime("00:00:00");
		if (haoqingbao.getLastReceiveTime() != null && DateUtil.isPass(checkPoint, haoqingbao.getLastReceiveTime())) {
			haoqingbao.setReceiveRedPacketNum(0);
			haoqingbao.setLastReceiveTime(checkPoint);
		}
	}

	/**
	 * 根据算法生成一组红包
	 * */
	private List<HaoqingbaoRedPacket> generateRedPacket(HaoqingbaoCfgT cfg, RoleHaoqingbaoItem item, int total, int num)
			throws NoteException {
		int avg = total / num;
		if (avg < cfg.minNum) {
			return null;
		}
		String rangeStr[] = cfg.range.split(",");
		double range[] = { Double.parseDouble(rangeStr[0]), Double.parseDouble(rangeStr[1]) };
		int baseNum = Math.max((int) (avg * range[0]), cfg.minNum);
		int restNum = total - (baseNum * num);
		Random r = new Random();
		int upvalue = (((int) (avg * range[1])) - ((int) (avg * range[0])));
		List<HaoqingbaoRedPacket> redpacketList = new ArrayList<HaoqingbaoRedPacket>();
		Date current = Calendar.getInstance().getTime();
		for (int i = 0; i < num;) {
			int nextavg = i < (num - 1) ? (restNum / (num - i - 1)) : restNum;
			if (nextavg == 0)
				nextavg = restNum;
			int upval = Math.min(upvalue, Math.min(restNum, nextavg));
			if (upval > 0) {
				int v = r.nextInt(upval);
				if ((num - i - 1) * upvalue < (restNum - v) && i < (num - 1)) {
					int tmpmin = (restNum / (num - i));
					int tmpuv = upval - tmpmin;
					if (tmpuv > 0) {
						v = r.nextInt(tmpuv) + tmpmin;
					} else {
						v = upval;
					}
				}
				if (i == (num - 1)) {
					v = Math.max(v, restNum);
				}
				redpacketList.add(new HaoqingbaoRedPacket(GlobalDataManager.getInstance().generatePrimaryKey(), roleRt
						.getRoleId(), "", item.getId(), v + baseNum, 0, current, null));
				restNum -= v;
				i++;
			} else {
				for (int j = i; j < num; j++) {
					redpacketList.add(new HaoqingbaoRedPacket(GlobalDataManager.getInstance().generatePrimaryKey(),
							roleRt.getRoleId(), "", item.getId(), baseNum, 0, current, null));
				}
				break;
			}
		}

		int sum = 0;
		for (HaoqingbaoRedPacket hrp : redpacketList) {
			// log.debug("RedPacket: " + hrp.getNum());
			sum += hrp.getNum();
		}
		if (sum != total) {
			log.error(TextUtil.format("RedPacket Error Num: {0}:{1}:{2}:{3}", item.getRoleId(), total, num, sum));
		}

		return redpacketList;
	}

	private int minSendNum(int type, int range) {
		switch (type) {
		case XsgHaoqingbaoManager.RedPacketType_All:
			HaoqingbaoAllT at = XsgHaoqingbaoManager.getInstance().getHaoqingbaoAllT();
			if (at != null)
				return at.minNum;
			break;
		case XsgHaoqingbaoManager.RedPacketType_Faction:
			HaoqingbaoFactionT factionT = XsgHaoqingbaoManager.getInstance().getHaoqingbaoFactionT(range);
			if (factionT != null)
				return factionT.minNum;
			break;
		case XsgHaoqingbaoManager.RedPacketType_Friend:
			HaoqingbaoFriendT friendT = XsgHaoqingbaoManager.getInstance().getHaoqingbaoFriendT(range);
			if (friendT != null)
				return friendT.minNum;
			break;
		default:
			break;
		}
		return 0;
	}

	@Override
	public void sendRedPacket(int type, int minLevel, int minVipLevel, int range, int minFriendPoint,
			int totalYuanbaoNum, int packetNum, String msg) throws NoteException {
		if (type <= 0 || minLevel < 0 || minVipLevel < 0 || range < 0 || minFriendPoint < 0 || totalYuanbaoNum <= 0
				|| packetNum <= 0 || type > 3 || range > 2 || packetNum < minSendNum(type, range)) {
			throw new NoteException(Messages.getString("HaoqingbaoController.errorParam"));
		}
		XsgHaoqingbaoManager manager = XsgHaoqingbaoManager.getInstance();
		HaoqingbaoCfgT cfg = manager.getHaoqingbaoCfgT();
		// 检查最大限额
		if (totalYuanbaoNum > cfg.maxTotalNum) {
			throw new NoteException(TextUtil.format(Messages.getString("HaoqingbaoController.maxPreItem"),
					cfg.maxTotalNum));
		}
		// 检查账户余额是否足够
		int beforeYuanbaoNum = getTotalYuanbaoNum();
		if (beforeYuanbaoNum < totalYuanbaoNum) {
			throw new NoteException(Messages.getString("HaoqingbaoController.notEnough"));
		}
		// 好友红包，检查好友数量
		if (type == XsgHaoqingbaoManager.RedPacketType_Friend) {
			HaoqingbaoFriendT friendT = manager.getHaoqingbaoFriendT(range);
			if (getFriendsNum() < friendT.minNum) {
				throw new NoteException(TextUtil.format(Messages.getString("HaoqingbaoController.friendsNumLimit"),
						friendT.minNum));
			}
		}
		// 好友红包是否有满足条件的好友
		if (type == XsgHaoqingbaoManager.RedPacketType_Friend && !canSendFriendRedPacket(minFriendPoint, range)) {
			throw new NoteException(Messages.getString("HaoqingbaoController.noFriends"));
		}
		String factionId = roleRt.getFactionControler().getFactionId();
		// 工会红包要检查是否已经加入工会
		if (type == XsgHaoqingbaoManager.RedPacketType_Faction && TextUtil.isBlank(factionId)) {
			throw new NoteException(Messages.getString("HaoqingbaoController.notInFaction"));
		}
		// 检查参数设置是否正确
		// 检查留言字数
		if (msg.length() > cfg.maxWords) {
			throw new NoteException(TextUtil.format(Messages.getString("HaoqingbaoController.maxWordsLimit"),
					cfg.maxWords));
		}
		// 每个红包最少不能少于x元宝
		if ((totalYuanbaoNum / (double) packetNum) < cfg.minNum) {
			throw new NoteException(TextUtil.format(Messages.getString("HaoqingbaoController.minYuanbao"), cfg.minNum));
		}
		// 生成红包
		String id = GlobalDataManager.getInstance().generatePrimaryKey();
		Date current = Calendar.getInstance().getTime();
		RoleHaoqingbaoItem redPacketItem = new RoleHaoqingbaoItem(id, roleRt.getRoleId(), type, range, minLevel,
				minVipLevel, minFriendPoint, totalYuanbaoNum, packetNum, 0, msg, factionId, current, null, 0);
		List<HaoqingbaoRedPacket> redPackets = generateRedPacket(cfg, redPacketItem, totalYuanbaoNum, packetNum);
		if (redPackets == null || redPackets.size() <= 0) {
			log.warn(TextUtil.format("主公,红包发送失败,请调整发送金额: {0}:{1}", totalYuanbaoNum, packetNum));
			throw new NoteException(Messages.getString("HaoqingbaoController.reChange"));
		}

		// 扣除豪情宝余额
		addYuanbaoNum(-totalYuanbaoNum);
		String desc = "";
		switch (type) {
		case XsgHaoqingbaoManager.RedPacketType_Faction:
			desc = Messages.getString("HaoqingbaoController.factionRedPacket");
			break;
		case XsgHaoqingbaoManager.RedPacketType_Friend:
			desc = Messages.getString("HaoqingbaoController.friendRedPacket");
			break;
		case XsgHaoqingbaoManager.RedPacketType_All:
			desc = Messages.getString("HaoqingbaoController.allRedPacket");
			break;
		}
		// 增加豪情宝操作日志
		addHaoqingbaoRecord(-totalYuanbaoNum, desc);
		// 红包入库
		manager.addHaoqingbaoItem(redPacketItem, redPackets);
		// 更新累计总金额
		haoqingbao.setTotalSendSum(haoqingbao.getTotalSendSum() + totalYuanbaoNum);
		// 更新排名
		manager.updateSendRankList(roleRt.getRoleId(), haoqingbao.getTotalSendSum());
		// 给相关收红包的玩家发送消息
		sendRedPacketMsg(id, totalYuanbaoNum, type, minLevel, minVipLevel, range, minFriendPoint, msg);
		// 发送跑马灯消息
		if (totalYuanbaoNum >= cfg.noticeLimit) {
			XsgChatManager.getInstance().sendAnnouncement(
					manager.parseRedPacketMessage(roleRt, id, totalYuanbaoNum, type));
		}
		// 开启倒计时
		manager.startNextRedPacketItem();

		sendRedPacket.onSendRedPacket(type, totalYuanbaoNum, packetNum, id, beforeYuanbaoNum, getTotalYuanbaoNum());
	}

	/**
	 * 给玩家发红包消息
	 * */
	private void sendRedPacketMsg(String redPacketItemId, int totalYuanbao, int type, int minLevel, int minVipLevel,
			int range, int minFriendPoint, String msg) throws NoteException {
		switch (type) {
		case XsgHaoqingbaoManager.RedPacketType_Faction: // 工会
			sendFactionRedPacketMsg(redPacketItemId, totalYuanbao, minLevel, minVipLevel, range, minFriendPoint, msg,
					type);
			break;
		case XsgHaoqingbaoManager.RedPacketType_Friend: // 好友
			sendFriendRedPacketMsg(redPacketItemId, totalYuanbao, minLevel, minVipLevel, range, minFriendPoint, msg,
					type);
			break;
		case XsgHaoqingbaoManager.RedPacketType_All: // 全服
			sendAllRedPacketMsg(redPacketItemId, totalYuanbao, minLevel, minVipLevel, range, minFriendPoint, msg, type);
			break;
		}
	}

	private TextMessage makeTextMessage(ChatChannel channel, String content) {
		ChatRole cr = new ChatRole();
		cr.id = roleRt.getRoleId();
		cr.name = roleRt.getName();
		cr.level = (short) roleRt.getLevel();
		cr.vip = roleRt.getVipLevel();
		cr.icon = roleRt.getHeadImage();
		cr.chatTime = DateUtil.toString(System.currentTimeMillis(), "HH:mm");
		cr.OfficalRankId = roleRt.getOfficalRankId();
		return new TextMessage(channel, null, cr, 1, content, 0, 0);
	}

	private void wrapTextMessageWithTargetRole(TextMessage tm, IRole target) {
		if (target != null && tm.cRole != null) {
			ChatRole cr = tm.cRole;
			cr.targetId = target.getRoleId();
			cr.targetName = target.getName();
			cr.targetLevel = (short) target.getLevel();
			cr.targetVip = target.getVipLevel();
			cr.targetIcon = target.getHeadImage();
			cr.targetOfficalRankId = target.getOfficalRankId();
		}
	}

	/**
	 * 工会红包消息, 发送工会离线消息
	 * 
	 * @throws NoGroupException
	 * @throws NoFactionException
	 * @throws NoteException
	 * */
	private void sendFactionRedPacketMsg(final String redPacketItemId, final int totalYuanbao, final int minLevel,
			final int minVipLevel, final int range, final int minFriendPoint, final String msg, final int type)
			throws NoteException {
		IFaction faction = XsgFactionManager.getInstance().findFaction(roleRt.getFactionControler().getFactionId());
		final List<String> recvIdList = new ArrayList<String>();
		if (faction != null) {
			Set<FactionMember> members = faction.getAllMember();
			if (members != null && members.size() > 0) {
				for (FactionMember m : members) {
					if (range == 1) { // 过滤会长和长老
						if (m.getDutyId() < Const.Faction.DUTY_ELDER) {
							continue;
						}
					}
					recvIdList.add(m.getRoleId());
				}
			}
		} else {
			log.error(TextUtil.format("can not find faction by id {0} role id is {1}", roleRt.getFactionControler()
					.getFactionId(), roleRt.getRoleId()));
		}
		if (recvIdList.size() > 0) {
			XsgRoleManager.getInstance().loadRoleAsync(recvIdList, new Runnable() {
				@Override
				public void run() {
					XsgRoleManager manager = XsgRoleManager.getInstance();
					String content = XsgHaoqingbaoManager.getInstance().parseRedPacketChatMessage(roleRt,
							redPacketItemId, totalYuanbao, type,
							roleRt.getChatControler().selSet(ChatChannel.Faction).get(1), msg);
					final TextMessage txtMsg = makeTextMessage(ChatChannel.Faction, content);
					txtMsg.type = XsgHaoqingbaoManager.getInstance().getChatMsgType(type);
					IChatControler controler = roleRt.getChatControler();
					boolean sendFaction = false;
					for (String id : recvIdList) {
						IRole target = manager.findRoleById(id);
						if (target != null) {
							if (target.getLevel() >= minLevel && target.getVipLevel() >= minVipLevel) {
								// range = 0 表示仅发给在线好友
								if (range == 0 && !target.isOnline()) {
									continue;
								}
								wrapTextMessageWithTargetRole(txtMsg, target);
								try {
									if (!sendFaction) {
										sendFaction = true;
										controler.speak(txtMsg);
									}
									// 不在线的人离线发送
									if (!target.isOnline()) {
										controler.speakToOffline(null, target.getName(), txtMsg, false);
									}
								} catch (NoteException e) {
									log.error("公会红包消息发送失败", e);
								} catch (NoFactionException e) {
									log.error("公会红包消息发送失败", e);
								} catch (NoGroupException e) {
									log.error("公会红包消息发送失败", e);
								}
							}
						}
					}
				}
			});
		}
	}

	/**
	 * 好友红包消息
	 * */
	private void sendFriendRedPacketMsg(final String redPacketItemId, final int totalYuanbao, final int minLevel,
			final int minVipLevel, final int range, final int minFriendPoint, final String msg, final int type) {
		String content = XsgHaoqingbaoManager.getInstance().parseRedPacketChatMessage(roleRt, redPacketItemId,
				totalYuanbao, type, roleRt.getChatControler().selSet(ChatChannel.Private).get(1), msg);
		final TextMessage txtMsg = makeTextMessage(ChatChannel.Private, content);
		txtMsg.type = XsgHaoqingbaoManager.getInstance().getChatMsgType(type);
		List<RoleSns> sns = roleRt.getSnsController().getRoleSns(SNSType.FRIEND);
		final List<String> recvList = new ArrayList<String>();
		for (RoleSns rs : sns) {
			if (rs.getFriendPoint() >= minFriendPoint) {
				recvList.add(rs.getTargetRoleId());
			}
		}
		if (recvList.size() > 0) {
			XsgRoleManager.getInstance().loadRoleAsync(recvList, new Runnable() {
				@Override
				public void run() {
					XsgRoleManager manager = XsgRoleManager.getInstance();
					IChatControler controler = roleRt.getChatControler();
					for (String id : recvList) {
						IRole target = manager.findRoleById(id);
						if (target != null) {
							if (target.getLevel() >= minLevel && target.getVipLevel() >= minVipLevel) {
								// range = 0 表示仅发给在线好友
								if ((range == 0 && !target.isOnline()) || target.getRoleId().equals(roleRt.getRoleId())) {
									continue;
								}
								wrapTextMessageWithTargetRole(txtMsg, target);
								try {
									controler.speakTo(null, target, txtMsg);
								} catch (NoteException e) {
									log.error("好友红包消息发送失败", e);
								}
							}
						}
					}
				}
			});
		}
	}

	/**
	 * 全服红包消息, 发送全服消息
	 * 
	 * @throws NoteException
	 * */
	private void sendAllRedPacketMsg(String redPacketItemId, int totalYuanbao, int minLevel, int minVipLevel,
			int range, int minFriendPoint, String msg, final int type) throws NoteException {
		try {
			String content = XsgHaoqingbaoManager.getInstance().parseRedPacketChatMessage(roleRt, redPacketItemId,
					totalYuanbao, type, roleRt.getChatControler().selSet(ChatChannel.World).get(1), msg);
			TextMessage txtMsg = makeTextMessage(ChatChannel.World, content);
			txtMsg.type = XsgHaoqingbaoManager.getInstance().getChatMsgType(type);
			roleRt.getChatControler().speak(txtMsg);
		} catch (NoFactionException e) {
			log.error("全服红包消息发送失败", e);
		} catch (NoGroupException e) {
			log.error("全服红包消息发送失败", e);
		}
	}

	@Override
	public int getTotalYuanbaoNum() {
		return haoqingbao.getYuanbaoNum();
	}

	// @Override
	public int addYuanbaoNum(int num) {
		int value = haoqingbao.getYuanbaoNum() + num;
		haoqingbao.setYuanbaoNum(value);
		saveToRoleDB();
		return value;
	}

	/**
	 * 是否已经抢过
	 * */
	private boolean hasRecved(XsgHaoqingbaoManager manager, String itemId) {
		HaoqingbaoRedPacket packet = manager.getHaoqingbaoRedPacket(itemId, roleRt.getRoleId());
		return packet != null;
	}

	@Override
	public RecvRedPacketResultView recvRedPacket(String packetId) throws NoteException {
		if (TextUtil.isBlank(packetId)) {
			throw new NoteException(Messages.getString("HaoqingbaoController.redpacketNotExist"));
		}
		XsgHaoqingbaoManager manager = XsgHaoqingbaoManager.getInstance();
		// 判断是否在红包的派发范围
		final RoleHaoqingbaoItem haoqingbaoItem = manager.getRoleHaoqingbaoItem(packetId);
		if (haoqingbaoItem == null) {
			throw new NoteException(Messages.getString("HaoqingbaoController.redpacketNotExist"));
		}
		clearStatus();
		// 今天已经抢的金额
		final HaoqingbaoCfgT cfg = manager.getHaoqingbaoCfgT();
		if (haoqingbao.getReceiveRedPacketNum() >= cfg.maxAcceptNum) {
			return new RecvRedPacketResultView(-3, 0); // 抢红包次数达到上限
		}
		// 自己不能抢自己发的好友红包
		if (haoqingbaoItem.getType() == XsgHaoqingbaoManager.RedPacketType_Friend
				&& roleRt.getRoleId().equals(haoqingbaoItem.getRoleId())) {
			throw new NoteException(Messages.getString("HaoqingbaoController.canNotRecvSelf"));
		}
		// 等级, vip等级
		if (haoqingbaoItem.getMinLevel() > roleRt.getLevel() || haoqingbaoItem.getMinVipLevel() > roleRt.getVipLevel()) {
			throw new NoteException(Messages.getString("HaoqingbaoController.cannotRecv"));
		}
		// 友情点数
		if (haoqingbaoItem.getType() == XsgHaoqingbaoManager.RedPacketType_Friend
				&& haoqingbaoItem.getMinFriendPoint() > 0
				&& haoqingbaoItem.getMinFriendPoint() > roleRt.getSnsController().getFriendPoint(
						haoqingbaoItem.getRoleId())) {
			throw new NoteException(Messages.getString("HaoqingbaoController.cannotRecv"));
		}
		// 工会红包, 检查是否在同一工会
		if (haoqingbaoItem.getType() == XsgHaoqingbaoManager.RedPacketType_Faction
				&& !haoqingbaoItem.getFactionId().equals(roleRt.getFactionControler().getFactionId())) {
			throw new NoteException(Messages.getString("HaoqingbaoController.cannotRecv"));
		}
		// 工会红包，长老限制
		if (haoqingbaoItem.getType() == XsgHaoqingbaoManager.RedPacketType_Faction
				&& haoqingbaoItem.getRange() == 1
				&& roleRt.getFactionControler().getMyFaction().getMemberByRoleId(roleRt.getRoleId()).getDutyId() < Const.Faction.DUTY_ELDER) {
			throw new NoteException(Messages.getString("HaoqingbaoController.cannotRecv"));
		}
		// 红包已经结束
		if (haoqingbaoItem.getFinished() == 1) {
			return new RecvRedPacketResultView(-2, 0);
		}
		// 是否已经抢过
		if (hasRecved(manager, packetId)) {
			return new RecvRedPacketResultView(-1, 0);
		}
		List<HaoqingbaoRedPacket> redPackets = manager.getHaoqingbaoRedPackets(packetId, false, 1);
		if (redPackets == null || redPackets.size() <= 0) {
			// 红包已抢完
			return new RecvRedPacketResultView(0, 0);
		}
		// 抢到了红包
		HaoqingbaoRedPacket redPacket = redPackets.get(0);
		int money = redPacket.getNum();
		// 加入豪情宝账户
		addYuanbaoNum(money);
		addHaoqingbaoRecord(money, Messages.getString("HaoqingbaoController.recvRedPacket"));
		Date current = Calendar.getInstance().getTime();
		// 加入抢红包的记录
		roleDB.getRedPacketRecords().add(
				new RoleHaoqingbaoRedPacketRecord(roleDB, packetId, redPacket.getSenderId(), money, 0, current));
		// 更新最后抢到的时间
		haoqingbaoItem.setReceivedNum(haoqingbaoItem.getReceivedNum() + 1);
		haoqingbaoItem.setLastRecvTime(current);
		// 设置抢到红包者的信息
		redPacket.setReceiverId(roleRt.getRoleId());
		redPacket.setReceiveDate(current);
		// 累计今日抢的金额
		haoqingbao.setReceiveRedPacketNum(haoqingbao.getReceiveRedPacketNum() + money);
		haoqingbao.setLastReceiveTime(current);
		// 累计抢到的红包金额
		haoqingbao.setTotalRecvCount(haoqingbao.getTotalRecvCount() + 1);
		haoqingbao.setTotalRecvNum(haoqingbao.getTotalRecvNum() + money);
		// 更新排名
		manager.updateRecvRankList(roleRt.getRoleId(), haoqingbao.getTotalRecvNum());
		// 红包已经被抢完，统计运气王
		manager.setupLuckyStar(haoqingbaoItem);
		// 如果是好友红包，增加友情点
		if (haoqingbaoItem.getType() == XsgHaoqingbaoManager.RedPacketType_Friend) {
			roleRt.getSnsController().addFriendPoint(haoqingbaoItem.getRoleId(), cfg.addFriendPointNum);
			XsgRoleManager.getInstance().loadRoleByIdAsync(haoqingbaoItem.getRoleId(), new Runnable() {
				@Override
				public void run() {
					IRole target = XsgRoleManager.getInstance().findRoleById(haoqingbaoItem.getRoleId());
					if (target != null) {
						target.getSnsController().addFriendPoint(roleRt.getRoleId(), cfg.addFriendPointNum);
					}
				}
			}, new Runnable() {
				@Override
				public void run() {

				}
			});
		}
		// 更新缓存记录
		manager.updateHaoqingbaoItem(haoqingbaoItem, redPackets);
		saveToRoleDB();

		recvRedPacket.onRecvRedPacket(haoqingbaoItem.getType(), haoqingbaoItem.getRoleId(), redPacket.getNum(),
				packetId, roleRt.getRoleId(), getTotalYuanbaoNum(), redPackets.size() - 1);

		return new RecvRedPacketResultView(money,
				haoqingbaoItem.getType() == XsgHaoqingbaoManager.RedPacketType_Friend ? cfg.addFriendPointNum : 0);
	}

	/**
	 * 增加豪情宝操作日志记录
	 * 
	 * @param num
	 *            数量
	 * @param desc
	 *            描述
	 * */
	private void addHaoqingbaoRecord(int num, String desc) {
		String id = GlobalDataManager.getInstance().generatePrimaryKey();
		Date current = Calendar.getInstance().getTime();
		RoleHaoqingbaoRecord record = new RoleHaoqingbaoRecord(id, roleDB, num, desc, current);
		roleDB.getHaoqingbaoRecords().add(record);
		saveToRoleDB();
	}

	@Override
	public void checkout(int num) throws NoteException {
		if (getTotalYuanbaoNum() < num) {
			throw new NoteException(Messages.getString("HaoqingbaoController.notEnough"));
		}
		if (num >= Const.Ten_Thousand * 10) {
			throw new NoteException(Messages.getString("HaoqingbaoController.overturn"));
		}
		// 扣除豪情宝
		addYuanbaoNum(-num);
		// 增加豪情宝记录日志
		addHaoqingbaoRecord(-num, Messages.getString("HaoqingbaoController.checkOut"));
		// 增加元宝账户
		roleRt.getRewardControler().acceptReward(Const.PropertyName.RMBY, num);

		checkout.onCheckout(num);
	}

	@Override
	public MyRedPacketView myRedPacket() throws NoteException {
		List<RoleHaoqingbaoRedPacketRecord> records = roleDB.getRedPacketRecords();
		if (records.size() > 0) {
			List<RedPacketDetailItemView> recordViews = new ArrayList<RedPacketDetailItemView>();
			Collections.sort(records, new Comparator<RoleHaoqingbaoRedPacketRecord>() {
				@Override
				public int compare(RoleHaoqingbaoRedPacketRecord o1, RoleHaoqingbaoRedPacketRecord o2) {
					return Long.valueOf(o2.getReceiveDate().getTime()).compareTo(o1.getReceiveDate().getTime());
				}
			});
			for (RoleHaoqingbaoRedPacketRecord record : records) {
				recordViews.add(new RedPacketDetailItemView(roleRt.getHeadImage(), roleRt.getName(), roleRt.getLevel(),
						roleRt.getVipLevel(), record.getNum(), record.getLuckyStar(), DateUtil.format(record
								.getReceiveDate())));
			}
			return new MyRedPacketView(Long.valueOf(haoqingbao.getTotalRecvNum()).intValue(), Long.valueOf(
					haoqingbao.getTotalRecvCount()).intValue(), haoqingbao.getLuckyStarCount(),
					recordViews.toArray(new RedPacketDetailItemView[0]));
		}
		return new MyRedPacketView(Long.valueOf(haoqingbao.getTotalRecvNum()).intValue(), Long.valueOf(
				haoqingbao.getTotalRecvCount()).intValue(), haoqingbao.getLuckyStarCount(),
				new RedPacketDetailItemView[0]);
	}

	@Override
	public void getRedPacketDetail(String itemId, final AMD_Haoqingbao_getRedPacketDetail __cb) throws NoteException {
		if (TextUtil.isBlank(itemId)) {
			__cb.ice_exception(new NoteException(Messages.getString("HaoqingbaoController.redpacketNotExist")));
			return;
		}
		XsgHaoqingbaoManager manager = XsgHaoqingbaoManager.getInstance();
		final RoleHaoqingbaoItem item = manager.getRoleHaoqingbaoItem(itemId);
		if (item == null) {
			__cb.ice_exception(new NoteException(Messages.getString("HaoqingbaoController.redpacketNotExist")));
			return;
		}
		List<HaoqingbaoRedPacket> packetList = manager.getHaoqingbaoRedPackets(item.getId(), true, -1);
		final Map<String, HaoqingbaoRedPacket> packetMap = new HashMap<String, HaoqingbaoRedPacket>();
		List<String> ids = new ArrayList<String>();
		ids.add(item.getRoleId());
		if (packetList != null && packetList.size() > 0) {
			for (HaoqingbaoRedPacket p : packetList) {
				if (!TextUtil.isBlank(p.getReceiverId())) {
					packetMap.put(p.getReceiverId(), p);
				}
			}
		}
		ids.addAll(packetMap.keySet());
		XsgRoleManager.getInstance().loadRoleAsync(ids, new Runnable() {
			@Override
			public void run() {
				XsgRoleManager manager = XsgRoleManager.getInstance();
				IRole sender = manager.findRoleById(item.getRoleId());
				if (sender == null) {
					__cb.ice_exception(new NoteException(Messages.getString("HaoqingbaoController.getFailure")));
					return;
				}
				int lastNum = item.getRedPacketNum() - item.getReceivedNum();
				int lastTime = -1;
				// 如果已经结束了，计算持续了多长时间
				if (lastNum <= 0) {
					lastTime = (int) (item.getLastRecvTime().getTime() - item.getStartTime().getTime());
				}
				List<RedPacketDetailItemView> detailItemViews = new ArrayList<RedPacketDetailItemView>();
				RedPacketDetailView view = new RedPacketDetailView(sender.getHeadImage(), sender.getName(), sender
						.getLevel(), sender.getVipLevel(), item.getRedPacketNum(), lastNum, lastTime / 1000, item
						.getMsg(), null);
				List<HaoqingbaoRedPacket> packetListSort = new ArrayList<HaoqingbaoRedPacket>(packetMap.values());
				// 按时间倒叙
				Collections.sort(packetListSort, new Comparator<HaoqingbaoRedPacket>() {
					@Override
					public int compare(HaoqingbaoRedPacket o1, HaoqingbaoRedPacket o2) {
						return Long.valueOf(o2.getReceiveDate().getTime()).compareTo(o1.getReceiveDate().getTime());
					}
				});
				for (HaoqingbaoRedPacket packet : packetListSort) {
					IRole target = manager.findRoleById(packet.getReceiverId());
					if (target != null) {
						detailItemViews.add(new RedPacketDetailItemView(target.getHeadImage(), target.getName(), target
								.getLevel(), target.getVipLevel(), packet.getNum(), packet.getLuckyStar(), DateUtil
								.format(packet.getReceiveDate())));
					}
				}
				view.items = detailItemViews.toArray(new RedPacketDetailItemView[0]);
				__cb.ice_response(LuaSerializer.serialize(view));
			}
		});
		return;
	}

	@Override
	public RankView getRankView(int type, final AMD_Haoqingbao_rankList __cb) throws NoteException {
		XsgHaoqingbaoManager manager = XsgHaoqingbaoManager.getInstance();
		String roleId = roleRt.getRoleId();
		List<RankItem> rankList = null;
		RankItem rank = null;
		switch (type) {
		case 1: // 发红包
			rankList = manager.headSendRank(HeadRankNum);
			rank = manager.getSendRankItem(roleId);
			break;
		case 2: // 抢红包
			rankList = manager.headRecvRank(HeadRankNum);
			rank = manager.getRecvRankItem(roleId);
			break;
		}
		final Map<String, RankItem> rankMap = new HashMap<String, XsgHaoqingbaoManager.RankItem>();
		if (rankList != null && rankList.size() > 0) {
			int index = 1;
			for (RankItem ri : rankList) {
				ri.index = index++;
				rankMap.put(ri.id, ri);
			}

			final RankItem finalRank = rank;
			XsgRoleManager.getInstance().loadRoleAsync(new ArrayList<String>(rankMap.keySet()), new Runnable() {
				@Override
				public void run() {
					XsgRoleManager manager = XsgRoleManager.getInstance();
					List<RankItemView> itemViews = new ArrayList<RankItemView>();
					for (RankItem ri : rankMap.values()) {
						IRole target = manager.findRoleById(ri.id);
						if (target != null) {
							itemViews.add(new RankItemView(ri.index, target.getHeadImage(), target.getRoleId(), target
									.getLevel(), target.getVipLevel(), target.getName(), ri.score));
						}
					}
					int currentRank = (finalRank == null ? 0 : finalRank.index);
					long score = (finalRank == null ? 0 : finalRank.score);
					__cb.ice_response(LuaSerializer.serialize(new RankView(currentRank, score, itemViews
							.toArray(new RankItemView[0]))));
				}
			});
		} else {
			int currentRank = (rank == null ? 0 : rank.index);
			long score = (rank == null ? 0 : rank.score);
			__cb.ice_response(LuaSerializer.serialize(new RankView(currentRank, score, new RankItemView[0])));
		}
		return null;
	}

	@Override
	public List<RoleHaoqingbaoRedPacketRecord> getRedPacketRecords() {
		return roleDB.getRedPacketRecords();
	}

	@Override
	public void updateLuckyStar(String itemId, int luckyStar) {
		List<RoleHaoqingbaoRedPacketRecord> records = roleDB.getRedPacketRecords();
		if (records != null) {
			for (RoleHaoqingbaoRedPacketRecord r : records) {
				if (r.getRedPacketId().equals(itemId)) {
					r.setLuckyStar(luckyStar);
					haoqingbao.setLuckyStarCount(haoqingbao.getLuckyStarCount() + (luckyStar == 1 ? 1 : -1));
					break;
				}
			}
		}
	}

	@Override
	public void sendGiveBackMsg(RoleHaoqingbaoItem item, int lastNum) {
		try {
			String content = XsgHaoqingbaoManager.getInstance().parseGiveBackMsg(roleRt, item, lastNum);
			roleRt.getChatControler().sendPrivateSystemMsg(content, roleRt.getRoleId());
		} catch (NoteException e) {
			log.error("全服红包消息发送失败", e);
		}
	}

	private void removeOldRecords() {
		HaoqingbaoCfgT cfg = XsgHaoqingbaoManager.getInstance().getHaoqingbaoCfgT();
		List<RoleHaoqingbaoRedPacketRecord> records = roleDB.getRedPacketRecords();
		if (records != null) {
			List<RoleHaoqingbaoRedPacketRecord> removeList = new ArrayList<RoleHaoqingbaoRedPacketRecord>();
			long checkTime = DateUtil.addDays(Calendar.getInstance(), -cfg.recordSaveTime).getTimeInMillis();
			for (RoleHaoqingbaoRedPacketRecord record : records) {
				if (checkTime > record.getReceiveDate().getTime()) {
					removeList.add(record);
				}
			}
			roleDB.getRedPacketRecords().removeAll(removeList);
		}
		List<RoleHaoqingbaoRecord> haoqingbaoRecords = roleDB.getHaoqingbaoRecords();
		if (haoqingbaoRecords != null) {
			List<RoleHaoqingbaoRecord> removeList = new ArrayList<RoleHaoqingbaoRecord>();
			long checkTime = DateUtil.addDays(Calendar.getInstance(), -cfg.recordSaveTime).getTimeInMillis();
			for (RoleHaoqingbaoRecord record : haoqingbaoRecords) {
				if (checkTime > record.getUpdateTime().getTime()) {
					removeList.add(record);
				}
			}
			roleDB.getHaoqingbaoRecords().removeAll(removeList);
		}
	}

	@Override
	public void updateChargeStatus() {
		lastChargeStatusTime = Calendar.getInstance().getTimeInMillis();
		haoqingbao.setLastChargeStatusTime(lastChargeStatusTime);
		saveToRoleDB();
	}

	@Override
	public boolean shouldAddToHaoqingbao() {
		HaoqingbaoCfgT cfg = XsgHaoqingbaoManager.getInstance().getHaoqingbaoCfgT();
		if (cfg == null) {
			return false;
		}
		long current = System.currentTimeMillis();
		long interval = cfg.chargeStatusTime * 60 * 1000L;
		if ((current - lastChargeStatusTime) > interval) {
			return false;
		}
		return true;
	}

	@Override
	public void acceptCharge(int yuanbaoNum, String desc) {
		addYuanbaoNum(yuanbaoNum);
		// 增加豪情宝记录日志
		addHaoqingbaoRecord(yuanbaoNum, desc);

		charge.onCharge(yuanbaoNum);
	}

	/**
	 * @return 0,正常可以抢;1,已抢完;2,已经抢过了
	 * */
	@Override
	public PreRecvRedPacketResultView preRecvRedPacket(String packetId) throws NoteException {
		if (TextUtil.isBlank(packetId)) {
			throw new NoteException(Messages.getString("HaoqingbaoController.redpacketNotExist"));
		}
		XsgHaoqingbaoManager manager = XsgHaoqingbaoManager.getInstance();
		// 判断是否在红包的派发范围
		final RoleHaoqingbaoItem haoqingbaoItem = manager.getRoleHaoqingbaoItem(packetId);
		if (haoqingbaoItem == null) {
			throw new NoteException(Messages.getString("HaoqingbaoController.redpacketNotExist"));
		}
		clearStatus();
		int isMine = roleRt.getRoleId().equals(haoqingbaoItem.getRoleId()) ? 1 : 0; // 是否是我发的红包
		// 今天已经抢的次数
		final HaoqingbaoCfgT cfg = manager.getHaoqingbaoCfgT();
		if (haoqingbao.getReceiveRedPacketNum() >= cfg.maxAcceptNum) {
			return new PreRecvRedPacketResultView(3, isMine); // 抢红包次数达到上限
		}
		// 自己不能抢自己发的好友红包
		if (haoqingbaoItem.getType() == XsgHaoqingbaoManager.RedPacketType_Friend
				&& roleRt.getRoleId().equals(haoqingbaoItem.getRoleId())) {
			return new PreRecvRedPacketResultView(4, isMine);
		}
		// 等级, vip等级
		if (haoqingbaoItem.getMinLevel() > roleRt.getLevel() || haoqingbaoItem.getMinVipLevel() > roleRt.getVipLevel()) {
			throw new NoteException(Messages.getString("HaoqingbaoController.cannotRecv"));
		}
		// 友情点数,好友红包检查好友点数
		if (haoqingbaoItem.getType() == XsgHaoqingbaoManager.RedPacketType_Friend
				&& haoqingbaoItem.getMinFriendPoint() > 0
				&& haoqingbaoItem.getMinFriendPoint() > roleRt.getSnsController().getFriendPoint(
						haoqingbaoItem.getRoleId())) {
			throw new NoteException(Messages.getString("HaoqingbaoController.cannotRecv"));
		}
		// 工会红包, 检查是否在同一工会
		if (haoqingbaoItem.getType() == XsgHaoqingbaoManager.RedPacketType_Faction
				&& !haoqingbaoItem.getFactionId().equals(roleRt.getFactionControler().getFactionId())) {
			throw new NoteException(Messages.getString("HaoqingbaoController.cannotRecv"));
		}
		// 工会红包，长老限制
		if (haoqingbaoItem.getType() == XsgHaoqingbaoManager.RedPacketType_Faction
				&& haoqingbaoItem.getRange() == 1
				&& roleRt.getFactionControler().getMyFaction().getMemberByRoleId(roleRt.getRoleId()).getDutyId() < Const.Faction.DUTY_ELDER) {
			throw new NoteException(Messages.getString("HaoqingbaoController.cannotRecv"));
		}
		// 红包已经结束
		if (haoqingbaoItem.getFinished() == 1) {
			return new PreRecvRedPacketResultView(1, isMine);
		}
		// 是否已经抢过
		if (hasRecved(manager, packetId)) {
			return new PreRecvRedPacketResultView(2, isMine);
		}
		// 是否已经抢完
		if (haoqingbaoItem.getReceivedNum() >= haoqingbaoItem.getRedPacketNum()) {
			return new PreRecvRedPacketResultView(5, isMine);
		}
		return new PreRecvRedPacketResultView(0, isMine);
	}

	@Override
	public void imluckyStar() {
		luckyStar.onLuckyStar();
	}

	private boolean hasClaimd(String packetId) {
		if (roleDB.getClaimRedPackets() != null) {
			for (RoleClaimRedPacket claim : roleDB.getClaimRedPackets()) {
				if (claim.getRedPacketId().equals(packetId)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 清理索要记录
	 * */
	private void cleanClaimRecords() {
		if (roleDB.getClaimRedPackets() != null) {
			List<RoleClaimRedPacket> removeList = new ArrayList<RoleClaimRedPacket>();
			for (RoleClaimRedPacket claim : roleDB.getClaimRedPackets()) {
				if (!XsgHaoqingbaoManager.getInstance().isRedPacketItemExist(claim.getRedPacketId())) {
					removeList.add(claim);
				}
			}
			roleDB.getClaimRedPackets().removeAll(removeList);
		}
	}

	@Override
	public void claimRedPacket(final String roleId, final String redPacketId) throws NoteException {
		if (TextUtil.isBlank(roleId)) {
			throw new NoteException(Messages.getString("HaoqingbaoController.roleNotExist"));
		}
		if (hasClaimd(redPacketId)) {
			throw new NoteException(Messages.getString("HaoqingbaoController.sendAlready"));
		}
		if (!XsgHaoqingbaoManager.getInstance().isRedPacketItemExist(redPacketId)) {
			throw new NoteException(Messages.getString("HaoqingbaoController.sendAlready"));
		}
		XsgRoleManager.getInstance().loadRoleByIdAsync(roleId, new Runnable() {
			@Override
			public void run() {
				IRole r = XsgRoleManager.getInstance().findRoleById(roleId);
				if (r != null) {
					String content = XsgHaoqingbaoManager.getInstance().parseClaimRedPacketMessage(r);
					TextMessage txtMsg = makeTextMessage(ChatChannel.World, content);
					txtMsg.type = 1; // 普通文字消息
					try {
						if (XsgHaoqingbaoManager.getInstance().getRedPacketClaimNum(redPacketId) < XsgGameParamManager
								.getInstance().getMaxClaimNum()) {
							roleRt.getChatControler().speak(txtMsg);
							XsgHaoqingbaoManager.getInstance().addRedPacketClaimNum(redPacketId, 1);
						}
						// 增加索要记录
						RoleClaimRedPacket rcrr = new RoleClaimRedPacket(GlobalDataManager.getInstance()
								.generatePrimaryKey(), roleDB, redPacketId, Calendar.getInstance().getTime());
						roleDB.getClaimRedPackets().add(rcrr);
					} catch (NoteException e) {
						e.printStackTrace();
					} catch (NoFactionException e) {
						e.printStackTrace();
					} catch (NoGroupException e) {
						e.printStackTrace();
					}
				}
			}
		}, new Runnable() {
			@Override
			public void run() {
			}
		});
	}

	@Override
	public void onGiveBack(String id, int num) {
		giveBack.onGiveBack(id, num, getTotalYuanbaoNum());
	}

}
