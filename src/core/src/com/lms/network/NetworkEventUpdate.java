package com.lms.network;

import java.net.DatagramPacket;

import com.lms.api.PlayerAPI;

public class NetworkEventUpdate extends NetworkEvent{

	public static final byte headerCode = 0x02;
	
	public NetworkEventUpdate(NetworkManage nm, NetworkServerAbstract ns) {
		super(nm, ns);
	}
	
	public byte headerCode() {
		return NetworkEventUpdate.headerCode;
	}

	/**
	 * Data rule
	 * NAME:X:Y:ANIMATION
	 * 
	 */
	@Override
	public void process(String data) {
		String[] dat = data.split(":");
		PlayerAPI.move(dat[0], Float.parseFloat(dat[1]), Float.parseFloat(dat[2]));
	}

	@Override
	public void processServer(String data, DatagramPacket incoming, String time) {
		// TODO Auto-generated method stub
		
	}

}
