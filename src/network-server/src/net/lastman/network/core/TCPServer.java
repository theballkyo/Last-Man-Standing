package net.lastman.network.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;

import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;

public class GameServer extends Thread{
	
	   private ServerSocket serverSocket;
	   
	   private HashMap<Integer, Socket> clientList;
	   
	   private int connectionId = 0;
	   
	   private HashMap<String, String> playerList;
	   
	   public GameServer() {
		   
		   clientList = new HashMap<Integer, Socket>();
		   playerList = new HashMap();
	   }
	   
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
			// start
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
						connectionId += 1;
						broadcast(client.getRemoteAddress() + " has connected to server.");
						clientList.put(connectionId, client);
						new Thread(new Runnable() {
							@Override
							public void run() {
								clientAccept(client, connectionId);
							}	
						}).start();
					}
				}			
			}).start();
			
			new Thread(new Runnable() {
				public void run() {
					while(true) {
						pingClient();
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	   
	   public void clientAccept(Socket client, int id) {
		   while(true) {
				try {
					DataInputStream in =
			                  new DataInputStream(client.getInputStream());
					String msg = in.readUTF();
					
					if(msg.startsWith("M")) {
						broadcast(msg);
					}
					
					if(msg.startsWith("J")) {
						String[] data = msg.split(" ");
						playerList.put(data[1], data[2] + " " + data[3]);
						broadcast(msg);
					}
					if(msg.equals("L")) {
						for(Entry<String, String> entry : playerList.entrySet()) {
							broadcast("J " + entry.getKey() + " " + entry.getValue());
						}
					}
					//else
					//	Gdx.app.log("Status", "Client msg: " + in.readUTF());
					// sendMsg(id, "Thank you for connecting to " + client.getRemoteAddress());	
				} catch (IOException e) {
					Gdx.app.log("Satus", "an error occured", e);
					if(e.getMessage().contains("Connection reset")) {
						System.out.println("Client: " + client.getRemoteAddress() + " has disconnected.");
						broadcast("Client: " + client.getRemoteAddress() + " has disconnected.");
						break;
					}
				}
			}
	   }
	   
	   public void broadcast(String msg) {
		   
		   for(Entry<Integer, Socket> entry : clientList.entrySet()) {
			   sendMsg(entry.getKey(), msg);
			}
	   }
	   
	   public void sendMsg(int clientId, String msg) {
		   try {
			   DataOutputStream out = new DataOutputStream(clientList.get(clientId).getOutputStream());
			   out.writeUTF(msg);
			} catch (IOException e) {
				if(e.getMessage().contains("Connection reset")) {
					clientList.remove(clientId);
				}
			}
	   }
	   
	   private void pingClient() {
		   broadcast("PING");
	   }
}
