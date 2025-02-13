// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FormationBuffChangeEvent implements IFormationBuffChange{
   private IEventDispatcher dispatcher;
   public FormationBuffChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFormationBuffChange(com.morefun.XSanGo.formation.IFormation formation,com.morefun.XSanGo.item.FormationBuffItem book) {
      this.dispatcher.emit(IFormationBuffChange.class,new Object[]{formation,book});
   }

}
