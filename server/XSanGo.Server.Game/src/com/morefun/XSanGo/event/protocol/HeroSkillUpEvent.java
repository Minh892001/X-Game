// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroSkillUpEvent implements IHeroSkillUp{
   private IEventDispatcher dispatcher;
   public HeroSkillUpEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onHeroSkillUp(com.morefun.XSanGo.hero.IHero hero,java.lang.String name,int oldLevel,int newLevel) {
      this.dispatcher.emit(IHeroSkillUp.class,new Object[]{hero,name,oldLevel,newLevel});
   }

}
