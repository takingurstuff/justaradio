package com.justaradio.RadioNet;

import java.util.Set;
import java.util.UUID;
import java.util.HashSet;
import java.util.ArrayList;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.UUIDUtil;


public record RadioNet(UUID netId, Set<Device> devices, boolean isEmitterNet, boolean hasController) {
    public static final Codec<Set<Device>> DEVICE_SET_CODEC = Device.CODEC.listOf().xmap(
        HashSet::new,
        ArrayList::new
    );
    public static final Codec<RadioNet> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        UUIDUtil.CODEC.fieldOf("netId").forGetter(RadioNet::netId),
        DEVICE_SET_CODEC.fieldOf("devices").forGetter(RadioNet::devices),
        Codec.BOOL.optionalFieldOf("isEmitterNet", false).forGetter(RadioNet::isEmitterNet),
        Codec.BOOL.optionalFieldOf("hasController", false).forGetter(RadioNet::hasController)
        
    ).apply(instance, RadioNet::new));

}
