// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class VipLevelUpEvent implements IVipLevelUp{
   private IEventDispatcher dispatcher;
   public VipLevelUpEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onVipLevelUp(int newLevel) {
      this.dispatcher.emit(IVipLevelUp.class,new Object[]{newLevel});
   }

}
