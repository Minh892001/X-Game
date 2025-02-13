// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class LevelGiftEvent implements ILevelGift{
   private IEventDispatcher dispatcher;
   public LevelGiftEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onGetGift(com.morefun.XSanGo.activity.UpGiftT upGiftT) {
      this.dispatcher.emit(ILevelGift.class,new Object[]{upGiftT});
   }

}
