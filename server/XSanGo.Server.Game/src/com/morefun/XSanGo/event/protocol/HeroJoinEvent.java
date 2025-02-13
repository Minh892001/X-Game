// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroJoinEvent implements IHeroJoin{
   private IEventDispatcher dispatcher;
   public HeroJoinEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onHeroJoin(com.morefun.XSanGo.hero.IHero hero,com.morefun.XSanGo.common.HeroSource source) {
      this.dispatcher.emit(IHeroJoin.class,new Object[]{hero,source});
   }

}
