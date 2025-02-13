package com.morefun.XSanGo.collect;

import com.morefun.XSanGo.script.ExcelColumn;

public class CollectHeroSoulT{

    /** 名将召唤的活动类型 */
    public static final String[] HERO_STR = { "在野名将", "限时活动" };
    /** 名将召唤的活动类型 */
    public static final int NORMAL_HERO = 0, TIMELIMIT_HERO = 1;
    /** 名将召唤的消耗类型 */
    public static final String[] CONSUME_TYPE_STR = { "wine", "gold", "rmby" };
    /** 名将召唤的消耗类型 */
    public static final int WINE = 0, GOLD = 1, RMBY = 2;

    @ExcelColumn(index = 0)
    public String type; // 货币道具种类 gold/rmby/wine
    @ExcelColumn(index = 1)
    public int price; // 价格数量
    @ExcelColumn(index = 2)
    public int num; // 可用次数
    @ExcelColumn(index = 3)
    public int randomMin; // 伪随机最低次数
    @ExcelColumn(index = 4)
    public int randomMax; // 伪随机最高次数
    @ExcelColumn(index = 5)
    public String specialTC; // 稀有伪随机物品
    @ExcelColumn(index = 6)
    public int specialWeight; // 稀有伪随机权重
    @ExcelColumn(index = 7)
    public int specialNotice; // 是否公告
    @ExcelColumn(index = 8)
    public String normalTC1; // 随机物品1
    @ExcelColumn(index = 9)
    public int normalWeight1; // 随机物品1权重
    @ExcelColumn(index = 10)
    public int normalNotice1; // 是否公告
    @ExcelColumn(index = 11)
    public String normalTC2; // 随机物品2
    @ExcelColumn(index = 12)
    public int normalWeight2; // 随机物品2权重
    @ExcelColumn(index = 13)
    public int normalNotice2; // 是否公告
    @ExcelColumn(index = 14)
    public String normalTC3; // 随机物品3
    @ExcelColumn(index = 15)
    public int normalWeight3; // 随机物品3权重
    @ExcelColumn(index = 16)
    public int normalNotice3; // 是否公告

    public CollectHeroSoulT() {
        
    }

    // 模版中没有，程序中使用的属性
    public String specialHeroId;
    public String normalHeroId1;
    public String normalHeroId2;
    public String normalHeroId3;
    public void setHeroIds (String specialHeroId,String normalHeroId1,String normalHeroId2, String normalHeroId3) {
        this.specialHeroId = specialHeroId;
        this.normalHeroId1 = normalHeroId1;
        this.normalHeroId2 = normalHeroId2;
        this.normalHeroId3 = normalHeroId3;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof CollectHeroSoulT) {
            CollectHeroSoulT objT = (CollectHeroSoulT)obj;
            return this.specialTC.equals(objT.specialTC) &&
                   this.normalTC1.equals(objT.normalTC1) &&
                   this.normalTC2.equals(objT.normalTC2) &&
                   this.normalTC3.equals(objT.normalTC3);
        }
        return false;
    }
}
