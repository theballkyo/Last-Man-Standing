package com.lms.network;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Map.Entry;

import com.lms.api.PlayerAPI;
import com.lms.api.PlayerData;
import com.lms.api.PlayerServerAPI;
import com.lms.game.LmsConfig;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class NetworkEventJoin extends NetworkEvent {

	public static byte headerCode;

	/**
	 * NetworkEventJoin
	 *
	 *
	 */

	public static boolean tcpJoin = false;
	public static boolean udpJoin = false;

	/**
	 * Data rule NAME:TYPE:X:Y
	 *
	 */
	@Override
	public void process(String data, UDPClient udp) {
		if (data.equals("Ok")) {
			NetworkEventJoin.udpJoin = true;
			return;
		}
		String[] dat = data.split(":");
	}

	@Override
	public void process(String data, TCPClient TCPcn) {
		if (data.equals("Ok")) {
			NetworkEventJoin.tcpJoin = true;
			return;
		}
		String[] dat = data.split(":");
		System.out.println(data);
		if (dat[0].equals(LmsConfig.playerName)) {
			return;
		}
		PlayerAPI.add(dat[0], dat[1], Float.parseFloat(dat[2]), Float.parseFloat(dat[3]));
		PlayerAPI.setKill(dat[0], Integer.parseInt(dat[4]));
	}

	@Override
	public void processServer(String data, InetAddress address, int port, String time, UDPServerInterface udp) {
		String[] dat = data.split(":");

		String name = dat[0];
		String type = dat[1];
		float x = Float.parseFloat(dat[2]);
		float y = Float.parseFloat(dat[3]);
		int kill = Integer.parseInt(dat[4]);
		
		PlayerServerAPI.add(name, type, x, y, kill);
		PlayerServerAPI.setUdpLastConn(name, System.currentTimeMillis());
		PlayerServerAPI.setUdpClient(name, address, port);
		udp.sendMsg(address, port, String.format("%cOk", headerCode));
	}

	@Override
	public void processServer(String data, Socket client, String time, TCPServerInterface tcp) {
		String[] dat = data.split(":");

		String name = dat[0];
		String type = dat[1];
		float x = Float.parseFloat(dat[2]);
		float y = Float.parseFloat(dat[3]);
		int kill = Integer.parseInt(dat[4]);
		
		PlayerServerAPI.add(name, type, x, y, kill);
		PlayerServerAPI.setTcpLastConn(dat[0], System.currentTimeMillis());
		PlayerServerAPI.setTcpClinet(name, client);
		tcp.broadcast(createJoinMsg(name, type, x, y, kill));
		tcp.sendMsg(client, String.format("%cOk", headerCode));

		for (Entry<String, PlayerData> e : PlayerServerAPI.getAll().entrySet()) {
			name = e.getValue().getName();
			type = e.getValue().getType();
			x = e.getValue().pos.x;
			y = e.getValue().pos.y;
			kill = e.getValue().getKill();
			tcp.broadcast(createJoinMsg(name, type, x, y, kill));
		}

	}
	
	public static String createJoinMsg(String name, String type, float x, float y, int kill) {
		return String.format("%c%s:%s:%.0f:%.0f:%d", headerCode, name, type, x, y, kill);
	}
}
