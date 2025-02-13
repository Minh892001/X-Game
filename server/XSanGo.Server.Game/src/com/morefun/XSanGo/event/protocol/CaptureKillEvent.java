// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class CaptureKillEvent implements ICaptureKill{
   private IEventDispatcher dispatcher;
   public CaptureKillEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onCaptureKilled(int copyId,com.XSanGo.Protocol.CaptureView view) {
      this.dispatcher.emit(ICaptureKill.class,new Object[]{copyId,view});
   }

}
