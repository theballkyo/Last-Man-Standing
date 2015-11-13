package com.lms.network;

import java.net.InetAddress;
import java.net.Socket;

import com.lms.api.PlayerAPI;
import com.lms.api.PlayerServerAPI;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class NetworkEventMove extends NetworkEvent {

	public static byte headerCode;

	/**
	 * Data rule NAME:X:Y:isAnimation|NAME:X:Y:isAnimation|...!TIME
	 */

	public static String createMoveMsg(String name, float x, float y) {
		return String.format("%c%s:%.0f:%.0f", headerCode, name, x, y);
	}

	@Override
	public void process(String data, UDPClient UDPcn) {
		String[] dat = data.split(":");
		// System.out.println(dat[0]+":"+Boolean.parseBoolean(dat[3]));
		PlayerAPI.move(dat[0], Float.parseFloat(dat[1]), Float.parseFloat(dat[2]));

	}

	@Override
	public void process(String data, TCPClient TCPcn) {

	}

	@Override
	public void processServer(String data, InetAddress address, int port, String time, UDPServerInterface udp) {
		String[] dat = data.split(":");
		PlayerServerAPI.update(dat[0], Float.parseFloat(dat[1]), Float.parseFloat(dat[2]));
	}

	@Override
	public void processServer(String data, Socket client, String time, TCPServerInterface tcp) {

	}
}
