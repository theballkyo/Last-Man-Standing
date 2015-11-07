package net.lastman.network.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;

import com.lms.network.NetworkEvent;
import com.lms.network.NetworkEventManage;
import com.lms.network.TCPServerInterface;

public class TCPServer implements TCPServerInterface, LMSServer {

	private ServerSocket serverSocket;
	private NetworkEventManage nem;
	private HashMap<Integer, Socket> clientList;

	private Thread sv;

	private int connectionId = 0;
	private int port;

	private HashMap<String, String> playerList;
	
	public TCPServer(int port) {
		this.port = port;
		this.nem = new NetworkEventManage(this);
		clientList = new HashMap<Integer, Socket>();
		playerList = new HashMap<String, String>();
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
							System.out.println("Just connected to " +
							client.getRemoteSocketAddress());
							client.setSoTimeout(3000);
							new Thread(new Runnable() {
								@Override
								public void run() {
									connectionId += 1;
									clientList.put(connectionId, client);
									clientAccept(client, connectionId);
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

	public void start() {
		sv.start();
	}

	public void stop() {
		sv.interrupt();
	}

	public void clientAccept(Socket client, int id) {
		String msg;
		while (true) {
			try {
				DataInputStream in = new DataInputStream(client.getInputStream());
				msg = in.readUTF();
				process(client, msg);
			} catch (IOException e) {
				if (e.getMessage().contains("Connection reset")) {
					System.out.println("Client: " + client.getInetAddress() + " has disconnected.");
					broadcast("Client: " + client.getInetAddress() + " has disconnected.");
					break;
				}
			}

		}
	}

	public void process(final Socket client, final String msg) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Byte header = msg.getBytes()[0];

				String data = new String(msg.getBytes(), 1, msg.length() - 1);
				NetworkEvent event = nem.get(header);
				String[] sData = data.split("!");
				if (event != null) {
					if (sData.length > 1) {
						event.processServer(sData[0], client, sData[1]);
					} else {
						event.processServer(sData[0], client, "0");
					}
				}
			}
		}).start();
	}

	public String readMsg() {
		return "";
	}

	public void broadcast(String msg) {

		for (Entry<Integer, Socket> entry : clientList.entrySet()) {
			sendMsg(entry.getKey(), msg);
		}
	}

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

	private void pingClient() {
		broadcast("PING");
	}
}
