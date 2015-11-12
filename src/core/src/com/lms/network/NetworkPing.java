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
		ping = new Thread(
			() -> {
				while(true) {
					ping();
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		);
		
		ping.start();
	}
	
	public static void addPingTime(long t) {
		NetworkPing.pingTime = t;
		NetworkPing.sumPingTime += t;
		NetworkPing.countPing += 1;
	}
	
	private static void ping() {
		avgPingTime = (sumPingTime / countPing) / 1000.0f;
		countPing = 0;
		sumPingTime = 0;
	}
	
	public static float getPing() {
		return avgPingTime;
	}
	
	public static String getString() {
		return String.format("Ping %.2f ms. | avg %.6f ms.", pingTime / 1000.0, avgPingTime);
	}
}
