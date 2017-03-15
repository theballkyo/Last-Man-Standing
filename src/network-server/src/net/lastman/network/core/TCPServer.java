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
import com.lms.game.LmsConfig;
import com.lms.network.NetworkEvent;
import com.lms.network.NetworkEventDisconnect;
import com.lms.network.NetworkEventItem;
import com.lms.network.NetworkEventManage;
import com.lms.network.TCPServerInterface;

public class TCPServer implements TCPServerInterface, LMSServer {

	private ServerSocket serverSocket;
	private NetworkEventManage nem;
	private HashMap<Integer, Socket> clientList;

	private TCPServer tcp;
	private Thread sv;
	private int connectionId = 0;

	public TCPServer(int port) {

		nem = new NetworkEventManage();
		tcp = this;
		clientList = new HashMap<>();
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("TCP Server is started. - port=" + LmsConfig.TCPport);
			sv = new Thread(new Runnable() {
				@Override
				public void run() {
					while (!Thread.currentThread().isInterrupted()) {
						try {
							final Socket client = serverSocket.accept();

							client.setSoTimeout(10000);
							new Thread(new Runnable() {
								@Override
								public void run() {
									connectionId += 1;
									System.out.println("Just connected to " + client.getRemoteSocketAddress()
											+ " connectionId " + connectionId);
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

		new Thread(new Runnable() {
			@Override
			public void run() {
				NetworkEventItem.processBg(tcp);
			}
		}).start();
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
			} catch (Exception e) {
				if (e == null || e.getMessage() == null) {
					String name = PlayerServerAPI.getName(client);
					System.out.println("Client: " + client.getInetAddress() + ":" + client.getPort() + " Error !?");
					if (PlayerServerAPI.remove(client)) {
						broadcast(NetworkEventDisconnect.removeMsg(name));
					}
					break;
				}
				if (e.getMessage().contains("Connection reset")) {
					System.out.println(
							"Client: " + client.getInetAddress() + ":" + client.getPort() + " has disconnected.");
					String name = PlayerServerAPI.getName(client);
					if (PlayerServerAPI.remove(client)) {
						broadcast(NetworkEventDisconnect.removeMsg(name));
					}
					break;
				} else if (e.getMessage().contains("Read timed out")) {
					System.out.println("Client: " + client.getInetAddress() + ":" + client.getPort() + " has timeout.");
					String name = PlayerServerAPI.getName(client);
					if (PlayerServerAPI.remove(client)) {
						broadcast(NetworkEventDisconnect.removeMsg(name));
					}
					break;
				} else {
					System.out.println("Client: " + client.getInetAddress() + ":" + client.getPort() + " Error !?");
					String name = PlayerServerAPI.getName(client);
					if (PlayerServerAPI.remove(client)) {
						broadcast(NetworkEventDisconnect.removeMsg(name));
					}
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
	public void close(Socket client) {
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	@Override
	public void sendMsg(Socket client, String msg) {
		synchronized (this) {
			if (client == null) {
				return;
			}

			try {
				DataOutputStream out = new DataOutputStream(client.getOutputStream());
				out.writeUTF(msg);
			} catch (Exception e) {

			}
		}
	}

	@Override
	public void broadcast(Socket client, String msg) {
		synchronized (this) {
			for (Entry<String, PlayerData> entry : PlayerServerAPI.getAll().entrySet()) {
				try {
					if (entry.getValue().getTcpSocket().equals(client)) {
						continue;
					}
					sendMsg(entry.getValue().getTcpSocket(), msg);
				} catch (Exception e) {
					if (e == null || e.getMessage() == null) {
						System.out.println("TCP Broadcast E = NULL");
					} else if (e.getMessage().contains("Broken pipe")) {
						PlayerServerAPI.remove(entry.getValue().getTcpSocket());
					}
				}
			}
		}
	}

	@Override
	public synchronized void broadcast(String msg) {
		synchronized (this) {
			broadcast(null, msg);
		}
	}

}
