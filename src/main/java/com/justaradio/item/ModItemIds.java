package com.justaradio.item;

import com.justaradio.Justaradio;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

public class ModItemIds {
    public static ResourceKey<Item> create(String name) {
		// Create the item key.
		return ResourceKey.create(Registries.ITEM, Justaradio.id(name));
	}
}
