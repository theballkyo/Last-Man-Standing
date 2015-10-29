package com.lms.network;

public class NetworkEventPong extends NetworkEvent{

	public static final Byte headerCode = 0x00;
	
	public NetworkEventPong(NetworkManage nm, NetworkServerAbstract ns) {
		super(nm, ns);
		System.out.println(headerCode);
	}

	@Override
	public byte headerCode() {
		return 0;
	}

	@Override
	public void process(String data) {
		System.out.println("Client say Ok");
	}

	@Override
	public void processServer(String data) {
		System.out.println("Sv say Ok");
	}
	
	public static String getMsg() {
		return headerCode+"";
	}
}
