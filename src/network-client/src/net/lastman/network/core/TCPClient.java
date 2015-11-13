package net.lastman.network.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient implements ClientNetwork{

	private Socket client;
	private String host;
	private int port;
	
	private boolean isConn = false;
	
	public TCPClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	@Override
	public void start() {
		try {
			client = new Socket(host, port);
			System.out.println("TCP client started ...");
			isConn = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			isConn = false;
		} catch (IOException e) {
			e.printStackTrace();
			isConn = false;
		}
	}

	@Override
	public String readMsg() {
		try {
			InputStream msgServer = client.getInputStream();
			DataInputStream msg = new DataInputStream(msgServer);
			return msg.readUTF();
		} catch (IOException e) {
			isConn = false;
			e.printStackTrace();
		}
        
		return "";
	}

	@Override
	public void sendMsg(String msg) {
		try {
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			out.writeUTF(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

	@Override
	public boolean isConnected() {
		return isConn;
	}

}
