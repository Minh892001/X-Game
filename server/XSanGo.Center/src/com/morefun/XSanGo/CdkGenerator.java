/**
 * 
 */
package com.morefun.XSanGo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * CDK生成器
 * 
 * @author sulingyun
 *
 */
public class CdkGenerator {
	private Random random = new Random();
	private String candidate = "ABCDEFGHJKMNPQRSTUVWXYZ0123456789";

	private String generateCode(int minSize, int maxSize) {
		int size = this.random.nextInt(maxSize + 1 - minSize) + minSize;
		String result = "";
		for (int i = 0; i < size; i++) {
			result += this.candidate.charAt(this.random.nextInt(this.candidate
					.length()));
		}

		return result;
	}

	public List<String> generateDistinctCode(int number, int minSize,
			int maxSize) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < number; i++) {
			String code = this.generateCode(minSize, maxSize);
			while (list.contains(code)) {
				code = this.generateCode(minSize, maxSize);
			}
			list.add(code);
		}

		return list;
	}
}
