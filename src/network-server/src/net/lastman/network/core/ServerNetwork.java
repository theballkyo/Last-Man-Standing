package net.lastman.network.core;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public interface ServerNetwork {
	
	public void start();
	public String readMsg(DatagramPacket incoming);
	public void sendMsg(InetAddress Address, int port, String msg);
	public DatagramSocket getSock();
	public void broadcast(String name, String msg);
}
