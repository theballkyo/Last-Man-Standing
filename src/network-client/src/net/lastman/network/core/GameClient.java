package net.lastman.network.core;

import java.net.*;
import java.io.*;

public class GameClient {
	
	Socket client;
	
	public GameClient() {
		
	}
	
	public void run() {
		String serverName = "127.0.0.1";
		int port = 20156;
		try
	      {
	         System.out.println("Connecting to " + serverName +
			 " on port " + port);
	         client = new Socket(serverName, port);
	         System.out.println("Client connected to " 
			 + client.getRemoteSocketAddress());
	         OutputStream outToServer = client.getOutputStream();
	         DataOutputStream out = new DataOutputStream(outToServer);
	         out.writeUTF("Hello from "
	                      + client.getLocalSocketAddress());
	         InputStream inFromServer = client.getInputStream();
	         DataInputStream in =
	                        new DataInputStream(inFromServer);
	         System.out.println("Server says " + in.readUTF());
	      }catch(IOException e)
	      {
	         e.printStackTrace();
	      }
	}
	
	public void ping() {
		try
	      {
	         System.out.println("Ping to " 
			 + client.getRemoteSocketAddress());
	         OutputStream outToServer = client.getOutputStream();
	         DataOutputStream out = new DataOutputStream(outToServer);
	         out.writeUTF("PING");
	         InputStream inFromServer = client.getInputStream();
	         DataInputStream in =
	                        new DataInputStream(inFromServer);
	         System.out.println("Server says " + in.readUTF());
	      }catch(IOException e)
	      {
	         if(e.getMessage().equals("Connection reset by peer: socket write error")) {
	        	 try {
					client.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	         }
	      }
	}
}
