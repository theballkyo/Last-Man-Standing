package net.lastman.network.core;

import java.net.*;
import java.io.*;

public class GameServer extends Thread{
	
	   private ServerSocket serverSocket;
	   
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
}
