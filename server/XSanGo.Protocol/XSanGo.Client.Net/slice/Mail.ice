#ifndef  _MAIL_ICE_
#define  _MAIL_ICE_

#include "ExceptionDef.ice"
#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{
		
			//邮件类型
			enum MailType{
				Announce,	//公告邮件
				System,		//系统邮件	阅后即焚
				Private,	//私人邮件
			};
			
			//邮件状态 0：已删除，1:未读，2：已读, 3:提取
			enum MailState{
				delete,
				unRead,
				read,
				extract,
			};
			
			//邮件 列表
			struct MailView {
				string id;
				int type;		//邮件类型
				string senderId;
				string senderName;
				string title;
				string body;
				int state;			//0：已删除，1:未读，2：已读, 3:提取
				PropertySeq attach;	//附件中的物品ID和数量,包括金币和元宝货币之类
				string createTime;	//发送的邮件的时间
			};
			sequence<MailView> MailViewList; //邮件 列表 
			
			interface RoleMail {
				//查询 邮件列表
				//["ami"] MailViewList selectMailViewList() throws NoteException;
				["ami"] string selectMailViewList() throws NoteException;
				
				//查询 单一 的 邮件
				//["ami"] MailView selectMailView(string mailId) throws NoteException;
				["ami"] string selectMailView(string mailId) throws NoteException ;
				
				//标记 邮件 已读未读的状态
				//["ami"] void markMail(string mailId, int state) throws NoteException;
				["ami"] void markMail(string mailId, int state) throws NoteException;
			};
		};
	};
};

#endif