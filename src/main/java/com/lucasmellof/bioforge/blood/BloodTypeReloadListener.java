package com.lucasmellof.bioforge.blood;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lucasmellof.bioforge.Const;
import com.mojang.serialization.JsonOps;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 25/10/2025
 */
@Slf4j
public class BloodTypeReloadListener extends SimpleJsonResourceReloadListener {
    public static final ResourceLocation ID = Const.of("blood_type_modifiers");

    public BloodTypeReloadListener(Gson gson) {
        super(gson, "blood_type_modifiers");
    }

    @Override
    protected void apply(
            Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        BloodTypeRegistry.clear();

        for (var entry : map.entrySet()) {
            try {
                var modifier = BloodTypeModifier.CODEC
                        .parse(JsonOps.INSTANCE, entry.getValue())
                        .resultOrPartial(err -> {
                            log.error("Error parsing blood type modifier {}: {}", entry.getKey(), err);
                        })
                        .orElse(null);

				if (modifier == null) return;

				for (var id : modifier.getEntities()) {
					var type = BuiltInRegistries.ENTITY_TYPE.get(id);
					if (type == null) {
						log.error("Error parsing id for modifier {}: {}", id, entry.getKey());
                        continue;
					}

					BloodTypeRegistry.register(type, modifier);
				}

            } catch (Exception e) {
				log.error("Error parsing blood type modifier {}: {}", entry.getKey(), e.getMessage());
            }
        }
    }
}
