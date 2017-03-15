package com.lms.api;

import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;

public class PlayerServerAPI {

	/**
	 *
	 * Use for Server side
	 *
	 */

	private static HashMap<String, PlayerData> playerList = new HashMap<>();

	private static Object obj1 = new Object();

	public static void add(String name, String type, float x, float y) {
		PlayerServerAPI.add(name, type, x, y, 0);
	}

	public synchronized static void add(String name, String type, float x, float y, int kill) {
		if (PlayerServerAPI.playerList.get(name) != null) {
			return;
		}

		PlayerServerAPI.playerList.put(name, new PlayerData(name, type, x, y));

		PlayerServerAPI.playerList.get(name).setKill(kill);

	}

	public static void add(int clientId, String type, float x, float y) {

	}

	public static void remove(String name) {
		PlayerServerAPI.playerList.remove(name);
	}

	public static void remove(int clientId) {

	}

	public static boolean remove(Socket client) {
		for (Entry<String, PlayerData> p : PlayerServerAPI.playerList.entrySet()) {
			if (p.getValue().getTcpSocket() == client) {
				System.out.println(p.getKey());
				PlayerServerAPI.playerList.remove(p.getKey());
				return true;
			}
		}
		return false;
	}

	public static void update(String name, float x, float y) {

		PlayerData pl = PlayerServerAPI.playerList.get(name);
		if (pl == null) {
			return;
		}

		pl.pos.x = x;
		pl.pos.y = y;
		// pl.getCoreEntity().setAnimation(isAnimation);
	}

	public static void setTcpLastConn(String name, long time) {
		PlayerData pl = PlayerServerAPI.playerList.get(name);
		if (pl == null) {
			return;
		}
		pl.lastTcpConn = time;
	}

	public static void setUdpLastConn(String name, long time) {
		PlayerData pl = PlayerServerAPI.playerList.get(name);
		if (pl == null) {
			return;
		}
		pl.lastUdpConn = time;
	}

	public static void setUdpClient(String name, InetAddress udpAddress, int udpPort) {
		PlayerData pl = PlayerServerAPI.playerList.get(name);
		if (pl == null) {
			return;
		}

		pl.setUdp(udpAddress, udpPort);
	}

	public static void setTcpClinet(String name, Socket client) {
		PlayerData pl = PlayerServerAPI.playerList.get(name);
		if (pl == null) {
			return;
		}
		pl.setTcp(client);
	}

	public static String getName(Socket client) {
		for (Entry<String, PlayerData> p : PlayerServerAPI.playerList.entrySet()) {
			if (p.getValue().getTcpSocket() == client) {
				return p.getKey();
			}
		}
		return "";
	}

	public static long getTcpLastConn(String name) {
		return PlayerServerAPI.playerList.get(name).lastTcpConn;
	}

	public static long getUdpLastConn(String name) {
		return PlayerServerAPI.playerList.get(name).lastUdpConn;
	}

	public final static HashMap<String, PlayerData> getAll() {
		synchronized (obj1) {
			@SuppressWarnings("unchecked")
			HashMap<String, PlayerData> clone = (HashMap<String, PlayerData>) PlayerServerAPI.playerList.clone();
			return clone;
		}

	}

	public static void addKill(String name) {
		PlayerData p = PlayerServerAPI.playerList.get(name);
		if (p == null) {
			return;
		}
		p.addKill();
	}

	public static InetAddress udpAddress(String name) {
		return PlayerServerAPI.playerList.get(name).getUdpAddress();
	}

	public static int udpPort(String name) {
		return PlayerServerAPI.playerList.get(name).getUdpPort();
	}

	public static boolean isNameSame(String name) {
		return PlayerServerAPI.playerList.containsKey(name);
	}

	public static PlayerData get(String name) {
		return PlayerServerAPI.playerList.get(name);
	}

	public static void addBuffData(String name, BuffData b) {
		PlayerData pl = PlayerServerAPI.playerList.get(name);
		if (pl == null) {
			return;
		}
		pl.addBuffData(b);
	}
}
