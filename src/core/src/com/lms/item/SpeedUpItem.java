package com.lms.item;

import com.badlogic.gdx.math.Vector2;
import com.lms.api.PlayerAPI;
import com.lms.api.PlayerData;
import com.lms.buff.CoreBuff;
import com.lms.buff.SpeedBuff;
import com.lms.game.LmsConfig;

public class SpeedUpItem extends Item {

	String playerName;
	
	public SpeedUpItem(int duration, Vector2 pos) {
		super(duration, pos);
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
