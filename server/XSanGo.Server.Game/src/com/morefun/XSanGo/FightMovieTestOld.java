package com.morefun.XSanGo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import com.morefun.XSanGo.util.HttpUtil;
import com.morefun.XSanGo.util.TextUtil;

/**
 *
 * @author qinguofeng
 */
public class FightMovieTestOld {
	public static void main(String[] args) throws IOException {
		final AtomicLong atomic = new AtomicLong(0);
		final AtomicLong count = new AtomicLong(0);
		
		File file = new File("F:\\test");
		List<String> list = new ArrayList<String>();
		if (file.isDirectory()) {
			File[] fs = file.listFiles();
			if (fs != null) {
				for (File f : fs) {
					BufferedReader br = new BufferedReader(new FileReader(f));
					String line = br.readLine();
					list.add(line);
					br.close();
				}
			}
		}

		final int NUM = 5;
		Executor exe = Executors.newFixedThreadPool(NUM);

		for (int i = 0; i < (60 * 5); i++) {
			for (int j = 0; j < NUM; j++) {
				final String body = list.get(j % list.size());
				exe.execute(new Runnable() {
					@Override
					public void run() {
//						System.out.println("send...");
						long start = System.currentTimeMillis();
						String str = HttpUtil.sendPost("http://192.168.4.213:18080/generate_report", body);
						long end = System.currentTimeMillis();
						long interval = (end - start);
						long total = atomic.addAndGet(interval);
						long c = count.incrementAndGet();
						if (c % 60 == 0) {
							System.out.println("average: " + (total / c));
						}
						if ((end - start) > 5000) {
							System.out.println("Time: " + (System.currentTimeMillis() - start));
						}
						if (TextUtil.isNotBlank(str)) {
//							System.out.println("Success: " + (System.currentTimeMillis() - start));
						} else {
							System.out.println("Failure: " + (System.currentTimeMillis() - start));
						}
						try {
							Thread.sleep(200L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
			}
//			try {
//				Thread.sleep(1000L);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}
}
