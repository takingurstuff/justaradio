package com.justaradio.block;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import com.justaradio.Justaradio;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

public class ModBlockEntities {
    public static final BlockEntityType<CableBlockEntity> CABLE_BLOCK_ENTITY = register("cable", CableBlockEntity::new,
            ModBlocks.CABLE);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks) {
        Identifier id = Justaradio.id(name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id,
                FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

    public static void initialize() {

    }
}
