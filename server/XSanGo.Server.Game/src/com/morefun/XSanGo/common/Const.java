/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo.common;

/**
 * @author BruceSu
 * 
 */
public class Const {
	public class PropertyName {
		/** 游戏币 */
		public static final String MONEY = "gold";
		/** 元宝 */
		public static final String RMBY = "rmby";
		/** 经验 */
		public static final String EXP = "exp";
		/** 竞技币 */
		public static final String ORDER = "order";
		/** 拍卖币 */
		public static final String AUCTION_COIN = "sth";
		/** 至尊币,比武大会货币 */
		public static final String ZZB = "zzb";
		/** 至尊银币,比武大会货币 */
		public static final String ZZYB = "zzyb";
		/** 技能点 */
		public static final String SKILL = "skp";
		/** 黑龙王將魂 */
		public static final String BlackDragonSoul = "shlw";
		
		/** 南华幻境：南华令 */
		public static final String NanHuaLing= "nhling";

		/** 主公等级 */
		public static final String Level = "role_lv";
		/** vip等级 */
		public static final String VIP_Level = "vip_lv";

		/** vip经验 */
		public static final String VIP_Exp = "vipExp";
		
		/**
		 * 粮草
		 */
		public static final String ARMY = "army";
		
		/**
		 * 荣誉
		 */
		public static final String HONOR = "honor";
		
		/**
		 * 声望
		 */
		public static final String POP = "pop";
		
		/** 行动力 */
		public static final String Vit = "vit";
		/** 行动力 购买的次数 */
		public static final String VitNum = "vitNum";
		/**
		 * 官阶
		 */
		public static final String RANK_ID = "rank_id";
		/** 有无公会 */
		public static final String HasFaction = "hasFaction";
		/** 战斗力 */
		public static final String CombatPower = "combatPower";

		/** 军令模板ID */
		public static final String JUNLING_TEMPLATE_ID = "med1";
		
		/**扩展头像*/
		public static final String HEAD_IMAGE = "headimage";
		
		/**头像边框*/
		public static final String HEAD_BORDER = "headborder";

		public class Hero {
			/** 生命 */
			public static final String HP = "hp";
			/** 智力 */
			public static final String Magic = "magic";
			/** 初始怒气 */
			public static final String SP = "sp";
			/** 物理防御 */
			public static final String PHYDEF = "armor";
			/** 魔法防御 */
			public static final String MAGICDEF = "resist";

			/** 命中率 */
			public static final String HITODDS = "hit%";
			/** 武力 */
			public static final String POWER = "power";
			/** 暴击 */
			public static final String CRIT = "crit";
			/** 暴击率 */
			public static final String CRIT_RATE = "crit%";
			/** 抗暴 */
			public static final String Decrit = "crit_res";
			/** 抗爆率 */
			public static final String Decrit_Rate = "crit_res%";
			/** 生命百分比 */
			public static final String Hp_Percent = "hp%";
			/** 武力百分比 */
			public static final String Power_Percent = "power%";
			/** 智力百分比 */
			public static final String Magic_Percent = "magic%";
			/** 生命回复 */
			public static final String Hp_Rec = "hp_rec";
			/** 攻击速度百分比 */
			public static final String Attatch_Speed_Percent = "att_speed%";
			/** 怒气恢复 */
			public static final String Anger_Rec = "anger_rec";
			/** 护甲穿透% */
			public static final String Armor_Pierce = "armor_pierce%";
			/** 魔抗穿透% */
			public static final String Resist_Pierce = "resist_pierce%";
			/** 怒气消耗减少% */
			public static final String Anger_Save = "anger_save%";
			/** 暴伤增加% */
			public static final String Crit_Damage_Percent = "crit_damage%";
			/** 施法速度% */
			public static final String Skill_Speed_Percent = "skill_speed%";
			/** 治疗效果% */
			public static final String Hp_Cure_Percent = "hp_cure%";
			/** 受暴伤害减少% */
			public static final String Crit_Redamage_Percent = "crit_redamage%";
			/** 生命吸取% */
			public static final String LL_Percent = "ll%";
			/** 怒气偷取% */
			public static final String AL_Percent = "al%";
			/** 反弹伤害% */
			public static final String Ref_Percent = "ref";
			/** 闪避% */
			public static final String Dodge_Percent = "dodge%";

			/** 勇猛 */
			public static final String Brave = "brave";
			/** 冷静 */
			public static final String Calm = "calm";
			/** 技能等级 */
			public static final String Total_Skill_level = "skills";
			/** 护甲百分比 */
			public static final String PHYDEF_Percent = "armor%";
			/** 魔抗百分比 */
			public static final String MAGICDEF_Percent = "resist%";
			/** 攻击间隔 */
			public static final String Attack_Interval = "span";
			/** 物伤减少% */
			public static final String Def_Poro = "defPoro%";
			/** 魔伤减少% */
			public static final String Magic_Def_Poro = "magicDefPoro%";
			/** 宝石专属--最终生命 */
			public static final String Final_Hp_Gem = "hpGem";
			/** 宝石专属--最终武力 */
			public static final String Final_Power_Gem = "powerGem";
			/** 宝石专属--最终智力 */
			public static final String Final_Magic_Gem = "magicGem";
			/** 宝石专属--最终护甲 */
			public static final String Final_PHYDEF_Gem = "armorGem";
			/** 宝石专属--最终魔抗 */
			public static final String Final_MAGICDEF_Gem = "resistGem";
			/** 被控减少% */
			public static final String CHANGE_BUFFLIFETIME_DEF = "ChangeBuffLifetime%";
			/** 反控 */
			public static final String CHANGE_BUFFLIFETIME = "ChangeBuffLifetime";
			
			/** 最终生命 */
			public static final String Final_Hp = "hpFinal";
			/** 最终武力 */
			public static final String Final_Power = "powerFinal";
			/** 最终智力 */
			public static final String Final_Magic = "magicFinal";
			/** 最终护甲 */
			public static final String Final_PHYDEF = "armorFinal";
			/** 最终魔抗 */
			public static final String Final_MAGICDEF = "resistFinal";

		}
	}

	public class Faction {
		/** 帮主 */
		public static final int DUTY_BOSS = 5;
		/** 副帮主 */
		public static final int DUTY_LITTLE_BOSS = 4;
		/** 长老 */
		public static final int DUTY_ELDER = 3;
		/** 堂主 */
		public static final int DUTY_MANAGER = 2;
		/** 精英 */
		public static final int DUTY_SENIOR = 1;
		/** 帮众 */
		public static final int DUTY_JUNIOR = 0;

		/** 帮派在缓存中的名字 */
		public static final String CACHE_NAME_FACTION = "faction";
		/** ID索引名称 */
		public static final String CACHE_INDEX_ID = "id";
		/** 名字索引名称 */
		public static final String CACHE_INDEX_NAME = "name";
		/** 等级索引名称 */
		public static final String CACHE_INDEX_LEVEL = "level";
		/** 人数索引名称 */
		public static final String CACHE_INDEX_SIZE = "size";

		/** 无需验证直接加入 */
		public static final int JOIN_TYPE_NOTCHECK = 0;
		/** 需要验证 */
		public static final int JOIN_TYPE_CHECK = 1;
		/** 拒绝加入 */
		public static final int JOIN_TYPE_REFUSE = 2;
		/** 公会起始ID */
		public static final long FACTION_ID = 1000;
	}

	public class FactionReq {
		/** 入帮申请在缓存中的名字 */
		public static final String CACHE_NAME_REQ = "factionReq";
		/** 入帮申请时间在缓存中的名字 */
		public static final String CACHE_NAME_REQ_DATE = "reqDate";
		/** 入帮申请缓存数据中的帮派ID索引 */
		public static final String CACHE_INDEX_FACTIONID = "factionId";
		/** 入帮申请缓存数据中的角色ID索引 */
		public static final String CACHE_INDEX_ROLEID = "roleId";
	}

	public class Role {
		/** 角色在缓存中的名字 */
		public static final String CACHE_NAME_ROLE = "role";
		public static final String CACHE_INDEX_ONLINE = "online";
		public static final String CACHE_INDEX_FACTIONID = "factionId";
		public static final String CACHE_INDEX_ROLENAME = "roleName";
		public static final String CACHE_INDEX_Account = "account";
		public static final String CACHE_INDEX_LEVEL = "level";
		public static final String CACHE_INDEX_RemoteAddress = "remoteAddress";
		public static final String CACHE_INDEX_ServerId = "serverId";
		/** 最大随从数量 */
		public static final int Max_Attendant_Count = 2;
	}

	public class Mail {
		public static final String CACHE_INDEX_ACCEPTOR_ID = "acceptorId";
		public static final String CACHE_NAME_MAIL = "mail";
		public static final String CACHE_INDEX_CREATETIME = "createTime";
		public static final String CACHE_All_MAIL = "allMail";
		/** 全服邮件标记 */
	}

	public class RankList {
		/**
		 * 战斗力
		 */
		public static final int COMBAT = 0;
		/**
		 * 公会
		 */
		public static final int FACTION = 1;
		/**
		 * 膜拜
		 */
		public static final int WORSHIP = 2;
		/**
		 * 成就
		 */
		public static final int ACHIEVE = 3;
	}

	/**
	 * 伙伴系统常量数据
	 * 
	 * @author xiaojun.zhang
	 *
	 */
	public class Partner {
		/**
		 * 重置伙伴阵位的缘分单数量
		 */
		public static final int YFD_NUM = 9999;
		/**
		 * 伙伴系统中的阵位数量
		 */
		public static final int POS_NUM = 7;
	}

	public class Chat {
		/** 公告默认颜色代码 */
		public static final String AnnouncementDefaultColor = "4261413119";
		// 默认聊天显示颜色
		public static final String CHAT_COLOR_DEFAULT = "4008419071";

		/**
		 * 走马灯绿色
		 */
		public static final String CHAT_COLOR_GREEN = "0x3cff00ff";

		/**
		 * 走马灯蓝色
		 */
		public static final String CHAT_COLOR_BLUE = "0x01deffff";

		/**
		 * 走马灯紫色
		 */
		public static final String CHAT_COLOR_PURPLE = "0xf601ffff";
	}

	/** 最高星级，适用关卡、武将、装备 */
	public static final byte MaxStar = 5;
	/** 一万 */
	public static final int Ten_Thousand = 10000;
	/** 十万 */
	public static final int Hundred_Thousand = 100000;
	/** 阵法最高等级 */
	public static final byte MaxFormationBuffLevel = 6;
	/** 保存失败的数据缓存名 */
	public static final String ErrorData_Cache_Name = "error_data";
	/**
	 * 公会保存失败数据缓存名
	 */
	public static final String FACTION_ERROR_DATA_NAME = "faction_error_data";
	/** 一个部队最多可配置多少个主公技能 */
	public static final int MaxSkillCountInFormation = 4;
	/** 全局二级缓存名，用于存放和数据库状态一致的数据，以降低IO压力 */
	public static final String L2_Cache_name = "L2_Cache";
	/** 除援军外，阵型同时上阵武将数量 */
	public static final int MaxHeroCountInFormationExcludeSupport = 5;
	/** 最大进阶等级 */
	public static final int MaxQualityLevel = 10;
	/** 最大突破等级 */
	public static final int MaxBreakLevel = 5;
	/**多个扩展头像分隔符*/
	public static final String EXTRA_HEAD_SPLIT = ",";
	/**头像和边框分隔符*/
	public static final String HEAD_BORDER_SPLIT = "~";

}
