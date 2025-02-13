/**
 * 
 */
package com.morefun.XSanGo.hero;

import org.apache.commons.lang.ArrayUtils;

import com.XSanGo.Protocol.HeroType;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 武将模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/武将相关/武将脚本.xls", sheetName = "武将数据", beginRow = 2)
public class HeroT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 3)
	public String name;

	@ExcelColumn(index = 5)
	public int open;

	@ExcelColumn(index = 6)
	public String contry;

	@ExcelColumn(index = 7)
	public String sex;

	@ExcelColumn(index = 8)
	public int type;

	@ExcelColumn(index = 9)
	public byte star;

	@ExcelColumn(index = 10)
	public byte color;

	@ExcelColumn(index = 13)
	public byte oneVsOne;

	@ExcelColumn(index = 14)
	public int brave;

	@ExcelColumn(index = 15)
	public int calm;

	@ExcelColumn(index = 16)
	public int hp;

	@ExcelColumn(index = 17)
	public int power;

	@ExcelColumn(index = 18)
	public int intel;

	@ExcelColumn(index = 19)
	public int phyDef;
	@ExcelColumn(index = 20)
	public int magDef;

	@ExcelColumn(index = 21)
	public int hp_rec;

	@ExcelColumn(index = 22)
	public int anger_rec;

	@ExcelColumn(index = 23)
	public int critRate;

	@ExcelColumn(index = 24)
	public int decritRate;
	@ExcelColumn(index = 25)
	public int hitRate;
	/** 闪避率 */
	@ExcelColumn(index = 26)
	public int dodgeRate;

	@ExcelColumn(index = 28)
	public String skill1;

	@ExcelColumn(index = 29)
	public String skill2;

	@ExcelColumn(index = 30)
	public String skill3;

	@ExcelColumn(index = 31)
	public String skill4;

	@ExcelColumn(index = 33)
	public int attackInterval;

	@ExcelColumn(index = 34)
	public int duelSkill;

	@ExcelColumn(index = 36)
	public String growing1;
	@ExcelColumn(index = 37)
	public String growing2;
	@ExcelColumn(index = 38)
	public String growing3;
	@ExcelColumn(index = 39)
	public String growing4;
	@ExcelColumn(index = 40)
	public String growing5;

	@ExcelColumn(index = 41)
	public String referenceRelationIds;

	private float[][] growing = new float[5][3];
	private boolean growingParsed;
	/** 缘分配置 */
	private RelationT[] relations;
	/** 随从配置 */
	private AttendantT[] attendants;

	/** 技能配置表 */
	private int[][] skillArray;

	@ExcelColumn(index = 46)
	public float defGrow;

	@ExcelColumn(index = 47)
	public float magDefGrow;

	@ExcelColumn(index = 48)
	public float critGrow;

	@ExcelColumn(index = 49)
	public float decritGrow;

	/** 突破扩展缘分 */
	@ExcelColumn(index = 51)
	public String relationFromBreak;
	
	/** 是否可以觉醒 */
	@ExcelColumn(index = 53)
	public byte isAwaken;
	
	/** 觉醒需要的武将等级 */
	@ExcelColumn(index = 54)
	public int awakenHeroLevel;
	
	/** 觉醒后获得的技能 */
	@ExcelColumn(index = 55)
	public int awakenSkillId;
	
	/** 觉醒后武将模型 */
	@ExcelColumn(index = 58)
	public int awaken3DID;

	/**
	 * 获取HP成长
	 * 
	 * @param starLevel
	 *            武将星级
	 * @return
	 */
	public float getHpGrow(byte starLevel) {
		this.tryParseGrowingData();

		return this.growing[starLevel - 1][0];
	}

	private void tryParseGrowingData() {
		if (this.growingParsed) {
			return;
		}
		String[] temp = new String[] { this.growing1, this.growing2,
				this.growing3, this.growing4, this.growing5 };
		for (int i = 0; i < temp.length; i++) {
			String[] data = temp[i].split(",");
			for (int j = 0; j < data.length; j++) {
				this.growing[i][j] = Float.parseFloat(data[j]);
			}
		}

		this.growingParsed = true;
	}

	public float getPowerGrow(byte starLevel) {
		this.tryParseGrowingData();

		return this.growing[starLevel - 1][1];
	}

	public float getIntelGrow(byte starLevel) {
		this.tryParseGrowingData();

		return this.growing[starLevel - 1][2];
	}

	public RelationT[] getRelations() {
		return this.relations;
	}

	public AttendantT[] getAttendants() {
		return this.attendants;
	}

	public void setAttendants(AttendantT[] attendants) {
		this.attendants = attendants;
	}

	public void setRelations(RelationT[] relations) {
		this.relations = relations;
	}

	public HeroType getType() {
		if (this.type == 1) {
			return HeroType.Power;
		}
		if (this.type == 2) {
			return HeroType.Intelligence;
		}
		return HeroType.Unknow;
	}

	/**
	 * 获取技能的位置索引
	 * 
	 * @param skillId
	 * @return
	 */
	public int getSkillIndex(int skillId) {
		for (int i = 0; i < this.getSkillArray().length; i++) {
			if (this.getSkillArray()[i][0] == skillId) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 通过技能位置索引获取技能id
	 * */
	public int getSkillIdByIndex(int index) {
		return getSkillArray()[index][0];
	}

	/**
	 * 武将初始技能数组<id,默认等级>
	 * 
	 * @return 
	 */
	public int[][] getSkillArray() {
		if (this.skillArray == null) {
			this.skillArray = new int[4][2];// 默认四个技能
			String[] temp = new String[] { this.skill1, this.skill2, this.skill3, this.skill4 };
			for (int i = 0; i < temp.length; i++) {
				this.skillArray[i] = TextUtil.GSON.fromJson(temp[i], int[].class);
			}
			if (isAwaken == 1 && awakenSkillId > 0) {// 可觉醒武将增加额外的第5技能 默认0级
				this.skillArray = (int[][]) ArrayUtils.add(this.skillArray, new int[]{awakenSkillId, 0});
			}
		}
		return this.skillArray;
	}

	public boolean canDuel() {
		return this.oneVsOne == 1;
	}
}
