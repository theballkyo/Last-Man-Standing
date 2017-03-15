package com.lms.network;

import java.net.InetAddress;
import java.net.Socket;

import com.badlogic.gdx.math.Rectangle;
import com.lms.object.BulletObject;
import com.lms.object.CoreObject;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class NetworkEventBullet extends NetworkEvent {

	public static byte headerCode;
	private int id = 0;

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

		if (name.equals("r")) {
			try {
				int id = Integer.parseInt(dat[1]);
				CoreObject.addBulletIdRemoveQuene(id);
				// System.out.println("Add remove quene");
			} catch (NumberFormatException e) {
				System.out.println("NW Event bullet: can't convert str to int");
			}
			return;
		}

		float x = Float.parseFloat(dat[1]);
		float y = Float.parseFloat(dat[2]);
		float width = Float.parseFloat(dat[3]);
		float height = Float.parseFloat(dat[4]);
		int side = Integer.parseInt(dat[5]);
		int id = Integer.parseInt(dat[6]);
		// System.out.println(dat);
		BulletObject.add(new BulletObject(new Rectangle(x, y, width, height), side, name, id));

	}

	@Override
	public void processServer(String data, InetAddress address, int port, String time, UDPServerInterface udp) {

	}

	@Override
	public void processServer(String data, Socket client, String time, TCPServerInterface tcp) {
		String[] dat = data.split(":");
		String name = dat[0];
		if (name.equals("r")) {
			try {
				int id = Integer.parseInt(dat[1]);
				tcp.broadcast(NetworkEventBullet.removeBullet(id));
			} catch (NumberFormatException e) {
				System.out.println("NW Event bullet: can't convert str to int");
			}
			return;
		}
		float x = Float.parseFloat(dat[1]);
		float y = Float.parseFloat(dat[2]);
		float width = Float.parseFloat(dat[3]);
		float height = Float.parseFloat(dat[4]);
		int side = Integer.parseInt(dat[5]);
		id++;
		tcp.broadcast(NetworkEventBullet.createMsg(name, x, y, width, height, side, id));

	}

	// For client sending
	public static String createMsg(String name, Rectangle r, int side) {
		return NetworkEventBullet.createMsg(name, r.x, r.y, r.width, r.height, side);
	}

	// For client sending
	public static String createMsg(String name, float x, float y, float width, float height, int side) {
		return String.format("%c%s:%.1f:%.1f:%.1f:%.1f:%d", NetworkEventBullet.headerCode, name, x, y, width, height,
				side);
	}

	// For client create remove message
	public static String removeBullet(int id) {
		return String.format("%cr:%s", NetworkEventBullet.headerCode, id);
	}

	public static String createMsg(String name, float x, float y, float width, float height, int side, int id) {
		return String.format("%c%s:%.1f:%.1f:%.1f:%.1f:%d:%d", NetworkEventBullet.headerCode, name, x, y, width, height,
				side, id);
	}
}
