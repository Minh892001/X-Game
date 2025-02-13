/**
 * 
 */
package com.morefun.XSanGo.reward;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.XSanGo.Protocol.ItemType;
import com.XSanGo.Protocol.ItemView;
import com.XSanGo.Protocol.Property;
import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.hero.HeroLevelupExpT;
import com.morefun.XSanGo.hero.IHero;
import com.morefun.XSanGo.hero.XsgHeroManager;
import com.morefun.XSanGo.item.AbsItemT;
import com.morefun.XSanGo.item.XsgItemManager;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.CollectionUtil;
import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

public class XsgRewardManager {

	public static final float Hero_Exp_Rate = 1.0f;
	private static XsgRewardManager instance;

	public static XsgRewardManager getInstance() {
		if (instance == null) {
			instance = new XsgRewardManager();
		}
		return instance;
	}

	public static void main(String[] args) {
		XsgRewardManager.getInstance();
		XsgItemManager.getInstance();
		PrintStream out = System.out;
		out.println("欢迎使用X三国TC测试工具！");

		while (true) {
			out.print("主公，请输入您要执行的TC代码和次数，以空格分开（次数可选，默认为1W）注意大小写：");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			String s;
			try {
				s = in.readLine();

				String[] array = s.split(" ");
				String tc = array[0];
				if (!XsgRewardManager.getInstance().rewardTMap.containsKey(tc)) {
					out.println("主公，您输入的TC不存在，请重新输入哈哈哈！");
					continue;
				}
				int count = Const.Ten_Thousand;
				if (array.length > 1) {
					count = NumberUtil.parseInt(array[array.length - 1]);
				}

				TcResult total = new TcResult(tc);
				for (int i = 0; i < count; i++) {
					total.add(XsgRewardManager.getInstance().doTc(null, tc));
				}

				out.println("物品名称\t数量：");
				for (Entry<String, Integer> entry : total) {
					out.println(TextUtil.format("{0}\t{1}", XsgItemManager
							.getInstance().findAbsItemT(entry.getKey())
							.getName(), entry.getValue()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Map<String, RewardT> rewardTMap;
	private Map<String, ChestItemMockT> chestItemMockTMap;
	private Map<String, TcMockT> tcMockTMap;
	
	/** 觉醒掉落 */
	//private Map<String, AwakenMockT> awakenMockMap;

	private XsgRewardManager() {
		loadTcScript();
	}

	/**
	 * 加载TC脚本
	 */
	public void loadTcScript() {
		this.rewardTMap = new HashMap<String, RewardT>();
		List<RewardT> list = ExcelParser.parse(RewardT.class);
		for (RewardT rewardT : list) {
			this.rewardTMap.put(rewardT.id, rewardT);
		}

		this.chestItemMockTMap = CollectionUtil.toMap(
				ExcelParser.parse(ChestItemMockT.class), "chestItem");
		this.tcMockTMap = CollectionUtil.toMap(
				ExcelParser.parse(TcMockT.class), "tc");
		
		// 初始化觉醒掉落
		//this.awakenMockMap = CollectionUtil.toMap(ExcelParser.parse(AwakenMockT.class), "mockTc");
	}

	/**
	 * 创建角色奖励控制器
	 * 
	 * @param role
	 * @param roleDB
	 * @return
	 */
	public IRewardControler createRewardControler(IRole role, Role roleDB) {
		return new RewardControler(role, roleDB);
	}

	/**
	 * 执行TC算法
	 * 
	 * @param role
	 *            执行该TC的角色对象，之所以需要这个参数，是因为这里要做伪随机控制
	 * @param tcCode
	 *            tc代码
	 * @return
	 */
	public TcResult doTc(IRole role, String tcCode) {
		int level = role == null ? 100 : role.getLevel();
		TcResult result = new TcResult(tcCode);
		RewardT template = this.findRewardT(tcCode);
		if (template == null) {
			return result;
		}

		if (template.money > 0) {
			result.appendProperty(Const.PropertyName.MONEY, template.money);
		}
		if (template.exp > 0) {
			result.appendProperty(Const.PropertyName.EXP, template.exp);
		}

		// 掉落物品
		if (level < template.dropLevel) {
			return result;
		}

		if (template.picks < 0) {// picks小于0，掉落固定数量物品，此时物品对应数值表示个数
			int count = Math.abs(template.picks); // 物品总数
			for (int i = 0; i < count; i++) {
				DropItemConfig config = template.itemConfigs[i];
				this.appendDrop(role, result, config, config.value);
			}
		} else {// picks大于0，表示执行几次随机，此时物品对应数值表示概率
			for (int i = 0; i < template.picks; i++) {
				int randomValue = NumberUtil.random(template.getTotalRate());
				if (randomValue < template.noDropRate) {
					continue;
				}

				int maxValue = template.noDropRate;
				for (DropItemConfig config : template.itemConfigs) {
					maxValue += config.value;
					if (randomValue < maxValue) {
						// 命中目标
						this.appendDrop(role, result, config, 1);
						break;
					}
				}
			}
		}

		if (role != null) {// 伪随机控制
			TcMockT tt = this.tcMockTMap.get(tcCode);
			result = role.getRewardControler().doMockTc(result, tt);
		}

		return result;
	}

	/**
	 * 添加掉落物品
	 * 
	 * @param role
	 * @param result
	 * @param config
	 * @param num
	 */
	private void appendDrop(IRole role, TcResult result, DropItemConfig config,
			int num) {
		if (config.isTcRedirect()) {
			result.add(this.doTc(role, config.code));
		} else if (config.isPiece()) {
			String baseCode = config.code
					.substring(0, config.code.length() - 2);
			// int max = XsgItemManager.getInstance().findAbsItemT(baseCode)
			// .getPieceCount();
			// int index = NumberUtil.random(1, max + 1);
			result.appendProperty(TextUtil.format("{0}-{1}", baseCode, "x"),
					num);
		} else {
			Property p = this.parseCodeRegular(config.code);
			result.appendProperty(p.code, num * p.value);
		}
	}

	/**
	 * 解析TC物品的数量规则，支持#固定数量/{min,max}两种格式
	 * 
	 * @param code
	 * @return
	 */
	private Property parseCodeRegular(String code) {
		// （1）数量范围随机：itemCode{min,max}
		// （2）数量固定：itemCode#count
		// 如 s1101#10，表示10个将魂
		// s1101{3,5}，表示随机获得3-5个将魂
		code = code.trim();
		Property p = new Property(code, 1);
		if (code.contains("#")) {
			String[] array = code.split("#");
			p.code = array[0];
			p.value = NumberUtil.parseInt(array[1]);
		} else if (code.contains("{")) {
			String[] array = code.split("\\{");// 截取后面部分
			p.code = array[0];
			array = array[1].split(",");// 截取最大和最小
			int min = NumberUtil.parseInt(array[0]);
			int max = NumberUtil.parseInt(array[1].substring(0,
					array[1].length() - 1));// 最后有个“}”需要去掉
			p.value = NumberUtil.randomContain(min, max);
		}

		return p;
	}

	private RewardT findRewardT(String tcCode) {
		return this.rewardTMap.get(tcCode);
	}

	/**
	 * 执行TC，并生成奖励的物品展示列表
	 * 
	 * @param role
	 *            执行该TC的角色对象，之所以需要这个参数，是因为这里要做伪随机控制
	 * @param tcCode
	 *            tc代码
	 * @return
	 */
	public ItemView[] doTcToItem(IRole role, String tcCode) {
		TcResult rewards = this.doTc(role, tcCode);
		return this.generateItemView(rewards);
	}

	/**
	 * 计算武将经验奖励
	 * 
	 * @param heros
	 * @return
	 */
	public Map<String, Integer> calculateHeroExp(Iterable<IHero> heros) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (IHero hero : heros) {
			HeroLevelupExpT hleT = XsgHeroManager.getInstance()
					.findHeroLevelupExpT(hero.getLevel());
			map.put(hero.getId(), (int) (hleT.rewardExp * Hero_Exp_Rate));
		}
		return map;
	}

	/**
	 * 获取过关获得的主公经验
	 * 
	 * @param level
	 * @return
	 */
	@Deprecated
	public int calculateRoleExp(int level) {
		return 0;
	}

	public ItemView generateItemView(String code, int value) {
		ItemType type = XsgItemManager.getInstance().getItemType(code);
		return new ItemView("", type, code, value, "");// 默认extendsProperty为空
	}

	public ChestItemMockT findMockT(String chestItemId) {
		return this.chestItemMockTMap.get(chestItemId);
	}
	
//	/**
//	 * 获取觉醒掉落TC
//	 * 
//	 * @param awakenMockId
//	 * @return 
//	 */
//	public AwakenMockT findAwakenMockT(String awakenMockId) {
//		return this.awakenMockMap.get(awakenMockId);
//	}

	/**
	 * 生成奖励的物品展示列表
	 * 
	 * @param rewards
	 * @return
	 */
	public ItemView[] generateItemView(TcResult rewards) {
		// 掉落物品
		List<ItemView> itemList = new ArrayList<ItemView>();
		for (Entry<String, Integer> entry : rewards) {
			// ItemType type = this.getItemType(entry.getKey());
			// itemList.add(new ItemView("", type, entry.getKey(), entry
			// .getValue()));
			itemList.add(this.generateItemView(entry.getKey(), entry.getValue()));
		}

		return itemList.toArray(new ItemView[0]);
	}

	public void checkScript() {
		List<String> errorList = new ArrayList<String>();
		for (String tc : this.rewardTMap.keySet()) {
			for (int i = 0; i < 100; i++) {
				ItemView[] items = this.doTcToItem(null, tc);
				for (ItemView view : items) {
					AbsItemT t = XsgItemManager.getInstance().findAbsItemT(
							view.templateId);
					if (t == null && !errorList.contains(tc)) {
						errorList.add(tc);
					}
				}
			}
		}

		if (errorList.size() > 0) {
			LogManager.warn(TextUtil.format("Error Tc list:{0}.",
					TextUtil.join(errorList, ",")));
		}
	}
}
