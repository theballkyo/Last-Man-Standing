package com.lms.api;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;

import com.badlogic.gdx.math.Vector2;
import com.lms.entity.CoreEntity;
import com.lms.game.LmsConfig;
import com.lms.game.LmsConfig.GameType;

public class PlayerData {

	private float hp;

	public Vector2 pos;
	public Vector2 scale;

	public float speedRun;
	public float speedJump;

	public long lastUdpConn;
	public long lastTcpConn;

	private String name;
	private String type;

	public String scene = "MainScene";
	
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
		pos = new Vector2(x, y);
		scale = new Vector2(1, 1);
	}

	public void setCoreEntity(CoreEntity entity) {
		this.entity = entity;
	}

	public CoreEntity getCoreEntity() {
		return entity;
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

	/**
	 *
	 * Update data to CoreEntity
	 *
	 * Client use only !
	 */
	public void updateEntity() {
		if (LmsConfig.gameType == GameType.Server) {
			return;
		}

		if (entity == null) {
			entity = PlayerAPI.me.newEntity(type, name);
			entity.create();
			speedRun = entity.getSpeedRun();
			speedJump = entity.getSpeedJump();
		}

		entity.setX(pos.x);
		entity.setY(pos.y);

		entity.setScaleX(scale.x);
		entity.setScaleY(scale.y);
	}
}
