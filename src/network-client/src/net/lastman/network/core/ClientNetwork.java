package net.lastman.network.core;

import java.net.DatagramSocket;

public interface ClientNetwork {

	public void start();
	public String readMsg();
	public void sendMsg(String msg);
	public DatagramSocket getSock();
	
	
}
