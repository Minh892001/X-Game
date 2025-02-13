package com.morefun.XSanGo.partner;

import java.util.List;
import java.util.Map;

import com.morefun.XSanGo.db.game.Role;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.script.ExcelParser;
import com.morefun.XSanGo.util.CollectionUtil;

/**
 * 玩家伙伴管理
 * @author xiaojun.zhang
 *
 */
public class XsgPartnerManager {

	private static XsgPartnerManager instance = new XsgPartnerManager();

	/**伙伴位配置*/
	private List<PartnerPosT> posList;
	
	/**伙伴属性配置 id-PartnerPropT*/
	private Map<Integer,PartnerPropT> propMap;

	/**伙伴武将配置模版id-PartnerHeroT*/
	private Map<Integer,PartnerHeroT> specialHeroMap;
	/***/
	private List<PartnerLevelRequiredT> levelRequiredT;
	
	public static XsgPartnerManager getInstance() {
		return instance;
	}

	public XsgPartnerManager() {
		posList = ExcelParser.parse("伙伴位", PartnerPosT.class);
		propMap = CollectionUtil.toMap(
				ExcelParser.parse(PartnerPropT.class), "id");
		specialHeroMap = CollectionUtil.toMap(
				ExcelParser.parse(PartnerHeroT.class), "id");
		levelRequiredT = ExcelParser.parse(PartnerLevelRequiredT.class);
	}
	
	public IPartnerControler createPartnerControler(IRole rt, Role db) {
		return new PartnerControler(rt, db);
	}

	public List<PartnerPosT> getPosList() {
		return posList;
	}
	
	public Map<Integer, PartnerPropT> getPropMap() {
		return propMap;
	}
	
	public Map<Integer, PartnerHeroT> getHeroMap() {
		return specialHeroMap;
	}
	
	public PartnerPropT findPropById(int id){
		return propMap.get(id);
	}
	
	public PartnerHeroT findHeroById(int code){
		return specialHeroMap.get(code);
	}
	
	public PartnerPosT getPartnerTById(int id){
		for (PartnerPosT partnerPosT : posList) {
			if(partnerPosT.id == id){
				return partnerPosT;
			}
		}
		return null;
	}
	
	public PartnerLevelRequiredT getReuiredLevel(){
		return levelRequiredT.get(0);
	}

	
}
