package com.lucasmellof.bioforge.commands;

import com.lucasmellof.bioforge.commands.arguments.GeneArguments;
import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 17/10/2025
 */
public class DebugCommand {
    public static final DebugCommand INSTANCE = new DebugCommand();

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("genes")
                .requires(it -> it.hasPermission(2))
                .then(Commands.literal("add_gene")
                        .then(Commands.argument("gene", GeneArguments.gene())
                                .then(Commands.argument("entity", EntityArgument.entity())
                                        .executes(this::onAdd))))
                .then(Commands.literal("remove_gene")
                        .then(Commands.argument("gene", GeneArguments.gene())
                                .then(Commands.argument("entity", EntityArgument.entity())
                                        .executes(this::onRemove))))
                .then(Commands.literal("clear_genes")
                        .then(Commands.argument("entity", EntityArgument.entity())
                                .executes(this::onClear))));
    }

    private int onRemove(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var entity = EntityArgument.getEntity(ctx, "entity");
        IEntityWithGene et = (IEntityWithGene) entity;
        var gene = GeneArguments.getGene(ctx, "gene");
        et.bioforge$removeGene(gene);
        return 1;
    }

    private int onClear(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var entity = EntityArgument.getEntity(ctx, "entity");
        IEntityWithGene et = (IEntityWithGene) entity;
        et.bioforge$clearGenes();
        return 1;
    }

    private int onAdd(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var entity = EntityArgument.getEntity(ctx, "entity");
        IEntityWithGene et = (IEntityWithGene) entity;

        var gene = GeneArguments.getGene(ctx, "gene");

        et.bioforge$addGene(gene.create());

        return 1;
    }
}
