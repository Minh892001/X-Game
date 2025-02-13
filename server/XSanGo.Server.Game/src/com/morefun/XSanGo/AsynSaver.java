/**
 * XSanGo ©2014 美峰数码
 * http://www.morefuntek.com
 */
package com.morefun.XSanGo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.morefun.XSanGo.util.LogManager;
import com.morefun.XSanGo.util.TextUtil;

/**
 * 对象异步保存器
 * 
 * @author BruceSu
 * 
 */
public class AsynSaver {
	/** 实际需要保存的对象 */
	private final IAsynSavable target;
	/** 保存队列 */
	private AtomicReference<byte[]> buff = new AtomicReference<byte[]>();

	private AtomicBoolean saving = new AtomicBoolean(false);

	public AsynSaver(IAsynSavable obj) {
		this.target = obj;
	}

	/**
	 * 异步保存，调用线程不阻塞
	 * 
	 */
	public void saveAsyn() {
		if (DBThreads.isShutdown()) {
			LogManager.warn(TextUtil.format("{0} can not save,because the database thread pool has been shutdown.",
					target.toString()));
			return;
		}

		this.buff.set(target.cloneData());
		DBThreads.execute(new Runnable() {
			@Override
			public void run() {
				if (!saving.compareAndSet(false, true)) {
					return;
				}

				byte[] temp = null;

				try {
					int count = 1;
					while ((temp = buff.getAndSet(null)) != null) {
						target.save(temp);
						count++;
						if (count > 3) {
							LogManager.warn(TextUtil.format("{0} save {1} times.", target, count));
						}
					}
				} finally {
					saving.set(false);
					if (buff.get() != null) {// double check
						LogManager.warn("Save request lost,then resave.");
						this.run();
					}
				}
			}
		});
	}
}
