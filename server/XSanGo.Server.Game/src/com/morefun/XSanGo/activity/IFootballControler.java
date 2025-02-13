package com.morefun.XSanGo.activity;

import com.XSanGo.Protocol.FootballBetLog;
import com.XSanGo.Protocol.FootballMatch;
import com.XSanGo.Protocol.FootballView;
import com.XSanGo.Protocol.NoteException;

/**
 * 欧洲杯活动
 * 
 * @author xiongming.li
 *
 */
public interface IFootballControler {
	FootballView getFootballView() throws NoteException;

	FootballMatch[] getCountryMatch(int countryId) throws NoteException;

	int buyTrophy() throws NoteException;

	int footballBet(int id, int countryId, int num) throws NoteException;

	int footballExchange(int id, int num) throws NoteException;

	FootballBetLog[] footballBetLogs() throws NoteException;
}
