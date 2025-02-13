// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class HeroArtifactChangeEvent implements IHeroArtifactChange{
   private IEventDispatcher dispatcher;
   public HeroArtifactChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onHeroArtifactChange(java.lang.String oldHero,java.lang.String newHero,int artifactId,int artifactLevel) {
      this.dispatcher.emit(IHeroArtifactChange.class,new Object[]{oldHero,newHero,artifactId,artifactLevel});
   }

}
