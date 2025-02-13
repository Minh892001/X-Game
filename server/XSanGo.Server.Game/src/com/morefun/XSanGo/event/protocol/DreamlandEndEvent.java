// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class DreamlandEndEvent implements IDreamlandEnd{
   private IEventDispatcher dispatcher;
   public DreamlandEndEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onEndDreamland(int sceneId,int heroCount,int remainCount,int star,int challengeNum,java.lang.String items) {
      this.dispatcher.emit(IDreamlandEnd.class,new Object[]{sceneId,heroCount,remainCount,star,challengeNum,items});
   }

}
