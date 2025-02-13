// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroAdmireSummonEvent implements IHeroAdmireSummon{
   private IEventDispatcher dispatcher;
   public HeroAdmireSummonEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onSummon(int heroId) {
      this.dispatcher.emit(IHeroAdmireSummon.class,new Object[]{heroId});
   }

}
