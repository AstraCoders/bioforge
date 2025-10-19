package com.lucasmellof.bioforge.gene.types;

import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.gene.Gene;
import com.lucasmellof.bioforge.gene.GeneType;
import com.lucasmellof.bioforge.registry.ModGenes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
public class AggressiveGene extends Gene {

	public static final MapCodec<AggressiveGene> CODEC = RecordCodecBuilder.mapCodec(
			it -> it.group(ResourceLocation.CODEC.fieldOf("id").forGetter(Gene::getId))
						  .apply(it, AggressiveGene::new));

	public AggressiveGene(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean removeGene(IEntityWithGene entity) {
		if (entity.bioforge$hasGene(ModGenes.AGGRESSIVE_GENE.get())) {
			entity.bioforge$addGene(ModGenes.PASSIVE_GENE.get().create());
		}

		return true;
	}

	@Override
	public void addGene(IEntityWithGene genes) {
		if (genes.bioforge$hasGene(ModGenes.PASSIVE_GENE.get())) {
			genes.bioforge$removeGene(ModGenes.PASSIVE_GENE.get());
		}
		if (!(genes.self() instanceof PathfinderMob mob)) {
			return;
		}
		boolean hasAttackGoal = false;
		for (WrappedGoal goal : mob.targetSelector.getAvailableGoals()) {
			if (goal.getGoal() instanceof NearestAttackableTargetGoal<?>) {
				hasAttackGoal = true;
				break;
			}
		}
		if (!hasAttackGoal) {
			mob.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mob, Player.class, false));
			mob.goalSelector.addGoal(1, new MeleeAttackGoal(mob, 1.0, false));
		}

	}

	@Override
	public GeneType<? extends Gene> getType() {
		return ModGenes.AGGRESSIVE_GENE.value();
	}
}
