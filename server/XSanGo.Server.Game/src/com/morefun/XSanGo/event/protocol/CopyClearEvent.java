// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class CopyClearEvent implements ICopyClear{
   private IEventDispatcher dispatcher;
   public CopyClearEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onClear(com.morefun.XSanGo.copy.SmallCopyT copyT,com.XSanGo.Protocol.CopyChallengeResultView mockView,java.util.List<com.XSanGo.Protocol.ItemView> additionList) {
      this.dispatcher.emit(ICopyClear.class,new Object[]{copyT,mockView,additionList});
   }

}
