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
    public static final Lang GENE_PASSIVE = Lang.of("gene.biogene.passive_gene");
    public static final Lang GENE_AGGRESSIVE = Lang.of("gene.biogene.aggressive_gene");

    public static final Lang ITEM_MICROSCOPE = Lang.of("block.biogene.microscope");
    public static final Lang ITEM_VIAL_HOLDER = Lang.of("block.biogene.vial");
    public static final Lang BLOCK_CENTRIFUGE = Lang.of("block.biogene.centrifuge");
    public static final Lang BLOCK_VIAL_HOLDER = Lang.of("block.biogene.vial_holder");

    @Override
    protected void addTranslations() {
        ITEM_GROUP_BIOFORGE.add(this, "BioGene Group");
        ITEM_SYRINGE.add(this, "Syringe");
        ITEM_NO_GENES.add(this, "No Genes");
        ITEM_GENES_LIST.add(this, "Genes:");
        GENE_PASSIVE.add(this, "Passive Gene");
        GENE_AGGRESSIVE.add(this, "Aggressive Gene");

        ITEM_MICROSCOPE.add(this, "Microscope");
        ITEM_VIAL_HOLDER.add(this, "Vial Holder");
        BLOCK_CENTRIFUGE.add(this, "Centrifuge");
        BLOCK_VIAL_HOLDER.add(this, "Vial Holder");
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
