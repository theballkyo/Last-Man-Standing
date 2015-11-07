package net.lastman.network.core;

import com.lms.game.LmsConfig;

public class MainServer {

	public static void main(String[] args) {

		final UDPServer UDPserver = new UDPServer(LmsConfig.UDPport);

		final TCPServer TCPserver = new TCPServer(LmsConfig.TCPport);

		if (args.length > 1) {
			UDPserver.setDelay(Integer.parseInt(args[0]));
		}

		System.out.println("UDP Server is started. - port=" + LmsConfig.UDPport);
		UDPserver.start();
		System.out.println("TCP Server is started. - port=" + LmsConfig.TCPport);
		TCPserver.start();
	}

}
