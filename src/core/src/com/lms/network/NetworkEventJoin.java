package com.lms.network;

public class NetworkEventJoin extends NetworkEvent{

	public static final byte headerCode = 0x01;
	
	@Override
	public byte headerCode() {
		return headerCode;
	}

}
