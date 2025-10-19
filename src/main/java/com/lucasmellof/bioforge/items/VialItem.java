package com.lucasmellof.bioforge.items;

import com.lucasmellof.bioforge.client.renderer.VialItemRenderer;
import com.lucasmellof.bioforge.data.BloodData;
import com.lucasmellof.bioforge.registry.ModComponentTypes;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 17/10/2025
 */
public class VialItem extends Item implements GeoItem {

    private static final RawAnimation EMPTY_ANIM = RawAnimation.begin().thenPlay("animation.empty");
    private static final RawAnimation FULL_ANIM = RawAnimation.begin().thenPlay("animation.full");
    private static final RawAnimation FULL_FILL_ANIM = RawAnimation.begin().thenPlay("animation.full_fill");
    private static final RawAnimation HALF_ANIM = RawAnimation.begin().thenPlay("animation.half");
    private static final RawAnimation HALF_FILL_ANIM = RawAnimation.begin().thenPlay("animation.half_fill");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public VialItem() {
        super(new Properties().stacksTo(4));

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    public static void addBlood(Player player, ItemStack stack, BloodData data) {
        if (player.level().isClientSide) return;
        var blood = getBloodData(stack);
        var canMix = canMix(stack);
        VialItem self = (VialItem) stack.getItem();
        if (blood != null && canMix) {
            var mixedBlood = blood.mix(data);
            setBloodData(stack, mixedBlood);
            setMixCount(stack, getMixCount(stack) + 1);
            self.triggerAnim(
                    player, GeoItem.getOrAssignId(stack, (ServerLevel) player.level()), "controller", "animation.full");
        } else if (blood == null) {
            setBloodData(stack, data);
            setMixCount(stack, 1);
            self.triggerAnim(
                    player, GeoItem.getOrAssignId(stack, (ServerLevel) player.level()), "controller", "animation.half");
        }
    }

    public static BloodData getBloodData(ItemStack stack) {
        return stack.get(ModComponentTypes.BLOOD_DATA.get());
    }

    public static void setBloodData(ItemStack stack, BloodData bloodData) {
        stack.set(ModComponentTypes.BLOOD_DATA.get(), bloodData);
    }

    public static boolean hasBlood(ItemStack stack) {
        return getBloodData(stack) != null;
    }

    public static int getMixCount(ItemStack stack) {
        Integer count = stack.get(ModComponentTypes.BLOOD_MIX_COUNT_DATA.get());
        return count != null ? count : 0;
    }

    public static void setMixCount(ItemStack stack, int count) {
        stack.set(ModComponentTypes.BLOOD_MIX_COUNT_DATA.get(), count);
    }

    public static boolean canMix(ItemStack stack) {
        return getMixCount(stack) < BloodData.MAX_MIX_COUNT;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        var mix = getMixCount(stack);
        tooltipComponents.add(Component.literal(mix + "/" + BloodData.MAX_MIX_COUNT + " mixes"));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

        AnimationController<VialItem> dialogueController =
                new AnimationController<>(this, "controller", 0, this::vialController);
        dialogueController.triggerableAnim("full_fill", FULL_FILL_ANIM);
        dialogueController.triggerableAnim("half_fill", HALF_FILL_ANIM);
        dialogueController.triggerableAnim("half", HALF_ANIM);
        dialogueController.triggerableAnim("full", FULL_ANIM);
        dialogueController.triggerableAnim("empty", EMPTY_ANIM);

        controllers.add(dialogueController);
    }

    private <E extends GeoAnimatable> PlayState vialController(AnimationState<E> state) {
        ItemStack stack = state.getData(DataTickets.ITEMSTACK);
        if (stack == null) return PlayState.STOP;

        boolean hasBlood = getBloodData(stack) != null;
        var mixCount = getMixCount(stack);

        //
        if (hasBlood) {
            if (mixCount == 1) {
                state.setAndContinue(HALF_FILL_ANIM);
            } else state.setAndContinue(FULL_FILL_ANIM);
        } else {
            state.setAndContinue(EMPTY_ANIM);
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
            private VialItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null) this.renderer = new VialItemRenderer();

                return this.renderer;
            }
        });
    }
}
