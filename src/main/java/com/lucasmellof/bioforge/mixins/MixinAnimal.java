package com.lucasmellof.bioforge.mixins;

import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.registry.ModGenes;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 21/10/2025
 */
@Mixin(Animal.class)
public class MixinAnimal {
	@ModifyArg(method = "finalizeSpawnChildFromBreeding", at = @At(value="INVOKE",target="Lnet/minecraft/world/entity/animal/Animal;setAge(I)V", ordinal = 0))
	public int bioforge$setAge(int age) {
		IEntityWithGene entity = (IEntityWithGene) this;
		if (entity.bioforge$hasGene(ModGenes.BREEDABLE_GENE.get())) {
			var gene = entity.bioforge$getGene(ModGenes.BREEDABLE_GENE.get());
			var level = gene.getLevel();

			age = (int) (age * Math.pow(0.9, level));
		}
		return age;
	}
}
