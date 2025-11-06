package com.lucasmellof.bioforge.gene.types;

import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.gene.GeneAction;
import com.lucasmellof.bioforge.gene.GeneInfo;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Animal;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 16/10/2025
 */
public class BreedableGene implements GeneAction {

    public static final GeneInfo INFO = GeneInfo.builder().build();

    @Override
    public boolean canApply(IEntityWithGene entity) {
        return false;
    }

    @Override
    public boolean apply(IEntityWithGene entity) {
        if (!(entity.self() instanceof Animal mob)) {
            return false;
        }
        boolean hasGoal = false;
        for (WrappedGoal goal : mob.targetSelector.getAvailableGoals()) {
            if (goal.getGoal() instanceof BreedGoal) {
                hasGoal = true;
                break;
            }
        }
        if (!hasGoal) {
            mob.goalSelector.addGoal(1, new BreedGoal(mob, 1.0));
        }

        return true;
    }

    @Override
    public void remove(IEntityWithGene entity) {
    }
}
