package net.lastman.network.core;

import java.io.IOException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.backends.headless.HeadlessNet;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader;
import com.badlogic.gdx.graphics.GL20;
public class MainServer {

	public static void main(String[] args) {
		HeadlessNativesLoader.load();
		MockGraphics mockGraphics = new MockGraphics();
		Gdx.graphics = mockGraphics;
		HeadlessNet headlessNet = new HeadlessNet();
		Gdx.net = headlessNet;
		HeadlessFiles headlessFiles = new HeadlessFiles();
		Gdx.files = headlessFiles;
		//Gdx.gl = mock(GL20.class);
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		//ApplicationListener myGdxGame = EntryPoint.getHeadlessMyGdxGame(config);
		try {
			new GameServer(20156).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
