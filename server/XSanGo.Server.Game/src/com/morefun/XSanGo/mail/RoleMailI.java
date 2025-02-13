/**
 * 
 */
package com.morefun.XSanGo.mail;

import java.util.List;

import Ice.Current;

import com.XSanGo.Protocol.MailView;
import com.XSanGo.Protocol.NoteException;
import com.XSanGo.Protocol._RoleMailDisp;
import com.morefun.XSanGo.role.IRole;
import com.morefun.XSanGo.util.LuaSerializer;

/**
 * 邮件系统接口
 * 
 * @author 吕明涛
 * 
 */
public class RoleMailI extends _RoleMailDisp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7886789561258634097L;

	private IRole roleRt;

	public RoleMailI(IRole dragonRole) {
		this.roleRt = dragonRole;
	}

	/**
	 * 显示邮件列表
	 */
	@Override
	public String selectMailViewList(Current __current) throws NoteException {
		List<MailView> mailList = this.roleRt.getMailControler()
				.selectMailViewList();

		return LuaSerializer.serialize(mailList);
	}

	/**
	 * 查询单一邮件信息
	 */
	@Override
	public String selectMailView(String mailId, Current __current)
			throws NoteException {
		MailView mail = this.roleRt.getMailControler().selectMailView(mailId);

		return LuaSerializer.serialize(mail);
	}

	/**
	 * 标记已读邮件
	 */
	@Override
	public void markMail(String mailId, int state, Current __current)
			throws NoteException {
		this.roleRt.getMailControler().markMail(mailId, state);
	}

}
