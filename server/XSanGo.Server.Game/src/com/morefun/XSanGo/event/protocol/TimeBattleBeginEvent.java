// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class TimeBattleBeginEvent implements ITimeBattleBegin{
   private IEventDispatcher dispatcher;
   public TimeBattleBeginEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBattleBegin(int id,boolean isClear,int junling) {
      this.dispatcher.emit(ITimeBattleBegin.class,new Object[]{id,isClear,junling});
   }

}
