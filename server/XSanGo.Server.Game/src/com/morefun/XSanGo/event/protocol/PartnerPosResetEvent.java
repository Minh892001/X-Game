// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class PartnerPosResetEvent implements IPartnerPosReset{
   private IEventDispatcher dispatcher;
   public PartnerPosResetEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onPartnerPositionReset(int pos,int id,java.lang.String specialHeroCode,int costNum) {
      this.dispatcher.emit(IPartnerPosReset.class,new Object[]{pos,id,specialHeroCode,costNum});
   }

}
