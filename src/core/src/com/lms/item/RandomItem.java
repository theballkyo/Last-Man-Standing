package com.lms.item;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.lms.buff.CoreBuff;
import com.lms.buff.GodBuff;
import com.lms.buff.JumpBuff;
import com.lms.buff.SpeedBuff;
import com.lms.game.LmsSound;

public class RandomItem extends Item {

	public RandomItem(int duration, Vector2 pos, int id) {
		super(duration, pos, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPick(String playerName) {
		LmsSound.playPickup();
		Random rand = new Random();
		int r = rand.nextInt(3);

		if (r == 0) {
			CoreBuff.add(playerName, new SpeedBuff(playerName, 5000, rand.nextInt(500 - 200 + 1) + 200));
		} else if (r == 1) {
			CoreBuff.add(playerName, new GodBuff(playerName, Math.min(2000, rand.nextInt(5000))));
		} else if (r == 2) {
			CoreBuff.add(playerName, new JumpBuff(playerName, 5000, rand.nextInt(600 - 200 + 1) + 200));
		}
	}

	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub

	}

}
