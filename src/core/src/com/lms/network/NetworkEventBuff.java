package com.lms.network;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Map.Entry;

import com.lms.api.BuffData;
import com.lms.api.PlayerData;
import com.lms.api.PlayerServerAPI;
import com.lms.buff.CoreBuff;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class NetworkEventBuff extends NetworkEvent {

	/**
	 * Data rule buffCode:name:duration:arg0|arg1...
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
		int duration = Integer.parseInt(dat[2]);
		// System.out.println(data);
		if (dat.length == 4) {
			if (dat[3].contains("|")) {
				CoreBuff.processBuff(buffCode, name, duration, dat[3].split("|"));
			} else {
				CoreBuff.processBuff(buffCode, name, duration, new String[] { dat[3] });
			}
		} else {
			CoreBuff.processBuff(buffCode, name, duration, new String[] {});
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
		if (data.equals("Req")) {
			for (Entry<String, PlayerData> p : PlayerServerAPI.getAll().entrySet()) {
				PlayerData pd = p.getValue();
				for (BuffData bf : pd.getBuffData()) {
					if (!bf.isTimeout()) {
						tcp.sendMsg(client, bf.toString());
					}
				}
			}
			return;
		}
		String[] dat = data.split(":");
		byte buffCode = data.getBytes()[0];
		String name = dat[1];
		int duration = Integer.parseInt(dat[2]);
		String arg;
		if (dat.length > 3) {
			arg = dat[3];
		} else {
			arg = "";
		}
		tcp.broadcast(String.format("%c%s", headerCode, data));

		PlayerServerAPI.get(name).addBuffData(new BuffData(buffCode, name, duration, System.currentTimeMillis(), arg));
	}

	/**
	 *
	 * @param name
	 * @param buffName
	 * @param time
	 * @return
	 */
	public static String createMsg(byte buffCode, String name, int duration, String[] arg) {
		return String.format("%c%c:%s:%d:%s", headerCode, buffCode, name, duration, String.join("|", arg));
	}

	public static String createMsg(byte buffCode, String name, int duration, String arg) {
		return String.format("%c%c:%s:%d:%s", headerCode, buffCode, name, duration, arg);
	}

	public static String reqBuffData() {
		return String.format("%cReq", headerCode);
	}
}
