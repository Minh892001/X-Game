// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class AttendantChangeEvent implements IAttendantChange{
   private IEventDispatcher dispatcher;
   public AttendantChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onAttendantChange(com.morefun.XSanGo.hero.IHero hero,byte pos,com.morefun.XSanGo.hero.IHero attendant) {
      this.dispatcher.emit(IAttendantChange.class,new Object[]{hero,pos,attendant});
   }

}
