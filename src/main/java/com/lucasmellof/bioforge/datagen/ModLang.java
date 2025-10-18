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

    public static final Lang ITEM_GROUP_BIOFORGE = Lang.of("itemGroup.bioforge");
    public static final Lang ITEM_SYRINGE = Lang.of("item.bioforge.syringe");
    public static final Lang ITEM_NO_GENES = Lang.of("item.bioforge.syringe.no_genes");
    public static final Lang ITEM_GENES_LIST = Lang.of("item.bioforge.syringe.genes");
    public static final Lang GENE_PASSIVE = Lang.of("gene.bioforge.passive_gene");
    public static final Lang GENE_AGGRESSIVE = Lang.of("gene.bioforge.aggressive_gene");

    @Override
    protected void addTranslations() {
        ITEM_GROUP_BIOFORGE.add(this, "Bioforge Group");
        ITEM_SYRINGE.add(this, "Syringe");
        ITEM_NO_GENES.add(this, "No Genes");
        ITEM_GENES_LIST.add(this, "Genes:");
        GENE_PASSIVE.add(this, "Passive Gene");
        GENE_AGGRESSIVE.add(this, "Aggressive Gene");
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
