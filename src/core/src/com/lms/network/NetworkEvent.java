package com.lms.network;

import java.net.DatagramPacket;
import java.net.Socket;

abstract public class NetworkEvent {

	NetworkManage nm;
	NetworkServerAbstract ns;
	TCPServerInterface tcp;

	public NetworkEvent(NetworkServerAbstract ns) {
		this.ns = ns;
	}

	public NetworkEvent(NetworkManage nm) {
		this.nm = nm;
	}

	public NetworkEvent(TCPServerInterface tcp) {
		this.tcp = tcp;
	}

	abstract public byte headerCode();

	abstract public void process(String data);

	abstract public void processServer(String data, DatagramPacket incoming, String time);

	abstract public void processServer(String data, Socket client, String time);

}
