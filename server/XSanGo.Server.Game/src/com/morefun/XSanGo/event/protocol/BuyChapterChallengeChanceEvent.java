// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class BuyChapterChallengeChanceEvent implements IBuyChapterChallengeChance{
   private IEventDispatcher dispatcher;
   public BuyChapterChallengeChanceEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onBuyChapterChallengeChance(int chapterId,int price) {
      this.dispatcher.emit(IBuyChapterChallengeChance.class,new Object[]{chapterId,price});
   }

}
