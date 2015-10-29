package com.lms.network;

import com.lms.api.PlayerAPI;
import com.lms.entity.CoreEntity;
import com.lms.entity.SheepEntity;

public class NetworkEventJoin extends NetworkEvent{

	/**
	 * NetworkEventJoin
	 *  
	 */
	
	public static final byte headerCode = 1;
	
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
	
	public static String createJoinMsg(String name, float x, float y) {
		return String.format("%c%s %.0f %.0f", headerCode, name, x, y);
	}

	@Override
	public void processServer(String data) {
		// TODO Auto-generated method stub
		
	}

}
