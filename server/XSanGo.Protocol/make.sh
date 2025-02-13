#!/bin/sh

if [ ! -d "$ICE_HOME" ]; then
	echo "Can't get ICE_HOME"
	exit
fi

rm -f XSanGo.Client.Net/generated/slice/*

"${ICE_HOME}/bin/slice2cs" XSanGo.Client.Net/slice/ArenaRank.ice 		-I "${ICE_HOME}/slice" --output-dir XSanGo.Client.Net/generated/slice
"${ICE_HOME}/bin/slice2cs" XSanGo.Client.Net/slice/Chat.ice 		 	-I "${ICE_HOME}/slice" --output-dir XSanGo.Client.Net/generated/slice
"${ICE_HOME}/bin/slice2cs" XSanGo.Client.Net/slice/Common.ice 	 		-I "${ICE_HOME}/slice" --output-dir XSanGo.Client.Net/generated/slice
"${ICE_HOME}/bin/slice2cs" XSanGo.Client.Net/slice/Copy.ice 		 	-I "${ICE_HOME}/slice" --output-dir XSanGo.Client.Net/generated/slice
"${ICE_HOME}/bin/slice2cs" XSanGo.Client.Net/slice/Equip.ice 	 		-I "${ICE_HOME}/slice" --output-dir XSanGo.Client.Net/generated/slice
"${ICE_HOME}/bin/slice2cs" XSanGo.Client.Net/slice/ExceptionDef.ice 	-I "${ICE_HOME}/slice" --output-dir XSanGo.Client.Net/generated/slice
"${ICE_HOME}/bin/slice2cs" XSanGo.Client.Net/slice/Formation.ice  		-I "${ICE_HOME}/slice" --output-dir XSanGo.Client.Net/generated/slice
"${ICE_HOME}/bin/slice2cs" XSanGo.Client.Net/slice/GameSession.ice  	-I "${ICE_HOME}/slice" --output-dir XSanGo.Client.Net/generated/slice
"${ICE_HOME}/bin/slice2cs" XSanGo.Client.Net/slice/Item.ice 		 	-I "${ICE_HOME}/slice" --output-dir XSanGo.Client.Net/generated/slice
"${ICE_HOME}/bin/slice2cs" XSanGo.Client.Net/slice/ItemChip.ice 	 	-I "${ICE_HOME}/slice" --output-dir XSanGo.Client.Net/generated/slice
"${ICE_HOME}/bin/slice2cs" XSanGo.Client.Net/slice/Role.ice 		 	-I "${ICE_HOME}/slice" --output-dir XSanGo.Client.Net/generated/slice
"${ICE_HOME}/bin/slice2cs" XSanGo.Client.Net/slice/Shop.ice 		 	-I "${ICE_HOME}/slice" --output-dir XSanGo.Client.Net/generated/slice
"${ICE_HOME}/bin/slice2cs" XSanGo.Client.Net/slice/Sns.ice 		 		-I "${ICE_HOME}/slice" --output-dir XSanGo.Client.Net/generated/slice
"${ICE_HOME}/bin/slice2cs" XSanGo.Client.Net/slice/TimeBattle.ice 		-I "${ICE_HOME}/slice" --output-dir XSanGo.Client.Net/generated/slice
"${ICE_HOME}/bin/slice2cs" XSanGo.Client.Net/slice/Trader.ice 			-I "${ICE_HOME}/slice" --output-dir XSanGo.Client.Net/generated/slice
