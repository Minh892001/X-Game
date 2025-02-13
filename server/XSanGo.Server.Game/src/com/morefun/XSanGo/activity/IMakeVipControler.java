package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.NoteException;
import com.morefun.XSanGo.role.IRedPointNotable;

public interface IMakeVipControler extends IRedPointNotable {

	/**
	 * 判断是否可开始答题
	 * 
	 * @return
	 */
	boolean isAnswer();

	/**
	 * 开始作答 返回题目列表
	 * 
	 * @return
	 */
	String beginAnswer() throws NoteException;

	/**
	 * 作答 返回作答结果
	 * 
	 * @param id
	 * @param result
	 * @return
	 */
	// ReplyAnswerView answer(int id, String result) throws NoteException;

	/**
	 * 结束答题
	 * 
	 * @param str
	 */
	int endAnswer(String str) throws NoteException;

}
