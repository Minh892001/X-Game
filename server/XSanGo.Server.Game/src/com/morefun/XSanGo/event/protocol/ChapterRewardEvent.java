// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ChapterRewardEvent implements IChapterReward{
   private IEventDispatcher dispatcher;
   public ChapterRewardEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onGetChapterReward(int chapterId,int level,java.lang.String tcCode) {
      this.dispatcher.emit(IChapterReward.class,new Object[]{chapterId,level,tcCode});
   }

}
