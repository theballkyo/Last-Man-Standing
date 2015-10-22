package net.lastman.network.core;

import java.util.Scanner;

public class MainClient {
	public static void main(String[] args) {

		GameClient g1 = new GameClient();
		g1.run();
		while(true) {
			if(g1.isClose())
				break;
			Scanner s = new Scanner(System.in);
	
			int inp = s.nextInt();
			g1.ping();	
		}

	}
}
