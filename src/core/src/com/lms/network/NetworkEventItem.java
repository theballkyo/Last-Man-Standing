package com.lms.network;

import java.net.InetAddress;
import java.net.Socket;

import com.badlogic.gdx.Gdx;
import com.lms.game.LmsConfig;
import com.lms.object.ItemObject;

import net.lastman.network.core.TCPClient;
import net.lastman.network.core.UDPClient;

public class NetworkEventItem extends NetworkEvent {

	public static byte headerCode;

	public static int[][] itemPos = { { -950, 905, 1 }, { -1840, 150, 1 }, { -645, 470, 1 }, { 905, 935, 1 },
			{ 735, 140, 1 }, { 1525, 215, 1 }, { 2835, 925, 1 }, { 2270, 645, 1 }, { 3360, 645, 1 }, { -4413, 1192, 1 },
			{ -7074, 550, 1 }, { -7605, 1002, 1 }, { -9255, 137, 1 }, { -4003, 219, 1 }, { 4685, 0, 1 },
			{ 7274, 450, 1 }, { 10251, 350, 1 }, { 10480, 925, 1 }, { 8019, 1161, 1 }, { 5109, 734, 1 } };

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
		if (dat.length == 1) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Gdx.app.postRunnable(new Runnable() {
						
						@Override
						public void run() {
							ItemObject.remove(Integer.parseInt(dat[0]));
						}
					});
					
				}
			}).start();
		} else {
			int id = Integer.parseInt(dat[0]);
			int x = Integer.parseInt(dat[1]);
			int y = Integer.parseInt(dat[2]);
			int show = Integer.parseInt(dat[3]);
	
			new Thread(new Runnable() {
				@Override
				public void run() {
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							if (show == 1) {
								ItemObject.addRandom(id, x, y);
							} else {
								if (LmsConfig.debug) {
								}
								ItemObject.remove(id);
							}
	
						}
					});
				}
			}).start();
		}

	}

	@Override
	public void processServer(String data, InetAddress address, int port, String time, UDPServerInterface udp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processServer(String data, Socket client, String time, TCPServerInterface tcp) {
		String s_num = data.split(":")[0];
		if (Integer.parseInt(s_num) > NetworkEventItem.itemPos.length) {
			return;
		}
		int num = Integer.parseInt(s_num);
		NetworkEventItem.itemPos[num][2] = 0;
		tcp.broadcast(client, pickMsg(num));
	}

	public static String pickMsg(int index) {
		return String.format("%c%d:", NetworkEventItem.headerCode, index);
	}

	public static String createMsg(int index, int x, int y, int show) {
		return String.format("%c%d:%d:%d:%d", NetworkEventItem.headerCode, index, x, y, show);
	}

	public static void processBg(TCPServerInterface tcp) {
		int count = 0;
		while (true) {
			int i = 0;
			for (int[] i1 : NetworkEventItem.itemPos) {
				tcp.broadcast(NetworkEventItem.createMsg(i, i1[0], i1[1], i1[2]));
				i++;
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			updateBg();
		}
	}

	public static void updateBg() {
		for (int[] i1 : NetworkEventItem.itemPos) {
			i1[2] = 1;
		}
	}
}
