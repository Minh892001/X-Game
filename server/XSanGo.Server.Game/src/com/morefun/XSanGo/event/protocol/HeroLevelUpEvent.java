// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroLevelUpEvent implements IHeroLevelUp{
   private IEventDispatcher dispatcher;
   public HeroLevelUpEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onHeroLevelUp(int tempId,int lvl) {
      this.dispatcher.emit(IHeroLevelUp.class,new Object[]{tempId,lvl});
   }

}
