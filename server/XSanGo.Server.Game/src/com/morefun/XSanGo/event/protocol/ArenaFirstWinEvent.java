// This class is automatically generated. Any changes will be erased.

package com.morefun.XSanGo.event.protocol;

import java.util.*;
import java.lang.reflect.*;

import net.sf.signalslot_apt.*;

@SuppressWarnings("all")
public class ArenaFirstWinEvent implements IArenaFirstWin{
   private IEventDispatcher dispatcher;
   public ArenaFirstWinEvent(IEventDispatcher dispatcher) {
      this.dispatcher = dispatcher;
   }

   public  final void onFirstWin() {
      this.dispatcher.emit(IArenaFirstWin.class,new Object[]{});
   }

}
