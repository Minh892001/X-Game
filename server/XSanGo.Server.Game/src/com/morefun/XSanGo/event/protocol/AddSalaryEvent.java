// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AddSalaryEvent implements IAddSalary{
   private IEventDispatcher dispatcher;
   public AddSalaryEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAddSalary(java.lang.String roleId,int yuanbao) {
      this.dispatcher.emit(IAddSalary.class,new Object[]{roleId,yuanbao});
   }

}
