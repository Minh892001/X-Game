/**
 * 
 */
package com.morefun.XSanGo.temp;

import java.util.Calendar;
import java.util.Date;

import com.morefun.XSanGo.role.IRole;

/**
 * 运营临时活动的包装器抽象类
 * 
 * @author sulingyun
 *
 */
public abstract class AbsTemporaryOperationWrapper {
	protected IRole rt;
	protected String params;
	protected Date begin;
	protected Date end;

	public AbsTemporaryOperationWrapper(IRole rt, String params, Date begin,
			Date end) {
		this.rt = rt;
		this.params = params;
		this.begin = begin;
		this.end = end;
	}

	/**
	 * 检测是否在有效期内
	 * 
	 * @return
	 */
	public final boolean checkPeriod() {
		Date now = Calendar.getInstance().getTime();
		if (this.begin != null && this.begin.after(now)) {
			return false;
		}
		if (this.end != null && this.end.before(now)) {
			return false;
		}

		return true;
	}

}
