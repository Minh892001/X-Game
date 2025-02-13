// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class DreamlandBuyChallengeNumEvent implements IDreamlandBuyChallengeNum{
   private IEventDispatcher dispatcher;
   public DreamlandBuyChallengeNumEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuyChallengeNum(int challengeNum) {
      this.dispatcher.emit(IDreamlandBuyChallengeNum.class,new Object[]{challengeNum});
   }

}
