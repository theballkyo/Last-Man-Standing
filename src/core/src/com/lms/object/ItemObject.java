package com.lms.object;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.lms.game.LmsConfig;
import com.lms.item.Item;
import com.lms.item.RandomItem;

public class ItemObject {

	private static ArrayList<Item> items = new ArrayList<>();

	public static void add(Item item) {
		items.add(item);
	}

	public static void addRandom(int id, int x, int y) {
		for (Item item: items) {
			if (item.id == id) {
				return;
			}
		}
		
		items.add(new RandomItem(999999, new Vector2(x, y), id));
	}
	public static ArrayList<Item> getAll() {
		return items;
	}
	
	public static void remove(int id) {
		Iterator<Item> iter = items.iterator();
		
		while(iter.hasNext()) {
			Item item = iter.next();
			if (item.id == id) {
				LmsConfig.sl.engine.removeEntity(item.entity);
				iter.remove();
				break;
			}
		}
	}
}
