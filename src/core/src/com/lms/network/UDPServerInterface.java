package com.lms.network;

import java.net.DatagramPacket;
import java.net.InetAddress;

public interface UDPServerInterface {

	/**
	 * Use for UDPServer
	 *
	 * @param Address
	 * @param port
	 * @param msg
	 */

	public void sendMsg(InetAddress Address, int port, String msg);

	public void sendMsg(InetAddress Address, int port, String msg, String time);

	public void broadcast(String name, String msg);

	public void broadcast(String name, String msg, String time);

	public void addClient(String name, DatagramPacket incoming);

}
