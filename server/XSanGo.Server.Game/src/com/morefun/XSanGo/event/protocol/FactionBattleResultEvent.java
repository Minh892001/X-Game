// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FactionBattleResultEvent implements IFactionBattleResult{
   private IEventDispatcher dispatcher;
   public FactionBattleResultEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onResult(int strongholdId,java.lang.String targetRole,boolean isRobot,int resultCode,boolean isWin,int badge,int cd,int kitsId,int evenkill,boolean isOccupy,int resultStrongholdId) {
      this.dispatcher.emit(IFactionBattleResult.class,new Object[]{strongholdId,targetRole,isRobot,resultCode,isWin,badge,cd,kitsId,evenkill,isOccupy,resultStrongholdId});
   }

}
