package com.lms.item;

import com.lms.api.PlayerAPI;
import com.lms.api.PlayerData;
import com.lms.game.LmsConfig;

public class SpeedUpItem extends Item {

	private PlayerData pd;
	
	public SpeedUpItem(long expire, int timeout) {
		super(expire, timeout);
	}

	@Override
	public void onPick(PlayerData pd) {
		this.pd = pd;
		PlayerAPI.get(LmsConfig.playerName).speedRun += 500;

	}

	@Override
	public void onTimeout() {
		PlayerAPI.get(LmsConfig.playerName).speedRun -= 500;
	}
	
}
