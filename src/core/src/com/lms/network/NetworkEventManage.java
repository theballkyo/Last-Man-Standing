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
	TCPServerInterface tcp;
	
	public static enum Type {
		TCP,
		UDP
	}
	
	public NetworkEventManage(NetworkManage nm, Type type) {
		this.nm = nm;
		events = new HashMap<Byte, NetworkEvent>();
		switch(type) {
			case TCP: TcpClientLoad();
				break;
			case UDP: UdpClientLoad();
				break;
		}
	}

	public NetworkEventManage(NetworkServerAbstract ns) {
		this.ns = ns;
		events = new HashMap<Byte, NetworkEvent>();
		UdpServerLoad();
	}
	
	public NetworkEventManage(TCPServerInterface tcp) {
		this.tcp = tcp;
		events = new HashMap<Byte, NetworkEvent>();
		TcpServerLoad();
	}
	
	private void UdpClientLoad() {
		events.put(NetworkEventJoin.headerCode, new NetworkEventJoin(nm));
		events.put(NetworkEventUpdate.headerCode, new NetworkEventJoin(nm));
		events.put(NetworkEventPong.headerCode, new NetworkEventPong(nm));
		events.put(NetworkEventMove.headerCode, new NetworkEventMove(nm));
		events.put(NetworkEventRqList.headerCode, new NetworkEventRqList(nm));
		events.put(NetworkEventDisconnect.headerCode, new NetworkEventDisconnect(nm));
	}
	
	private void UdpServerLoad() {
		events.put(NetworkEventJoin.headerCode, new NetworkEventJoin(ns));
		events.put(NetworkEventUpdate.headerCode, new NetworkEventJoin(ns));
		events.put(NetworkEventPong.headerCode, new NetworkEventPong(ns));
		events.put(NetworkEventMove.headerCode, new NetworkEventMove(ns));
		events.put(NetworkEventRqList.headerCode, new NetworkEventRqList(ns));
		events.put(NetworkEventDisconnect.headerCode, new NetworkEventDisconnect(ns));
	}
	
	private void TcpClientLoad() {
		
	}

	private void TcpServerLoad() {
		
	}
	
	private void add(Byte headerCode, NetworkEvent ne) {
		events.put(headerCode, ne);
	}
	
	public NetworkEvent get(Byte header) {
		return events.get(header);
	}
}
