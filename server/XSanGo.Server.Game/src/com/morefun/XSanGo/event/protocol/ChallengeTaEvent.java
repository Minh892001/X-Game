// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ChallengeTaEvent implements IChallengeTa{
   private IEventDispatcher dispatcher;
   public ChallengeTaEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onChallengeTaWin(java.lang.String byChallengeId) {
      this.dispatcher.emit(IChallengeTa.class,new Object[]{byChallengeId});
   }

}
