// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AttendantResetEvent implements IAttendantReset{
   private IEventDispatcher dispatcher;
   public AttendantResetEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAttendantReset(com.morefun.XSanGo.hero.IHero hero,byte pos,int attendantItemid) {
      this.dispatcher.emit(IAttendantReset.class,new Object[]{hero,pos,attendantItemid});
   }

}
