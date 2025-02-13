// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ShootScoreChangeEvent implements IShootScoreChange{
   private IEventDispatcher dispatcher;
   public ShootScoreChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onShootScoreChange(int shootType,boolean isFree,int beforeScore,int score,int afterScore,int totalScore) {
      this.dispatcher.emit(IShootScoreChange.class,new Object[]{shootType,isFree,beforeScore,score,afterScore,totalScore});
   }

}
