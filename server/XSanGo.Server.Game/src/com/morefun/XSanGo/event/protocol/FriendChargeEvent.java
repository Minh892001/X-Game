// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FriendChargeEvent implements IFriendCharge{
   private IEventDispatcher dispatcher;
   public FriendChargeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onChargeFromFriend(com.morefun.XSanGo.vip.ChargeItemT template,java.lang.String friendAccount) {
      this.dispatcher.emit(IFriendCharge.class,new Object[]{template,friendAccount});
   }

}
