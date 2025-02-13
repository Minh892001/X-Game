// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class FactionRenameEvent implements IFactionRename{
   private IEventDispatcher dispatcher;
   public FactionRenameEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFactionRename(java.lang.String factionId,java.lang.String oldName,java.lang.String newName) {
      this.dispatcher.emit(IFactionRename.class,new Object[]{factionId,oldName,newName});
   }

}
