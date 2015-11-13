package net.lastman.network.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.lms.api.PlayerData;
import com.lms.api.PlayerServerAPI;
import com.lms.network.NetworkEvent;
import com.lms.network.NetworkEventDisconnect;
import com.lms.network.NetworkEventJoin;
import com.lms.network.NetworkEventManage;
import com.lms.network.NetworkEventMove;
import com.lms.network.NetworkEventPong;
import com.lms.network.UDPServerInterface;

public class UDPServer implements UDPServerInterface, LMSServer {
	private DatagramSocket sock = null;
	private int port;
	private Thread updatePl;
	private Thread ping;
	private Thread sv;
	private UDPServer udp;
	NetworkEventManage nem;
	public HashMap<String, ClientProfile> clientList;

	private int delayUpdate = 10;

	public UDPServer(int port) {
		this.port = port;
		this.nem = new NetworkEventManage();
		this.udp = this;
		clientList = new HashMap<String, ClientProfile>();

		try {
			sock = new DatagramSocket(port);

			sv = new Thread(new Runnable() {
				@Override
				public void run() {
					// Wait for msg from client
					System.out.println("Start");
					while (!Thread.currentThread().isInterrupted()) {
						byte[] buffer = new byte[65536];
						final DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
						listener(incoming);
						// id+=1;
					}
				}
			});

			// Thread - Update player position to player
			updatePl = new Thread(new Runnable() {
				@Override
				public void run() {
					while (!Thread.currentThread().isInterrupted()) {
						HashMap<String, PlayerData> pl = PlayerServerAPI.getAll();
						
						try {
							for (Entry<String, PlayerData> p : pl.entrySet()) {
								String name = p.getKey();
								PlayerData dat = p.getValue();
								broadcast(name, NetworkEventMove.createMoveMsg(name, dat.pos.x, dat.pos.y) + "!"
										+ System.currentTimeMillis());
							}
						} catch (ConcurrentModificationException e) {
							break;
						}
						try {
							Thread.sleep(delayUpdate);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});

			// Thread - Check client is connecting
			ping = new Thread(new Runnable() {
				@Override
				public void run() {
					while (!Thread.currentThread().isInterrupted()) {

						checkClient();

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});

		} catch (SocketException e1) {
			e1.printStackTrace();
		}
	}

	public void setDelay(int ms) {
		delayUpdate = ms;
	}

	@Override
	public void start() {
		sv.start();
		// ping.start();
		updatePl.start();

	}

	@Override
	public void stop() {

	}

	private void listener(final DatagramPacket incoming) {
		final String msg = readMsg(incoming);

		new Thread(new Runnable() {
			@Override
			public void run() {
				Byte header = msg.getBytes()[0];

				String data = new String(msg.getBytes(), 1, msg.length() - 1);
				
				NetworkEvent event = nem.get(header);
				String[] sData = data.split("!");
				if (event != null) {
					if (sData.length > 1) {
						event.processServer(sData[0], incoming.getAddress(), incoming.getPort(), sData[1], udp);
					} else {
						event.processServer(sData[0], incoming.getAddress(), incoming.getPort(), "0", udp);
					}
				}
			}
		}).start();

	}

	public String readMsg(DatagramPacket incoming) {
		try {
			sock.receive(incoming);
			byte[] data = incoming.getData();
			String s = new String(data, 0, incoming.getLength());
			return s;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void sendMsg(InetAddress Address, int port, String msg) {
		try {
			DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.getBytes().length, Address, port);
			sock.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendMsg(InetAddress Address, int port, String msg, String time) {
		sendMsg(Address, port, msg + "!" + time);
	}

	public void broadcast(String msg) {
		
		for (Entry<String, PlayerData> entry : PlayerServerAPI.getAll().entrySet()) {
			PlayerData cp = entry.getValue();
			sendMsg(cp.getUdpAddress(), cp.getUdpPort(), msg);
		}
	}

	@Override
	public void broadcast(String name, String msg) {
		for (Entry<String, PlayerData> entry : PlayerServerAPI.getAll().entrySet()) {
			if (entry.getKey().equals(name)) {
				continue;
			}
			PlayerData cp = entry.getValue();
			// Check player address
			if (cp.getUdpAddress() == null || cp.getUdpPort() == 0)
				continue;
			sendMsg(cp.getUdpAddress(), cp.getUdpPort(), msg);
		}
	}

	@Override
	public void broadcast(String name, String msg, String time) {
		broadcast(name, msg + "!" + time);
	}

	@Override
	public void addClient(String name, DatagramPacket incoming) {
		clientList.put(name, new ClientProfile(name, incoming));
	}

	public void delClient(String name) {
		clientList.remove(name);
	}

	private synchronized void checkClient() {
		broadcast(NetworkEventPong.getMsg());

		HashMap<String, PlayerData> pl = PlayerServerAPI.getAll();
		for (Entry<String, PlayerData> p : pl.entrySet()) {
			String name = p.getKey();
			PlayerData dat = p.getValue();
			if (dat.lastUdpConn + 5000 < System.currentTimeMillis()) {
				System.out.println("Player " + name + " time out.");
				delClient(name);
				PlayerServerAPI.remove(name);
				broadcast(NetworkEventDisconnect.removeMsg(name));
				break;
			} else if (dat.lastUdpConn > System.currentTimeMillis()) {
				System.out.println("Player " + name + " time out.");
				delClient(name);
				PlayerServerAPI.remove(name);
				broadcast(NetworkEventDisconnect.removeMsg(name));
				break;
			}
		}
	}
}
