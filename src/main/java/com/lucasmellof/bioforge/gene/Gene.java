package com.lucasmellof.bioforge.gene;

import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.registry.ModGenes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
@Getter
public class Gene {

    public static final Codec<Gene> CODEC = RecordCodecBuilder.create((it) -> it.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(Gene::getId),
                    GeneType.CODEC.fieldOf("type").forGetter(Gene::getType),
                    Codec.INT.fieldOf("level").forGetter(Gene::getLevel))
            .apply(it, Gene::new));

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

    public void tick(LivingEntity entity, Gene gene) {
        if (this.action == null) return;
        this.action.tick(entity, gene);
    }

    public boolean canApplyGene(IEntityWithGene entity, Gene gene) {
		Registry<GeneType<?>> geneRegistry = ModGenes.GENE_REGISTRY;
        DefaultedRegistry<EntityType<?>> entityRegistry = BuiltInRegistries.ENTITY_TYPE;

		for (ResourceLocation incompatibleGene : gene.getInfo().getIncompatibleGenes()) {
			var geneType = geneRegistry.get(incompatibleGene);
            if (entity.bioforge$hasGene(geneType)) {
                return false;
            }
        }

        for (ResourceLocation incompatibleEntity : gene.getInfo().getIncompatibleEntities()) {
			var type = entityRegistry.get(incompatibleEntity);
            if (type.equals(entity.self().getType())) {
                return false;
            }
        }

        for (ResourceLocation applicableEntity : gene.getInfo().getApplicableEntities()) {
			var type = entityRegistry.get(applicableEntity);
            if (type.equals(entity.self().getType())) {
                return true;
            }
        }

        var existing = entity.bioforge$getGene(gene.getType());
        if (existing == null) return true;
        return existing.getLevel() >= gene.getLevel();
    }

    public boolean removeGene(IEntityWithGene genes) {
        return true;
    }

    public boolean addGene(IEntityWithGene entity) {
        var existing = entity.bioforge$getGene(getType());
        if (existing == null) {
            return true;
        }
        if (existing.getLevel() < this.getLevel()) {
            return false;
        }

        existing.addLevel(1);
        return false;
    }

    public GeneInfo getInfo() {
        return this.getType().info();
    }

    public void addLevel(int level) {
        this.level += level;
    }
}
