package com.justaradio.item;

import java.util.function.Function;

import com.justaradio.Justaradio;

import net.minecraft.tags.TagKey;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

public class ModItems {
        public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory,
            Item.Properties settings) {
        // Create the item instance.
        Item item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }
    
    public static void initialize() {
    }
}
