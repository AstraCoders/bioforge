package com.lucasmellof.bioforge.gene;

import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.Collections;
import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
@Getter
public class Gene {

    public static final Codec<Gene> CODEC = RecordCodecBuilder.create((it) -> it.group(
        ResourceLocation.CODEC.fieldOf("id").forGetter(Gene::getId),
        GeneType.CODEC.fieldOf("type").forGetter(Gene::getType),
        Codec.INT.fieldOf("level").forGetter(Gene::getLevel)
    ).apply(it, Gene::new
    ));

    public static final StreamCodec<ByteBuf, Gene> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
    public static final StreamCodec<ByteBuf, List<Gene>> STREAM_CODEC_LIST = ByteBufCodecs.fromCodec(CODEC.listOf());

    @Setter
    private int level = 1;
    private final ResourceLocation id;
    private final GeneType<?> type;
    private final GeneAction action;

    public Gene(ResourceLocation id, GeneType<?> type) {
        this.id = id;
        this.type = type;
		this.action = type.action().get();
    }

    public Gene(ResourceLocation id, GeneType<?> type, int level) {
        this.id = id;
        this.type = type;
        this.level = level;
        this.action = type.action().get();
    }


    public void tick() {}

    public boolean canApplyGene(IEntityWithGene entity, Gene gene) {
        for (Holder<GeneType<?>> incompatibleGene : gene.getInfo().getIncompatibleGenes()) {
            if (entity.bioforge$hasGene(incompatibleGene.value())) {
                return false;
            }
        }

        for (EntityType<?> incompatibleEntity : gene.getInfo().getIncompatibleEntities()) {
            if (incompatibleEntity.equals(entity.self().getType())) {
                return false;
            }
        }

        for (EntityType<?> applicableEntity : gene.getInfo().getApplicableEntities()) {
            if (applicableEntity.equals(entity.self().getType())) {
                return true;
            }
        }
        if (!entity.bioforge$hasGene(gene.getType())) {
            return true;
        }

        var existing = entity.bioforge$getGene(gene.getType());
		return existing == null || existing.getLevel() < gene.getLevel();
	}

    public boolean removeGene(IEntityWithGene genes) {
        return true;
    }

    public void addGene(IEntityWithGene entity) {
        var existing = entity.bioforge$getGene(getType());
        if (existing == null || existing.getLevel() >= this.getLevel()) {
            return;
        }

        existing.addLevel(1);
    }

    @SafeVarargs
    public final void setIncompatibleGenes(Holder<GeneType<?>>... genes) {
        if (getInfo().getIncompatibleGenes() == null) {
            throw new IllegalStateException("Incompatible genes list is null");
        }
        getInfo().getIncompatibleGenes().clear();
        Collections.addAll(getInfo().getIncompatibleGenes(), genes);
    }


    public GeneInfo getInfo() {
        return this.getType().info();
    }
    public void addLevel(int level) {
        this.level += level;
    }

}
