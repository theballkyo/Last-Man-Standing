package com.lms.network;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

import com.lms.item.Item;
import com.lms.object.ItemObject;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class NetworkEventItem extends NetworkEvent {

	public static byte headerCode;
	
	public static int[][] itemPos = {
			{-950, 905, 1},
			{-1840, 150, 1},
			{-645,470, 1},
			{905,935, 1},
			{735,140,1},
			{1525,215, 1},
			{2835,925, 1},
			{2270,645, 1},
			{3360,645, 1}
			
			
	};
	
	/**
	 * 
	 * X:Y:OK
	 * 
	 */
	
	@Override
	public void process(String data, UDPClient UDPcn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void process(String data, TCPClient TCPcn) {
		String[] dat = data.split(":");
		int id = Integer.parseInt(dat[0]);
		int x = Integer.parseInt(dat[1]);
		int y = Integer.parseInt(dat[2]);
		int show = Integer.parseInt(dat[3]);
		if (show == 1) 
			ItemObject.addRandom(id, x, y);
		else 
			ItemObject.remove(id);
	}

	@Override
	public void processServer(String data, InetAddress address, int port, String time, UDPServerInterface udp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processServer(String data, Socket client, String time, TCPServerInterface tcp) {

		if (Integer.parseInt(data)  > itemPos.length)
			return;
		
		itemPos[Integer.parseInt(data)][2] = 0;
		
		
	}
	
	public static String pickMsg(int index) {
		return String.format("%c%d", headerCode, index);
	}
	
	public static String createMsg(int index, int x, int y, int show) {
		return String.format("%c%d:%d:%d:%d", headerCode, index, x, y, show);
	}

	public static void processBg(TCPServerInterface tcp) {
		int count = 0;
		while (true) {
			int i = 0;
			for (int[] i1 : itemPos) {
				tcp.broadcast(NetworkEventItem.createMsg(i, i1[0], i1[1], i1[2]));
				i++;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			count += 1;
			if (count >= 100) {
				updateBg();
				count = 0;
			}
		}
	}
	
	public static void updateBg() {
		for (int[] i1 : itemPos) {
			i1[2] = 1;
		}
	}
}
