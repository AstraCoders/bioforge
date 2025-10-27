//package com.lucasmellof.bioforge.gene.types;
//
//import com.lucasmellof.bioforge.entity.IEntityWithGene;
//import com.lucasmellof.bioforge.gene.Gene;
//import com.lucasmellof.bioforge.gene.GeneAction;
//import com.lucasmellof.bioforge.gene.GeneInfo;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.effect.MobEffects;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.alchemy.Potions;
//
//import java.util.List;
//
///*
// * @author Lucasmellof, Lucas de Mello Freitas created on 26/10/2025
// */
//public class NightVisionGene implements GeneAction {
//	public static final GeneInfo INFO = GeneInfo.builder()
//												.build();
//
//	@Override
//	public boolean canApply(IEntityWithGene entity) {
//		return true;
//	}
//
//	@Override
//	public boolean apply(IEntityWithGene entity) {
//		entity.self().addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1200, 0, false, false));
//		return true;
//	}
//
//	@Override
//	public void remove(IEntityWithGene entity) {
////		entity.self().removeEffect(MobEffects.NIGHT_VISION);
//	}
//
//	@Override
//	public void tick(LivingEntity entity, Gene gene) {
//		if (entity.tickCount % 120 == 0) {
//			entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1200, 0, false, false));
//		}
//	}
//
//}
