// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class SnsRelationChangeEvent implements ISnsRelationChange{
   private IEventDispatcher dispatcher;
   public SnsRelationChangeEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void relationChanged(java.lang.String target,com.morefun.XSanGo.sns.SNSType relationType,com.morefun.XSanGo.sns.SnsController.RelationChangeEventActionType actionType) {
      this.dispatcher.emit(ISnsRelationChange.class,new Object[]{target,relationType,actionType});
   }

}
