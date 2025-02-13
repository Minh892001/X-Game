// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HaoqingbaoRecvRedPacketEvent implements IHaoqingbaoRecvRedPacket{
   private IEventDispatcher dispatcher;
   public HaoqingbaoRecvRedPacketEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onRecvRedPacket(int type,java.lang.String senderId,int num,java.lang.String redpacketID,java.lang.String recvId,int money,int lastNum) {
      this.dispatcher.emit(IHaoqingbaoRecvRedPacket.class,new Object[]{type,senderId,num,redpacketID,recvId,money,lastNum});
   }

}
