// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class TimeBattleEvent implements ITimeBattle{
   private IEventDispatcher dispatcher;
   public TimeBattleEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onPassBattle(int id,boolean isClear,int junling) {
      this.dispatcher.emit(ITimeBattle.class,new Object[]{id,isClear,junling});
   }

}
