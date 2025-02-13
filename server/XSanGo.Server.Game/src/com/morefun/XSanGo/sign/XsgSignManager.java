package com.morefun.XSanGo.sign;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.morefun.XSanGo.script.ExcelParser;

/**
 * 第二版策划配置，忽略配置表里月份字段。第一版配置测试的时候策划觉得每个月配一次很累，就要求只配30天的，每个月奖励都一样。<br/>
 */
public class XsgSignManager {

	Map<Integer, SignT> signT = new HashMap<Integer, SignT>();
	Map<String, TotalSignGiftPacksT> totalSignGiftPacksT = new HashMap<String, TotalSignGiftPacksT>();
	Map<Integer, SignRouletteT> SignRouletteT = new HashMap<Integer, SignRouletteT>();

	Map<Integer, PseudoRandomRange> ranges = new HashMap<Integer, PseudoRandomRange>();
	private static XsgSignManager instance = new XsgSignManager();

	public static XsgSignManager getInstance() {
		return instance;
	}

	private XsgSignManager() {
		loadSignScript();
		loadTotalSignScript();
		loadRouletteScript();
	}

	/** 每日签到礼包 */
	public void loadSignScript() {
		List<SignT> signTlist = ExcelParser.parse(SignT.class);
		signT.clear();
		for (SignT signT : signTlist) {
			this.signT.put(signT.day, signT);
		}
	}

	/** 签到累计礼包 */
	public void loadTotalSignScript() {
		List<TotalSignGiftPacksT> giftList = ExcelParser.parse(TotalSignGiftPacksT.class);
		totalSignGiftPacksT.clear();
		for (TotalSignGiftPacksT t : giftList) {
			totalSignGiftPacksT.put(t.gift, t);
		}
	}

	/** 签到抽奖,包括伪随机脚本 */
	public void loadRouletteScript() {
		PseudoRandomSignTcT tcT = ExcelParser.parse(PseudoRandomSignTcT.class).get(0);

		List<SignRouletteT> rlist = ExcelParser.parse(SignRouletteT.class);
		ranges.clear();
		for (SignRouletteT t : rlist) {
			SignRouletteT.put(t.day, t);
			List<RandomRoulette> list = new ArrayList<RandomRoulette>();
			list.add(new RandomRoulette(t.prob1, t.item1, t.num1, t.gold_need, t.broadcast1 == 1));
			list.add(new RandomRoulette(t.prob2, t.item2, t.num2, t.gold_need, t.broadcast2 == 1));
			list.add(new RandomRoulette(t.prob3, t.item3, t.num3, t.gold_need, t.broadcast3 == 1));
			list.add(new RandomRoulette(t.prob4, t.item4, t.num4, t.gold_need, t.broadcast4 == 1));
			list.add(new RandomRoulette(t.prob5, t.item5, t.num5, t.gold_need, t.broadcast5 == 1));
			list.add(new RandomRoulette(t.prob6, t.item6, t.num6, t.gold_need, t.broadcast6 == 1));
			list.add(new RandomRoulette(t.prob7, t.item7, t.num7, t.gold_need, t.broadcast7 == 1));
			list.add(new RandomRoulette(t.prob8, t.item8, t.num8, t.gold_need, t.broadcast8 == 1));

			RandomRoulette finalItem = new RandomRoulette(1, t.finalTcItem, getFinalItemCount(list, t.finalTcItem),
					t.gold_need, getFinalItemBroadcast(list, t.finalTcItem));
			for (RandomRoulette rr : list) {
				if (rr.templateId.equals(t.finalTcItem)) {
					finalItem = rr;
				}
			}

			ranges.put(t.day, new PseudoRandomRange(tcT, list, finalItem));

		}
	}

	private int getFinalItemCount(List<RandomRoulette> list, String item) {
		for (RandomRoulette rr : list) {
			if (item.equals(rr.templateId)) {
				return rr.count;
			}
		}
		return 1;
	}

	private boolean getFinalItemBroadcast(List<RandomRoulette> list, String item) {
		for (RandomRoulette rr : list) {
			if (item.equals(rr.templateId)) {
				return rr.broadcast;
			}
		}
		return true;
	}

	SignT getSignT(int day) {
		return signT.get(day);
	}

	SignRouletteT todaySignRouletteT() {
		return SignRouletteT.get(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
	}

	SignT[] allSignTItems() {
		List<SignT> sortList = new ArrayList<SignT>(signT.values());
		Collections.sort(sortList, new Comparator<SignT>() {
			@Override
			public int compare(SignT o1, SignT o2) {
				return o1.day - o2.day;
			}
		});
		List<SignT> result = sortList.subList(0, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
		return result.toArray(new SignT[result.size()]);
	}

	Map<String, TotalSignGiftPacksT> getSignGiftPacksT() {
		return totalSignGiftPacksT;
	}

	/**
	 * 是否存在指定的累计签到次数奖励
	 * 
	 * @param totalSignGiftTimes
	 * @return
	 */
	public TotalSignGiftPacksT getSignGiftPacksTBySignCount(int totalSignGiftTimes) {
		for (TotalSignGiftPacksT item : this.totalSignGiftPacksT.values()) {
			if (item.timeLimit == totalSignGiftTimes) {
				return item;
			}
		}
		return null;
	}
}
