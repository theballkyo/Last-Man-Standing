package com.lms.network;

import java.net.Socket;

public interface TCPServerInterface {

	public void sendMsg(int clientId, String msg);

	public void sendMsg(Socket client, String msg);

	public void broadcast(String msg);

	public void broadcast(Socket client, String msg);

}