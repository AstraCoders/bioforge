package com.lucasmellof.bioforge.datagen;

import com.lucasmellof.bioforge.Const;
import lombok.AllArgsConstructor;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.data.LanguageProvider;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class ModLang extends LanguageProvider {
    public ModLang(PackOutput output) {
        super(output, Const.MOD_ID, "en_us");
    }

    public static final Lang ITEM_GROUP_BIOFORGE = Lang.of("itemGroup.biogene");
    public static final Lang ITEM_SYRINGE = Lang.of("item.biogene.syringe");
    public static final Lang ITEM_NO_GENES = Lang.of("item.biogene.syringe.no_genes");
    public static final Lang ITEM_GENES_LIST = Lang.of("item.biogene.syringe.genes");
    public static final Lang ITEM_VIAL = Lang.of("item.biogene.vial");


    public static final Lang ITEM_MICROSCOPE = Lang.of("block.biogene.microscope");
    public static final Lang ITEM_VIAL_HOLDER = Lang.of("block.biogene.vial");
    public static final Lang BLOCK_CENTRIFUGE = Lang.of("block.biogene.centrifuge");
    public static final Lang BLOCK_VIAL_HOLDER = Lang.of("block.biogene.vial_holder");

    public static final Lang NOT_MIXED_VIAL = Lang.of("item.biogene.vial.not_mixed");
    public static final Lang MISC_SAMPLE = Lang.of("misc.biogene.sample");
    public static final Lang MISC_NO_GENES = Lang.of("misc.biogene.no_genes");
    public static final Lang MISC_NO_GENES_DISCOVERED = Lang.of("misc.biogene.no_genes_discovered");

    public static final Lang UI_START_BUTTON = Lang.of("ui.biogene.start_button");

    public static final Lang GENE_BREEDABLE = Lang.of("gene.biogene.breedable_gene");
    public static final Lang GENE_AGGRESSIVE = Lang.of("gene.biogene.aggressive_gene");
    public static final Lang GENE_FIRE_RESISTANCE = Lang.of("gene.biogene.fire_resistant_gene");
    public static final Lang GENE_SELF_REGEN = Lang.of("gene.biogene.self_regen_gene");

    @Override
    protected void addTranslations() {
        ITEM_GROUP_BIOFORGE.add(this, "BioGene Group");
        ITEM_SYRINGE.add(this, "Syringe");
        ITEM_NO_GENES.add(this, "No Genes");
        ITEM_GENES_LIST.add(this, "Genes:");
        ITEM_VIAL.add(this, "Vial");


        ITEM_MICROSCOPE.add(this, "Microscope");
        ITEM_VIAL_HOLDER.add(this, "Vial Holder");
        BLOCK_CENTRIFUGE.add(this, "Centrifuge");
        BLOCK_VIAL_HOLDER.add(this, "Vial Holder");

        NOT_MIXED_VIAL.add(this, "Not Mixed");
        MISC_SAMPLE.add(this, "Sample");
        MISC_NO_GENES.add(this, "No Genes");
        MISC_NO_GENES_DISCOVERED.add(this, "No Genes Discovered");

        UI_START_BUTTON.add(this, "Start");

        GENE_BREEDABLE.add(this, "Breedable");
        GENE_AGGRESSIVE.add(this, "Aggressive");
        GENE_FIRE_RESISTANCE.add(this, "Fire Resistance");
        GENE_SELF_REGEN.add(this, "Self Regeneration");
    }

    @AllArgsConstructor(staticName = "of")
    public static class Lang {
        private String key;

        public Component as(String... args) {
            return Component.translatable(key, (Object[]) args);
        }

        public void add(LanguageProvider provider, String value) {
            provider.add(key, value);
        }
    }
}
