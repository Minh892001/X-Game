#ifndef _CLIENTLOG_ICE_
#define _CLIENTLOG_ICE_

module com{ 
	module XSanGo{
		module Protocol{
			interface ClientLog {
				["ami"] void report(string type, int lvlId, string params);
			};
		};
	};
};
#endif