/**
 * 
 */
package com.morefun.XSanGo.copy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.morefun.XSanGo.common.Const;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.IRandomHitable;
import com.morefun.XSanGo.util.NumberUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 小关卡模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/副本和怪物/关卡脚本.xls", sheetName = "关卡数据", beginRow = 2)
public class SmallCopyT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String name;

	@ExcelColumn(index = 2)
	public int parentId;

	/** 关卡类型，1-小关卡，2-中关卡，3-章节 */
	@ExcelColumn(index = 3)
	public int type;

	@ExcelColumn(index = 14)
	public int nextId;

	@ExcelColumn(index = 15)
	public String tc;

	@ExcelColumn(index = 16)
	public String firstTc;
	
	@ExcelColumn(index = 19)
	public int exp;

	@ExcelColumn(index = 24)
	public String catchHeroConfig;
	
	@ExcelColumn(index = 27)
	public int copyBuff;
	@ExcelColumn(index = 28)
	public int copyBuffEffect;

	private BigCopyT parent;

	private List<CatchRateT> CaptureRatelist;

	private List<CopySceneT> sceneTList = new ArrayList<CopySceneT>();

	public BigCopyT getParent() {
		return this.parent;
	}

	public void setParent(BigCopyT parent) {
		this.parent = parent;
	}

	/**
	 * 随机抓俘虏，没抓到则返回0
	 * 
	 * @return
	 */
	public int randomCatchHero() {
		if (this.CaptureRatelist == null) {
			this.CaptureRatelist = new ArrayList<CatchRateT>();
			if (TextUtil.isBlank(this.catchHeroConfig)) {
				return 0;
			}

			String[] arr = this.catchHeroConfig.split(";");
			for (String input : arr) {
				CaptureRatelist.add(new CatchRateT(input));
			}
		}

		int value = NumberUtil.random(10000);
		int temp = 0;
		for (CatchRateT crt : this.CaptureRatelist) {
			temp += crt.getRank();
			if (value < temp) {
				return crt.getHeroId();
			}
		}

		return 0;
	}

	public void addSceneT(CopySceneT cst) {
		this.sceneTList.add(cst);
	}

	/**
	 * 根据场景配置，随机获取可单挑怪物MAP，KEY为场景序列
	 * 
	 * @return
	 */
	public Map<Integer, MonsterT> randomDuelMonsterMap() {
		Map<Integer, MonsterT> map = new HashMap<Integer, MonsterT>();
		for (CopySceneT cst : this.sceneTList) {
			if (cst.duelRate > 0
					&& NumberUtil.random(Const.Ten_Thousand) < cst.duelRate
					&& cst.duelMonsterId != 0) {// 随机触发单挑且配置了单挑怪物
				map.put(cst.sceneSeq, XsgCopyManager.getInstance()
						.findMonsterT(cst.duelMonsterId));
			}
		}

		return map;
	}

	public List<CopySceneT> getSceneTList() {
		return sceneTList;
	}

	@Override
	public String toString() {
		return TextUtil.format("[id={0},name={1}]", this.id, this.name);
	}

	/**
	 * 是否中关卡
	 * 
	 * @return
	 */
	public boolean isMiddleCopy() {
		return this.type == 2;
	}

	/**
	 * 是否小关卡
	 * 
	 * @return
	 */
	public boolean isMiniCopy() {
		return this.type == 1;
	}
}

class DuelMonster implements IRandomHitable {
	private MonsterT mt;

	public DuelMonster(MonsterT mt) {
		this.mt = mt;
	}

	@Override
	public int getRank() {
		return this.mt.brave;
	}

	public MonsterT getMonsterT() {
		return this.mt;
	}

}

class CatchRateT implements IRandomHitable {

	private int heroId;
	private int rank;

	public CatchRateT(String input) {
		// [4101,50]
		input = input.substring(0, input.length() - 1);
		input = input.substring(1);
		String[] arr = input.split(",");
		this.heroId = NumberUtil.parseInt(arr[0]);
		this.rank = NumberUtil.parseInt(arr[1]);
	}

	public int getHeroId() {
		return this.heroId;
	}

	@Override
	public int getRank() {
		return this.rank;
	}

}
