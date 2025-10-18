package com.lucasmellof.bioforge.items;

import com.lucasmellof.bioforge.client.renderer.SyringeItemRenderer;
import com.lucasmellof.bioforge.datagen.ModLang;
import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.gene.GeneType;
import com.lucasmellof.bioforge.registry.ModComponentTypes;
import com.lucasmellof.bioforge.registry.ModGenes;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 17/10/2025
 */
public class SyringeItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SyringeItem() {
        super(new Properties().stacksTo(16));

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 40;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CROSSBOW;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration <= 1) {
            var player = (livingEntity instanceof ServerPlayer p) ? p : null;
            if (player == null) return;

            player.stopUsingItem();

            // target
            //            var hitResult = player.pick(2f, 0f, false);

            var hitResult = ProjectileUtil.getEntityHitResult(
                    player,
                    player.getEyePosition(),
                    player.getEyePosition().add(player.getLookAngle().scale(5)),
                    player.getBoundingBox()
                            .expandTowards(player.getLookAngle().scale(5))
                            .inflate(1),
                    e -> true,
                    5f);
            if (hitResult == null) return;
            player.displayClientMessage(Component.literal("HitResult: " + hitResult.getType()), true);
            if (hitResult.getType() != HitResult.Type.ENTITY) return;
            if (!(hitResult instanceof EntityHitResult entityHitResult
                    && entityHitResult.getEntity() instanceof LivingEntity target)) return;

            if (hasBlood(stack)) {
                inject(stack, target);
                target.hurt(level.damageSources().cactus(), 1f); // todo: add custom damage source
                player.getCooldowns().addCooldown(this, 10);
                return;
            }

            var entityWithGene = (IEntityWithGene) target;
            var genes = entityWithGene.bioforge$getGenes();

            addGenes(stack, genes);
            entityWithGene.bioforge$clearGenes();
            target.hurt(level.damageSources().cactus(), 1f); // todo: add custom damage source
            player.getCooldowns().addCooldown(this, 10);
        }
    }

    public void inject(ItemStack stack, LivingEntity target) {
        IEntityWithGene entity = (IEntityWithGene) target;
        var genes = getGenes(stack);

        for (Holder<GeneType<?>> gene : genes) {
            entity.bioforge$addGene(gene.value().create());
        }

        clearGenes(stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        player.startUsingItem(usedHand);
        return super.use(level, player, usedHand);
    }

    @Override
    public void appendHoverText(
            ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        var genes = getGenes(stack);
        if (genes.isEmpty()) {
            tooltipComponents.add(ModLang.ITEM_NO_GENES.as());
        } else {
            tooltipComponents.add(ModLang.ITEM_GENES_LIST.as());
            for (Holder<GeneType<?>> gene : genes) {
                tooltipComponents.add(
                        Component.literal("- ").append(gene.value().name()));
            }
        }
    }

    public static Set<Holder<GeneType<?>>> getGenes(ItemStack stack) {
        HolderSet<GeneType<?>> t = stack.get(ModComponentTypes.GENE.get());
        if (t == null) {
            return Set.of();
        }

        return t.stream().collect(HashSet::new, Set::add, Set::addAll);
    }

    public static void addGene(ItemStack stack, GeneType<?> gene) {
        var currentGenes = getGenes(stack);

        var newGenes = new HashSet<Holder<GeneType<?>>>();
        if (currentGenes != null) {
            newGenes.addAll(currentGenes.stream().toList());
        }
        newGenes.add(Holder.direct(gene));

        stack.set(ModComponentTypes.GENE.get(), HolderSet.direct(new ArrayList<>(newGenes)));
    }

    public static void addGenes(ItemStack stack, Set<GeneType<?>> genes) {
        var currentGenes = getGenes(stack);

        var newGenes = new HashSet<Holder<GeneType<?>>>();
        if (currentGenes != null) {
            newGenes.addAll(currentGenes.stream().toList());
        }
        for (GeneType<?> gene : genes) {
            newGenes.add(Holder.direct(gene));
        }

        stack.set(ModComponentTypes.GENE.get(), HolderSet.direct(new ArrayList<>(newGenes)));
    }

    public static boolean hasBlood(ItemStack stack) {
        var genes = getGenes(stack);
        return !genes.isEmpty();
    }

    public static void removeGenes(ItemStack stack, Set<Holder<GeneType<?>>> genesToRemove) {
        var currentGenes = getGenes(stack);

        var newGenes = new HashSet<Holder<GeneType<?>>>();
        if (currentGenes != null) {
            newGenes.addAll(currentGenes.stream().toList());
        }
        newGenes.removeAll(genesToRemove);

        stack.set(ModComponentTypes.GENE.get(), HolderSet.direct(new ArrayList<>(newGenes)));
    }

    public static void clearGenes(ItemStack stack) {
        stack.set(ModComponentTypes.GENE.get(), HolderSet.direct(new ArrayList<>()));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private SyringeItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null) this.renderer = new SyringeItemRenderer();

                return this.renderer;
            }
        });
    }
}
