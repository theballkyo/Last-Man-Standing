package com.lms.network;

import java.util.HashMap;

import com.sun.org.apache.xerces.internal.impl.dtd.models.CMAny;

import net.lastman.network.core.ClientNetwork;

public class NetworkManage implements Runnable{

	ClientNetwork cn;
	
	private final byte JOIN = 0x00;
	private final byte NEW_ENTITY = 0x01;
	private final byte MOVE = 0x02;
	private final byte ATK = 0x03;
	private final byte JON = 0x04;
	
	private HashMap<Byte, NetworkEvent> events;
	
	public NetworkManage(ClientNetwork cn) {
		this.cn = cn;
		events = new HashMap<Byte, NetworkEvent>();
		load();
	}
	
	private void load() {
		events.put(NetworkEventJoin.headerCode, new NetworkEventJoin());
	}
	
	@Override
	public void run() {
		cn.start();
		while(true) {
			listener();
		}
	}
	
	private void listener() {
		String msg = cn.readMsg();
		byte header = msg.getBytes()[0];
		String data = new String(msg.getBytes(), 1, msg.length());
		
		if(header == JOIN) {
			
		}
	}
	
	public void addEvent() {
		
	}
	
	public void sendMsg(String msg) {
		cn.sendMsg(msg);
	}
}
