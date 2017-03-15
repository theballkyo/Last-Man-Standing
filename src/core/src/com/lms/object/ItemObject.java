package com.lms.object;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import com.badlogic.gdx.math.Vector2;
import com.lms.item.Item;
import com.lms.item.RandomItem;

public class ItemObject {

	private static ArrayList<Item> items = new ArrayList<>();
	private static Stack<Integer> queneRemove = new Stack<>();

	public static void add(Item item) {
		ItemObject.items.add(item);
	}

	public static void addRandom(int id, int x, int y) {
		for (Item item : ItemObject.items) {
			if (item.id == id) {
				return;
			}
		}

		ItemObject.items.add(new RandomItem(999999, new Vector2(x, y), id));
	}

	public synchronized static ArrayList<Item> getAll() {
		return ItemObject.items;
	}

	public synchronized static void remove(int id) {
		queneRemove.push(id);
	}

	public static void removeInStack() {
		Iterator<Item> items = ItemObject.getAll().iterator();
		synchronized (items) {
			while (items.hasNext()) {
				Item item = items.next();
				if (item == null) {
					continue;
				}
				if (queneRemove.remove(new Integer(item.id))) {
					try {
						item.remove();
						items.remove();
					} catch (NullPointerException e) {
						System.out.println("Item object: Can't fine entity item");
					}

				}
			}
		}
	}
}
