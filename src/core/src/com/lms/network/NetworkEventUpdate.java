package com.lms.network;

public class NetworkEventUpdate extends NetworkEvent{

	public static final byte headerCode = 0x02;
	
	public NetworkEventUpdate(NetworkManage nm) {
		super(nm);
	}
	
	public byte headerCode() {
		return NetworkEventUpdate.headerCode;
	}

	@Override
	public void process(String data) {
		
	}

}
