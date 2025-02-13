#ifndef  _EXCEPTIONDEF_ICE_
#define  _EXCEPTIONDEF_ICE_

#include "Common.ice"

module com{
	module XSanGo{
		module Protocol{
      		//铜钱不足
      		exception NotEnoughMoneyException{
      			int margin;
      		};
        
         	exception NotEnoughYuanBaoException{
         		int margin;
         	};
         	
         	exception NoteException{
         		string  reason ;
         	};
         	
         	exception NoFactionException{
         	};
         	exception NoGroupException{
         	};
         	
         	exception NotEnoughException{
         		CurrencyType type;
         	};
      };
        
    };
      
};

#endif