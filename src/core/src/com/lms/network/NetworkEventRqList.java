package com.lms.network;

import java.net.DatagramPacket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;

import com.lms.api.PlayerServerAPI;

public class NetworkEventRqList extends NetworkEvent{

	public static final byte headerCode = 0x05;
	
	public NetworkEventRqList(NetworkServerAbstract ns) {
		super(ns);
	}
	
	public NetworkEventRqList(NetworkManage nm) {
		super(nm);
	}
	
	public NetworkEventRqList(TCPServerInterface tcp) {
		super(tcp);
	}

	@Override
	public byte headerCode() {
		return 0;
	}

	@Override
	public void process(String data) {
		
	}

	@Override
	public synchronized void processServer(String data, DatagramPacket incoming, String time) {
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

	@Override
	public void processServer(String data, Socket client, String time) {
		// TODO Auto-generated method stub
		
	}

}
