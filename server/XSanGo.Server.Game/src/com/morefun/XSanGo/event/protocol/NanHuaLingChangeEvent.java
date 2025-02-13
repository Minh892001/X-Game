// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class NanHuaLingChangeEvent implements INanHuaLingChange{
   private IEventDispatcher dispatcher;
   public NanHuaLingChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onNanHuaLingChange(int value,int before,int after) {
      this.dispatcher.emit(INanHuaLingChange.class,new Object[]{value,before,after});
   }

}
