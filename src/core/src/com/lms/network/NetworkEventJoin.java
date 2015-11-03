package com.lms.network;

import java.net.DatagramPacket;
import java.net.Socket;

import com.lms.api.PlayerAPI;
import com.lms.api.PlayerServerAPI;
import com.lms.entity.CoreEntity;
import com.lms.entity.SheepEntity;
import com.lms.game.LmsConfig;

public class NetworkEventJoin extends NetworkEvent{

	/**
	 * NetworkEventJoin
	 *  
	 */
	
	public static boolean isJoin = false;
	public static final byte headerCode = 0x01;
	
	public NetworkEventJoin(NetworkServerAbstract ns) {
		super(ns);
	}
	
	public NetworkEventJoin(NetworkManage nm) {
		super(nm);
	}
	
	public NetworkEventJoin(TCPServerInterface tcp) {
		super(tcp);
	}
	
	public byte headerCode() {
		return NetworkEventJoin.headerCode;
	}

	/**
	 * Data rule 
	 * NAME:TYPE:X:Y
	 * 
	 */
	@Override
	public void process(String data) {
		if(data.equals("Ok")) {
			NetworkEventJoin.isJoin = true;
			return;
		}
		String[] dat = data.split(":");
		PlayerAPI.add(dat[0], dat[1], Float.parseFloat(dat[2]), Float.parseFloat(dat[3]));
	}
	
	public static String createJoinMsg(String name, String type, float x, float y) {
		return String.format("%c%s:%s:%.0f:%.0f", headerCode, name, type, x, y);
	}

	@Override
	public void processServer(String data, DatagramPacket incoming, String time) {
		
		String[] dat = data.split(":");
		System.out.println("join: " + dat[0]);
		ns.addClient(dat[0], incoming);
		PlayerServerAPI.add(dat[0], dat[1], Float.parseFloat(dat[2]), Float.parseFloat(dat[3]));
		PlayerServerAPI.setLastConn(dat[0], System.currentTimeMillis());
		ns.broadcast(dat[0], createJoinMsg(dat[0], dat[1], Float.parseFloat(dat[2]), Float.parseFloat(dat[3])));
		ns.sendMsg(incoming.getAddress(), incoming.getPort(), String.format("%cOk", headerCode));
	
	}

	@Override
	public void processServer(String data, Socket client, String time) {
		// TODO Auto-generated method stub
		
	}

}
