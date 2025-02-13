package com.morefun.XSanGo.makewine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.XSanGo.Protocol.IntString;
import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.DBThreads;
import com.morefun.XSanGo.LogicThread;
import com.morefun.XSanGo.Messages;
import com.morefun.XSanGo.ServerLancher;
import com.morefun.XSanGo.activity.ActivityT;
import com.morefun.XSanGo.activity.XsgActivityManage;
import com.morefun.XSanGo.activity.XsgActivityManage.ActivityTemplate;
import com.morefun.XSanGo.common.MailTemplate;
import com.morefun.XSanGo.db.game.MakeWineDao;
import com.morefun.XSanGo.db.game.MakeWineShareRecordDao;
import com.morefun.XSanGo.db.game.RoleMakeWine;
import com.morefun.XSanGo.db.game.RoleMakeWineShareRecord;
import com.morefun.XSanGo.mail.XsgMailManager;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.DateUtil;
import com.morefun.XSanGo.util.DelayedTask;
import com.morefun.XSanGo.util.LRULinkedHashMap;

/**
 * 酿酒
 * @author zhuzhi.yang
 *
 */
public class XsgMakeWineManager {
	
	private final Log log = LogFactory.getLog(getClass());

	public static XsgMakeWineManager instance;
	
	private MakeWineDao makeWineDao;
	
	private MakeWineShareRecordDao shareRecordDao;
	
	/**酿酒基础配置*/
	public MakeWineConfigT makeWineConfig;
	
	/**酿酒和分享配置*/
	public Map<Integer, MakeWineShareT> map_make_share = null;
	
	/**酿酒积分目标*/
	public List<MakeWineScoreTargetT> list_score_target = null;
	
	/**材料领取*/
	public List<MaterialReceiveT> list_material_receive = null;
	
	/**积分排名奖励*/
	public List<MakeWineScoreRankAwardT> list_score_rank_award = null;
	
	/**积分兑换配置*/
	public List<MakeWineScoreExchangeT> list_score_exchange = null;
	
	/**排行榜中的玩家数据 */
	private Map<String, RoleMakeWine> mapRoleInfos;
	
	/**排行榜显示条数*/
	public final int RANK_LIMIT = 50;
	
	/**每种酒已经酿造的次数*/
	public final int[] DEFAULT_COMPOSED_COUNT = new int[]{0, 0, 0, 0};
	
	/**酿造日志*/
	public LRULinkedHashMap<String, List<String>> map_make_log = new LRULinkedHashMap<String, List<String>>(1000);
	
	/**领取日志*/
	public LRULinkedHashMap<String, List<String>> map_receive_log = new LRULinkedHashMap<String, List<String>>(1000);
	
	/**兑换日志*/
	public LRULinkedHashMap<String, List<String>> map_exchange_log = new LRULinkedHashMap<String, List<String>>(1000);
	
	private XsgMakeWineManager(){
		loadConfig();
		
		makeWineDao = MakeWineDao.getFromApplicationContext(ServerLancher.getAc());
		shareRecordDao = MakeWineShareRecordDao.getFromApplicationContext(ServerLancher.getAc());
		
		initRankPlayerList();
		grantScoreRankAward();
		clearData();
	}
	
	public static XsgMakeWineManager getInstance() {
		if(instance == null){
			instance = new XsgMakeWineManager();
		}
		return instance;
	}
	

	public Map<String, RoleMakeWine> getMapRoleInfos() {
		return mapRoleInfos;
	}

	public void setMapRoleInfos(Map<String, RoleMakeWine> mapRoleInfos) {
		this.mapRoleInfos = mapRoleInfos;
	}
	
	
	/**
	 * 加载配置
	 */
	public void loadConfig(){
		makeWineConfig = ExcelParser.parse(MakeWineConfigT.class).get(0);
		map_make_share = new LinkedHashMap<Integer, MakeWineShareT>();
		List<MakeWineShareT> list_make_share = ExcelParser.parse(MakeWineShareT.class);
		for(MakeWineShareT ms : list_make_share){
			// 解析需求材料
			String[] needItemIdNums = ms.needItem.split(",");
			for(String item : needItemIdNums){
				String[] idNum = item.split(":");
				ms.list_needItem.add(new IntString(Integer.valueOf(idNum[1]), idNum[0]));
			}
			
//			// 解析分享获得的道具
//			String[] shareItems = ms.shareItem.split(",");
//			for(String item : shareItems) {
//				String[] idNum = item.split(":");
//				ms.list_share_item.add(new IntString(Integer.valueOf(idNum[1]), idNum[0]));
//			}
			map_make_share.put(ms.id, ms);
		}
		list_score_target = ExcelParser.parse(MakeWineScoreTargetT.class);
		for(MakeWineScoreTargetT mst : list_score_target){
			String[] idNum = mst.itemID.split(":");
			mst.itemID = idNum[0];
			mst.itemNum = Integer.valueOf(idNum[1]);
		}
		list_material_receive = ExcelParser.parse(MaterialReceiveT.class);
		for(MaterialReceiveT mr : list_material_receive) {
			mr.receiveTime = DateUtil.parseDate("HH:mm:ss", mr.setFodderTime);
		}
//		// 按照时间倒叙排列,方便遍历
//		Collections.sort(list_material_receive, new Comparator<MaterialReceiveT>() {
//
//			@Override
//			public int compare(MaterialReceiveT o1, MaterialReceiveT o2) {
//				if(o1.receiveTime.getTime() > o2.receiveTime.getTime()) {
//					return -1;
//				} else if(o1.receiveTime.getTime() < o2.receiveTime.getTime()) {
//					return 1;
//				}
//				return 0;
//			}
//			
//		});
		
		list_score_rank_award = ExcelParser.parse(MakeWineScoreRankAwardT.class);
		for(MakeWineScoreRankAwardT sra : list_score_rank_award) {
			String[] items = sra.items.split(",");
			for (int i = 0; i < items.length; i++) {
				String[] idNum = items[i].split(":");
				sra.item_array.add(new IntString(Integer.valueOf(idNum[1]), idNum[0]));
			}
		}
		
		list_score_exchange = ExcelParser.parse(MakeWineScoreExchangeT.class);
	}
	
	/**
	 * 加载排行榜中的玩家酿酒数据
	 */
	private void initRankPlayerList() {
		if(getMapRoleInfos() == null) {
			setMapRoleInfos(new LinkedHashMap<String, RoleMakeWine>());
		}
		
		List<RoleMakeWine> list = makeWineDao.findRankData();
		for(RoleMakeWine mw : list) {
			getMapRoleInfos().put(mw.getRoleId(), mw);
		}
	}
	
	/**
	 * 调整积分排行榜排名
	 */
	private List<Map.Entry<String, RoleMakeWine>> sortRoleInfos() {
		List<Map.Entry<String, RoleMakeWine>> keys =
			    new ArrayList<Map.Entry<String, RoleMakeWine>>(getMapRoleInfos().entrySet());
		// 按照总分数排行
		Collections.sort(keys, new Comparator<Map.Entry<String, RoleMakeWine>>() {

			@Override
			public int compare(Entry<String, RoleMakeWine> o1, Entry<String, RoleMakeWine> o2) {
				int score1 = o1.getValue().getComposeScore() + o1.getValue().getShareScore();
				int score2 = o2.getValue().getComposeScore() + o2.getValue().getShareScore(); 
				if(score1 > score2) {
					return -1;
				} else if(score1 < score2) {
					return 1;
				}
				return 0;
			}
			
		});
		return keys;
	}
	
	/**
	 * 异步保存到数据库
	 * 
	 * @param 酿酒对象
	 */
	public void saveRoleMakeWine2DB(final RoleMakeWine roleMakeWine) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				makeWineDao.save(roleMakeWine);
			}
		});
	}
	
	/**
	 * 异步保存到数据库
	 * 
	 * @param 酿酒分享记录
	 */
	public void saveShareRecord2DB(final RoleMakeWineShareRecord shareRecord) {
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				shareRecordDao.save(shareRecord);
			}
		});
	}
	
	/**
	 * 获取排行榜数据
	 * @return
	 */
	public List<RoleMakeWine> getRankList() {
		List<Map.Entry<String, RoleMakeWine>> keyValues = sortRoleInfos();
		List<RoleMakeWine> list_rank = new ArrayList<RoleMakeWine>();
		for(Map.Entry<String, RoleMakeWine> kv : keyValues) {
			RoleMakeWine rmw = kv.getValue();
			if(list_rank.size() >= RANK_LIMIT) {
				break;
			}
			if(rmw.getComposeScore() + rmw.getShareScore() > makeWineConfig.mixScore) {
				list_rank.add(rmw);
			}
		}
		return list_rank;
	}
	
	/**
	 * 获取角色积分排名
	 * @param roleId
	 * @return
	 */
	public int getRoleRank(String roleId) {
		int rank = 0;
		List<Map.Entry<String, RoleMakeWine>> keyValues = sortRoleInfos();
		for(Map.Entry<String, RoleMakeWine> kv : keyValues) {
			RoleMakeWine rmw = kv.getValue();

			rank += 1;
			if(rmw.getRoleId().equalsIgnoreCase(roleId)) {
				return rank;
			}
		}
		return getMapRoleInfos().size();
	}
	
	/**
	 * 发放积分排名奖励 
	 */
	private void grantScoreRankAward()	{
		long interval = makeWineConfig.endTime.getTime() - System.currentTimeMillis();
		if(interval < 0) {
			return;
		}
		LogicThread.scheduleTask(new DelayedTask(interval, 0) {
			
			@Override
			public void run() {
				try {
					int rank = 0;
					List<Map.Entry<String, RoleMakeWine>> keyValues = sortRoleInfos();
					for(Map.Entry<String, RoleMakeWine> kv : keyValues) {
						RoleMakeWine makeWine = kv.getValue();
						rank += 1;
						// 超出发放范围
						if(rank > list_score_rank_award.get(list_score_rank_award.size() - 1).stopRank) {
							break;
						}
						
						// 不满足发奖积分
						MakeWineScoreRankAwardT scoreRankAwardT = getAwardByRank(rank, makeWine.getComposeScore() + makeWine.getShareScore());
//						if(makeWine.getComposeScore() + makeWine.getShareScore() < scoreRankAwardT.needScore) {
						if(scoreRankAwardT == null) {
							continue;
						}
						
						Map<String, Integer> awardMap = new HashMap<String, Integer>();
						for(IntString idNum : scoreRankAwardT.item_array) {
							awardMap.put(idNum.strValue, idNum.intValue);
						}
						
						// 邮件内容中的参数
						Map<String, String> replaceMap = new HashMap<String, String>();
						replaceMap.put("$m", String.valueOf(rank));
						// 发送邮件
						XsgMailManager.getInstance().sendTemplate(makeWine.getRoleId(), MailTemplate.MakeWineScoreRank,
								awardMap, replaceMap);
						
					}
				}catch(Exception e){
					log.error(e);
				}
			}
		});
	}
	
	/**
	 * 根据排名获取对应的奖励
	 * @param rank
	 * @return
	 */
	private MakeWineScoreRankAwardT getAwardByRank(int rank, int score) {
		for(MakeWineScoreRankAwardT sra : list_score_rank_award) {
			if(rank <= sra.stopRank
					&& score >= sra.needScore) {
				return sra;
			}
		}
		return null;
	}
	
	/**
	 * 活动结束清除数据
	 */
	private void clearData(){
		long delayed = makeWineConfig.hideTime.getTime() - System.currentTimeMillis();
		if(delayed < 0) {
			return;
		}
		LogicThread.scheduleTask(new DelayedTask(delayed, 0) {

			@Override
			public void run() {
				DBThreads.execute(new Runnable() {
					
					@Override
					public void run() {
						// 清空分享记录
						shareRecordDao.delete();
						
						// 重置积分、领取时间等参数
						makeWineDao.clearData();
					}
				});
			}
			
		});
	}
	
	/**
	 * 检测开放时间和等级
	 */
	public void checkTimeLevel(int level) throws NoteException {
		// 判断是否在活动期间内
		Calendar now = Calendar.getInstance();
		if(now.getTimeInMillis() < makeWineConfig.startTime.getTime()
				|| now.getTimeInMillis() > makeWineConfig.endTime.getTime()) {
			throw new NoteException(Messages.getString("ShootControler.0")); // ApiController.1,LotteryControler.0
		}
		// 判断等级条件
		checkLevel(level);
	}
	
	/**
	 * 检测开放等级
	 * @param level
	 */
	public void checkLevel(int level) throws NoteException {
		int openLevel = makeWineConfig.openLevel;
		if(level < openLevel){
			throw new NoteException(Messages.getString("LadderControler.16") + openLevel + Messages.getString("LadderControler.17"));
		}
	}
	
	/**
	 * 主城是否显示酿酒图标
	 * @param level
	 * @return
	 */
	public boolean showIcon(int level) {
		Calendar now = Calendar.getInstance();
		
		// 活动未开放
		if(now.getTimeInMillis() < makeWineConfig.showTime.getTime()
				|| now.getTimeInMillis() >= makeWineConfig.hideTime.getTime()){
			return false;
		}
		
		// 等级不够
		if(level < makeWineConfig.showLevel) {
			return false;
		}
		
		return true;
	}
}
