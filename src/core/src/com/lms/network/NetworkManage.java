package com.lms.network;

import java.util.HashMap;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.lms.entity.MainEntity;
import com.uwsoft.editor.renderer.SceneLoader;

import net.lastman.network.core.ClientNetwork;

public class NetworkManage implements Runnable{

	ClientNetwork cn;
	SceneLoader sl;
	MainEntity me;
	Viewport vp;
	private HashMap<Byte, NetworkEvent> events;
	
	public NetworkManage(ClientNetwork cn, SceneLoader sl, MainEntity me, Viewport vp) {
		this.cn = cn;
		this.sl = sl;
		this.me = me;
		this.vp = vp;
		events = new HashMap<Byte, NetworkEvent>();
		load();
	}
	
	private void load() {
		events.put(NetworkEventJoin.headerCode, new NetworkEventJoin(this));
		events.put(NetworkEventUpdate.headerCode, new NetworkEventJoin(this));
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
		String data = new String(msg.getBytes(), 1, msg.length());
		
		events.get(header).process(data);
	}
	
	public void addEvent() {
		
	}
	
	public void sendMsg(String msg) {
		cn.sendMsg(msg);
	}
	
}
