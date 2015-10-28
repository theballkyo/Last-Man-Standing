package com.lms.network;

abstract public class NetworkEvent {
	
	NetworkManage nm;
	
	public NetworkEvent(NetworkManage nm) {
		this.nm = nm;
	}
	
	abstract public byte headerCode();
	
	abstract public void process(String data);
	
}
