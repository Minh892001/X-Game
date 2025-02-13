// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class RoleHeadAndBorderChangeEvent implements IRoleHeadAndBorderChange{
   private IEventDispatcher dispatcher;
   public RoleHeadAndBorderChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onRoleHeadChange(java.lang.String old,java.lang.String headAndBorder) {
      this.dispatcher.emit(IRoleHeadAndBorderChange.class,new Object[]{old,headAndBorder});
   }

}
