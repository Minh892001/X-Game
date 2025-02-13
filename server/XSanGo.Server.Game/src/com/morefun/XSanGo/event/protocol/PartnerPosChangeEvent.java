// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class PartnerPosChangeEvent implements IPartnerPosChange{
   private IEventDispatcher dispatcher;
   public PartnerPosChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onHeroPositionChange(com.morefun.XSanGo.partner.IPartner partner,int pos,com.morefun.XSanGo.hero.IHero hero) {
      this.dispatcher.emit(IPartnerPosChange.class,new Object[]{partner,pos,hero});
   }

}
