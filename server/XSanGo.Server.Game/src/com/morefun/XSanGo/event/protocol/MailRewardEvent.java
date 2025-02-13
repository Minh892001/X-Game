// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class MailRewardEvent implements IMailReward{
   private IEventDispatcher dispatcher;
   public MailRewardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onReward(java.lang.String mialTiltle,com.morefun.XSanGo.mail.AbsMailAttachObject[] attaches) {
      this.dispatcher.emit(IMailReward.class,new Object[]{mialTiltle,attaches});
   }

}
