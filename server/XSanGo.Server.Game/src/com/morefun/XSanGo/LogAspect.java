package com.morefun.XSanGo;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.morefun.XSanGo.monitor.XsgMonitorManager;

public class LogAspect {
	/**
	 * 前置通知：在某连接点之前执行的通知，但这个通知不能阻止连接点前的执行
	 * 
	 * @param jp
	 *            连接点
	 */
	public void doBefore(JoinPoint jp) {

	}

	/**
	 * 环绕通知：包围一个连接点的通知，可以在方法的调用前后完成自定义的行为，也可以选择不执行
	 * 类似Web中Servlet规范中的Filter的doFilter方法。
	 * 
	 * @param pjp
	 *            当前进程中的连接点
	 * @return
	 * @throws Throwable
	 */
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		long time = System.currentTimeMillis();
		Object retVal = pjp.proceed();
		time = System.currentTimeMillis() - time;

		String logMsg = pjp.toString();
		logMsg = logMsg.substring(logMsg.indexOf(" ") + 1);
		logMsg = logMsg.substring(0, logMsg.length() - 1);
		String[] array = logMsg.split("\\.");
		logMsg = array[array.length - 2] + "." + array[array.length - 1];
		XsgMonitorManager.getInstance().process(logMsg, (int) time);
		return retVal;
	}

	/**
	 * 抛出异常后通知 ： 在方法抛出异常退出时执行的通知。
	 * 
	 * @param jp
	 *            连接点
	 */
	public void doAfter(JoinPoint jp) {
	}
}
