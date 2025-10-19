package com.lucasmellof.bioforge.mixins;

import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.gene.Gene;
import com.lucasmellof.bioforge.gene.GeneType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity implements IEntityWithGene {

    @Unique
    private final Set<GeneType<?>> bioforge$genes = new HashSet<>();

    @Override
    public Set<GeneType<?>> bioforge$getGenes() {
        return this.bioforge$genes;
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
                if (Gene.class.isAssignableFrom(geneClass)) {
                    Gene gene = (Gene) geneClass.getDeclaredConstructor().newInstance();
                    this.bioforge$addGene(gene);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public LivingEntity self() {
        return (LivingEntity) (Object) this;
    }

    @Redirect(method = "createLivingAttributes", at = @At(value="INVOKE",target="Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;builder()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;"))
    private static AttributeSupplier.Builder onCreateAttributes() {
        // all entities should have attack damage attribute to avoid crashes
        return AttributeSupplier.builder().add(Attributes.ATTACK_DAMAGE);
    }
}
