// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AttackCastleResetEvent implements IAttackCastleReset{
   private IEventDispatcher dispatcher;
   public AttackCastleResetEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAttackCastleReset(int resetCount) {
      this.dispatcher.emit(IAttackCastleReset.class,new Object[]{resetCount});
   }

}
