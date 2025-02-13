// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class LadderFightEvent implements ILadderFight{
   private IEventDispatcher dispatcher;
   public LadderFightEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFight(java.lang.String rivalId,int resFlag,int fightStar) {
      this.dispatcher.emit(ILadderFight.class,new Object[]{rivalId,resFlag,fightStar});
   }

}
