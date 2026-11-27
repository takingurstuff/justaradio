package com.justaradio.RadioNet;

import java.util.UUID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;

public record Device(DeviceType type, BlockPos devicePosition, UUID netId) {
    public static final Codec<Device> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        DeviceType.CODEC.fieldOf("type").forGetter(Device::type),
        BlockPos.CODEC.fieldOf("devicePosition").forGetter(Device::devicePosition),
        UUIDUtil.CODEC.fieldOf("netId").forGetter(Device::netId)
    ).apply(instance, Device::new));
}
