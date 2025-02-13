// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class RoleNameChangeEvent implements IRoleNameChange{
   private IEventDispatcher dispatcher;
   public RoleNameChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onRoleNameChange(java.lang.String old,java.lang.String name) {
      this.dispatcher.emit(IRoleNameChange.class,new Object[]{old,name});
   }

}
