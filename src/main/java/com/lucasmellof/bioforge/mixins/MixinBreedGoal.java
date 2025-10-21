package com.lucasmellof.bioforge.mixins;

import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.registry.ModGenes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 21/10/2025
 */
@Mixin(BreedGoal.class)
public class MixinBreedGoal {
	@Shadow @Final protected Animal animal;

	@Shadow @Final protected Level level;

	@Shadow @Nullable protected Animal partner;

	@Inject(method = "breed", at = @At(value="TAIL"))
	public void bioforge$setAge(CallbackInfo ci) {
		IEntityWithGene entity = (IEntityWithGene) this.animal;
		if (!entity.bioforge$hasGene(ModGenes.BREEDABLE_GENE.get())) {
			return;
		}

		var gene = entity.bioforge$getGene(ModGenes.BREEDABLE_GENE.get());
		// higher levels have chance to spawn another baby
		var percentage = 0.1 * gene.getLevel();
		boolean chance = Math.random() < percentage;
		if (chance) {
			this.animal.spawnChildFromBreeding((ServerLevel)this.level, this.partner);
		}
	}
}
