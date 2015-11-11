package com.lms.network;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

abstract public class NetworkEvent {
	
	abstract public void process(String data, UDPClient UDPcn);

	abstract public void process(String data, TCPClient TCPcn);

	abstract public void processServer(String data, InetAddress address, int port, String time, UDPServerInterface udp);

	abstract public void processServer(String data, Socket client, String time, TCPServerInterface tcp);

}
