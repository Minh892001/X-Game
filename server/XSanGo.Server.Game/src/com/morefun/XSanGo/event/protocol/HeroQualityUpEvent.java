// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroQualityUpEvent implements IHeroQualityUp{
   private IEventDispatcher dispatcher;
   public HeroQualityUpEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onHeroQualityUp(com.morefun.XSanGo.hero.IHero hero,int beforeQualityLevel) {
      this.dispatcher.emit(IHeroQualityUp.class,new Object[]{hero,beforeQualityLevel});
   }

}
