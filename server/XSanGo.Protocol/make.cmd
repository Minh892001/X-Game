@echo off

for /f "tokens=2*" %%a in ('reg query "HKEY_LOCAL_MACHINE\Software\ZeroC\Ice 3.5.1" /v InstallDir^|findstr /i "InstallDir"') do (set "ICE_ROOT=%%b")

if not exist "%ICE_ROOT%" (
	echo "Ice SDKÎ´°²×°"
	goto :eof
)

del XSanGo.Client.Net\generated\slice\* /Q
@echo on
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\ArenaRank.ice 	-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\Chat.ice 			-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\Common.ice 		-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\Copy.ice 			-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\Equip.ice 		-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\ExceptionDef.ice 	-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\Formation.ice 	-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\GameSession.ice 	-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\Item.ice 			-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\ItemChip.ice 		-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\Role.ice 			-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\Shop.ice 			-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\Sns.ice 			-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\TimeBattle.ice 	-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\Trader.ice 		-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\Ladder.ice 		-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice
"%ICE_ROOT%\bin\slice2cs" XSanGo.Client.Net\slice\Faction.ice 		-I "%ICE_ROOT%/slice" --output-dir XSanGo.Client.Net\generated\slice