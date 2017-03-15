package com.lms.network;

import java.net.InetAddress;
import java.net.Socket;

import com.lms.api.PlayerAPI;
import com.lms.object.SwordObject;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class NetworkEventSword extends NetworkEvent {

	public static byte headerCode;

	@Override
	public void process(String data, UDPClient UDPcn) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(String data, TCPClient TCPcn) {
		String[] dat = data.split(":");
		String name = dat[0];
		int width = Integer.parseInt(dat[1]);
		PlayerAPI.get(name).setSword(true);
		SwordObject.add(new SwordObject(PlayerAPI.get(name), width));
	}

	@Override
	public void processServer(String data, InetAddress address, int port, String time, UDPServerInterface udp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processServer(String data, Socket client, String time, TCPServerInterface tcp) {
		tcp.broadcast(String.format("%c%s", NetworkEventSword.headerCode, data));

	}

	public static String createMsg(String name, int width) {
		return String.format("%c%s:%d", NetworkEventSword.headerCode, name, width);
	}

}
