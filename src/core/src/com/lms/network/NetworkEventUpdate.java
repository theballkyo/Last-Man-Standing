package com.lms.network;

import java.net.DatagramPacket;
import java.net.Socket;

import com.lms.api.PlayerAPI;

public class NetworkEventUpdate extends NetworkEvent {

	public static final byte headerCode = 0x02;

	public NetworkEventUpdate(NetworkServerAbstract ns) {
		super(ns);
	}

	public NetworkEventUpdate(NetworkManage nm) {
		super(nm);
	}

	public NetworkEventUpdate(TCPServerInterface tcp) {
		super(tcp);
	}

	@Override
	public byte headerCode() {
		return NetworkEventUpdate.headerCode;
	}

	/**
	 * Data rule NAME:X:Y:ANIMATION
	 *
	 */
	@Override
	public void process(String data) {
		String[] dat = data.split(":");
		PlayerAPI.move(dat[0], Float.parseFloat(dat[1]), Float.parseFloat(dat[2]));
	}

	@Override
	public void processServer(String data, DatagramPacket incoming, String time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processServer(String data, Socket client, String time) {
		// TODO Auto-generated method stub

	}

}
