package com.lucasmellof.bioforge.datagen.providers;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.lucasmellof.bioforge.blood.BloodTypeModifier;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 25/10/2025
 */
public abstract class BloodTypeProvider implements DataProvider {
    private final Map<ResourceLocation, BloodTypeModifier> map = Maps.newLinkedHashMap();
    private final PackOutput output;

    protected BloodTypeProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cached) {
        this.generateBloodTypes();

        return CompletableFuture.allOf(this.map.entrySet().stream()
                .map(it -> {
                    var loc = it.getKey();
                    var path = this.output
                            .getOutputFolder()
                            .resolve("data")
                            .resolve(loc.getNamespace())
                            .resolve("blood_type_modifiers")
                            .resolve(loc.getPath() + ".json");

                    JsonElement json = BloodTypeModifier.CODEC
                            .encodeStart(com.mojang.serialization.JsonOps.INSTANCE, it.getValue())
                            .resultOrPartial(err -> {
                                throw new IllegalStateException(
                                        "Failed to serialize blood type modifier " + loc + ": " + err);
                            })
                            .orElse(null);
                    if (json == null) {
                        throw new IllegalStateException("Failed to serialize blood type modifier " + loc);
                    }
                    return DataProvider.saveStable(cached, json, path);
                })
                .toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "BloodType Provider";
    }

    protected abstract void generateBloodTypes();

    public void addBloodType(ResourceLocation loc, BloodTypeModifier modifier) {
        this.map.put(loc, modifier);
    }
}
