package net.lastman.network.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;

import java.io.*;

public class GameServer extends Thread{
	
	   private ServerSocket serverSocket;
	   
	   /*
	   public GameServer(int port) throws IOException
	   {
	      serverSocket = new ServerSocket(port);
	      serverSocket.setSoTimeout(0);
	   }

	   public void run()
	   {
	      while(true)
	      {
	         try
	         {
	            System.out.println("Waiting for client on port " +
	            serverSocket.getLocalPort() + "...");
	            final Socket server = serverSocket.accept();
	            System.out.println("Just connected to "
		                  + server.getRemoteSocketAddress());
	            server.setSoTimeout(1000);
	            new Thread(new Runnable() {

					@Override
					public void run() {
						while(true) {
							try {
					            DataInputStream in =
					                  new DataInputStream(server.getInputStream());
					            System.out.println(in.readUTF());
					            DataOutputStream out =
					                 new DataOutputStream(server.getOutputStream());
					            out.writeUTF("Thank you for connecting to "
					                    + server.getRemoteSocketAddress());
					            
				            }catch(IOException e) {
				            	if(e.getMessage().equals("Connection reset")) {
				            		try {
										server.close();
										System.out.println("Client " + server.getRemoteSocketAddress() + " disconnected");
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
				            		break;
				            	}
				            }
						}
					}
	            	
	            }).start();
	         }catch(IOException e)
	         {
	            e.printStackTrace();
	            break;
	         }
	      }
	   }
	   */
	   public void runSocket() {
			// to the server
		   Gdx.app.log("Status", "Server starting...");
			new Thread(new Runnable() {
				@Override
				public void run() {
					ServerSocketHints hints = new ServerSocketHints();
					hints.acceptTimeout = 0;
					ServerSocket server = Gdx.net.newServerSocket(Protocol.TCP, 20156, hints);
					while(true) {
						final Socket client = server.accept(null);
						System.out.println("Client connected: " + client.getRemoteAddress());
						new Thread(new Runnable() {
							int connectionId;
							@Override
							public void run() {
								clientAccept(client);
							}	
						}).start();
					}
				}			
			}).start();				
		}
	   
	   public void clientAccept(Socket client) {
		   while(true) {
				try {
					DataInputStream in =
			                  new DataInputStream(client.getInputStream());
					
					Gdx.app.log("Status", "Client msg: " + in.readUTF());
					
					DataOutputStream out =
					new DataOutputStream(client.getOutputStream());
		            out.writeUTF("Thank you for connecting to "
		                    + client.getRemoteAddress());	
				} catch (IOException e) {
					Gdx.app.log("Satus", "an error occured", e);
				}
			}
	   }
}
