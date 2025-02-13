// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AttackCastleBeginEvent implements IAttackCastleBegin{
   private IEventDispatcher dispatcher;
   public AttackCastleBeginEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAttackCastleBegin(int nodeIndex,java.lang.String roleId) {
      this.dispatcher.emit(IAttackCastleBegin.class,new Object[]{nodeIndex,roleId});
   }

}
