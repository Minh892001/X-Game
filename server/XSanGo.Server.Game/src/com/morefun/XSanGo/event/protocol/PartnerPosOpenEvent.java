// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class PartnerPosOpenEvent implements IPartnerPosOpen{
   private IEventDispatcher dispatcher;
   public PartnerPosOpenEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onPartnerPositionChange(int pos,int id,java.lang.String specialHeroCode) {
      this.dispatcher.emit(IPartnerPosOpen.class,new Object[]{pos,id,specialHeroCode});
   }

}
