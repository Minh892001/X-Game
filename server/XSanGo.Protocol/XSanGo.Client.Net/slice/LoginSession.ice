#ifndef  _LOGINSESSION_ICE_
#define  _LOGINSESSION_ICE_

#include "Glacier2/Session.ice"
#include "ExceptionDef.ice"
// #include "LoginSessionF.ice"


 
module com{
	module XSanGo{
		module Protocol{
			struct AnnounceView{
				string title;
				string content;
			};

			 struct ServerItem{ 
             	int  id ;
             	int showId;
             	string  name ;
             	string  ip ;
             	int  port ;
             	int  state ;
             	bool  isNew ;
             	int targetId;
             };
             
             
         	sequence<ServerItem> ServerItemSeq; 
             	
             struct ServerList{ 
             	ServerItemSeq  lastLogin ;
             	ServerItemSeq  totalList ;
             };
         
         	/**/
			interface LoginCallback{
				["ami"] void announce(string content); 
			};
         
         	interface LoginSession  extends Glacier2::Session{
         		["ami"] void setCallback(LoginCallback* cb,string macAddress,int packageId)  ;
				["ami","amd"] void register(string account,string password)   throws NoteException  ;
				["ami","amd"] ServerList login(string account,string password)   throws NoteException  ;
				["ami","amd"] string selectServer(int serverId)   throws NoteException  ;
         	};
      };
        
    };
      
};

#endif