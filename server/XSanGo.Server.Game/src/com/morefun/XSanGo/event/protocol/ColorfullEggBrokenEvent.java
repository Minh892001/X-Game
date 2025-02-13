// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ColorfullEggBrokenEvent implements IColorfullEggBroken{
   private IEventDispatcher dispatcher;
   public ColorfullEggBrokenEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBroken(int eggFlag,int count,java.lang.String itemId) {
      this.dispatcher.emit(IColorfullEggBroken.class,new Object[]{eggFlag,count,itemId});
   }

}
