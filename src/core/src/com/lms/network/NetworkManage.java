package com.lms.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lms.entity.MainEntity;
import com.lms.game.LmsConfig;
import com.uwsoft.editor.renderer.SceneLoader;

import net.lastman.network.core.ClientNetwork;

public class NetworkManage implements Runnable{

	ClientNetwork cn;
	SceneLoader sl;
	MainEntity me;
	Viewport vp;
	NetworkEventManage nem;
	Socket client;
	
	private long lastRecv = 0;
	
	public NetworkManage(ClientNetwork cn, SceneLoader sl, MainEntity me, Viewport vp) {
		this.cn = cn;
		this.sl = sl;
		this.me = me;
		this.vp = vp;
		this.nem = new NetworkEventManage(this);
		Gdx.app.log("Network", "Create object");
	}
	
	@Override
	public void run() {
		cn.start();
		
		try {
			client = new Socket(LmsConfig.host, LmsConfig.port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		new Thread(new Runnable() {
			public void run() {
				while(true) {
					UDPListener();
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			public void run() {
				while(true) {
					TCPListener();
				}
			}
		}).start();
		
	}
	
	private void UDPListener() {
		
		String msg = cn.readMsg();
		byte header = msg.getBytes()[0];
		String data = new String(msg.getBytes(), 1, msg.length()-1);
		String[] dat = data.split("!");

		//Drop packet
		/*
		if(dat.length > 1) {
			if(Float.parseFloat(dat[1]) < lastRecv) {
				System.out.println("Drop: " + data);
				return;
			}
		}
		*/
		
		lastRecv = System.currentTimeMillis();
		NetworkEvent event = nem.get(header);
		if(event != null)
			event.process(dat[0]);
	}
	
	public void TCPListener() {
		try {
			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
	        System.out.println("Server says " + in.readUTF());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addEvent() {
		
	}
	
	public void sendMsg(String msg) {
		cn.sendMsg(msg + "!" + System.currentTimeMillis());
	}
	
	public void sendJoin(String name, String type, float x, float y) {
		Gdx.app.log("Network", "Player Join...");
		this.sendMsg(NetworkEventJoin.createJoinMsg(name, type, x, y));
	}
	
	public void sendMove(String name, float x, float y) {
		this.sendMsg(NetworkEventMove.createMoveMsg(name, x, y));
	}
	
	public void rqList() {
		this.sendMsg(NetworkEventRqList.createRqListMsg());
	}
	
	public void testPing() {
		this.sendMsg(NetworkEventPong.getMsg());
	}
	
}
