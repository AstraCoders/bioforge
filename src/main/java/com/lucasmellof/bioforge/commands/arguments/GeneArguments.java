package com.lucasmellof.bioforge.commands.arguments;

import com.lucasmellof.bioforge.gene.GeneType;
import com.lucasmellof.bioforge.registry.ModGenes;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 18/10/2025
 */
public class GeneArguments implements ArgumentType<GeneType<?>> {

	private static final Lazy<List<String>> EXAMPLES = Lazy.of(() -> Stream.of(ModGenes.AGGRESSIVE_GENE.get())
			.map(geneType -> geneType.location().toString())
			.collect(Collectors.toList()));

	public static GeneArguments gene() {
		return new GeneArguments();
	}

	public static GeneType<?> getGene(CommandContext<?> context, String name) {
		return context.getArgument(name, GeneType.class);
	}

	@Override
	public GeneType<?> parse(StringReader reader) throws CommandSyntaxException {
		var content = ResourceLocation.read(reader);
		return ModGenes.GENE_REGISTRY.get(content);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return context.getSource() instanceof SharedSuggestionProvider
					   ? SharedSuggestionProvider.suggestResource(ModGenes.GENE_REGISTRY.keySet(), builder)
					   : Suggestions.empty();
	}

	@Override
	public Collection<String> getExamples() {
		return EXAMPLES.get();
	}
}
