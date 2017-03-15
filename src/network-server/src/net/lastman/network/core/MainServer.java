package net.lastman.network.core;

import com.lms.game.LmsConfig;
import com.lms.game.LmsConfig.GameType;

public class MainServer {

	public static void main(String[] args) {

		LmsConfig.gameType = GameType.Server;

		final UDPServer UDPserver = new UDPServer(LmsConfig.UDPport);

		final TCPServer TCPserver = new TCPServer(LmsConfig.TCPport);

		if (args.length > 1) {
			UDPserver.setDelay(Integer.parseInt(args[0]));
		}
		UDPserver.start();
		TCPserver.start();
		System.out.println("Server version: " + LmsConfig.version);
	}

}
