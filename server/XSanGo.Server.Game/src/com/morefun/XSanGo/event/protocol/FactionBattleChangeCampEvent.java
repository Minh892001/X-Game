// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FactionBattleChangeCampEvent implements IFactionBattleChangeCamp{
   private IEventDispatcher dispatcher;
   public FactionBattleChangeCampEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onChangeCamp(int campId,int dutyId) {
      this.dispatcher.emit(IFactionBattleChangeCamp.class,new Object[]{campId,dutyId});
   }

}
