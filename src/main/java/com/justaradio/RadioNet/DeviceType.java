package com.justaradio.RadioNet;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum DeviceType implements StringRepresentable {
    BASE_STATION("base_station"),
    RELAY("relay"),
    RECEIVER("receiver"),
    SPEAKER("speaker"),
    CABLE("cable");

    public static final Codec<DeviceType> CODEC = StringRepresentable.fromEnum(DeviceType::values);
    private final String SeralizedName;

    DeviceType(String seralizedName) {
        this.SeralizedName = seralizedName;
    }

    @Override
    public String getSerializedName() {
        return this.SeralizedName;
    }

}
