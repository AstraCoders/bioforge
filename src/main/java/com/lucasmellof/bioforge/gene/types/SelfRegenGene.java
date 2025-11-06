package com.lucasmellof.bioforge.gene.types;

import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.gene.Gene;
import com.lucasmellof.bioforge.gene.GeneAction;
import com.lucasmellof.bioforge.gene.GeneInfo;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 26/10/2025
 */
public class SelfRegenGene implements GeneAction {
    public static final GeneInfo INFO = GeneInfo.builder()
            .build();

    @Override
    public boolean canApply(IEntityWithGene entity) {
        return false;
    }

    @Override
    public boolean apply(IEntityWithGene entity) {
        return false;
    }

    @Override
    public void remove(IEntityWithGene entity) {
        entity.self().removeEffect(MobEffects.REGENERATION);
    }

    @Override
    public void tick(LivingEntity entity, Gene gene) {
        if (entity.tickCount % 20 != 0) {
            return;
        }
        if (!(entity.getRandom().nextFloat() < 0.05F) || !(entity.getHealth() < entity.getMaxHealth())) {
            return;
        }
        if (!entity.isSilent()) {
            entity.level()
                    .playSound(
                            null,
                            entity.getX(),
                            entity.getY(),
                            entity.getZ(),
                            SoundEvents.WITCH_DRINK,
                            entity.getSoundSource(),
                            1.0F,
                            0.8F + entity.getRandom().nextFloat() * 0.4F);
        }
        MobEffectInstance instance = new MobEffectInstance(MobEffects.REGENERATION, 900);
        entity.addEffect(instance);
        entity.heal(1.0F);
    }
}
