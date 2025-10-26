package com.lucasmellof.bioforge.network;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.block.entity.CentrifugeBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 23/10/2025
 */
public record S2CStopCentrifugePacket(BlockPos blockPos) implements CustomPacketPayload {
	public static final Type<S2CStopCentrifugePacket> TYPE = new Type<>(Const.of("stop_centrifuge_s2c"));

	public static final StreamCodec<ByteBuf, S2CStopCentrifugePacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			S2CStopCentrifugePacket::blockPos,
			S2CStopCentrifugePacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	@OnlyIn(Dist.CLIENT)
	public void handle(IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			BlockEntity blockEntity = Minecraft.getInstance().player.level().getBlockEntity(blockPos);
			if (blockEntity instanceof CentrifugeBlockEntity centrifuge) {
				centrifuge.stopCentrifuge(blockPos, ctx.player());
			}

		});
	}
}
