// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FormationPosChangeEvent implements IFormationPosChange{
   private IEventDispatcher dispatcher;
   public FormationPosChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFormationPositionChange(com.morefun.XSanGo.formation.IFormation formation,int pos,com.morefun.XSanGo.hero.IHero hero) {
      this.dispatcher.emit(IFormationPosChange.class,new Object[]{formation,pos,hero});
   }

}
