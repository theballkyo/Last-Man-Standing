package com.lms.network;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Map.Entry;

import com.lms.api.PlayerData;
import com.lms.api.PlayerServerAPI;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class NetworkEventUpdate extends NetworkEvent {

	public static byte headerCode;

	/**
	 * Data rule NAME:X:Y:ANIMATION
	 *
	 */
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
		data.split(":");

	}

	@Override
	public void processServer(String data, Socket client, String time, TCPServerInterface tcp) {
		for (Entry<String, PlayerData> p : PlayerServerAPI.getAll().entrySet()) {
			p.getKey();
			p.getValue().getType();
			p.getValue();
			p.getValue();

			// tcp.sendMsg(client, NetworkEventJoin.createJoinMsg(name, type, x,
			// y));
		}
	}

	public static String createUpdateMsg() {
		return String.format("%c", NetworkEventUpdate.headerCode);
	}
}
