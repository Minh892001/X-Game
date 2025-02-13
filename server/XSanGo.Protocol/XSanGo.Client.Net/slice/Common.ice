#ifndef  COMMONICE
#define  COMMONICE


module com{
	module XSanGo{
		module Protocol{
			struct ClientConfig {
				string version;		//版本号 
				string channel;		//打包标识
				string mac;			//MAC地址	
				string xmdmm;		//xmdmm=小马的秘密=客户端程序签名MD5
				string xmamg;		//xmamg=md5(userName+channel+KEY)
			};
			
			//货币类型
			enum CurrencyType{
				VipLevel,		//当一个物品的货币单位是VIP等级时，表示玩家VIP等级大于等于物品的卖价时可以免费购买
				Jinbi,
				Yuanbao,
				BindYuanbao,
				UnbindYuanbao,
				FactionToken,	//公会奖章
				AuctionMoney,	//拍卖币
				MilitaryOrder,	//军令
			};
			
			struct Money{
				CurrencyType type;
				int num;
			};
			
			//物品类型，用于客户端定位脚本位置
			enum ItemType{
//				None,					//无类型的虚拟物品，如经验，金钱，元宝等
				DefaultItemType,		//默认道具
				FormationBuffItemType,	//阵法书
				EquipItemType,			//装备
			};
			
			enum Contry{
				QunXiong=0,Wei,Shu,Wu,
			};
			
			enum HeroType{
				Unknow=0,Power,Intelligence,
			};
			
			enum QualityColor {
				Silver=0,Green,Blue,Violet,Orange
			};
			
			enum CopyDifficulty{
				Junior,Senior,Top
			};
			
			enum EquipPosition{
				EquipPositionHead,
				EquipPositionWeapon,
				EquipPositionClothes,
				EquipPositionShoe,
				EquipPositionDecoration,
				EquipPositionTreasureOrHorse
			};
			enum EquipType{
				EquipTypeHead,
				EquipTypeWeapon,
				EquipTypeClothes,
				EquipTypeShoe,
				EquipTypeDecoration,
				EquipTypeTreasure,
				EquipTypeHorse
			};
			enum DuelTemplateType{
				DuelTemplateTypeHero,
				DuelTemplateTypeMonster,
			};
			
			struct IntIntPair{
				int first;
				int second;
			 };
			 struct IntString{
				int intValue;
				string strValue;
			 };
			 
			 sequence<IntIntPair> IntIntPairSeq;
			 sequence<int> IntSeq;
			 sequence<byte> ByteSeq;
			 sequence<string> StringSeq;
			 sequence<IntString> IntStringSeq;
			 
			 struct ItemView{
				string id;
				ItemType iType;
				string templateId;
				int num;
				
				string extendsProperty;
			 };
			 
			 // 阵法扩展属性View,仅供服务器调用对象使用
			 struct FormationBuffExtendsView{
				 byte level;
				 int exp;
				 int provideExp;		//被吞噬时提供多少经验
			};
			
			// 普通道具扩展属性View,仅供服务器调用对象使用
			struct NormalItemExtendsView {
				byte 		limitType;			// 限时类型 0 不限时 1时长限时 2日期限时
				string		limitTime;			// 到期时间 yyyy.MM.dd HH:mm
			};
			
			//属性描述类
			struct Property{
				string code;		//属性代码
				int value;			//属性值
			};
			
			//带成长的属性
			struct GrowableProperty {
				// Property
				string code;		//属性代码
				int value;			//属性值
				// Property end
				float grow;
			};
			
			sequence<Property> PropertySeq;
			sequence<GrowableProperty> GrowablePropertySeq;
			
			// 装备扩展属性View,仅供服务器调用对象使用
			 struct EquipExtendsView {			
				 EquipType type;
				 int level;
				 byte star;
				 int starExp;		//星级经验
				 int battlePower;	//战斗力
				 GrowablePropertySeq growableProperties; // 装备主属性
				 PropertySeq properties; // 装备副属性为空
				 IntStringSeq gems; // 镶嵌的宝石列表
			 };
			 
			 sequence<ItemView> ItemViewSeq;
			 
			 struct AttendantView{
					string attendantId;			//上阵随从ID
					int value;					//属性变更值
					bool special;				//是否特殊
					IntStringSeq costItems;     //重置随从消耗物品数量
					int specialAttendantId;     //特殊随从模版id
			};
			sequence<AttendantView> AttendantViewSeq;
				
			//武将状态
			enum HeroState{
				InTheFormation,		//在部队配置中
				InTheSupport,		//援军
				Default,
				Attendant,			//别人的随从
				PartnerShip,        //伙伴
			};
			struct HeroView{
					string id;
					int templateId;
					string name;
					int compositeCombat;//综合战力
					short level;
					byte star;
					int qualityLevel;
					int color; // 武将显示颜色
					byte breakLevel;		//突破等级
					int exp;
					int levelupExp;
					float hpGrow;
					float powerGrow;
					float intelGrow;
					byte qualityAddPercents;//进阶加成
					GrowablePropertySeq properties; // 成长属性
					// PropertySeq properties;	//战斗非成长属性集合
					
					StringSeq equips;
					IntIntPairSeq levelupRelations;	//已升级缘分
					IntIntPairSeq activeRelations;	//已激活缘分
					AttendantViewSeq attendants;		//随从数据
					HeroState state;		//武将状态
					
					IntIntPairSeq skills;
					bool isTreasure;//是否出发寻宝
					byte awakenState;		// 0不可觉醒 1可觉醒 2已觉醒
					int	 awakenStar;		// 觉醒星级
			 };
			sequence<HeroView> HeroViewSeq;
			
			struct FormationPosView{
				byte index;		//0-2前，3-5中，6-8后，9-11援
				string heroId;	//武将唯一编号，没有则为空
			};
			sequence<FormationPosView> FormationPosViewSeq;
			
			struct FormationView{
				string id;
				byte index;						//0-2分别表示部队1-3
				string buffItemId;				//阵法书对应唯一编号，没有则为空
				IntIntPairSeq buffAdvanced;		//阵法进阶等级
				int buffAdvancedType;			//阵法进阶类型
				FormationPosViewSeq postions;
				IntIntPairSeq skills;			//主公技设置
				int battlePower;				//部队战力
			};
			sequence<FormationView> FormationViewSeq;
			
			struct PvpOpponentFormationView {
				FormationView view;
				HeroViewSeq heros;
			};
			sequence<PvpOpponentFormationView> PvpOpponentFormationViewSeq;
			
			/**PVP角色数据*/
			struct PvpRoleView{
				string roleId;
				string roleName;
				string headImg;
				int level;
				int vipLevel;
				int serverId;
				int sex;
				string factionName;
			};
			
			//队伍摘要信息
			struct FormationSummaryView{
				byte index;		//0-2前，3-5中，6-8后，9-11援
				int templateId;
				int quality;
				byte star;
				int level;
				int color;  // 武将显示颜色
				byte breakLevel; // 突破等级
				bool isAwaken;	// 是否武将觉醒
			};
			sequence<FormationSummaryView> FormationSummaryViewSeq;
			
			//商品信息视图
			struct CommodityView {
				// ItemView			 
			 	string id;
				ItemType iType;
				string templateId;
				int num;    
				// ItemView end
				
				 Money price;
				 bool alreadyBuy;
				 int group;
			};
			sequence<CommodityView> CommodityViewSeq;
			
			struct RoleViewForOtherPlayer{
				string id;
				string name;
				string headImg;
				short level;
				int vip;
				int compositeCombat;				//综合战力
				string faction;						//所属公会
				ItemViewSeq buff;					//阵法书,返回 FormationBuffView
				FormationSummaryViewSeq guardHeroArr;//部队武将信息
			};
			sequence<RoleViewForOtherPlayer> RoleViewForOtherPlayerSeq;
			
			
			//单挑相关数据
			enum DuelResult{
				DuelResultWin, DuelResultFail, DuelResultNoWinNoFail,
			};
			struct DuelSkillTemplateView{
				int id;
				string target;
				string effectProperty;
				int effectValue;
				int time;
				string desc;
				string icon;
			};
			sequence<DuelSkillTemplateView> DuelSkillTemplateViewSeq;
			struct DuelUnitView{
				int identity;		//战斗中的唯一标识
				DuelTemplateType type;
				int templateId;		//模板ID
				string name;		//名字
				int hp;
				int maxHp;
				int sp;
				int maxSp;
				int lastHp;
				int power;			//武力
				int star;			//星级
				int colorLevel;		//颜色
				int quality;		//品质
				byte breakLevel;     //突破等级
				
				int level;			//等级
				int brave;			//勇猛
				int calm; 			//冷静
				int intel;			//智力
				int dodge;			//闪避
				int critRate;		//暴击率
				int critResRate;	//抗暴率
				int damageRes;		//伤害减免
				DuelSkillTemplateViewSeq skills;
			};
			sequence<DuelUnitView> DuelUnitViewSeq; 
			enum AttackResult{
				Hit, Dodge, Block,
			};
			
			struct DuelBuffView{//单挑BUFF
				int targetId;		//目标ID
				string desc;		//文字描述
				string icon;		//图标
			};
			
			sequence<DuelBuffView> DuelBuffViewSeq;
			struct ActionReportView{
				int executorId;		//出招方ID
				int targetId;		//目标ID，没有则为0
				int skillId;
				AttackResult hitResult;
				int damage;
				bool crit;
				int sp1;
				int sp2;
				int power1;
				int power2;
				DuelBuffViewSeq buff;
			};
			sequence<ActionReportView> ActionReportViewSeq;
			struct DuelRoundReportView{//单回合战报
				ActionReportViewSeq actions;
				IntStringSeq removeBuff;  //失效BUFF列表
			};
			sequence<DuelRoundReportView> DuelRoundReportViewSeq;
            
            
			struct EffectN{
                string talkSoundResName;
                string talkSoundContent;
                string effectSoundResName;
                string effectAniResName;
                string effectSoundDeadResName;
            };
            sequence<EffectN> EffectNSeq;
			
			struct DamageN{
                bool redbg;
                int damage;
                bool crit;
                int attackResult;
                int skillId;
                DuelBuffViewSeq buffs;
            };
            sequence<DamageN> DamageNSeq;
            
			struct ActionN{
                int playerPos;
                int triggerPos;
                EffectNSeq effects;
                DamageNSeq damages;
                int type;
                string actionName;
                string actionQueName;
                float actionBeginTime;
                float actionOverTime;
                bool targetSelf;
                bool slowAction;
                float actionTime;
            };
            sequence<ActionN> ActionNSeq;
            
            struct FloatIntPair{
                float floatVal;
                int intVal;
            };
            sequence<FloatIntPair> FloatIntPairSeq;
			
			// 单挑战报
			struct SoloReportN{
                string resultTalk;
                bool runEscape;
                int runType;
                int firstLasthp;    
                int secondLasthp;   
                int defyPos;
                int skipDefyTalkPos;
                float skipTime;
                bool sneakTrigger;
                bool secKillTrigger;

                bool skipBattle;
                int aiSkill;
                ActionNSeq actions;
                FloatIntPairSeq controlOrder;

                int actionIndex;
                int addActionIndex; 
            };
			sequence<SoloReportN> SoloReportNSeq;
			struct DuelReportView{
				DuelResult result;
				//发起者可选数组，只有当元素数量>1时客户端才触发武将选择逻辑
				DuelUnitViewSeq firsts;
				DuelUnitViewSeq seconds;	//接受者
				DuelRoundReportViewSeq rounds;
				SoloReportNSeq soloReport;
			};
			sequence<DuelReportView> DuelReportViewSeq;
						
			struct SceneDuelView{
				int sceneSeq;				//场景序列
				int eventId;				//剧情事件编号，非剧情单挑则为0
				DuelReportViewSeq reports;
			};
			sequence<SceneDuelView> SceneDuelViewSeq;
			
			//主界面功能入口
			enum MajorMenu{
				HeroCardMarketMenu, 
				TaskMenu,
				ItemChipMenu, //碎片掠夺
				MailMenu,
				ArenaRankMenu,//竞技场排行
				SignMenu,
				SnsMenu,	  //好友
				Activity,	//活动
				ShopMenu,		//商城有免费礼包
				FactionReqMenu,	//工会有入会申请
				AnswerMenu,      //我要做VIP
				LadderMenu,		//群雄争霸
				HeroAdmireMenu,		//名将仰慕
				SumChargeMenu,	//累计充值
				SumConsumeMenu,	//累计消费
				CollectHeroSoulMenu, // 名将召唤
				TimeBattleMenu,//时空战役
				InviteFriendMenu,   //邀请好友
				SeckillMenu,		//秒杀	
				DayChargeMenu,		//日充值	
				DayConsumeMenu,		//日消费	
			    FundMenu,			// 成长基金
			    LevelRewardMenu,	// 冲级有奖
		    	PowerRewardMenu,	// 战斗力嘉奖
		    	VipGiftPacksMenu,	// 购买vip特权礼包
		    	FirstJiaMenu,	// 第一佳活动
		    	LeijiLogin, // 累计登录
		    	SendJunLing, // 送军令
		    	BigDayChargeMenu,		//big日充值	
				BigDayConsumeMenu,		//big日消费	
				BigSumChargeMenu,	//big累计充值
				BigSumConsumeMenu,	//big累计消费
				FortuneWheel, // 幸运大转盘
				LuckyBagMenu,//福袋
				CornucopiaMenu,//聚宝盆
				ExchangItemMenu,//福利兑换
				LevelWeal, // 等级福利
				TreasureMenu,//寻宝可收获
				MarksManMenu,//百步穿杨可免费射击或领奖
				ForverLeijiLogin,//累计在线永久

				SuperChargeMenu,//超级充值
				SuperRaffleMenu,//超级转盘
				FriendsRecallTask, //老友召回任务

				Achieve,//成就
				Api, //api
				AnnounceMenu,//活动公告
				OpenServerActive,//开服活动
				Lottery,//大富温
				Share,//分享活动
				TournamentMenu, // 比武大会 
				ResourceBack, // 资源返还
				MakeWine,     // 酿酒
				MarksManShootFree,	  // 百步免费射箭
				Artifact			  //神器	
			};
			
			struct MajorUIRedPointNote{
				MajorMenu menu;
				bool clearWhenOpen;
			};
			sequence<MajorUIRedPointNote> MajorUIRedPointNoteSeq;
			
			struct AccountView{
				string username;
				string createTime;
				int registerChannel;
				bool frozen;
			};

			// 战报对象
			struct FightMovieByteView {
				SceneDuelViewSeq soloMovie;
			    ByteSeq fightMovie;
			};
			sequence<FightMovieByteView> FightMovieByteViewSeq;
		};
	};  
};

#endif