package com.lms.network;

import java.net.DatagramPacket;

import com.lms.api.PlayerAPI;
import com.lms.api.PlayerServerAPI;
import com.lms.entity.CoreEntity;
import com.lms.entity.SheepEntity;

public class NetworkEventJoin extends NetworkEvent{

	/**
	 * NetworkEventJoin
	 *  
	 */
	
	public static final byte headerCode = 0x01;
	
	public NetworkEventJoin(NetworkManage nm, NetworkServerAbstract ns) {
		super(nm, ns);
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
		String[] dat = data.split(":");
		PlayerAPI.add(dat[0], dat[1], Float.parseFloat(dat[2]), Float.parseFloat(dat[3]));
	}
	
	public static String createJoinMsg(String name, String type, float x, float y) {
		return String.format("%c%s:%s:%.0f:%.0f", headerCode, name, type, x, y);
	}

	@Override
	public void processServer(String data, DatagramPacket incoming, String time) {
		
		String[] dat = data.split(":");
		System.out.println("broadcast from: " + dat[0]);
		ns.addClient(dat[0], incoming);
		PlayerServerAPI.add(dat[0], dat[1], Float.parseFloat(dat[2]), Float.parseFloat(dat[3]));
		ns.broadcast(dat[0], createJoinMsg(dat[0], dat[1], Float.parseFloat(dat[2]), Float.parseFloat(dat[3])));
	}

}
