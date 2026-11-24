package com.justaradio.item;


import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.context.UseOnContext;

public class FunniStick extends Item {
    public FunniStick(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        Level lvl = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (lvl.isClientSide()) {
            return InteractionResult.PASS;
        }
        lvl.setBlockAndUpdate(pos, Blocks.WHITE_CONCRETE.defaultBlockState());
        return InteractionResult.SUCCESS;
    }
}
