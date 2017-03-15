package com.lms.network;

import java.net.InetAddress;
import java.net.Socket;

import com.badlogic.gdx.Gdx;
import com.lms.api.PlayerAPI;
import com.lms.api.PlayerData;
import com.lms.api.PlayerServerAPI;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class NetworkEventMove extends NetworkEvent {

	public static byte headerCode;

	/**
	 * Data rule NAME:X:Y:isAnimation|NAME:X:Y:isAnimation|...!TIME
	 */

	public static String createMoveMsg(String name, long seq, float x, float y) {
		return String.format("%c%s:%d:%.0f:%.0f", NetworkEventMove.headerCode ,name, seq, x, y);
	}

	@Override
	public void process(String data, UDPClient UDPcn) {
		String[] dat = data.split(":");
		PlayerData player = PlayerAPI.get(dat[0]);
		if (player == null) {
			return;
		}
		long lastSeq = player.getMoveSeq();
		if (lastSeq < Long.parseLong(dat[1])) {
			PlayerAPI.move(dat[0], Float.parseFloat(dat[2]), Float.parseFloat(dat[3]));
			// player.setMoveSeq(Long.parseLong(dat[1]));
//			Gdx.app.log("NW => Move", dat[0] + "" + Long.parseLong(dat[1]));
		} else {
//			Gdx.app.log("NW => Move", "Data seq slow");
		}
	}

	@Override
	public void process(String data, TCPClient TCPcn) {

	}

	@Override
	public void processServer(String data, InetAddress address, int port, String time, UDPServerInterface udp) {
		String[] dat = data.split(":");
		PlayerServerAPI.update(dat[0], Float.parseFloat(dat[2]), Float.parseFloat(dat[3]));
		PlayerData player = PlayerServerAPI.get(dat[0]);
		udp.broadcast(dat[0], NetworkEventMove.createMoveMsg(dat[0], player.getMoveSeq(), Float.parseFloat(dat[2]), Float.parseFloat(dat[3])) + "!"
				+ System.currentTimeMillis());
		player.incrementMoveSeq();
	}

	@Override
	public void processServer(String data, Socket client, String time, TCPServerInterface tcp) {

	}
}
