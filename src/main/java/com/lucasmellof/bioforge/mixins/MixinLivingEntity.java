package com.lucasmellof.bioforge.mixins;

import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.gene.IGene;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
@Mixin(LivingEntity.class)
public class MixinLivingEntity implements IEntityWithGene {

    @Unique
    private final List<IGene> bioforge$genes = new ArrayList<>();

    @Override
    public List<? extends IGene> bioforge$getGenes() {
        return this.bioforge$genes;
    }

    @Override
    public boolean bioforge$addGene(IGene gene) {
        if (!gene.canApplyGene(this, gene)) {
            return false;
        }
        this.bioforge$genes.add(gene);
        return false;
    }

    @Override
    public void bioforge$removeGene(IGene gene) {
        if (!gene.removeGene(this)) return;
        this.bioforge$genes.remove(gene);
    }

    @Override
    public boolean bioforge$hasGene(Class<? extends IGene> geneClass) {
        for (IGene gene : this.bioforge$genes) {
			if (gene.getClass().equals(geneClass)) {
				return true;
			}
		}
		return false;
    }

	@Override
	public void bioforge$removeGene(Class<? extends IGene> geneClass) {
		IGene toRemove = null;
		for (IGene gene : this.bioforge$genes) {
			if (gene.getClass().equals(geneClass)) {
				toRemove = gene;
				break;
			}
		}
		if (toRemove != null) {
			this.bioforge$removeGene(toRemove);
		}
	}

	@Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    public void onSave(CompoundTag compound, CallbackInfo ci) {
        ListTag nbt = compound.contains("BioforgeGenes") ? compound.getList("BioforgeGenes", 10) : null;
        if (nbt != null && this.bioforge$genes != null) {
            nbt.add(StringTag.valueOf(this.bioforge$genes.getClass().getName()));
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    public void onRead(CompoundTag compound, CallbackInfo ci) {
        if (!compound.contains("BioforgeGenes")) {
           return;
        }
		ListTag nbt = compound.getList("BioforgeGenes", 8);
		for (int i = 0; i < nbt.size(); i++) {
			try {
				Class<?> geneClass = Class.forName(nbt.getString(i));
				if (IGene.class.isAssignableFrom(geneClass)) {
					IGene gene = (IGene) geneClass.getDeclaredConstructor().newInstance();
					this.bioforge$addGene(gene);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
}
