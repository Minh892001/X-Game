// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class SignGiftEvent implements ISignGift{
   private IEventDispatcher dispatcher;
   public SignGiftEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onGift(java.lang.String id,int count,com.XSanGo.Protocol.ItemView[] items) {
      this.dispatcher.emit(ISignGift.class,new Object[]{id,count,items});
   }

}
