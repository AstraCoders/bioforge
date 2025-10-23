package com.lucasmellof.bioforge.network;

import com.lucasmellof.bioforge.Const;
import com.lucasmellof.bioforge.block.entity.CentrifugeBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 23/10/2025
 */
public record C2SStartCentrifugePacket(BlockPos blockPos) implements CustomPacketPayload {
	public static final Type<C2SStartCentrifugePacket> TYPE = new Type<>(Const.of("start_centrifuge_c2s"));

	public static final StreamCodec<ByteBuf, C2SStartCentrifugePacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			C2SStartCentrifugePacket::blockPos,
			C2SStartCentrifugePacket::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			BlockEntity block = ctx.player().level().getBlockEntity(blockPos);
			if (block instanceof CentrifugeBlockEntity centrifuge) {
				centrifuge.startCentrifuge(blockPos, ctx.player());
			}

		});
	}
}
