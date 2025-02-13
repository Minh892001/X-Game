// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HaoqingbaoSendRedPacketEvent implements IHaoqingbaoSendRedPacket{
   private IEventDispatcher dispatcher;
   public HaoqingbaoSendRedPacketEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onSendRedPacket(int type,int total,int num,java.lang.String id,int before,int after) {
      this.dispatcher.emit(IHaoqingbaoSendRedPacket.class,new Object[]{type,total,num,id,before,after});
   }

}
