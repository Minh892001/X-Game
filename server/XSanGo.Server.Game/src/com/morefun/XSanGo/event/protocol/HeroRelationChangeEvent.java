// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroRelationChangeEvent implements IHeroRelationChange{
   private IEventDispatcher dispatcher;
   public HeroRelationChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onRelationChange(com.morefun.XSanGo.hero.IHero hero,int orignalRelationId,int oldLevel,int level) {
      this.dispatcher.emit(IHeroRelationChange.class,new Object[]{hero,orignalRelationId,oldLevel,level});
   }

}
