package com.morefun.XSanGo.equip;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 装备熔炼配置
 * 
 * @author qinguofeng
 * */
@ExcelTable(fileName = "script/道具相关/装备脚本.xls", beginRow = 1, sheetName = "装备熔炼公式")
public class EquipSmeltT {
    @ExcelColumn(index = 0)
    public int type; // 装备成色，白色（0），绿色（1）
    @ExcelColumn(index = 1)
    public int rate; // 出售价格折扣
    @ExcelColumn(index = 2)
    public int backRate;// 强化花费的返还比例
    @ExcelColumn(index = 3)
    public int num; // 奖励TC阀值
    @ExcelColumn(index = 4)
    public String reward; // 奖励TC
    
    /** 1星返还升星石TC */
    @ExcelColumn(index = 5)
    public String star1TC;
    /** 2星返还升星石TC */
    @ExcelColumn(index = 6)
    public String star2TC;
    /** 3星返还升星石TC */
    @ExcelColumn(index = 7)
    public String star3TC;
    /** 4星返还升星石TC */
    @ExcelColumn(index = 8)
    public String star4TC;
    /** 5星返还升星石TC */
    @ExcelColumn(index = 9)
    public String star5TC;
}
