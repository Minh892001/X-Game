// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroResetEvent implements IHeroReset{
   private IEventDispatcher dispatcher;
   public HeroResetEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onHeroReset(java.lang.String heroId,int release,com.XSanGo.Protocol.HeroView originView,com.XSanGo.Protocol.HeroConsumeView consumeBack) {
      this.dispatcher.emit(IHeroReset.class,new Object[]{heroId,release,originView,consumeBack});
   }

}
