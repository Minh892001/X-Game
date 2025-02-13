/**
 * 
 */
package com.morefun.XSanGo.util;

import java.util.Date;

/**
 * 数值恢复辅助类
 * 
 * @author sulingyun
 *
 */
public class RecoveryUtil {

	/**
	 * 处理时间恢复类数值，适用于一个下次恢复时间及对应一个数值且有最大值的情况<br>
	 * 如果数值大于等于最大值则将下次恢复时间设为空
	 * 
	 * @param recoverable
	 */
	public static void recovery(IRecoverable recoverable) {
		int maxPoint = recoverable.getLimit();
		long now = System.currentTimeMillis();

		// 下次恢复时间为空表示原来是满的，由于配置或者VIP提升等原因上限提高了之后重新计算
		int current = recoverable.getValue();
		if (recoverable.getNextRecTime() == null && maxPoint > current) {
			long next = now + recoverable.getInterval();
			recoverable.setTime(new Date(next));
		}

		int change = 0;
		while (recoverable.getNextRecTime() != null
				&& recoverable.getNextRecTime().getTime() < now && current < maxPoint) {
			change++;

			if (current + change >= maxPoint) {
				break;
			} else {
				long next = recoverable.getNextRecTime().getTime()
						+ recoverable.getInterval();
				recoverable.setTime(new Date(next));
			}
		}

		if (change != 0) {
			recoverable.changeValue(change);
		}

		if (recoverable.getValue() >= maxPoint) {
			recoverable.setTime(null);
		}
	}
}
