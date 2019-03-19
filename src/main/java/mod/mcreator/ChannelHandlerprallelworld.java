package mod.mcreator;

import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.INetHandler;
import net.minecraft.client.Minecraft;

import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.FMLCommonHandler;

public class ChannelHandlertestenvironmentmod extends FMLIndexedMessageToMessageCodec<IPackettestenvironmentmod> {

	@Override
	public void encodeInto(ChannelHandlerContext ctx, IPackettestenvironmentmod packet, ByteBuf data) throws Exception {
		packet.writeBytes(data);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data, IPackettestenvironmentmod packet) {
		packet.readBytes(data);
		switch (FMLCommonHandler.instance().getEffectiveSide()) {
			case CLIENT :
				packet.executeClient(Minecraft.getMinecraft().thePlayer);
				break;
			case SERVER :
				INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
				packet.executeServer(((NetHandlerPlayServer) netHandler).playerEntity);
				break;
		}
	}

}
