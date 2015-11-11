package com.lms.item;

import com.lms.api.PlayerAPI;
import com.lms.game.LmsConfig;

public class SpeedUpItem extends Item {

	public SpeedUpItem(long expire) {
		super(expire);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPick() {
		PlayerAPI.get(LmsConfig.playerName).speedRun += 500;
		
		new Thread(
				() -> {
					try {
						Thread.sleep(5000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					PlayerAPI.get(LmsConfig.playerName).speedRun -= 500;
				}
			).start();
	}
	
}
