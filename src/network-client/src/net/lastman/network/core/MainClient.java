package net.lastman.network.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class MainClient {
	/*
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
	*/
	public static void main(String args[])
    {
		final UDPClient sock = new UDPClient("127.0.0.1", 20510);
		sock.start();
		new Thread(new Runnable(){
			public void run() {
				while(true) {
					echo(sock.readMsg());
				}
			}
		}).start();
		sock.sendMsg("AA");
    }
     
    //simple function to echo data to terminal
    public static void echo(String msg)
    {
        System.out.println(msg);
    }
}
