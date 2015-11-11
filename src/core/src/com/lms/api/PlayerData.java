package com.lms.api;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;

import com.badlogic.gdx.math.Vector2;
import com.lms.entity.CoreEntity;

public class PlayerData {
	
	private float hp;
	
	public Vector2 pos;
	
	public long lastUdpConn;
	public long lastTcpConn;
	
	private String name;
	private String type;

	/* For TCP */
	private Socket client;
	private int clientId;
	/* For UDP */
	private DatagramPacket udp;
	private InetAddress udpAddress;
	private int udpPort;
	
	// Entity player - for client
	private CoreEntity entity;

	public PlayerData(String name, String type, float x, float y) {
		this.name = name;
		this.type = type;
		this.pos = new Vector2(x, y);
	}
	
	public void setCoreEntity(CoreEntity entity) {
		this.entity = entity;
	}
	
	public CoreEntity getCoreEntity() {
		return this.entity;
	}

	public void setTcp(Socket client) {
		this.client = client;
	}

	public void setUdp(DatagramPacket udp) {
		this.udp = udp;
	}

	public void setUdp(InetAddress udpAddress, int udpPort) {
		this.udpAddress = udpAddress;
		this.udpPort = udpPort;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public InetAddress getUdpAddress() {
		return udpAddress;
	}
	
	public int getUdpPort() {
		return udpPort;
	}
	
	public Socket getTcpSocket() {
		return client;
	}
}
