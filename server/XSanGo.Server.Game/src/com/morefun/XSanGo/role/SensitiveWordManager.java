/**
 * 
 */
package com.morefun.XSanGo.role;

import java.util.ArrayList;
import java.util.List;

import com.morefun.XSanGo.script.ExcelParser;

/**
 * 敏感字管理
 * 
 * @author sulingyun
 *
 */
public class SensitiveWordManager {
	private static SensitiveWordManager instance = new SensitiveWordManager();

	public static SensitiveWordManager getInstance() {
		return instance;
	}

	private List<String> sensitiveWordList;

	private SensitiveWordManager() {
		this.sensitiveWordList = new ArrayList<String>();
		for (SensitiveWord sensitiveWord : ExcelParser
				.parse(SensitiveWord.class)) {
			this.sensitiveWordList.add(sensitiveWord.word);
		}
	}

	/**
	 * 是否包含敏感词
	 * 
	 * @param input
	 * @return
	 */
	public boolean hasSensitiveWord(String input) {
		for (String word : this.sensitiveWordList) {
			if (input.contains(word)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 敏感词屏蔽
	 * 
	 * @param content
	 * @return
	 */
	public String shieldSensitiveWord(String content) {
		if(content == null){
			return "";
		}
		for (String word : this.sensitiveWordList) {
			content = content.replace(word, "**");
		}
		return content;
	}
}
