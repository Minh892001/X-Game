// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ArenaRevengeEvent implements IArenaRevenge{
   private IEventDispatcher dispatcher;
   public ArenaRevengeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFight(int resFlag,int roleRank,int rivalRank) {
      this.dispatcher.emit(IArenaRevenge.class,new Object[]{resFlag,roleRank,rivalRank});
   }

}
