package com.justaradio.RadioNet;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Channel(Integer frequency, String url) {
    public static final Codec<Channel> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        Codec.INT.fieldOf("frequency").forGetter(Channel::frequency),
        Codec.STRING.fieldOf("url").forGetter(Channel::url)
    ).apply(instance, Channel::new));
}
