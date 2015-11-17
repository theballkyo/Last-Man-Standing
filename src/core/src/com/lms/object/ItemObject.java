package com.lms.object;

import java.util.ArrayList;

import com.lms.item.Item;

public class ItemObject {

	private static ArrayList<Item> items = new ArrayList<>();

	public static void add(Item item) {
		items.add(item);
	}

	public static ArrayList<Item> getAll() {
		return items;
	}
}
