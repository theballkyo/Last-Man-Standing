package net.lastman.network.core;

import java.util.Scanner;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.backends.headless.HeadlessNet;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.lms.game.LmsConfig;


public class MainServer {

	public static void main(String[] args) {

		/*
		HeadlessNativesLoader.load();
		MockGraphics mockGraphics = new MockGraphics();
		Gdx.graphics = mockGraphics;
		HeadlessNet headlessNet = new HeadlessNet();
		Gdx.net = headlessNet;
		HeadlessFiles headlessFiles = new HeadlessFiles();
		Gdx.files = headlessFiles;

		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		ApplicationListener handlessApp = new HeadlessApplication(new ListenerServer()).getApplicationListener();
		*/
		final UDPServer server = new UDPServer(LmsConfig.port);
		
		if (args.length > 1) {
			server.setDelay(Integer.parseInt(args[0]));
		}
		/*
		new Thread(new Runnable(){
			public void run() {
				final Scanner in = new Scanner(System.in);
		        while(in.hasNext()) {
		            final String line = in.nextLine();
		            System.out.println("> " + line);
		            if ("end".equalsIgnoreCase(line)) {
		            	
		                System.out.println("Ending one thread");
		                break;
		            }
		            server.broadcast(null, line);
		        }
			}
		}).start();
		*/
		System.out.println("Server is started.");
		server.start();
		// new UDPServer(7777);
	}

}
