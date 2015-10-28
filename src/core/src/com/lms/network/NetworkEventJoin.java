package com.lms.network;

import com.lms.api.PlayerAPI;
import com.lms.entity.CoreEntity;
import com.lms.entity.SheepEntity;

public class NetworkEventJoin extends NetworkEvent{

	public static final byte headerCode = 0x01;
	
	public NetworkEventJoin(NetworkManage nm) {
		super(nm);
	}
	
	public byte headerCode() {
		return NetworkEventJoin.headerCode;
	}

	@Override
	public void process(String data) {
		String[] dat = data.split(":");
		SheepEntity player = nm.me.newEntity("sheep", dat[0]);
		PlayerAPI.PlayerList.put(dat[0], player);
		SheepEntity a =  (SheepEntity) PlayerAPI.PlayerList.get(dat[0]);
	}

}
