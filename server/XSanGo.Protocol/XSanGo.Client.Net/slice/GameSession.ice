#ifndef  _GAMESESSION_ICE_
#define  _GAMESESSION_ICE_


#include "Glacier2/Session.ice"
#include "RoleF.ice"
   
module com
{  
     
    module XSanGo
    {  
       
      module Protocol
      {         
         interface GameSessionCallback{
        	 void timeSyncronized(string time);
        	 void close(string note);
         };
     
         interface GameSession  extends Glacier2::Session   
         {
        	 ["ami"] void destroyWarp(bool reconnectable);
         	["ami"] void setCallback(GameSessionCallback* cb);
         	["ami"] void syncTime();
         	["ami"] Role* getRole();
         };
      };
        
    };
      
};

#endif