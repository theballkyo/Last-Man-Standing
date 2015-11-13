package com.lms.network;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Map.Entry;

import com.lms.api.PlayerAPI;
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
		String[] dat = data.split(":");
		// PlayerAPI.move(dat[0], Float.parseFloat(dat[1]), Float.parseFloat(dat[2]));

	}

	@Override
	public void processServer(String data, Socket client, String time, TCPServerInterface tcp) {
		for (Entry<String, PlayerData> p : PlayerServerAPI.getAll().entrySet()) {
			String name = p.getKey();
			String type = p.getValue().getType();
			float x = p.getValue().pos.x;
			float y = p.getValue().pos.y;

			tcp.sendMsg(client, NetworkEventJoin.createJoinMsg(name, type, x, y));
		}
	}

	public static String createUpdateMsg() {
		return String.format("%c", headerCode);
	}
}
