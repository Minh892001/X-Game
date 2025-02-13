// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class DreamlandSceneStarAwardEvent implements IDreamlandSceneStarAward{
   private IEventDispatcher dispatcher;
   public DreamlandSceneStarAwardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onDrawSceneStarAward(int sceneId,int star,java.lang.String items) {
      this.dispatcher.emit(IDreamlandSceneStarAward.class,new Object[]{sceneId,star,items});
   }

}
