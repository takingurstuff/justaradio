package com.justaradio.item;


import java.util.List;
import java.util.function.Consumer;

import net.minecraft.core.BlockPos;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;

public class FunniStick extends Item {
    public FunniStick(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        Level lvl = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (lvl.isClientSide()) {
            return InteractionResult.PASS;
        }
        lvl.setBlockAndUpdate(pos, Blocks.WHITE_CONCRETE.defaultBlockState());
        if (player != null) {
            context.getItemInHand().hurtAndBreak(1, player, context.getHand());
        }
        return InteractionResult.SUCCESS;
    }

    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }
        if (!user.isFallFlying()) {
            return InteractionResult.PASS;
        }
        ItemStack rocketz = new ItemStack(Items.FIREWORK_ROCKET);
        rocketz.set(DataComponents.FIREWORKS, new Fireworks(3, List.of()));
        FireworkRocketEntity et = new FireworkRocketEntity(level, rocketz, user);
        level.addFreshEntity(et);
        user.getItemInHand(hand).hurtAndBreak(1, user, hand);
        return InteractionResult.SUCCESS;
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
    	textConsumer.accept(Component.translatable("itemTooltip.justaradio.funni_stick.info").withStyle(ChatFormatting.WHITE));
    }
}
