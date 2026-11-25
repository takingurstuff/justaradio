package com.justaradio.item;

import java.util.function.Function;

import com.justaradio.Justaradio;

import net.minecraft.tags.TagKey;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

public class ModItems {
    public static final TagKey<Block> INCORRECT_FOR_ERM = TagKey.create(BuiltInRegistries.BLOCK.key(),
            Identifier.fromNamespaceAndPath(Justaradio.MOD_ID, "incorrect_for_erm"));
    public static final TagKey<Block> MINEABLE_WITH_ERM = TagKey.create(BuiltInRegistries.BLOCK.key(),
            Identifier.fromNamespaceAndPath(Justaradio.MOD_ID, "mineable_with_erm"));
    public static final ToolMaterial ERM = new ToolMaterial(INCORRECT_FOR_ERM, 5000, 1000, 1000, 1, null);
    public static final Item FUNNI_STICK = register(ModItemIds.FUNNI_STICK, FunniStick::new, new FunniStick.Properties().tool(ERM, MINEABLE_WITH_ERM, 1, 1, 5));
    public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory,
            Item.Properties settings) {
        // Create the item instance.
        Item item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }
    
    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(ModItems.FUNNI_STICK));
    }
}
