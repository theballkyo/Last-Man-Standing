package com.lms.network;

import java.net.DatagramPacket;
import java.net.Socket;

import com.lms.api.PlayerServerAPI;
import com.lms.game.LmsConfig;

public class NetworkEventPong extends NetworkEvent {

	public static final Byte headerCode = 0x00;

	public NetworkEventPong(NetworkServerAbstract ns) {
		super(ns);
	}

	public NetworkEventPong(NetworkManage nm) {
		super(nm);
	}

	public NetworkEventPong(TCPServerInterface tcp) {
		super(tcp);
	}

	@Override
	public byte headerCode() {
		return 0;
	}

	@Override
	public void process(String data) {
		nm.TCPsendMsg(createPingMsg(LmsConfig.playerName));
	}

	@Override
	public void processServer(String data, DatagramPacket incoming, String time) {
		PlayerServerAPI.setLastConn(data, System.currentTimeMillis());
	}

	public static String createPingMsg(String name) {
		return String.format("%c%s", headerCode, name);
	}

	public static String getMsg() {
		return String.format("%caaa", headerCode);
	}

	@Override
	public void processServer(String data, Socket client, String time) {
		// TODO Auto-generated method stub

	}
}
