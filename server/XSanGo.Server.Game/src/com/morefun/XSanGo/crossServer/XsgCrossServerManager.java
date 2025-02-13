package com.morefun.XSanGo.crossServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.XSanGo.Protocol.CrossServerCallbackPrx;
import com.XSanGo.Protocol.NoteException;

/**
 * 跨服管理类
 * 
 * @author guofeng.qin
 */
public class XsgCrossServerManager {
	private static final Logger logger = LoggerFactory.getLogger(XsgCrossServerManager.class);

	private static XsgCrossServerManager instance = new XsgCrossServerManager();

	private CrossServerCallbackPrx crossServerPrx;
	private long timeDiff = 0L;

	private XsgCrossServerManager() {

	}

	public static XsgCrossServerManager getInstance() {
		return instance;
	}

	public void setCrossServerCallback(CrossServerCallbackPrx cb) {
		logger.warn("setup cross server callback ...");
		crossServerPrx = cb;
	}

	public CrossServerCallbackPrx getCrossServerCbPrx() {
		return crossServerPrx;
	}

	public boolean isUsable() {
		return crossServerPrx != null;
	}

	public static NoteException getNoteException(Exception e) {
		Throwable exception = e;
		while (exception != null) {
			if (exception instanceof NoteException) {
				return (NoteException) exception;
			}
			exception = exception.getCause();
		}
		return null;
	}
	
	public long currentRemoteTime() {
		return System.currentTimeMillis() + timeDiff;
	}
	
	public void updateTimeDiff(long time) {
		if (time > 0) {
			long current = System.currentTimeMillis();
			timeDiff = time - current;
		}
	}
}
