package com.justaradio.block;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import com.justaradio.RadioNet.CanNetwork;
import com.justaradio.RadioNet.DeviceType;
import com.justaradio.RadioNet.RadioNetSaveData;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class CableBlock extends BaseEntityBlock {
    private static final VoxelShape CORE_SHAPE = Block.box(5, 5, 5, 11, 11, 11);
    private static final VoxelShape NORTH_SHAPE = Block.box(5, 5, 0, 11, 11, 5);
    private static final VoxelShape SOUTH_SHAPE = Block.box(5, 5, 11, 11, 11, 16);
    private static final VoxelShape EAST_SHAPE = Block.box(11, 5, 5, 16, 11, 11);
    private static final VoxelShape WEST_SHAPE = Block.box(0, 5, 5, 5, 11, 11);
    private static final VoxelShape UP_SHAPE = Block.box(5, 11, 5, 11, 16, 11);
    private static final VoxelShape DOWN_SHAPE = Block.box(5, 0, 5, 11, 5, 11);

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    public CableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    private VoxelShape buildShape(BlockState state) {
        VoxelShape shape = CORE_SHAPE;
        if (Boolean.TRUE.equals(state.getValue(NORTH)))
            shape = Shapes.or(shape, NORTH_SHAPE);
        if (Boolean.TRUE.equals(state.getValue(SOUTH)))
            shape = Shapes.or(shape, SOUTH_SHAPE);
        if (Boolean.TRUE.equals(state.getValue(EAST)))
            shape = Shapes.or(shape, EAST_SHAPE);
        if (Boolean.TRUE.equals(state.getValue(WEST)))
            shape = Shapes.or(shape, WEST_SHAPE);
        if (Boolean.TRUE.equals(state.getValue(UP)))
            shape = Shapes.or(shape, UP_SHAPE);
        if (Boolean.TRUE.equals(state.getValue(DOWN)))
            shape = Shapes.or(shape, DOWN_SHAPE);
        return shape;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return buildShape(state);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
            CollisionContext context) {
        return buildShape(state);
    }

    private boolean canConnectTo(BlockPos neighborPos, LevelReader level) {
        return level.getBlockEntity(neighborPos) instanceof CanNetwork;
    }

    private BooleanProperty propertyForDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            case UP -> UP;
            case DOWN -> DOWN;
        };
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = this.defaultBlockState();

        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.relative(dir);
            // BlockState neighborState = level.getBlockState(neighborPos);
            state = state.setValue(propertyForDirection(dir), canConnectTo(neighborPos, level));
        }
        return state;
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            LevelReader level,
            ScheduledTickAccess ticks,
            BlockPos pos,
            Direction directionFromNeighbor,
            BlockPos neighborPos,
            BlockState neighborState,
            RandomSource random) {
        BooleanProperty prop = propertyForDirection(directionFromNeighbor);
        if (canConnectTo(neighborPos, level)) {
            return state.setValue(prop, true);
        }
        return state;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CableBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CableBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
            ItemStack stack) {
        if (level.isClientSide()) {
            return;
        }
        RadioNetSaveData radioNet = RadioNetSaveData.get(level);
        BlockEntity self = level.getBlockEntity(pos);
        if (self != null && self instanceof CanNetwork selfEntity) {
            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = pos.relative(dir);
                if (canConnectTo(neighborPos, level)) {
                    BlockEntity neighbor = level.getBlockEntity(neighborPos);
                    if (neighbor != null && neighbor instanceof CanNetwork neigborEntity) {
                        selfEntity.setNetworkId(neigborEntity.getNetworkId());
                        radioNet.addDevice(neigborEntity.getNetworkId(), DeviceType.CABLE, pos);
                    }
                }
            }
            if (selfEntity.getNetworkId() == null) {
                UUID netId = radioNet.newNet();
                selfEntity.setNetworkId(netId);
                radioNet.addDevice(netId, DeviceType.CABLE, pos);
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof CableBlockEntity cableBlock)) {
            return super.useWithoutItem(state, level, pos, player, hit);
        }

        UUID netId = cableBlock.getNetworkId();

        if (level.isClientSide()) {
            player.sendOverlayMessage(
                    Component.literal("UUID is: " + ((netId != null) ? netId.toString() : "missing")));
        }

        return InteractionResult.SUCCESS;
    }
}