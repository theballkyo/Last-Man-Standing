package com.lms.network;

public class NetworkPing {

	private static float pingTime = 0;
	private static float avgPingTime = 0;
	private static float sumPingTime = 0;
	private static int countPing = 0;

	private static Thread ping;

	/**
	 *
	 * Set avg ping time (ms).
	 *
	 */

	public static void start() {
		NetworkPing.ping = new Thread(() -> {
			while (true) {
				NetworkPing.ping();
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		NetworkPing.ping.start();
	}

	public static void addPingTime(long t) {
		NetworkPing.pingTime = t;
		NetworkPing.sumPingTime += t;
		NetworkPing.countPing += 1;
	}

	private static void ping() {
		NetworkPing.avgPingTime = (NetworkPing.sumPingTime / NetworkPing.countPing) / 1000.0f;
		NetworkPing.countPing = 0;
		NetworkPing.sumPingTime = 0;
	}

	public static float getPing() {
		return NetworkPing.avgPingTime;
	}

	public static String getString() {
		return String.format("Ping %.2f ms. | avg %.6f ms.", NetworkPing.pingTime / 1000.0, NetworkPing.avgPingTime);
	}
}
