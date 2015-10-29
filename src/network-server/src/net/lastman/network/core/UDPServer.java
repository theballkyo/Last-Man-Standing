package net.lastman.network.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.lms.api.PlayerServerAPI;
import com.lms.network.NetworkEvent;
import com.lms.network.NetworkEventDisconnect;
import com.lms.network.NetworkEventManage;
import com.lms.network.NetworkEventPong;
import com.lms.network.NetworkServerAbstract;

public class UDPServer implements NetworkServerAbstract, ServerNetwork{
	private DatagramSocket sock = null;
	private int port;
	NetworkEventManage nem;
	public HashMap<String, ClientProfile> clientList;

	public UDPServer(int port) {
		this.port = port;
		this.nem = new NetworkEventManage(this);
		clientList = new HashMap<String, ClientProfile>();
	}
	
	public void start() {
		try {
			sock = new DatagramSocket(port);
			
			new Thread(new Runnable() {
				public void run() {
					while(true) {
						
						checkClient();
					
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();
			
			while (true) {
				byte[] buffer = new byte[65536];
				final DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
				
				listener(incoming);
				// id+=1;
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
	}
	
	private void listener(final DatagramPacket incoming) {
		final String msg = readMsg(incoming);
		new Thread(new Runnable() {
			public void run() {
				Byte header = msg.getBytes()[0];

				String data = new String(msg.getBytes(), 1, msg.length()-1);
				// System.out.println(data);
				NetworkEvent event = nem.get(header);
				String[] sData = data.split("!");
				if(event != null)
					event.processServer(sData[0], incoming, sData[1]);
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

	public void sendMsg(InetAddress Address, int port, String msg) {
		try {
			DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.getBytes().length, Address, port);
			sock.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMsg(InetAddress Address, int port, String msg, String time) {
		sendMsg(Address, port, msg + "!" + time);
	}
	
	public void broadcast(String msg) {
		for(Entry<String, ClientProfile> entry : clientList.entrySet()) {
			ClientProfile cp = entry.getValue();
			sendMsg(cp.getAddress(), cp.getPort(), msg);
		}
	}
	
	public void broadcast(String name, String msg) {
		for(Entry<String, ClientProfile> entry : clientList.entrySet()) {
			if(entry.getKey().equals(name))
				continue;
			ClientProfile cp = entry.getValue();
			sendMsg(cp.getAddress(), cp.getPort(), msg);
		}
	}
	
	public void broadcast(String name, String msg, String time) {
		broadcast(name, msg + "!" + time);
	}
	
	@Override
	public DatagramSocket getSock() {
		return null;
	}
	
	public void addClient(String name, DatagramPacket incoming) {
		clientList.put(name, new ClientProfile(name, incoming));
	}
	
	public void delClient(String name) {
		clientList.remove(name);
	}
	
	private synchronized void checkClient() {
		broadcast(NetworkEventPong.getMsg());
		
		HashMap<String, PlayerServerAPI> pl = PlayerServerAPI.getAll();
		for(Entry<String, PlayerServerAPI> p :pl.entrySet()) {
			String name = p.getKey();
			PlayerServerAPI dat = p.getValue();
			if(dat.lastConn + 5000 < System.currentTimeMillis()) {
				System.out.println("Player " + name + " time out.");
				delClient(name);
				PlayerServerAPI.remove(name);
				broadcast(NetworkEventDisconnect.removeMsg(name));
				break;
			}
		}
	}
}
