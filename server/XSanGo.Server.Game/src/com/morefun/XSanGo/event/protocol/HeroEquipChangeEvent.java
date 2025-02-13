// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroEquipChangeEvent implements IHeroEquipChange{
   private IEventDispatcher dispatcher;
   public HeroEquipChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onHeroEquipChange(com.morefun.XSanGo.hero.IHero hero,com.morefun.XSanGo.equip.EquipItem equip) {
      this.dispatcher.emit(IHeroEquipChange.class,new Object[]{hero,equip});
   }

}
