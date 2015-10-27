package net.lastman.network.core;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class ClientProfile {
	
	private String name;
	private int id;
	private DatagramPacket dp;
	
	public ClientProfile(int id, DatagramPacket dp) {
		this.id = id;
		this.dp = dp;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public InetAddress getAddress() {
		return dp.getAddress();
	}
	
	public int getPort() {
		return dp.getPort();
	}
}
