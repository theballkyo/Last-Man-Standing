package com.lms.network;

import java.net.InetAddress;
import java.net.Socket;

import com.lms.buff.CoreBuff;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class NetworkEventBuff extends NetworkEvent {

	/**
	 * Data rule buffCode:name:arg0|arg1...
	 */
	public static byte headerCode;

	/**
	 *
	 * @param data
	 * @param UDPcn
	 */
	@Override
	public void process(String data, UDPClient UDPcn) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @param data
	 * @param TCPcn
	 */
	@Override
	public void process(String data, TCPClient TCPcn) {
		String[] dat = data.split(":");
		byte buffCode = dat[0].getBytes()[0];
		String name = dat[1];

		if (dat[2].contains("|")) {
			CoreBuff.processBuff(buffCode, name, dat[2].split("|"));
		} else {
			CoreBuff.processBuff(buffCode, name, new String[] { dat[2] });
		}

	}

	/**
	 *
	 * @param data
	 * @param address
	 * @param port
	 * @param time
	 * @param udp
	 */
	@Override
	public void processServer(String data, InetAddress address, int port, String time, UDPServerInterface udp) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @param data
	 * @param client
	 * @param time
	 * @param tcp
	 */
	@Override
	public void processServer(String data, Socket client, String time, TCPServerInterface tcp) {
		tcp.broadcast(client, String.format("%c%s", headerCode, data));
	}

	/**
	 *
	 * @param name
	 * @param buffName
	 * @param time
	 * @return
	 */
	public static String createMsg(byte buffCode, String name, String[] arg) {
		System.out.println("J" + String.join("|", arg));
		return String.format("%c%c:%s:%s", headerCode, buffCode, name, String.join("|", arg));
	}
}
