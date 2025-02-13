// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroInheritEvent implements IHeroInherit{
   private IEventDispatcher dispatcher;
   public HeroInheritEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onHeroInherit(int baseHero,int inheritHero) {
      this.dispatcher.emit(IHeroInherit.class,new Object[]{baseHero,inheritHero});
   }

}
