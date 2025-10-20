package com.lucasmellof.bioforge.gene.types;

import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.gene.GeneAction;
import com.lucasmellof.bioforge.gene.GeneInfo;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
public class AggressiveGene implements GeneAction {

    public static final GeneInfo INFO = GeneInfo.builder().build();

    @Override
    public boolean canApply(IEntityWithGene entity) {
        return false;
    }

    @Override
    public boolean apply(IEntityWithGene entity) {
        if (!(entity.self() instanceof PathfinderMob mob)) {
            return false;
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

        return true;
    }

    @Override
    public void remove(IEntityWithGene entity) {}
}
