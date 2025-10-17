package com.lucasmellof.bioforge.mixins;

import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.gene.types.AggressiveGene;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
@Mixin(MeleeAttackGoal.class)
public abstract class MixinMeleeAttackGoal extends Goal {
	@Shadow @Final protected PathfinderMob mob;

	@Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
	public void onCanUse(CallbackInfoReturnable<Boolean> cir) {
		if (!(this.mob instanceof IEntityWithGene entity)) return;
		if (!entity.bioforge$hasGene(AggressiveGene.class)) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "canContinueToUse", at = @At("HEAD"), cancellable = true)
	public void onCanContinueToUse(CallbackInfoReturnable<Boolean> cir) {
		if (!(this.mob instanceof IEntityWithGene entity)) return;
		if (!entity.bioforge$hasGene(AggressiveGene.class)) {
			cir.setReturnValue(false);
		}
	}
}
