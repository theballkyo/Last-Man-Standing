package com.lms.item;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.lms.buff.CoreBuff;
import com.lms.buff.GodBuff;
import com.lms.buff.SpeedBuff;
import com.lms.game.LmsGame;
import com.lms.network.NetworkEventItem;

public class RandomItem extends Item {

	public RandomItem(int duration, Vector2 pos, int id) {
		super(duration, pos, id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPick(String playerName) {
		Random rand = new Random();
		int r = rand.nextInt(2);
		
		if (r == 0) {
			CoreBuff.add(playerName, new SpeedBuff(playerName, 5000, rand.nextInt(800)-300));
		} else if (r == 1) {
			CoreBuff.add(playerName, new GodBuff(playerName, Math.min(1000, rand.nextInt(5000))));
		}
		LmsGame.networkManage.sendPick(r);
	}

	@Override
	public void onTimeout() {
		// TODO Auto-generated method stub
		
	}

}
