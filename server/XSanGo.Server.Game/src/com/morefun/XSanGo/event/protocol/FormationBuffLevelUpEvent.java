// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FormationBuffLevelUpEvent implements IFormationBuffLevelUp{
   private IEventDispatcher dispatcher;
   public FormationBuffLevelUpEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFormationBuffLevelUp(com.morefun.XSanGo.item.FormationBuffItem buff,int money,int expDiff,int beforeLevel,int beforeExp,int afterLevel,int afterExp) {
      this.dispatcher.emit(IFormationBuffLevelUp.class,new Object[]{buff,money,expDiff,beforeLevel,beforeExp,afterLevel,afterExp});
   }

}
