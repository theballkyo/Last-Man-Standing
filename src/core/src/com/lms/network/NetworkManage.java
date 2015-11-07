package com.lms.network;

import java.net.Socket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lms.entity.MainEntity;
import com.lms.game.LmsGame;
import com.uwsoft.editor.renderer.SceneLoader;

import net.lastman.network.core.ClientNetwork;

public class NetworkManage implements Runnable {

	ClientNetwork UDPcn;
	ClientNetwork TCPcn;
	SceneLoader sl;
	MainEntity me;
	Viewport vp;
	NetworkEventManage tcpNet;
	NetworkEventManage udpNet;
	Socket client;

	private long lastRecv = 0;

	public NetworkManage(ClientNetwork UDPcn, ClientNetwork TCPcn, SceneLoader sl, MainEntity me, Viewport vp) {
		this.UDPcn = UDPcn;
		this.TCPcn = TCPcn;
		this.sl = sl;
		this.me = me;
		this.vp = vp;
		this.tcpNet = new NetworkEventManage(this, NetworkEventManage.Type.TCP);
		this.udpNet = new NetworkEventManage(this, NetworkEventManage.Type.UDP);
		// Gdx.app.log("Network", "Create object");
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

		lastRecv = System.currentTimeMillis();
		NetworkEvent event = udpNet.get(header);
		if (dat.length > 1) {
			LmsGame.pingTime = System.currentTimeMillis() - Long.parseLong(dat[1]);
			LmsGame.sumPingTime += LmsGame.pingTime;
			LmsGame.countPing += 1;
			// System.out.println(dat[0] + " | Ping: " + LmsGame.pingTime);
		}
		if (event != null) {
			event.process(dat[0]);
		}
	}

	public void TCPListener() {

	}

	public void addEvent() {

	}

	public void UDPsendMsg(String msg) {
		UDPcn.sendMsg(msg + "!" + System.currentTimeMillis());
	}

	public void TCPsendMsg(String msg) {
		TCPcn.sendMsg(msg + "!" + System.currentTimeMillis());
	}

	public void sendJoin(String name, String type, float x, float y) {
		Gdx.app.log("Network", "Send packet Player join ...");
		this.TCPsendMsg(NetworkEventJoin.createJoinMsg(name, type, x, y));
	}

	public void sendMove(String name, float x, float y) {
		this.UDPsendMsg(NetworkEventMove.createMoveMsg(name, x, y));
	}

	/*
	 * public void rqList() {
	 * this.sendMsg(NetworkEventRqList.createRqListMsg()); }
	 */

	public void testPing() {
		this.TCPsendMsg(NetworkEventPong.getMsg());
	}

}
