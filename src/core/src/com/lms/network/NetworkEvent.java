package com.lms.network;

import java.net.DatagramPacket;

abstract public class NetworkEvent {
	
	NetworkManage nm;
	NetworkServerAbstract ns;
	public NetworkEvent(NetworkManage nm, NetworkServerAbstract ns) {
		this.nm = nm;
		this.ns = ns;
	}
	
	abstract public byte headerCode();
	
	abstract public void process(String data);
	
	abstract public void processServer(String data, DatagramPacket incoming, String time);
	
}
