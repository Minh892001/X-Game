package com.morefun.XSanGo.event.protocol;

import net.sf.signalslot_apt.annotations.signalslot;

@signalslot
public interface ISign {
	void sign(int day);
}
