/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleUtil
 * 功能描述：
 * 文件名：FactionBattleUtil.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;

import com.XSanGo.Protocol.CrossPvpView;
import com.XSanGo.Protocol.CrossRoleView;
import com.XSanGo.Protocol.PvpOpponentFormationView;
import com.morefun.XSanGo.faction.IFaction;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 公会战相关工具类
 * 
 * @author zwy
 * @since 2016-1-14
 * @version 1.0
 */
public class FactionBattleUtil {

	/**
	 * 依据提供的一组随机概率，得出最后胜出的某个概率的位置,支持某个随机概率为0的情况
	 * 
	 * @param rations
	 * @param randomSeed 随机种子，即各个随机和不能超过此值
	 * @return
	 */
	public static int calcRandomPositionByRatio(int[] rations, int randomSeed) {
		if (rations == null) {
			return -1;
		}
		// 计算各随机种子的范围数组
		int[] tmp = new int[rations.length];
		tmp[0] = rations[0];
		for (int i = 1; i < tmp.length; i++) {
			tmp[i] = tmp[i - 1] + rations[i];
		}
		// 如果给定的概率都为0或者最大值操作指定的随机种子,则返回-1
		int maxRatio = tmp[tmp.length - 1];
		if (maxRatio == 0 || maxRatio > randomSeed) {
			return -1;
		}
		// 如果最后一个值没有达到指定的随机种子，需要补足最大种子，达到可以随机到最大种子的概率
		if (maxRatio < randomSeed) {
			tmp = ArrayUtils.add(tmp, randomSeed);
			maxRatio = tmp[tmp.length - 1];
		}
		// 随机抽取
		Random random = new Random();
		int value = random.nextInt(maxRatio) + 1;
		for (int i = 0; i < tmp.length; i++) {
			if (value <= tmp[i]) {
				if (i == rations.length) {// 超过原随机范围，则-1
					return -1;
				} else {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 依据提供的一组随机概率，得出最后胜出的某个概率的位置,支持某个随机概率为0的情况
	 * 
	 * @param rations
	 * @return
	 */
	public static int calcRandomPositionByRatio(int[] rations) {
		if (rations == null || rations.length == 0) {
			return -1;
		}
		if (rations.length == 1) {
			return 0;
		}
		// 计算各随机种子的范围数组
		int[] tmp = new int[rations.length];
		tmp[0] = rations[0];
		for (int i = 1; i < tmp.length; i++) {
			tmp[i] = tmp[i - 1] + rations[i];
		}
		// 如果给定的概率都为0,则返回-1
		if (tmp[tmp.length - 1] == 0) {
			return -1;
		}
		// 随机抽取
		Random random = new Random();
		int value = random.nextInt(tmp[tmp.length - 1]) + 1;
		for (int i = 0; i < tmp.length; i++) {
			if (value <= tmp[i]) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 依据提供的一组随机概率，得出最后胜出的某个概率的位置
	 * 
	 * @param rations
	 * @return
	 */
	public static int calcRandomPositionByRatio(Integer[] rations) {
		return calcRandomPositionByRatio(ArrayUtils.toPrimitive(rations));
	}

	/**
	 * 根据给定的数据和权值列表，获取相应的随机数据
	 * 
	 * @param objects 数据列表
	 * @param rations 权值数组，各权值需要与数据列表的数据一一对应
	 * @param num 需要随机的数量
	 * @return
	 */
	public static <T> List<T> calcRandomPositionByRatio(List<T> objects, int[] rations, int num) {
		return calcRandomPositionByRatio(objects, rations, num, false);
	}

	/**
	 * 根据给定的数据和权值列表，获取相应的随机数据
	 * 
	 * @param objects 数据列表
	 * @param rations 权值数组，各权值需要与数据列表的数据一一对应
	 * @param num 需要随机的数量
	 * @param isResetRatios 是否需要重置权值数据，针对不允许出现重复数据
	 * @return
	 */
	public static <T> List<T> calcRandomPositionByRatio(List<T> objects, int[] rations, int num, boolean isResetRatios) {
		List<T> results = new ArrayList<T>();
		if (objects.size() > num) {// 随机列表数据数量大于需要的数量
			for (int i = 0; i < num; i++) {
				int rad = calcRandomPositionByRatio(rations);
				results.add(objects.get(rad));
				if (isResetRatios && i != num) {
					rations = ArrayUtils.remove(rations, rad);
					objects.remove(rad);
				}
			}
		} else {
			results.addAll(objects);
		}
		return results;
	}

	/**
	 * 字符串拼接
	 * 
	 * @param str
	 * @param params
	 * @return
	 */
	public static String join(String str, Object... params) {
		StringBuilder sb = new StringBuilder();
		sb.append(str == null ? "" : str);
		for (Object param : params) {
			sb.append(param);
		}
		return sb.toString();
	}

	/**
	 * 获取对象成员变量的名称和值
	 * 
	 * @param object
	 * @return
	 */
	public static String object2string(Object object) {
		StringBuffer sb = new StringBuffer();
		if (object.getClass().getSuperclass() != null) {
			Field[] fields = object.getClass().getSuperclass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				sb.append("[" + field.getName());
				try {
					sb.append("-->" + field.get(object) + "]");
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			sb.append("[" + field.getName());
			try {
				sb.append("-->" + field.get(object) + "]");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 初始化战斗参战角色信息
	 * 
	 * @param role
	 * @return
	 */
	private static CrossRoleView initCrossRoleView(IRole role) {
		CrossRoleView roleView = new CrossRoleView();
		roleView.roleId = role.getRoleId();
		roleView.roleName = role.getName();
		roleView.headImg = role.getHeadImage();
		roleView.level = role.getLevel();
		roleView.vipLevel = role.getVipLevel();
		roleView.serverId = role.getServerId();
		roleView.sex = role.getSex();
		IFaction faction = role.getFactionControler().getMyFaction();
		roleView.factionName = faction == null ? "" : faction.getName();
		return roleView;
	}

	/**
	 * 初始化战斗部队数据
	 * 
	 * @param role
	 * @param isRighrRobot 右边参战玩家是否机器人
	 * @return
	 */
	private static PvpOpponentFormationView initPvpOpponentFormationView(IRole role, boolean isRighrRobot) {
		String formationId = role.getFormationControler().getDefaultFormation().getId();
		return role.getFormationControler().getPvpOpponentFormationView(formationId, isRighrRobot ? false : true);
	}

	/**
	 * 初始化战斗对象数据
	 * 
	 * @param type 战斗类型
	 * @param leftRole 左边参战玩家（一般是战斗发起者）
	 * @param rightRole 右边参战玩家
	 * @param isRighrRobot 右边参战玩家是否机器人
	 * @return
	 */
	public static CrossPvpView initCrossPvpView(int type, IRole leftRole, IRole rightRole, boolean isRighrRobot) {
		return initCrossPvpView(type, null, 0, leftRole, rightRole, isRighrRobot);
	}

	/**
	 * 初始化战斗对象数据
	 * 
	 * @param type 战斗类型
	 * @param sceneName 战斗场景名称
	 * @param sceneID 战斗场景编号
	 * @param leftRole 左边参战玩家（一般是战斗发起者）
	 * @param rightRole 右边参战玩家
	 * @param isRighrRobot 右边参战玩家是否机器人
	 * @return
	 */
	public static CrossPvpView initCrossPvpView(int type, String sceneName, int sceneID, IRole leftRole, IRole rightRole, boolean isRighrRobot) {
		CrossPvpView pvpView = new CrossPvpView();
		pvpView.type = type;
		pvpView.leftRoleView = initCrossRoleView(leftRole);
		pvpView.leftPvpView = initPvpOpponentFormationView(leftRole, false);

		pvpView.rightRoleView = initCrossRoleView(rightRole);
		pvpView.rightPvpView = initPvpOpponentFormationView(rightRole, isRighrRobot);

		pvpView.sceneName = sceneName;
		pvpView.sceneID = sceneID;
		return pvpView;
	}

	public static void main(String[] args) {
		System.out.println(TextUtil.GSON.toJson(null));
	}
}
