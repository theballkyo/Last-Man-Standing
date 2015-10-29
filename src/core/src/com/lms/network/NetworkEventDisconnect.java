package com.lms.network;

import java.net.DatagramPacket;

import com.lms.api.PlayerAPI;
import com.lms.api.PlayerServerAPI;

public class NetworkEventDisconnect extends NetworkEvent{

	/**
	 * Remove player on player is disconnected
	 * 
	 */
	
	public static Byte headerCode = 0x06;
	
	public NetworkEventDisconnect(NetworkManage nm, NetworkServerAbstract ns) {
		super(nm, ns);
	}

	@Override
	public byte headerCode() {
		return 0;
	}

	/**
	 * Data rule
	 * NAME
	 */
	@Override
	public void process(String data) {
		System.out.println("Get disconnected");
		PlayerAPI.remove(data);
	}

	@Override
	public void processServer(String data, DatagramPacket incoming, String time) {
		PlayerServerAPI.remove(data);
		ns.broadcast(data, removeMsg(data));
	}

	public static String removeMsg(String name) {
		return String.format("%c%s", headerCode, name);
	}
}
