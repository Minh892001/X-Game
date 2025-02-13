// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class CollectHeroSoulDoRefreshEvent implements ICollectHeroSoulDoRefresh{
   private IEventDispatcher dispatcher;
   public CollectHeroSoulDoRefreshEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onDoRefresh(int type,int cost) {
      this.dispatcher.emit(ICollectHeroSoulDoRefresh.class,new Object[]{type,cost});
   }

}
