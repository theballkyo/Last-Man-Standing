package com.lms.network;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lms.entity.MainEntity;
import com.uwsoft.editor.renderer.SceneLoader;

import net.lastman.network.core.ClientNetwork;

public class NetworkManage implements Runnable{

	ClientNetwork cn;
	SceneLoader sl;
	MainEntity me;
	Viewport vp;
	NetworkEventManage nem;
	
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
		while(true) {
			listener();
		}
	}
	
	private void listener() {
		String msg = cn.readMsg();
		byte header = msg.getBytes()[0];
		String data = new String(msg.getBytes(), 1, msg.length()-1);
		
		//Drop packet
		if(Long.parseLong(data.split("!")[1]) < lastRecv) {
			return;
		}
		lastRecv = System.currentTimeMillis();
		NetworkEvent event = nem.get(header);
		if(event != null)
			event.process(data.split("!")[0]);
	}
	
	public void addEvent() {
		
	}
	
	public void sendMsg(String msg) {
		cn.sendMsg(msg + "!" + System.currentTimeMillis());
	}
	
	public void sendJoin(String name, String type, float x, float y) {
		Gdx.app.log("Network", "Player Join...");
		cn.sendMsg(NetworkEventJoin.createJoinMsg(name, type, x, y));
	}
	
	public void sendMove(String name, float x, float y) {
		cn.sendMsg(NetworkEventMove.createMoveMsg(name, x, y));
	}
	
	public void rqList() {
		cn.sendMsg(NetworkEventRqList.createRqListMsg());
	}
	
	public void testPing() {
		cn.sendMsg(NetworkEventPong.getMsg());
	}
	
}
