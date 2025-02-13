// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroBaptizeUpgradeEvent implements IHeroBaptizeUpgrade{
   private IEventDispatcher dispatcher;
   public HeroBaptizeUpgradeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBaptizeUpgrade(java.lang.String heroId,int lvl) {
      this.dispatcher.emit(IHeroBaptizeUpgrade.class,new Object[]{heroId,lvl});
   }

}
