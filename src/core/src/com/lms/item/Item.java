package com.lms.item;

public abstract class Item {
	
	private long expire;
	
	public Item(long expire) {
		this.expire = expire;
	}
	
	public abstract void onPick();
	
}
