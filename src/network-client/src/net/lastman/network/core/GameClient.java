package net.lastman.network.core;

import java.net.*;
import java.io.*;

public class GameClient {
	
	Socket client;
	
	private boolean isConnected;
	private boolean isClose;
	
	public GameClient() {
	
	}
	
	public boolean run() {
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
	      }catch(IOException e)
	      {
	         e.printStackTrace();
	         return false;
	      }
		
		return true;
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
	      }catch(IOException e)
	      {
	         if(e.getMessage().equals("Connection reset by peer: socket write error")) {
	        	 try {
	        		System.out.println("Status: Closed Socket");
					client.close();
				} catch (IOException e1) {
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
	
	public Socket getClient() {
		return client;
	}
	
	public String getInput() {
		try {
			InputStream inFromServer;
			inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			String msg = in.readUTF();
			if(msg.equals("PING"))
				return "P";
			// System.out.println("Server says " + msg);
			return msg;
		} catch (IOException e) {
			if(e.getMessage().contains("Connection reset")) {
				System.out.println("Server closed !");
				return null;
			}
		}
		
		return null;
	}
	
	public void sendMsg(String msg) {
		   try {
			   DataOutputStream out = new DataOutputStream(client.getOutputStream());
			   out.writeUTF(msg);
			} catch (IOException e) {
				if(e.getMessage().equals("Connection reset by peer: socket write error")) {
		        	 try {
		        		System.out.println("Status: Closed Socket");
						client.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
		         }
		         else {
		        	 System.out.println("Status: " + e.getMessage());
		         }
			}
	   }
}
