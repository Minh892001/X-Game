// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroPolishDivisionUpgradeEvent implements IHeroPolishDivisionUpgrade{
   private IEventDispatcher dispatcher;
   public HeroPolishDivisionUpgradeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onHeroPolishDivisionUpgrade(int beforeLvl,int afterLvl) {
      this.dispatcher.emit(IHeroPolishDivisionUpgrade.class,new Object[]{beforeLvl,afterLvl});
   }

}
