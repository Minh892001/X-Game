// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HaoqingbaoGiveBackEvent implements IHaoqingbaoGiveBack{
   private IEventDispatcher dispatcher;
   public HaoqingbaoGiveBackEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onGiveBack(java.lang.String id,int num,int after) {
      this.dispatcher.emit(IHaoqingbaoGiveBack.class,new Object[]{id,num,after});
   }

}
