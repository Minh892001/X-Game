// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FactionBattleMarchEvent implements IFactionBattleMarch{
   private IEventDispatcher dispatcher;
   public FactionBattleMarchEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onMarch(int oldStrongHoldId,int strongholdId,boolean isUseKit) {
      this.dispatcher.emit(IFactionBattleMarch.class,new Object[]{oldStrongHoldId,strongholdId,isUseKit});
   }

}
