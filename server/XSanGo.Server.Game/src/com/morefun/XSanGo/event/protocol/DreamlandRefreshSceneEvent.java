// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class DreamlandRefreshSceneEvent implements IDreamlandRefreshScene{
   private IEventDispatcher dispatcher;
   public DreamlandRefreshSceneEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onRefreshScene(int oldSceneId,java.lang.String lastRefreshTime) {
      this.dispatcher.emit(IDreamlandRefreshScene.class,new Object[]{oldSceneId,lastRefreshTime});
   }

}
