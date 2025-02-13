package com.morefun.XSanGo.common;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class GroovyManager {

	private static GroovyManager instance = new GroovyManager();

	public static GroovyManager getInstance() {
		return instance;
	}

	private GroovyManager() {
		try {
			this.executeScript("println(\"initialize groovy engine.\")");
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	public void executeScript(String input) throws ScriptException {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("groovy");

		engine.eval(input);
	}
}
