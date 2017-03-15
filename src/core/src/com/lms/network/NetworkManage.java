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

	private Thread pong;
	private static long byteRecv = 0;

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
				Thread.currentThread().setName("UDP Listener");
				while (true) {
					UDPListener();
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				Thread.currentThread().setName("TCP Listener");
				while (true) {
					TCPListener();
				}
			}
		}).start();

		pong = new Thread(() -> {
			Thread.currentThread().setName("PING Listener");
			while (true) {
				TCPsendMsg("p");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}
			}
		});

		pong.start();
	}

	private void UDPListener() {
		String msg = UDPcn.readMsg();
		byte header = msg.getBytes()[0];
		String data = new String(msg.getBytes(), 1, msg.length() - 1);
		String[] dat = data.split("!");

		NetworkManage.byteRecv += msg.length();
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

		NetworkManage.byteRecv += msg.length();
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
		UDPcn.sendMsg(msg + "!" + System.currentTimeMillis());
	}

	public void TCPsendMsg(String msg) {
		if (!TCPcn.isConnected()) {
			Gdx.app.error("TCP", "Not connected..." + msg);
			return;
		}
		TCPcn.sendMsg(msg + "!" + System.currentTimeMillis());
	}

	public void sendJoin(String name, String type, float x, float y) {
		Gdx.app.log("Network", "Send packet Player join ...");
		if (!TCPcn.isConnected() || !NetworkEventJoin.tcpJoin) {
			TCPsendMsg(NetworkEventJoin.createJoinMsg(name, type, x, y, 0));
		}
		if (!UDPcn.isConnected() || !NetworkEventJoin.udpJoin) {
			UDPsendMsg(NetworkEventJoin.createJoinMsg(name, type, x, y, 0));
		}
	}

	public void sendMove(String name, long seq, float x, float y) {
		UDPsendMsg(NetworkEventMove.createMoveMsg(name, seq, x, y));
	}

	public void sendBullet(String name, Rectangle r, int side) {
		TCPsendMsg(NetworkEventBullet.createMsg(name, r, side));
	}

	public void sendSword(String name, int width) {
		TCPsendMsg(NetworkEventSword.createMsg(name, width));
	}

	public void sendDead(String playerKill, String playerDead) {
		TCPsendMsg(NetworkEventDead.createMsg(playerKill, playerDead));
	}

	public void sendBuff(byte buffCode, String name, int duration, String[] arg) {
		TCPsendMsg(NetworkEventBuff.createMsg(buffCode, name, duration, arg));
	}

	public void sendRemoveBullt(int id) {
		TCPsendMsg(NetworkEventBullet.removeBullet(id));
	}

	public void sendPick(int id) {
		TCPsendMsg(NetworkEventItem.pickMsg(id));
	}

	public void updateList() {
		TCPsendMsg(NetworkEventUpdate.createUpdateMsg());
	}

	public void testPing() {
		TCPsendMsg(NetworkEventPong.getMsg());
	}

	public void reqBuff() {
		TCPsendMsg(NetworkEventBuff.reqBuffData());
	}

	public boolean isConn() {
		return TCPcn.isConnected() && UDPcn.isConnected();
	}

	public static long getByteRecv() {
		return NetworkManage.byteRecv;
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		pong.interrupt();
		pong.stop();
		TCPcn.stop();
		UDPcn.stop();
	}

}
