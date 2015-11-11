package com.lms.network;

import java.util.HashMap;
import java.util.Map.Entry;

public class NetworkEventManage {
	private HashMap<Byte, NetworkEvent> events;

	/**
	 * NetworkManage nm use for Client side ! NetworkServerAbstract ns use for
	 * Server side !
	 */

	public NetworkEventManage() {
		events = new HashMap<Byte, NetworkEvent>();
		init();
		// loads();
	}

	private void init() {
		
		NetworkEventJoin.headerCode = 0x01;
		NetworkEventDisconnect.headerCode = 0x02;
		NetworkEventPong.headerCode = 0x03;
		NetworkEventMove.headerCode = 0x04;
		NetworkEventAdd.headerCode = 0x05;
		NetworkEventUpdate.headerCode = 0x06;
		
		events.put(NetworkEventJoin.headerCode, new NetworkEventJoin());
		events.put(NetworkEventDisconnect.headerCode, new NetworkEventDisconnect());
		events.put(NetworkEventPong.headerCode, new NetworkEventPong());
		events.put(NetworkEventMove.headerCode, new NetworkEventMove());
		events.put(NetworkEventAdd.headerCode, new NetworkEventAdd());
		events.put(NetworkEventUpdate.headerCode, new NetworkEventUpdate());
	}

	private void add(Byte headerCode, NetworkEvent ne) {
		events.put(headerCode, ne);
	}

	public NetworkEvent get(Byte header) {
		return events.get(header);
	}
}
