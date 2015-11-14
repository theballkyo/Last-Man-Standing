package com.lms.network;

import java.net.InetAddress;
import java.net.Socket;

import com.lms.api.PlayerAPI;
import com.lms.api.PlayerServerAPI;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class NetworkEventDead extends NetworkEvent {

	public static byte headerCode;
	
	/**
	 * Data rule
	 * playerKill:playerDead
	 */
	@Override
	public void process(String data, UDPClient UDPcn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(String data, TCPClient TCPcn) {
		String[] dat = data.split(":");
		PlayerAPI.addKill(dat[0]);
		PlayerAPI.dead(dat[1]);
	}

	@Override
	public void processServer(String data, InetAddress address, int port, String time, UDPServerInterface udp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processServer(String data, Socket client, String time, TCPServerInterface tcp) {
		String[] dat = data.split(":");
		PlayerServerAPI.addKill(dat[0]);
		tcp.broadcast(createMsg(dat[0], dat[1]));
	}
	
	public static String createMsg(String playerKill, String playerDead) {
		return String.format("%c%s:%s", headerCode, playerKill, playerDead);
	}

}
