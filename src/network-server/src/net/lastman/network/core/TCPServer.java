package net.lastman.network.core;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;

import com.lms.network.NetworkEvent;
import com.lms.network.NetworkEventManage;
import com.lms.network.TCPServerInterface;

public class TCPServer extends Thread implements TCPServerInterface{
	
	private ServerSocket serverSocket;
	private NetworkEventManage nem;   
	private HashMap<Integer, Socket> clientList;
	   
	private int connectionId = 0;
	private int port;
	
	private HashMap<String, String> playerList;
	   
	public TCPServer() {	   
		clientList = new HashMap<Integer, Socket>();
		playerList = new HashMap();
	}
	   
	   
	public TCPServer(int port)
	{
		this.port = port;
		this.nem = new NetworkEventManage(this);
		try {
		   serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		   e.printStackTrace();
		}
	}

	public void start()
	{
	      while(true)
	      {
	         try
	         {
	            System.out.println("Waiting for client on port " +
	            serverSocket.getLocalPort() + "...");
	            final Socket client = serverSocket.accept();
	            System.out.println("Just connected to "
		                  + client.getRemoteSocketAddress());
	            client.setSoTimeout(3000);
	            new Thread(new Runnable() {
					@Override
					public void run() {
						connectionId += 1;
						clientList.put(connectionId, client);
						clientAccept(client, connectionId);
					}
	            	
	            }).start();
	         }catch(IOException e)
	         {
	            e.printStackTrace();
	            break;
	         }
	      }
	   }
	   
	   public void clientAccept(Socket client, int id) {
		   String msg;
		   while(true) {
				try {
					DataInputStream in =
			                  new DataInputStream(client.getInputStream());
					msg = in.readUTF();
					process(client, msg);
				} catch (IOException e) {
					if(e.getMessage().contains("Connection reset")) {
						System.out.println("Client: " + client.getInetAddress() + " has disconnected.");
						broadcast("Client: " + client.getInetAddress() + " has disconnected.");
						break;
					}
				}
				
				
			}
	   }
	   
	   public void process(final Socket client, final String msg) {
		   new Thread(new Runnable() {
				public void run() {
					Byte header = msg.getBytes()[0];

					String data = new String(msg.getBytes(), 1, msg.length()-1);
					// System.out.println(data);
					NetworkEvent event = nem.get(header);
					String[] sData = data.split("!");
					if(event != null) {
						if(sData.length > 1)
							event.processServer(sData[0], client, sData[1]);
						else
							event.processServer(sData[0], client, "0");
					}
				}
			}).start();
	   }
	   
	   public String readMsg() {
		   return "";
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
