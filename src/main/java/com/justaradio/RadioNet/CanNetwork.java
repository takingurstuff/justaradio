package com.justaradio.RadioNet;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

public interface CanNetwork {
    @Nullable
    public UUID getNetworkId();

    public void setNetworkId(UUID networkId);

    public DeviceType getDeviceType();
}
