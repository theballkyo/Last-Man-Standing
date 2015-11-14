package com.lms.network;

import java.net.Socket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lms.entity.MainEntity;
import com.uwsoft.editor.renderer.SceneLoader;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class NetworkManage implements Runnable {

	UDPClient UDPcn;
	TCPClient TCPcn;
	SceneLoader sl;
	MainEntity me;
	Viewport vp;
	NetworkEventManage nem;
	Socket client;

	private static long byteRecv = 0;

	private long lastRecv = 0;

	public NetworkManage(UDPClient UDPcn, TCPClient TCPcn, SceneLoader sl, MainEntity me, Viewport vp) {
		this.UDPcn = UDPcn;
		this.TCPcn = TCPcn;
		this.sl = sl;
		this.me = me;
		this.vp = vp;
		nem = new NetworkEventManage();
		// Gdx.app.log("Network", "Create object");

		NetworkPing.start();
	}

	@Override
	public void run() {
		UDPcn.start();
		TCPcn.start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					UDPListener();
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					TCPListener();
				}
			}
		}).start();

	}

	private void UDPListener() {
		String msg = UDPcn.readMsg();
		byte header = msg.getBytes()[0];
		String data = new String(msg.getBytes(), 1, msg.length() - 1);
		String[] dat = data.split("!");

		byteRecv += msg.length();
		NetworkEvent event = nem.get(header);
		if (dat.length > 1) {
			NetworkPing.addPingTime(System.currentTimeMillis() - Long.parseLong(dat[1]));
		}
		if (event != null) {
			event.process(dat[0], UDPcn);
		}
	}

	public void TCPListener() {
		String msg = TCPcn.readMsg();
		byte header = msg.getBytes()[0];
		String data = new String(msg.getBytes(), 1, msg.length() - 1);
		String[] dat = data.split("!");

		byteRecv += msg.length();
		NetworkEvent event = nem.get(header);
		if (dat.length > 1) {

		}
		if (event != null) {
			event.process(dat[0], TCPcn);
		}
	}

	public void addEvent() {

	}

	public void UDPsendMsg(String msg) {
		new Thread(() -> {
			UDPcn.sendMsg(msg + "!" + System.currentTimeMillis());
		}).start();
	}

	public void TCPsendMsg(String msg) {
		if (!TCPcn.isConnected()) {
			Gdx.app.error("TCP", "Not connected...");
			return;
		}
		TCPcn.sendMsg(msg + "!" + System.currentTimeMillis());
	}

	public void sendJoin(String name, String type, float x, float y) {
		Gdx.app.log("Network", "Send packet Player join ...");
		TCPsendMsg(NetworkEventJoin.createJoinMsg(name, type, x, y, 0));
		UDPsendMsg(NetworkEventJoin.createJoinMsg(name, type, x, y, 0));
	}

	public void sendMove(String name, float x, float y) {
		UDPsendMsg(NetworkEventMove.createMoveMsg(name, x, y));
	}

	public void sendBullet(String name, Rectangle r, int side) {
		TCPsendMsg(NetworkEventBullet.createMsg(name, r, side));
	}

	public void sendDead(String playerKill, String playerDead) {
		TCPsendMsg(NetworkEventDead.createMsg(playerKill, playerDead));
	}

	public void updateList() {
		TCPsendMsg(NetworkEventUpdate.createUpdateMsg());
	}

	public void testPing() {
		TCPsendMsg(NetworkEventPong.getMsg());
	}

	public boolean isConn() {
		return TCPcn.isConnected() && UDPcn.isConnected();
	}

	public static long getByteRecv() {
		return byteRecv;
	}
}
