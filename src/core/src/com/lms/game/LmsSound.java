package com.lms.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class LmsSound {

	private static Sound swordSound;
	private static Sound gunSound;

	public static void load() {
		swordSound = Gdx.audio.newSound(Gdx.files.internal("sounds/swing.mp3"));
		gunSound = Gdx.audio.newSound(Gdx.files.internal("sounds/gun.wav"));
	}

	public static void playSword() {
		swordSound.play();
	}

	public static void playGun() {
		gunSound.play();
	}
}
