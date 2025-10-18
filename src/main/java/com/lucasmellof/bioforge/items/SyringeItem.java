package com.lucasmellof.bioforge.items;

import com.lucasmellof.bioforge.client.renderer.SyringeItemRenderer;
import com.lucasmellof.bioforge.data.BloodData;
import com.lucasmellof.bioforge.datagen.ModLang;
import com.lucasmellof.bioforge.entity.IEntityWithGene;
import com.lucasmellof.bioforge.gene.GeneType;
import com.lucasmellof.bioforge.registry.ModComponentTypes;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.constant.DataTickets;
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

    private static final RawAnimation EMPTY_ANIM = RawAnimation.begin().thenPlay("animation.empty");
    private static final RawAnimation FULL_ANIM = RawAnimation.begin().thenPlay("animation.full");
    private static final RawAnimation INJECT_ANIM = RawAnimation.begin().thenPlay("animation.filling");

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
        return UseAnim.BOW;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (remainingUseDuration <= 1) {
            var player = (livingEntity instanceof ServerPlayer p) ? p : null;
            if (player == null) return;

            triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerLevel) player.level()), "controller", "empty");


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
                setBloodData(stack, null);
                target.hurt(level.damageSources().cactus(), 1f); // todo: add custom damage source
                player.getCooldowns().addCooldown(this, 10);
                return;
            }

            var entityWithGene = (IEntityWithGene) target;
            var genes = entityWithGene.bioforge$getGenes();
            //TODO: var bloodData = entityWithGene.bioforge$getBloodData();

            triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerLevel) player.level()), "controller", "animation.full");
            addGenes(stack, genes);
            setBloodData(stack, new BloodData(0xffff5555));
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

    public static void setBloodData(ItemStack stack, BloodData bloodData) {
        stack.set(ModComponentTypes.BLOOD_DATA.get(), bloodData);
    }

    @Nullable
    public static BloodData getBloodData(ItemStack stack) {
        return stack.get(ModComponentTypes.BLOOD_DATA.get());
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
        return !genes.isEmpty() || getBloodData(stack) != null;
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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

        AnimationController<SyringeItem> dialogueController = new AnimationController<>(this, "controller", 0, this::syringeController);
        dialogueController.triggerableAnim("full", FULL_ANIM);
        dialogueController.triggerableAnim("empty", EMPTY_ANIM);
        dialogueController.triggerableAnim("filling", INJECT_ANIM);

        controllers.add(dialogueController);
    }

    private <E extends GeoAnimatable> PlayState syringeController(AnimationState<E> state) {
        ItemStack stack = state.getData(DataTickets.ITEMSTACK);
        if (stack == null)
            return PlayState.STOP;

        // Verifica se há sangue armazenado
        boolean hasBlood = getBloodData(stack) != null;

        // Define qual animação deve rodar
        if (!hasBlood) {
            state.setAndContinue(RawAnimation.begin().thenLoop("animation.empty"));
        } else {
            state.setAndContinue(RawAnimation.begin().thenPlay("animation.full"));
        }

        return PlayState.CONTINUE;
    }

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
