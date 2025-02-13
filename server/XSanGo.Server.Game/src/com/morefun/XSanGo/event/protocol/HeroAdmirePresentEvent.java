// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroAdmirePresentEvent implements IHeroAdmirePresent{
   private IEventDispatcher dispatcher;
   public HeroAdmirePresentEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onPresent(int heroValue,java.lang.String itemId,int num) {
      this.dispatcher.emit(IHeroAdmirePresent.class,new Object[]{heroValue,itemId,num});
   }

}
