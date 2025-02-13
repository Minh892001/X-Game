// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class GuideCompleteEvent implements IGuideComplete{
   private IEventDispatcher dispatcher;
   public GuideCompleteEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onGuideCompleted(int guideId) {
      this.dispatcher.emit(IGuideComplete.class,new Object[]{guideId});
   }

}
