// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ResetPracticeEvent implements IResetPractice{
   private IEventDispatcher dispatcher;
   public ResetPracticeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onResetPractice(com.morefun.XSanGo.hero.IHero hero,int index,java.lang.String oldName,int oldColor,int oldLevel,int oldExp,java.lang.String newName,int newColor) {
      this.dispatcher.emit(IResetPractice.class,new Object[]{hero,index,oldName,oldColor,oldLevel,oldExp,newName,newColor});
   }

}
