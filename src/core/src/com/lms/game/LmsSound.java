package com.lms.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class LmsSound {

	private static Sound swordSound;
	private static Sound gunSound;
	private static Sound pickUp;
	private static Sound gameSound;
	private static Sound loginSound;

	static {
		LmsSound.swordSound = Gdx.audio.newSound(Gdx.files.internal("sounds/swing.mp3"));
		LmsSound.gunSound = Gdx.audio.newSound(Gdx.files.internal("sounds/gun2.mp3"));
		LmsSound.pickUp = Gdx.audio.newSound(Gdx.files.internal("sounds/pickup.mp3"));
		LmsSound.gameSound = Gdx.audio.newSound(Gdx.files.internal("sounds/fight.mp3"));
		LmsSound.loginSound = Gdx.audio.newSound(Gdx.files.internal("sounds/start.mp3"));
	}

	public static void playSword() {
		LmsSound.swordSound.play();
	}

	public static void playGun() {
		LmsSound.gunSound.play();
	}

	public static void playPickup() {
		LmsSound.pickUp.play();
	}

	public static void playGameSound() {
		gameSound.loop();
		gameSound.play();
	}

	public static void stopGameSound() {
		gameSound.stop();
	}

	public static void playLoginSound() {
		loginSound.play();
	}

	public static void stopLoginSound() {
		loginSound.stop();
	}
}
