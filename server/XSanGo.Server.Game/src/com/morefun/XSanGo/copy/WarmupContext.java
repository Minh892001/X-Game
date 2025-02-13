/**
 * 
 */
package com.morefun.XSanGo.copy;

import com.XSanGo.Protocol.WarmupView;

/**
 * 副本热身赛的上下文，每次请求副本详情会产生一个上下文对象
 * 
 * @author sulingyun
 *
 */
public class WarmupContext {
	private WarmupState state;
	private WarmupView view;
	private String movieId;
	private String snapshotRoleId;

	public WarmupView getView() {
		return view;
	}

	public void setView(WarmupView view) {
		this.view = view;
	}

	public WarmupState getState() {
		return state;
	}

	public void setState(WarmupState state) {
		this.state = state;
	}

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	public String getSnapshotRoleId() {
		return snapshotRoleId;
	}

	public void setSnapshotRoleId(String snapshotRoleId) {
		this.snapshotRoleId = snapshotRoleId;
	}
}

enum WarmupState {
	Init, Battle, End,

}