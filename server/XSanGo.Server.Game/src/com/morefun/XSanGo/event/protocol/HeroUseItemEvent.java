// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroUseItemEvent implements IHeroUseItem{
   private IEventDispatcher dispatcher;
   public HeroUseItemEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onItemUseForHero(java.lang.String itemTemplate,int count,com.morefun.XSanGo.hero.IHero hero) {
      this.dispatcher.emit(IHeroUseItem.class,new Object[]{itemTemplate,count,hero});
   }

}
