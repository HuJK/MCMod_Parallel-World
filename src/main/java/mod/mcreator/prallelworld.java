package mod.mcreator;

import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.IFuelHandler;

@Mod(modid = testenvironmentmod.MODID, version = testenvironmentmod.VERSION)
public class testenvironmentmod implements IFuelHandler, IWorldGenerator {

	public static final String MODID = "testenvironmentmod";
	public static final String VERSION = "1.0.4";

	@SidedProxy(clientSide = "mod.mcreator.ClientProxytestenvironmentmod", serverSide = "mod.mcreator.CommonProxytestenvironmentmod")
	// @SidedProxy(clientSide="mod.mcreator.ClientProxytestenvironmentmod",
	// serverSide="mod.mcreator.CommonProxytestenvironmentmod")
	public static CommonProxytestenvironmentmod proxy;
	// public static EnumMap<Side, FMLEmbeddedChannel> channels =
	// NetworkRegistry.INSTANCE.newChannel("MCRBUS", new
	// ChannelHandlertestenvironmentmod());

	@Instance(MODID)
	public static testenvironmentmod instance;

	mcreator_anotherWorld mcreator_0 = new mcreator_anotherWorld();
	mcreator_anotherNether mcreator_1 = new mcreator_anotherNether();

	@Override
	public int getBurnTime(ItemStack fuel) {
		if (mcreator_0.addFuel(fuel) != 0)
			return mcreator_0.addFuel(fuel);
		if (mcreator_1.addFuel(fuel) != 0)
			return mcreator_1.addFuel(fuel);
		return 0;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

		chunkX = chunkX * 16;
		chunkZ = chunkZ * 16;
		if (world.provider.dimensionId == -1)
			mcreator_0.generateNether(world, random, chunkX, chunkZ);
		if (world.provider.dimensionId == 0)
			mcreator_0.generateSurface(world, random, chunkX, chunkZ);
		if (world.provider.dimensionId == -1)
			mcreator_1.generateNether(world, random, chunkX, chunkZ);
		if (world.provider.dimensionId == 0)
			mcreator_1.generateSurface(world, random, chunkX, chunkZ);

	}

	@EventHandler
	public void load(FMLInitializationEvent event) {

		GameRegistry.registerFuelHandler(this);
		GameRegistry.registerWorldGenerator(this, 1);
		MinecraftForge.EVENT_BUS.register(new mcreator_GlobalEventstestenvironmentmod());
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		mcreator_0.load();
		mcreator_1.load();

	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		mcreator_0.serverLoad(event);
		mcreator_1.serverLoad(event);
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		mcreator_0.instance = this.instance;
		mcreator_1.instance = this.instance;
		mcreator_0.preInit(event);
		mcreator_1.preInit(event);
		proxy.registerRenderers(this);
	}

	public static class GuiHandler implements IGuiHandler {
		@Override
		public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
			return null;
		}

		@Override
		public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
			return null;
		}
	}

}
