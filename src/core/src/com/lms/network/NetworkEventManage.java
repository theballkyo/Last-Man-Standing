package com.lms.network;

import java.util.HashMap;

public class NetworkEventManage {
	private HashMap<Byte, NetworkEvent> events;
	
	/**
	 * NetworkManage nm use for Client side !
	 * NetworkServerAbstract ns use for Server side !
	 */
	
	NetworkManage nm;
	NetworkServerAbstract ns;
	public NetworkEventManage(NetworkManage nm) {
		this.nm = nm;
		events = new HashMap<Byte, NetworkEvent>();
		load();
	}

	public NetworkEventManage(NetworkServerAbstract ns) {
		this.ns = ns;
		events = new HashMap<Byte, NetworkEvent>();
		load();
	}
	
	private void load() {
		events.put(NetworkEventJoin.headerCode, new NetworkEventJoin(nm, ns));
		events.put(NetworkEventUpdate.headerCode, new NetworkEventJoin(nm, ns));
		events.put(NetworkEventPong.headerCode, new NetworkEventPong(nm, ns));
		events.put(NetworkEventMove.headerCode, new NetworkEventMove(nm, ns));
		events.put(NetworkEventRqList.headerCode, new NetworkEventRqList(nm, ns));
	}
	
	public NetworkEvent get(Byte header) {
		return events.get(header);
	}
}
