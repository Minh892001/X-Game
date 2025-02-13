// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class YuanbaoChangeEvent implements IYuanbaoChange{
   private IEventDispatcher dispatcher;
   public YuanbaoChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onYuanbaoChange(int oldBind,int oldUnbind,int newBind,int newUnbind,int change) {
      this.dispatcher.emit(IYuanbaoChange.class,new Object[]{oldBind,oldUnbind,newBind,newUnbind,change});
   }

}
