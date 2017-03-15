package com.lms.network;

import java.net.InetAddress;
import java.net.Socket;

import com.lms.api.PlayerServerAPI;
import com.lms.game.LmsConfig;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class NetworkEventPong extends NetworkEvent {

	public static byte headerCode;

	public static String createPingMsg(String name) {
		return String.format("%c%s", NetworkEventPong.headerCode, name);
	}

	public static String getMsg() {
		return String.format("%caaa", NetworkEventPong.headerCode);
	}

	@Override
	public void process(String data, UDPClient UDPcn) {
		UDPcn.sendMsg(NetworkEventPong.createPingMsg(LmsConfig.playerName));

	}

	@Override
	public void process(String data, TCPClient TCPcn) {
		// TCPcn.sendMsg("p");
	}

	@Override
	public void processServer(String data, InetAddress address, int port, String time, UDPServerInterface udp) {
		PlayerServerAPI.setUdpLastConn(data, System.currentTimeMillis());

	}

	@Override
	public void processServer(String data, Socket client, String time, TCPServerInterface tcp) {

	}
}
