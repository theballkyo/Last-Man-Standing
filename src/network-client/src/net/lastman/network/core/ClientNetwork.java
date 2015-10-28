package net.lastman.network.core;

import java.net.DatagramSocket;

public interface ClientNetwork {
	
	/**
	 * Start server
	 */
	public void start();
	
	/**
	 * read message from server
	 * @return String msg
	 */
	public String readMsg();
	
	/**
	 * Send message to server
	 * @param msg
	 */
	public void sendMsg(String msg);
	
	public DatagramSocket getSock();
	
	/**
	 * Check client is connected to server
	 * @return boolean isConnected
	 */
	public boolean isConnected();
	
}
