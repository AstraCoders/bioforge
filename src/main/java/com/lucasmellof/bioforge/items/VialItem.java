package com.lucasmellof.bioforge.items;

import com.lucasmellof.bioforge.blood.BloodData;
import com.lucasmellof.bioforge.client.renderer.VialItemRenderer;
import com.lucasmellof.bioforge.datagen.ModLang;
import com.lucasmellof.bioforge.gene.Gene;
import com.lucasmellof.bioforge.registry.ModComponentTypes;
import com.lucasmellof.bioforge.utils.RomanNumeralUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    public static boolean addBlood(Player player, ItemStack stack, BloodData data) {
        if (player.level().isClientSide) return false;
        var blood = getBloodData(stack);
        VialItem self = (VialItem) stack.getItem();
        if (blood != null && blood.size() >= BloodData.MAX_MIX_COUNT) {
            return false;
        }
        addBloodData(stack, data);
        self.triggerAnim(
                player, GeoItem.getOrAssignId(stack, (ServerLevel) player.level()), "controller", "animation.full");
        return true;
    }

    public static List<BloodData> getBloodData(ItemStack stack) {
        return stack.get(ModComponentTypes.BLOOD_DATA.get());
    }

    public static void setBloodData(ItemStack stack, List<BloodData> bloodData) {
        stack.set(ModComponentTypes.BLOOD_DATA.get(), bloodData);
    }

    public static void addBloodData(ItemStack stack, BloodData bloodData) {
        var data = getBloodData(stack);
        if (data == null) {
            data = new ArrayList<>();
        } else {
            data = new ArrayList<>(data);
        }
        if (data.size() < BloodData.MAX_MIX_COUNT) {
            data.add(bloodData);
        }
        stack.set(ModComponentTypes.BLOOD_DATA.get(), data);
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

    public static boolean isFull(ItemStack stack) {
        var data = getBloodData(stack);
        if (data == null) return false;

        return data.size() >= BloodData.MAX_MIX_COUNT;
    }

    ResourceLocation font = ResourceLocation.withDefaultNamespace("alt");
    private static final String[] randomWords = new String[]{
            "gene",
            "locus",
            "allele",
            "genome",
            "chromosome",
            "nucleotide",
            "mutation",
            "phenotype",
            "genotype",
            "heredity"
    };

    @Override
    public Component getName(ItemStack stack) {
        var data = getBloodData(stack);
        if (data == null || data.isEmpty()) {
            return super.getName(stack);
        }
        if (data.size() == 1) {
            return super.getName(stack);
        }
        return ((MutableComponent) super.getName(stack)).append(Component.literal(" (").append(ModLang.NOT_MIXED_VIAL.as()).append(")").withStyle(ChatFormatting.RED));
    }

    private static final Random random = new Random();
    private final int randomIndex = random.nextInt(randomWords.length);

    @Override
    public void appendHoverText(
            ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        var blood = getBloodData(stack);
        if (blood == null) return;
        int index = randomIndex;
        boolean noGeneMsg = false;
        int bloodIndex = 1;
        for (BloodData data : blood) {

            if (blood.size() > 1 && index != 0) {
                tooltipComponents.add(Component.empty());
            }
            MutableComponent sample = Component.literal("§7").append(ModLang.MISC_SAMPLE.as());
            if (blood.size() > 1) {
                tooltipComponents.add(sample.append(Component.literal(" " + bloodIndex++)));
            } else {
                tooltipComponents.add(sample);
            }

            if (data.genes().isEmpty()) {
                if (!noGeneMsg) {
                    index++;
                    if (!data.discovered()) {
                        tooltipComponents.add(Component.literal("- ").append(Component.empty().append(ModLang.MISC_NO_GENES_DISCOVERED.as()).withStyle(Style.EMPTY.withFont(font))).withColor(ChatFormatting.GRAY.getColor()));
                        noGeneMsg = true;
                        continue;
                    }
                    tooltipComponents.add(Component.literal("- ").append(ModLang.ITEM_NO_GENES.as()).withStyle(ChatFormatting.GRAY));
                }
                continue;
            }

            for (Gene gene : data.genes()) {
                if (!data.discovered()) {
                    index = (index + 1) % randomWords.length;
                    tooltipComponents.add(Component.literal("§7- ").append(Component.literal( randomWords[index]).withStyle(Style.EMPTY.withFont(font).withColor(ChatFormatting.GRAY))));
                    continue;
                }
                Style style = Style.EMPTY;
                //if (gene.isHarmful()) {
                //    style = style.withColor(0xFF0000); // Adicionar algo para ser agressivo
                //} else {
                style = style.withColor(ChatFormatting.GRAY);
                //}
                tooltipComponents.add(Component.literal("- ").append(gene.getType().name()).append(Component.literal(" " + RomanNumeralUtil.toRoman(gene.getLevel()))).setStyle(style));
            }
        }
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
