package com.lucasmellof.bioforge.datagen;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.blood.BloodTypeModifier;
import com.lucasmellof.bioforge.datagen.providers.BloodTypeProvider;
import net.minecraft.data.PackOutput;

import static net.minecraft.world.entity.EntityType.*;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 25/10/2025
 */
public class ModBloodType extends BloodTypeProvider {

    protected ModBloodType(String folder, PackOutput output) {
        super(output);
    }

    @Override
    protected void generateBloodTypes() {
        addBloodType(
                Const.of("warm_blood"),
                BloodTypeModifier.of(
						0xB22222,
                        COW,
                        PIG,
                        CHICKEN,
                        BAT,
                        CAMEL,
                        DOLPHIN,
                        DONKEY,
                        HORSE,
                        CAT,
                        WOLF,
                        FOX,
                        MULE,
                        OCELOT,
                        VILLAGER,
                        POLAR_BEAR,
                        PARROT,
                        PANDA,
                        GOAT,
                        LLAMA,
                        RABBIT,
                        SHEEP,
                        PLAYER,
                        TRADER_LLAMA,
                        WANDERING_TRADER,
                        ARMADILLO,
                        MOOSHROOM,

                        // pensar dps
                        PIGLIN,
                        PIGLIN_BRUTE,
                        HOGLIN,
                        PILLAGER,
                        RAVAGER,
                        VINDICATOR));

        addBloodType(
                Const.of("cold_blood"),
                BloodTypeModifier.of(
						0x3CB371, AXOLOTL, COD, FROG, SQUID, SALMON, TADPOLE, TROPICAL_FISH, TURTLE, PUFFERFISH
                        // NAUTILUS

                        ));
        addBloodType(
                Const.of("magic_blood"),
                BloodTypeModifier.of(
						0x9370DB, WITCH, ALLAY, VEX, ENDERMAN, ENDERMITE, WITHER, BREEZE, EVOKER, SHULKER));

        addBloodType(Const.of("lava_blood"), BloodTypeModifier.of(0xFF4500, MAGMA_CUBE, BLAZE));

        addBloodType(Const.of("bug_blood"), BloodTypeModifier.of(0x8B008B, BEE, SPIDER, CAVE_SPIDER, SILVERFISH));

        addBloodType(
                Const.of("undead_blood"),
                BloodTypeModifier.of(
						0x556B2F,
                        ZOMBIE,
                        ZOMBIFIED_PIGLIN,
                        ZOMBIE_VILLAGER,
                        ZOMBIE_HORSE,
                        HUSK,
                        PHANTOM,
                        DROWNED,
                        ZOGLIN));

        addBloodType(Const.of("glow_blood"), BloodTypeModifier.of(0x00CED1, GLOW_SQUID));

        addBloodType(Const.of("snow_blood"), BloodTypeModifier.of(0xADD8E6, SNOW_GOLEM));

        addBloodType(Const.of("slimy_blood"), BloodTypeModifier.of(0x6B8E23, SLIME));
        addBloodType(
                Const.of("metal_blood"),
                BloodTypeModifier.of(
						0xC0C0C0, IRON_GOLEM
                        // ,COPPER_GOLEM
                        ));
        addBloodType(
                Const.of("dragon_blood"),
                BloodTypeModifier.of(
						0x8A2BE2, ENDER_DRAGON
                        // ,COPPER_GOLEM
                        ));

        addBloodType(
                Const.of("ancient_blood"),
                BloodTypeModifier.of(
						0xDAA520, SNIFFER, STRIDER, GHAST, ELDER_GUARDIAN, GUARDIAN, WARDEN

                        // CREEKING,
                        // HAPPY_GHAST

                        ));

        addBloodType(
                Const.of("marrow_blood"),
                BloodTypeModifier.of(0xA9A9A9, SKELETON, SKELETON_HORSE, STRAY, BOGGED, WITHER_SKELETON));

        addBloodType(Const.of("explosive_blood"), BloodTypeModifier.of(0x32CD32, CREEPER));
    }
}
