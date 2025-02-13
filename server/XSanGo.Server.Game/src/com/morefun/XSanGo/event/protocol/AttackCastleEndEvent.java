// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AttackCastleEndEvent implements IAttackCastleEnd{
   private IEventDispatcher dispatcher;
   public AttackCastleEndEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAttackCastleEnd(int nodeIndex,byte heroCount,byte heroRemain,int star) {
      this.dispatcher.emit(IAttackCastleEnd.class,new Object[]{nodeIndex,heroCount,heroRemain,star});
   }

}
