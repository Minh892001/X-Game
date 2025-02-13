/**
 * 
 */
package com.morefun.XSanGo.db.game;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 公会战个人荣誉排行榜
 * 
 * @author lixiongming
 *
 */
@Entity
@Table(name = "faction_member_rank")
public class FactionMemberRank implements Serializable {
	private static final long serialVersionUID = -6992910797442558795L;
	private String id;
	private String roleId;
	private Date createDate;
	private int honor;// 荣誉

	public FactionMemberRank() {

	}
	public FactionMemberRank(String id, String roleId, Date createDate,
			int honor) {
		super();
		this.id = id;
		this.roleId = roleId;
		this.createDate = createDate;
		this.honor = honor;
	}

	@Id
	@Column(name = "id", nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "role_id")
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "honor")
	public int getHonor() {
		return honor;
	}

	public void setHonor(int honor) {
		this.honor = honor;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
