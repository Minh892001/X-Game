// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroBreakUpEvent implements IHeroBreakUp{
   private IEventDispatcher dispatcher;
   public HeroBreakUpEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onHeroBreakUp(com.morefun.XSanGo.hero.IHero hero,int beforeBreakLevel) {
      this.dispatcher.emit(IHeroBreakUp.class,new Object[]{hero,beforeBreakLevel});
   }

}
