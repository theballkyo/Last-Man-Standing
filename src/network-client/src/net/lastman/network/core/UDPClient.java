package net.lastman.network.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient implements ClientNetwork {

	DatagramSocket sock = null;

	private InetAddress host;
	private int port;
	private String s;
	private boolean isConn;
	
	public static final int CONN_OK = 1;
	public static final int CONN_LOSE = 2;
	public static final int CONN_TIMEOUT = 3;
	
	public UDPClient(String host, int port) {
		this.port = port;
		try {
			this.host = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		try {
			sock = new DatagramSocket();
			System.out.println("UDP client is started..");
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String readMsg() {
        try {
        	byte[] buffer = new byte[65536];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			sock.receive(reply);
			byte[] data = reply.getData();
			s = new String(data, 0, reply.getLength());
			return s;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
	}

	@Override
	public void sendMsg(String msg) {
		try {
			DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.length(), host, port);
			sock.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isConnected() {
		return isConn;
	}
}
