// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class CaptureEmployEvent implements ICaptureEmploy{
   private IEventDispatcher dispatcher;
   public CaptureEmployEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onCaptureEmploy(com.XSanGo.Protocol.EmployCaptureResult result) {
      this.dispatcher.emit(ICaptureEmploy.class,new Object[]{result});
   }

}
