// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FactionBattleBuyMarchCoolEvent implements IFactionBattleBuyMarchCool{
   private IEventDispatcher dispatcher;
   public FactionBattleBuyMarchCoolEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuyMarchCooling(int cdNum,java.lang.String coolingEndTime) {
      this.dispatcher.emit(IFactionBattleBuyMarchCool.class,new Object[]{cdNum,coolingEndTime});
   }

}
