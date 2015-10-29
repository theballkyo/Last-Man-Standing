package com.lms.network;

import java.net.DatagramPacket;

import com.lms.api.PlayerServerAPI;
import com.lms.game.LmsConfig;

public class NetworkEventPong extends NetworkEvent{

	public static final Byte headerCode = 0x00;
	
	public NetworkEventPong(NetworkManage nm, NetworkServerAbstract ns) {
		super(nm, ns);
		System.out.println(headerCode);
	}

	@Override
	public byte headerCode() {
		return 0;
	}

	@Override
	public void process(String data) {
		System.out.println(LmsConfig.playerName+" Okay");
		nm.sendMsg(createPingMsg(LmsConfig.playerName));
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
}
