// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class LotteryThrowBallEvent implements ILotteryThrowBall{
   private IEventDispatcher dispatcher;
   public LotteryThrowBallEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onThrowBall(int type,int frontGridId,int curGridId,int throwPoint,int frontScore,int frontSpecialScore,int addScore,int addSpecialScore,int isCycle,int cycleNum,int throwNum,int autoNum) {
      this.dispatcher.emit(ILotteryThrowBall.class,new Object[]{type,frontGridId,curGridId,throwPoint,frontScore,frontSpecialScore,addScore,addSpecialScore,isCycle,cycleNum,throwNum,autoNum});
   }

}
