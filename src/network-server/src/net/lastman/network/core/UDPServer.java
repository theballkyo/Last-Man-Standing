package net.lastman.network.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.net.Socket;

public class UDPServer implements ServerNetwork {
	private DatagramSocket sock = null;

	private int port;
	private int id = 0;
	
	private HashMap<Integer, ClientProfile> clientList;
	
	public UDPServer(int port) {
		this.port = port;
		clientList = new HashMap<Integer, ClientProfile>();
	}

	@Override
	public void start() {
		try {
			sock = new DatagramSocket(port);
			// buffer to receive incoming data
			while (true) {
				byte[] buffer = new byte[65536];
				final DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
	
				// 2. Wait for an incoming data
				System.out.println("Server socket created. Waiting for incoming data...");
	
				// communication loop
				new Thread(new Runnable() {
					public void run() {
						id += 1;
						clientList.put(id, new ClientProfile(id, incoming));
						accept(incoming, id);
					}
				}).start();
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
	}
	
	private void accept(DatagramPacket incoming, int id) {
		while (true) {
			String s = readMsg(incoming);
			// echo the details of incoming data - client ip :
			// client port - client message
			System.out.println(
					incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + s);

			s = "OK : " + s;
			sendMsg(incoming.getAddress(), incoming.getPort(), s);
		}
	}
	
	@Override
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
		}
	}

	@Override
	public DatagramSocket getSock() {
		return sock;
	}

	@Override
	public void broadcast(int id, String msg) {
		for(Entry<Integer, ClientProfile> entry : clientList.entrySet()) {
			if(entry.getKey() == id)
				continue;
			ClientProfile cp = entry.getValue();
			sendMsg(cp.getAddress(), cp.getPort(), msg);
		}
	}
}
