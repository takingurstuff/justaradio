package com.justaradio.RadioNet;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class RadioNetSaveData extends SavedData {
    private Map<UUID, RadioNet> networks = new HashMap<>();

    public static final Codec<RadioNetSaveData> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(RadioNet.CODEC.listOf().fieldOf("networks").forGetter(RadioNetSaveData::getAllNetsAsList))
            .apply(instance, RadioNetSaveData::new));

    public static final SavedDataType<RadioNetSaveData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath("justaradio", "radionet"), RadioNetSaveData::new, CODEC, null);

    public RadioNetSaveData(Collection<RadioNet> nets) {
        for (RadioNet net : nets) {
            this.networks.put(net.netId(), net);
        }
    }

    public RadioNetSaveData() {
    }

    public Collection<RadioNet> getAllNets() {
        return this.networks.values();
    }

    public List<RadioNet> getAllNetsAsList() {
        return List.copyOf(this.networks.values());
    }

    public boolean netExists(UUID netId) {
        return (netId != null) && this.networks.containsKey(netId);
    }

    public UUID newNet() {
        UUID netId = UUID.randomUUID();
        this.networks.put(netId, new RadioNet(netId, new HashSet<>(), false, false));
        setDirty();
        return netId;
    }

    public boolean addDevice(UUID netId, DeviceType type, BlockPos pos) {
        if (!this.networks.containsKey(netId)) {
            return false;
        }
        RadioNet old = this.networks.get(netId);
        Set<Device> devices = old.devices();
        devices.add(new Device(type, pos, netId));
        RadioNet newnet = new RadioNet(netId, devices, old.isEmitterNet(), old.hasController());
        this.networks.replace(netId, newnet);
        setDirty();
        return true;
    }

    public boolean removeDevice(UUID netId, DeviceType type, BlockPos pos) {
        if (!this.networks.containsKey(netId)) {
            return false;
        }
        RadioNet old = this.networks.get(netId);
        Set<Device> devices = old.devices();
        devices.remove(new Device(type, pos, netId));
        RadioNet newnet = new RadioNet(netId, devices, old.isEmitterNet(), old.hasController());
        this.networks.replace(netId, newnet);
        setDirty();
        return true;
    }

    public static RadioNetSaveData get(Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            throw new IllegalStateException("RadioNetSaveData can only be accessed on a ServerLevel");
        }
        return serverLevel.getDataStorage().computeIfAbsent(TYPE);
    }
}
