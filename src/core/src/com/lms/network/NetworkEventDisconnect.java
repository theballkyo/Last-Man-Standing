package com.lms.network;

import java.net.InetAddress;
import java.net.Socket;

import com.lms.api.PlayerAPI;
import com.lms.api.PlayerServerAPI;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class NetworkEventDisconnect extends NetworkEvent {

	/**
	 * Remove player on player is disconnected
	 *
	 */
	public static byte headerCode;

	/**
	 * Data rule NAME
	 */
	@Override
	public void process(String data, TCPClient tcp) {
		System.out.println("Get disconnected");
		PlayerAPI.remove(data);
	}

	@Override
	public void process(String data, UDPClient UDPcn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processServer(String data, InetAddress address, int port, String time, UDPServerInterface udp) {
		PlayerServerAPI.remove(data);
		udp.broadcast(data, NetworkEventDisconnect.removeMsg(data));
	}

	@Override
	public void processServer(String data, Socket client, String time, TCPServerInterface tcp) {
		// TODO Auto-generated method stub

	}

	public static String removeMsg(String name) {
		return String.format("%c%s", NetworkEventDisconnect.headerCode, name);
	}

}
