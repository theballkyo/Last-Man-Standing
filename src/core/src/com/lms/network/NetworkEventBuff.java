package com.lms.network;

import java.net.InetAddress;
import java.net.Socket;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class NetworkEventBuff extends NetworkEvent {

	public static byte headerCode;
	@Override
	public void process(String data, UDPClient UDPcn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(String data, TCPClient TCPcn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processServer(String data, InetAddress address, int port, String time, UDPServerInterface udp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processServer(String data, Socket client, String time, TCPServerInterface tcp) {
		// TODO Auto-generated method stub

	}

	public static String createMsg(String name, String buffName, long time) {
		return String.format("%c%s", headerCode, name);
	}
}
