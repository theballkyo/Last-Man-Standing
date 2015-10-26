package net.lastman.network.core;

import java.io.IOException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.backends.headless.HeadlessNet;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.net.ServerSocketHints;
public class MainServer {

	public static void main(String[] args) {
		HeadlessNativesLoader.load();
		MockGraphics mockGraphics = new MockGraphics();
		Gdx.graphics = mockGraphics;
		HeadlessNet headlessNet = new HeadlessNet();
		Gdx.net = headlessNet;
		HeadlessFiles headlessFiles = new HeadlessFiles();
		Gdx.files = headlessFiles;

		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		ApplicationListener handlessApp = new HeadlessApplication(new ListenerServer()).getApplicationListener();

		new GameServer().runSocket();
	}

}
