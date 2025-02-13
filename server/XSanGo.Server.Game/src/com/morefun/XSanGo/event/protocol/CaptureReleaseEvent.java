// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class CaptureReleaseEvent implements ICaptureRelease{
   private IEventDispatcher dispatcher;
   public CaptureReleaseEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onCaptureRelease(int copyId,com.XSanGo.Protocol.CaptureView view) {
      this.dispatcher.emit(ICaptureRelease.class,new Object[]{copyId,view});
   }

}
