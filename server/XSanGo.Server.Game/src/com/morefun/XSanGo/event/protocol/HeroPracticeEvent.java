// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroPracticeEvent implements IHeroPractice{
   private IEventDispatcher dispatcher;
   public HeroPracticeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onHeroPractice(com.morefun.XSanGo.hero.IHero hero,int index,java.lang.String prop,int color,int oldLevel,int oldExp,int addExp,int newLevel,int newExp,int sumGx) {
      this.dispatcher.emit(IHeroPractice.class,new Object[]{hero,index,prop,color,oldLevel,oldExp,addExp,newLevel,newExp,sumGx});
   }

}
