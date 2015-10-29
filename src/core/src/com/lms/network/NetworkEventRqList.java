package com.lms.network;

import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.Map.Entry;

import com.lms.api.PlayerServerAPI;

public class NetworkEventRqList extends NetworkEvent{

	public static final byte headerCode = 0x05;
	
	public NetworkEventRqList(NetworkManage nm, NetworkServerAbstract ns) {
		super(nm, ns);
	}

	@Override
	public byte headerCode() {
		return 0;
	}

	@Override
	public void process(String data) {
		
	}

	@Override
	public void processServer(String data, DatagramPacket incoming, String time) {
		System.out.println("Server: RqList");
		HashMap<String, PlayerServerAPI> pl = PlayerServerAPI.getAll();
		for(Entry<String, PlayerServerAPI> p :pl.entrySet()) {
			String name = p.getKey();
			PlayerServerAPI dat = p.getValue();
			ns.sendMsg(incoming.getAddress(), incoming.getPort(), NetworkEventJoin.createJoinMsg(name, dat.getType(), dat.getX(), dat.getY()), time);
		}
	}
	
	public static String createRqListMsg() {
		return String.format("%c", headerCode);
	}

}
