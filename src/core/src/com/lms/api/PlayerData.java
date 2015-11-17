package com.lms.api;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.lms.buff.Buff;
import com.lms.entity.CoreEntity;
import com.lms.game.LmsConfig;
import com.lms.game.LmsConfig.GameType;

public class PlayerData {

	private float hp;

	public Vector2 pos;
	public Vector2 scale;
	public Vector2 speed;

	private int kill;

	public float speedRun;
	public float speedJump;

	public long lastUdpConn;
	public long lastTcpConn;

	private long id;

	private String name;
	private String type;
	private String currentAnimation;

	public String scene = "MainScene";

	private ArrayList<Buff> buffs;
	private ArrayList<BuffData> buffData;
	private boolean isWalk;
	private boolean isGod;
	private int tWalk = 0;
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
		buffs = new ArrayList<>();
		speed = new Vector2();
		buffData = new ArrayList<>();
	}

	public void setCoreEntity(CoreEntity entity) {
		this.entity = entity;
		speed = entity.speed;
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
			speed = entity.speed;
		}
		if (entity.getX() != pos.x) {
			entity.setWalk(true);
			tWalk = 0;
		}
		if (entity.getX() == pos.x) {
			tWalk += 1;
			if (tWalk > 3) {
				entity.setWalk(false);
			}
		}

		if (entity.getX() > pos.x) {
			scale.x = (-Math.abs(scale.x));
		} else if (entity.getX() < pos.x) {
			scale.x = Math.abs(scale.x);
		}

		entity.setX(pos.x);
		entity.setY(pos.y);

		entity.setScaleX(scale.x);
		entity.setScaleY(scale.y);

	}

	public String getCurrentAnimation() {
		return currentAnimation;
	}

	public void setCurrentAnimation(String currentAnimation) {
		this.currentAnimation = currentAnimation;
	}

	public boolean isWalk() {
		return isWalk;
	}

	public void setWalk(boolean r) {
		isWalk = r;
	}

	public boolean isGod() {
		return isGod;
	}

	public void setGodMode(long time) {
		isGod = true;
		new Thread(() -> {
			long endTime = System.currentTimeMillis() + time;
			Color c = entity.tc.color;
			Random r = new Random();
			while (System.currentTimeMillis() <= endTime) {
				c.set(r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat());
				try {
					Thread.sleep(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			c.set(1, 1, 1, 1);
			isGod = false;
		}).start();
	}

	public int getKill() {
		return kill;
	}

	public void setKill(int kill) {
		this.kill = kill;
	}

	public void addKill() {
		kill += 1;
	}

	public void addBuff(Buff buff) {
		buffs.add(buff);
	}

	public ArrayList<Buff> getAllBuff() {
		return buffs;
	}

	public long getId() {
		if (entity == null) {
			return -1;
		}
		return entity.getEntity().getId();
	}

	public Rectangle getRect() {
		return new Rectangle(pos.x, pos.y, entity.getWidth(), entity.getHeight());
	}

	public ArrayList<BuffData> getBuffData() {
		return buffData;
	}

	public void addBuffData(BuffData b) {
		buffData.add(b);
	}
}
