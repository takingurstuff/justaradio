package com.justaradio.block;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

import com.justaradio.RadioNet.CanNetwork;
import com.justaradio.RadioNet.DeviceType;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;

public class CableBlockEntity extends BlockEntity implements CanNetwork {
    public CableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CABLE_BLOCK_ENTITY, pos, state);
    }

    @Nullable
    private UUID networkId;

    @Nullable
    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
        setChanged();
    }

    public DeviceType getDeviceType() {
        return DeviceType.CABLE;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        if (this.networkId != null) {
            output.putString("networkId", this.networkId.toString());
            super.saveAdditional(output);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.networkId = null;

        input.getString("networkId").ifPresent(raw -> {
            try {
                this.networkId = UUID.fromString(raw);
            } catch (IllegalArgumentException ignored) {
                this.networkId = null;
            }
        });
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (level == null)
            return;

        BlockState state = getBlockState();
        level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
    }
}
