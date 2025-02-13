// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ChangeChallengeMoneyEvent implements IChangeChallengeMoney{
   private IEventDispatcher dispatcher;
   public ChangeChallengeMoneyEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onChange(int before,int after,int change) {
      this.dispatcher.emit(IChangeChallengeMoney.class,new Object[]{before,after,change});
   }

}
