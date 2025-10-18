package com.lucasmellof.bioforge.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * @author Rok, Pedro Lucas nmm. Created on 18/10/2025
 * @project bioforge
 */
public record BloodData(int color) {
    
    public static final BloodData EMPTY = new BloodData(0xFFFFFF);
    
    public static final Codec<BloodData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.INT.fieldOf("color").forGetter(BloodData::color)
        ).apply(instance, BloodData::new)
    );
    
    public static final StreamCodec<ByteBuf, BloodData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, BloodData::color,
        BloodData::new
    );
    
    public int getRed() {
        return (color >> 16) & 0xFF;
    }
    
    public int getGreen() {
        return (color >> 8) & 0xFF;
    }
    
    public int getBlue() {
        return color & 0xFF;
    }
    
    public float getRedNormalized() {
        return getRed() / 255.0f;
    }
    
    public float getGreenNormalized() {
        return getGreen() / 255.0f;
    }
    
    public float getBlueNormalized() {
        return getBlue() / 255.0f;
    }
}