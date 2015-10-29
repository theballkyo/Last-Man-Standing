package net.lastman.network.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.net.Socket;
import com.lms.network.NetworkEvent;
import com.lms.network.NetworkEventManage;
import com.lms.network.NetworkServerAbstract;

public class UDPServer implements NetworkServerAbstract, ServerNetwork{
	private DatagramSocket sock = null;
	private int port;
	private int id = 0;
	NetworkEventManage nem;
	private HashMap<Integer, ClientProfile> clientList;

	public UDPServer(int port) {
		this.port = port;
		this.nem = new NetworkEventManage(this);
		clientList = new HashMap<Integer, ClientProfile>();
	}
	
	public void start() {
		try {
			sock = new DatagramSocket(port);
			// buffer to receive incoming data
			while (true) {
				byte[] buffer = new byte[65536];
				final DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
				// 2. Wait for an incoming data
				// System.out.println("Server socket created. Waiting for incoming data...");
				
				//final String fristMsg = readMsg(incoming);
				//System.out.println(
				//		incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + fristMsg + " : " + id);
				
				listener(incoming);
				
				id+=1;
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
	}
	
	private void listener(DatagramPacket incoming) {
		String msg = readMsg(incoming);
		System.out.println(msg);
		Byte header = msg.getBytes()[0];
		System.out.println(header.toString());
		String data = new String(msg.getBytes(), 1, msg.length()-1);
		
		NetworkEvent event = nem.get(header);
		if(event != null)
			event.processServer(data);
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

	public void broadcast(int id, String msg) {
		for(Entry<Integer, ClientProfile> entry : clientList.entrySet()) {
			if(entry.getKey() == id)
				continue;
			ClientProfile cp = entry.getValue();
			sendMsg(cp.getAddress(), cp.getPort(), msg);
		}
	}

	@Override
	public DatagramSocket getSock() {
		// TODO Auto-generated method stub
		return null;
	}
}
