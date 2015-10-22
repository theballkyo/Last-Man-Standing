package net.lastman.network.core;

import java.net.*;
import java.io.*;

public class GameClient {
	
	Socket client;
	
	private boolean isConnected;
	private boolean isClose;
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
			client.setSoTimeout(1000);
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
	        		 System.out.println("Status: Closed Socket");
					client.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	         }
	         else {
	        	 System.out.println("Status: " + e.getMessage());
	         }
	      }
	}
	
	public boolean isConnected() {
		return client.isConnected();
	}
	
	public boolean isClose() {
		return client.isClosed();
	}
}
