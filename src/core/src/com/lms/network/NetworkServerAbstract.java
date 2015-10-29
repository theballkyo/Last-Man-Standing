package com.lms.network;

import java.net.InetAddress;

public interface NetworkServerAbstract {
	
	/**
	 * Use for UDPServer
	 * @param Address
	 * @param port
	 * @param msg
	 */
	
	public void sendMsg(InetAddress Address, int port, String msg);
	public void broadcast(int id, String msg);
	
}
