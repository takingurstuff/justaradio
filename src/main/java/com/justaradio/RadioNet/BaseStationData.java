package com.justaradio.RadioNet;

import java.util.HashMap;

import org.jetbrains.annotations.Nullable;

public class BaseStationData {
    private HashMap<Integer, Channel> channels = new HashMap<>();
    private final Device HOST_DEVICE;

    public BaseStationData(Device hostDevice) {
        this.HOST_DEVICE = hostDevice;
    }

    public void addChannel(Integer frequency, String url) {
        this.channels.put(frequency, new Channel(frequency, url));
    }

    public void removeChannel(Integer frequency) {
        this.channels.remove(frequency);
    }

    public HashMap<Integer, Channel> getChannels() {
        return this.channels;
    }

    public @Nullable Channel getChannelForFreq(Integer frequency) {
        if (this.channels.containsKey(frequency)) {
            return this.channels.get(frequency);
        }
        return null;
    }
}
