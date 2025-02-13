// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ArenaFightEvent implements IArenaFight{
   private IEventDispatcher dispatcher;
   public ArenaFightEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onArenaFight(int resFlag,int fromRank,int toRank,int sneerId,java.lang.String reward) {
      this.dispatcher.emit(IArenaFight.class,new Object[]{resFlag,fromRank,toRank,sneerId,reward});
   }

}
