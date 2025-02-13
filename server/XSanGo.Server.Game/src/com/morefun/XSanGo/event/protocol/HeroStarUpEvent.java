// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroStarUpEvent implements IHeroStarUp{
   private IEventDispatcher dispatcher;
   public HeroStarUpEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onHeroStarUp(com.morefun.XSanGo.hero.IHero hero,int beforeStar) {
      this.dispatcher.emit(IHeroStarUp.class,new Object[]{hero,beforeStar});
   }

}
