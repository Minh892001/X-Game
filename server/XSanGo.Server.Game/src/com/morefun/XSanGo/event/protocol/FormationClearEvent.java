// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FormationClearEvent implements IFormationClear{
   private IEventDispatcher dispatcher;
   public FormationClearEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFormationClear(com.morefun.XSanGo.formation.IFormation formation) {
      this.dispatcher.emit(IFormationClear.class,new Object[]{formation});
   }

}
