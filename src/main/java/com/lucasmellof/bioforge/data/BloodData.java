package com.lucasmellof.bioforge.data;

import com.lucasmellof.bioforge.gene.Gene;
import com.lucasmellof.bioforge.utils.ColorUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rok, Pedro Lucas nmm. Created on 18/10/2025
 * @project bioforge
 */
public record BloodData(int color, boolean full, List<Gene> genes, boolean discovered) {

    public static final BloodData EMPTY = new BloodData(0xFFFFFF, false, List.of(), false);

    public static final Codec<BloodData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT.fieldOf("color").forGetter(BloodData::color),
                    Codec.BOOL.fieldOf("full").forGetter(BloodData::full),
                    Gene.CODEC.listOf().fieldOf("genes").forGetter(BloodData::genes),
                    Codec.BOOL.fieldOf("discovered").forGetter(BloodData::discovered))
            .apply(instance, BloodData::new));

    public static final Codec<List<BloodData>> HOLDER_LIST_CODEC = CODEC.listOf();

    public static final StreamCodec<ByteBuf, BloodData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            BloodData::color,
            ByteBufCodecs.BOOL,
            BloodData::full,
            Gene.STREAM_CODEC_LIST,
            BloodData::genes,
            ByteBufCodecs.BOOL,
            BloodData::discovered,
            BloodData::new);

    public static final StreamCodec<ByteBuf, List<BloodData>> HOLDER_LIST_STREAM_CODEC =
            ByteBufCodecs.fromCodec(HOLDER_LIST_CODEC);

    public static final int MAX_MIX_COUNT = 2;
    public static final Codec<Integer> MIX_COUNT_CODEC = Codec.intRange(0, MAX_MIX_COUNT);

    public static final StreamCodec<ByteBuf, Integer> MIX_COUNT_STREAM_CODEC = ByteBufCodecs.INT;

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

    public BloodData mix(BloodData data) {
        var list = new ArrayList<>(this.genes);
        for (var gene : data.genes) {
            if (!list.contains(gene)) {
                list.add(gene);
            }
        }
        return new BloodData(ColorUtils.blend(this.color, data.color, 0.5f), true, list, false);
    }

    public BloodData discover() {
        return new BloodData(this.color, this.full, this.genes, true);
    }
}
