package com.lms.network;

import java.net.InetAddress;
import java.net.Socket;

import com.badlogic.gdx.math.Rectangle;
import com.lms.object.BulletObject;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class NetworkEventBullet extends NetworkEvent {

	public static byte headerCode;

	/**
	 * Data rule NAME:X:Y:WIDTH:HEIGHT:SIDE
	 */
	@Override
	public void process(String data, UDPClient UDPcn) {

	}

	@Override
	public void process(String data, TCPClient TCPcn) {
		String[] dat = data.split(":");
		String name = dat[0];
		float x = Float.parseFloat(dat[1]);
		float y = Float.parseFloat(dat[2]);
		float width = Float.parseFloat(dat[3]);
		float height = Float.parseFloat(dat[4]);
		int side = Integer.parseInt(dat[5]);
		BulletObject.add(new BulletObject(new Rectangle(x, y, width, height), side, name));

	}

	@Override
	public void processServer(String data, InetAddress address, int port, String time, UDPServerInterface udp) {

	}

	@Override
	public void processServer(String data, Socket client, String time, TCPServerInterface tcp) {
		String[] dat = data.split(":");
		String name = dat[0];
		float x = Float.parseFloat(dat[1]);
		float y = Float.parseFloat(dat[2]);
		float width = Float.parseFloat(dat[3]);
		float height = Float.parseFloat(dat[4]);
		int side = Integer.parseInt(dat[5]);
		tcp.broadcast(client, createMsg(name, x, y, width, height, side));

	}

	public static String createMsg(String name, Rectangle r, int side) {
		return createMsg(name, r.x, r.y, r.width, r.height, side);
	}

	public static String createMsg(String name, float x, float y, float width, float height, int side) {
		return String.format("%c%s:%.1f:%.1f:%.1f:%.1f:%d", headerCode, name, x, y, width, height, side);
	}
}
