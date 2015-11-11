package net.lastman.network.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;

import com.lms.api.PlayerData;
import com.lms.api.PlayerServerAPI;
import com.lms.network.NetworkEvent;
import com.lms.network.NetworkEventManage;
import com.lms.network.TCPServerInterface;

public class TCPServer implements TCPServerInterface, LMSServer {

	private ServerSocket serverSocket;
	private NetworkEventManage nem;
	private HashMap<Integer, Socket> clientList;

	private TCPServer tcp;
	private Thread sv;

	private int connectionId = 0;
	private int port;


	public TCPServer(int port) {
		this.port = port;
		this.nem = new NetworkEventManage();
		tcp = this;
		clientList = new HashMap<Integer, Socket>();
		try {
			serverSocket = new ServerSocket(port);

			sv = new Thread(new Runnable() {
				@Override
				public void run() {
					while (!Thread.currentThread().isInterrupted()) {
						try {
							// System.out.println("Waiting for client on port "
							// + serverSocket.getLocalPort() + "...");
							final Socket client = serverSocket.accept();
							System.out.println("Just connected to " + client.getRemoteSocketAddress());
							client.setSoTimeout(3000);
							new Thread(new Runnable() {
								@Override
								public void run() {
									connectionId += 1;
									// clientList.put(connectionId, client);
									clientAccept(client);
								}

							}).start();
						} catch (IOException e) {
							e.printStackTrace();
							break;
						}
					}
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		sv.start();
	}

	@Override
	public void stop() {
		sv.interrupt();
	}

	public void clientAccept(Socket client) {
		String msg;
		while (true) {
			try {
				DataInputStream in = new DataInputStream(client.getInputStream());
				msg = in.readUTF();
				process(msg, client);
			} catch (IOException e) {
				if (e.getMessage().contains("Connection reset")) {
					System.out.println("Client: " + client.getInetAddress() + " has disconnected.");
					PlayerServerAPI.remove(client);
					break;
				}
			}

		}
	}

	public void process(final String msg, final Socket client) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Byte header = msg.getBytes()[0];
				
				String data = new String(msg.getBytes(), 1, msg.length() - 1);
				NetworkEvent event = nem.get(header);
				String[] sData = data.split("!");
				if (event != null) {
					if (sData.length > 1) {
						event.processServer(sData[0], client, sData[1], tcp);
					} else {
						event.processServer(sData[0], client, "0", tcp);
					}
				}
			}
		}).start();
	}

	@Override
	public void sendMsg(int clientId, String msg) {
		try {
			DataOutputStream out = new DataOutputStream(clientList.get(clientId).getOutputStream());
			out.writeUTF(msg);
		} catch (IOException e) {
			if (e.getMessage().contains("Connection reset")) {
				clientList.remove(clientId);
			}
		}
	}
	
	public void sendMsg(Socket client, String msg) {
		try {
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			out.writeUTF(msg);
		} catch (IOException e) {
			if (e.getMessage().contains("Connection reset")) {
				// clientList.remove(clientId);
			} else {
				e.printStackTrace();
			}
		}
	}
	
	private void pingClient() {
		broadcast("PING");
	}

	@Override
	public void broadcast(int clientId, String msg) {
		for (Entry<String, PlayerData> entry : PlayerServerAPI.getAll().entrySet()) { 
			sendMsg(entry.getValue().getTcpSocket(), msg);
		}
	}
	
	@Override
	public synchronized void broadcast(String msg) {

		for (Entry<String, PlayerData> entry : PlayerServerAPI.getAll().entrySet()) { 
			sendMsg(entry.getValue().getTcpSocket(), msg);
		}
	}

}
