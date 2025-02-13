// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ArenaRankChangeEvent implements IArenaRankChange{
   private IEventDispatcher dispatcher;
   public ArenaRankChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onChange(java.lang.String opponentId,int from,int to,int type,int flag) {
      this.dispatcher.emit(IArenaRankChange.class,new Object[]{opponentId,from,to,type,flag});
   }

}
