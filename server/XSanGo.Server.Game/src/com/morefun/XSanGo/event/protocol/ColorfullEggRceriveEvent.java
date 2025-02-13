// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ColorfullEggRceriveEvent implements IColorfullEggRcerive{
   private IEventDispatcher dispatcher;
   public ColorfullEggRceriveEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onReceive(java.lang.String item,int num) {
      this.dispatcher.emit(IColorfullEggRcerive.class,new Object[]{item,num});
   }

}
