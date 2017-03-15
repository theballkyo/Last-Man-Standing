package com.lms.item;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.lms.buff.CoreBuff;
import com.lms.buff.SpeedBuff;

public class SpeedUpItem extends Item {

	String playerName;

	public SpeedUpItem(int duration, Vector2 pos) {
		super(duration, pos, new Random().nextInt(50000));
	}

	@Override
	public void onPick(String playerName) {
		this.playerName = playerName;
		CoreBuff.add(playerName, new SpeedBuff(playerName, 5000, 500));
	}

	@Override
	public void onTimeout() {

	}

}
