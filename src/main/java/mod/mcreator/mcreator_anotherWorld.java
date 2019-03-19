package mod.mcreator;

import mod.ConfigHandler.ConfigHandler;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.DimensionManager;

import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.WorldType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.World;
import net.minecraft.world.Teleporter;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.village.Village;
import net.minecraft.util.Vec3;
import net.minecraft.util.ReportedException;
import net.minecraft.util.MathHelper;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IIcon;
import net.minecraft.util.Direction;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.Entity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.Block;

import java.util.Random;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.WorldProviderSurface;

@SuppressWarnings("unchecked")
public class mcreator_anotherWorld {

	public Object instance;
	public static int DIMID      = mcreator_VarListtestenvironmentmod.AWorldDimID;
     public static int SeedOffset = mcreator_VarListtestenvironmentmod.AWorldSeedOffset;



	public static BlockTutorialPortal portal;
	public static ModTrigger block;

	static {

		portal = (BlockTutorialPortal) (new BlockTutorialPortal().setBlockName("anotherWorld_portal").setBlockTextureName("glass_lime"));
		Block.blockRegistry.addObject(182, "anotherWorld_portal", portal);
		block = (ModTrigger) (new ModTrigger().setUnlocalizedName("anotherWorld_trigger").setTextureName("flint_and_steel"));
		Item.itemRegistry.addObject(429, "anotherWorld_trigger", block);
	}

	public mcreator_anotherWorld() {
     
	}

	public void load() {
          DIMID      = ConfigHandler.getInt("dim_id", "World");
          SeedOffset = ConfigHandler.getInt("seed_offset", "World");
          if (DIMID==0){
               DIMID = mcreator_VarListtestenvironmentmod.AWorldDimID;
          }
		GameRegistry.registerBlock(portal, "anotherWorld_portal");
		DimensionManager.registerProviderType(DIMID, mcreator_anotherWorld.WorldProviderMod.class, false);
		DimensionManager.registerDimension(DIMID, DIMID);
          ConfigHandler.writeConfig("dim_id", "World", DIMID);
          ConfigHandler.writeConfig("seed_offset", "World", SeedOffset);

		GameRegistry.addRecipe(new ItemStack(block, 1), new Object[]{"XXX", "X45", "X78", Character.valueOf('4'),
				new ItemStack(Items.flint_and_steel, 1), Character.valueOf('5'), new ItemStack(Blocks.dirt, 1), Character.valueOf('7'),
				new ItemStack(Blocks.dirt, 1), Character.valueOf('8'), new ItemStack(Blocks.dirt, 1),});

	}

	public void registerRenderers() {
	}

	public void generateNether(World world, Random random, int chunkX, int chunkZ) {
	}

	public void generateSurface(World world, Random random, int chunkX, int chunkZ) {
	}

	public int addFuel(ItemStack fuel) {
		return 0;
	}

	public void serverLoad(FMLServerStartingEvent event) {
	}

	public void preInit(FMLPreInitializationEvent event) {
	}

	public static class WorldProviderMod extends WorldProviderSurface {
          @Override
		public void registerWorldChunkManager() {
			this.worldChunkMgr = new WorldChunkManager(this.worldObj);
			this.isHellWorld = false;
			this.hasNoSky = false;
			this.dimensionId = DIMID;
		}

		@SideOnly(Side.CLIENT)
		public Vec3 getFogColor(float par1, float par2) {
			return Vec3.createVectorHelper(0.7137254901960784D, 0.8196078431372549D, 0.996078431372549D);
		}

		public IChunkProvider createChunkGenerator() {
			return new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed()+SeedOffset,true);
		}

		public boolean isSurfaceWorld() {
			return true;
		}

		public boolean canCoordinateBeSpawn(int par1, int par2) {
			return false;
		}

		public boolean canRespawnHere() {
			return true;
		}


		public String getDimensionName() {
			return "anotherWorld";
		}

	}

	public class TutorialPortalPosition extends ChunkCoordinates {
		public long field_85087_d;
		final TeleporterDimensionMod field_85088_e;

		public TutorialPortalPosition(TeleporterDimensionMod tutorialTeleporter, int par2, int par3, int par4, long par5) {
			super(par2, par3, par4);
			this.field_85088_e = tutorialTeleporter;
			this.field_85087_d = par5;
		}
	}

	public static class TeleporterDimensionMod extends Teleporter {

		private final WorldServer worldServerInstance;
		/**
		 * A private Random() function in Teleporter
		 */
		private final Random random;
		/**
		 * Stores successful portal placement locations for rapid lookup.
		 */
		private final LongHashMap destinationCoordinateCache = new LongHashMap();
		/**
		 * A list of valid keys for the destinationCoordainteCache. These are
		 * based on the X & Z of the players initial location.
		 */
		private final List destinationCoordinateKeys = new ArrayList();
		private static final String __OBFID = "CL_00000153";

		public TeleporterDimensionMod(WorldServer par1WorldServer) {
			super(par1WorldServer);
			this.worldServerInstance = par1WorldServer;
			this.random = new Random(par1WorldServer.getSeed());
		}

		/**
		 * Place an entity in a nearby portal, creating one if necessary.
		 */
		public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
			if (this.worldServerInstance.provider.dimensionId != 1) {
				if (!this.placeInExistingPortal(par1Entity, par2, par4, par6, par8)) {
					this.makePortal(par1Entity);
					this.placeInExistingPortal(par1Entity, par2, par4, par6, par8);
				}
			} else {
				int i = MathHelper.floor_double(par1Entity.posX);
				int j = MathHelper.floor_double(par1Entity.posY) - 1;
				int k = MathHelper.floor_double(par1Entity.posZ);
				byte b0 = 1;
				byte b1 = 0;

				for (int l = -2; l <= 2; ++l) {
					for (int i1 = -2; i1 <= 2; ++i1) {
						for (int j1 = -1; j1 < 3; ++j1) {
							int k1 = i + i1 * b0 + l * b1;
							int l1 = j + j1;
							int i2 = k + i1 * b1 - l * b0;
							boolean flag = j1 < 0;
							this.worldServerInstance.setBlock(k1, l1, i2, flag ? Blocks.hay_block : Blocks.air);
						}
					}
				}

				par1Entity.setLocationAndAngles((double) i, (double) j, (double) k, par1Entity.rotationYaw, 0.0F);
				par1Entity.motionX = par1Entity.motionY = par1Entity.motionZ = 0.0D;
			}
		}

		/**
		 * Place an entity in a nearby portal which already exists.
		 */
		public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
			short short1 = 128;
			double d3 = -1.0D;
			int i = 0;
			int j = 0;
			int k = 0;
			int l = MathHelper.floor_double(par1Entity.posX);
			int i1 = MathHelper.floor_double(par1Entity.posZ);
			long j1 = ChunkCoordIntPair.chunkXZ2Int(l, i1);
			boolean flag = true;
			double d7;
			int l3;

			if (this.destinationCoordinateCache.containsItem(j1)) {
				Teleporter.PortalPosition portalposition = (Teleporter.PortalPosition) this.destinationCoordinateCache.getValueByKey(j1);
				d3 = 0.0D;
				i = portalposition.posX;
				j = portalposition.posY;
				k = portalposition.posZ;
				portalposition.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
				flag = false;
			} else {
				for (l3 = l - short1; l3 <= l + short1; ++l3) {
					double d4 = (double) l3 + 0.5D - par1Entity.posX;

					for (int l1 = i1 - short1; l1 <= i1 + short1; ++l1) {
						double d5 = (double) l1 + 0.5D - par1Entity.posZ;

						for (int i2 = this.worldServerInstance.getActualHeight() - 1; i2 >= 0; --i2) {
							if (this.worldServerInstance.getBlock(l3, i2, l1) == portal) {
								while (this.worldServerInstance.getBlock(l3, i2 - 1, l1) == portal) {
									--i2;
								}

								d7 = (double) i2 + 0.5D - par1Entity.posY;
								double d8 = d4 * d4 + d7 * d7 + d5 * d5;

								if (d3 < 0.0D || d8 < d3) {
									d3 = d8;
									i = l3;
									j = i2;
									k = l1;
								}
							}
						}
					}
				}
			}

			if (d3 >= 0.0D) {
				if (flag) {
					this.destinationCoordinateCache.add(j1, new Teleporter.PortalPosition(i, j, k, this.worldServerInstance.getTotalWorldTime()));
					this.destinationCoordinateKeys.add(Long.valueOf(j1));
				}

				double d11 = (double) i + 0.5D;
				double d6 = (double) j + 0.5D;
				d7 = (double) k + 0.5D;
				int i4 = -1;

				if (this.worldServerInstance.getBlock(i - 1, j, k) == portal) {
					i4 = 2;
				}

				if (this.worldServerInstance.getBlock(i + 1, j, k) == portal) {
					i4 = 0;
				}

				if (this.worldServerInstance.getBlock(i, j, k - 1) == portal) {
					i4 = 3;
				}

				if (this.worldServerInstance.getBlock(i, j, k + 1) == portal) {
					i4 = 1;
				}

				int j2 = par1Entity.getTeleportDirection();

				if (i4 > -1) {
					int k2 = Direction.rotateLeft[i4];
					int l2 = Direction.offsetX[i4];
					int i3 = Direction.offsetZ[i4];
					int j3 = Direction.offsetX[k2];
					int k3 = Direction.offsetZ[k2];
					boolean flag1 = !this.worldServerInstance.isAirBlock(i + l2 + j3, j, k + i3 + k3)
							|| !this.worldServerInstance.isAirBlock(i + l2 + j3, j + 1, k + i3 + k3);
					boolean flag2 = !this.worldServerInstance.isAirBlock(i + l2, j, k + i3)
							|| !this.worldServerInstance.isAirBlock(i + l2, j + 1, k + i3);

					if (flag1 && flag2) {
						i4 = Direction.rotateOpposite[i4];
						k2 = Direction.rotateOpposite[k2];
						l2 = Direction.offsetX[i4];
						i3 = Direction.offsetZ[i4];
						j3 = Direction.offsetX[k2];
						k3 = Direction.offsetZ[k2];
						l3 = i - j3;
						d11 -= (double) j3;
						int k1 = k - k3;
						d7 -= (double) k3;
						flag1 = !this.worldServerInstance.isAirBlock(l3 + l2 + j3, j, k1 + i3 + k3)
								|| !this.worldServerInstance.isAirBlock(l3 + l2 + j3, j + 1, k1 + i3 + k3);
						flag2 = !this.worldServerInstance.isAirBlock(l3 + l2, j, k1 + i3)
								|| !this.worldServerInstance.isAirBlock(l3 + l2, j + 1, k1 + i3);
					}

					float f1 = 0.5F;
					float f2 = 0.5F;

					if (!flag1 && flag2) {
						f1 = 1.0F;
					} else if (flag1 && !flag2) {
						f1 = 0.0F;
					} else if (flag1 && flag2) {
						f2 = 0.0F;
					}

					d11 += (double) ((float) j3 * f1 + f2 * (float) l2);
					d7 += (double) ((float) k3 * f1 + f2 * (float) i3);
					float f3 = 0.0F;
					float f4 = 0.0F;
					float f5 = 0.0F;
					float f6 = 0.0F;

					if (i4 == j2) {
						f3 = 1.0F;
						f4 = 1.0F;
					} else if (i4 == Direction.rotateOpposite[j2]) {
						f3 = -1.0F;
						f4 = -1.0F;
					} else if (i4 == Direction.rotateRight[j2]) {
						f5 = 1.0F;
						f6 = -1.0F;
					} else {
						f5 = -1.0F;
						f6 = 1.0F;
					}

					double d9 = par1Entity.motionX;
					double d10 = par1Entity.motionZ;
					par1Entity.motionX = d9 * (double) f3 + d10 * (double) f6;
					par1Entity.motionZ = d9 * (double) f5 + d10 * (double) f4;
					par1Entity.rotationYaw = par8 - (float) (j2 * 90) + (float) (i4 * 90);
				} else {
					par1Entity.motionX = par1Entity.motionY = par1Entity.motionZ = 0.0D;
				}

				par1Entity.setLocationAndAngles(d11, d6, d7, par1Entity.rotationYaw, par1Entity.rotationPitch);
				return true;
			} else {
				return false;
			}
		}

		public boolean makePortal(Entity par1Entity) {
			byte b0 = 16;
			double d0 = -1.0D;
			int i = MathHelper.floor_double(par1Entity.posX);
			int j = MathHelper.floor_double(par1Entity.posY);
			int k = MathHelper.floor_double(par1Entity.posZ);
			int l = i;
			int i1 = j;
			int j1 = k;
			int k1 = 0;
			int l1 = this.random.nextInt(4);
			int i2;
			double d1;
			double d2;
			int k2;
			int i3;
			int k3;
			int j3;
			int i4;
			int l3;
			int k4;
			int j4;
			int i5;
			int l4;
			double d3;
			double d4;

			for (i2 = i - b0; i2 <= i + b0; ++i2) {
				d1 = (double) i2 + 0.5D - par1Entity.posX;

				for (k2 = k - b0; k2 <= k + b0; ++k2) {
					d2 = (double) k2 + 0.5D - par1Entity.posZ;
					label274 :

					for (i3 = this.worldServerInstance.getActualHeight() - 1; i3 >= 0; --i3) {
						if (this.worldServerInstance.isAirBlock(i2, i3, k2)) {
							while (i3 > 0 && this.worldServerInstance.isAirBlock(i2, i3 - 1, k2)) {
								--i3;
							}

							for (j3 = l1; j3 < l1 + 4; ++j3) {
								k3 = j3 % 2;
								l3 = 1 - k3;

								if (j3 % 4 >= 2) {
									k3 = -k3;
									l3 = -l3;
								}

								for (i4 = 0; i4 < 3; ++i4) {
									for (j4 = 0; j4 < 4; ++j4) {
										for (k4 = -1; k4 < 4; ++k4) {
											l4 = i2 + (j4 - 1) * k3 + i4 * l3;
											i5 = i3 + k4;
											int j5 = k2 + (j4 - 1) * l3 - i4 * k3;

											if (k4 < 0 && !this.worldServerInstance.getBlock(l4, i5, j5).getMaterial().isSolid() || k4 >= 0
													&& !this.worldServerInstance.isAirBlock(l4, i5, j5)) {
												continue label274;
											}
										}
									}
								}

								d4 = (double) i3 + 0.5D - par1Entity.posY;
								d3 = d1 * d1 + d4 * d4 + d2 * d2;

								if (d0 < 0.0D || d3 < d0) {
									d0 = d3;
									l = i2;
									i1 = i3;
									j1 = k2;
									k1 = j3 % 4;
								}
							}
						}
					}
				}
			}

			if (d0 < 0.0D) {
				for (i2 = i - b0; i2 <= i + b0; ++i2) {
					d1 = (double) i2 + 0.5D - par1Entity.posX;

					for (k2 = k - b0; k2 <= k + b0; ++k2) {
						d2 = (double) k2 + 0.5D - par1Entity.posZ;
						label222 :

						for (i3 = this.worldServerInstance.getActualHeight() - 1; i3 >= 0; --i3) {
							if (this.worldServerInstance.isAirBlock(i2, i3, k2)) {
								while (i3 > 0 && this.worldServerInstance.isAirBlock(i2, i3 - 1, k2)) {
									--i3;
								}

								for (j3 = l1; j3 < l1 + 2; ++j3) {
									k3 = j3 % 2;
									l3 = 1 - k3;

									for (i4 = 0; i4 < 4; ++i4) {
										for (j4 = -1; j4 < 4; ++j4) {
											k4 = i2 + (i4 - 1) * k3;
											l4 = i3 + j4;
											i5 = k2 + (i4 - 1) * l3;

											if (j4 < 0 && !this.worldServerInstance.getBlock(k4, l4, i5).getMaterial().isSolid() || j4 >= 0
													&& !this.worldServerInstance.isAirBlock(k4, l4, i5)) {
												continue label222;
											}
										}
									}

									d4 = (double) i3 + 0.5D - par1Entity.posY;
									d3 = d1 * d1 + d4 * d4 + d2 * d2;

									if (d0 < 0.0D || d3 < d0) {
										d0 = d3;
										l = i2;
										i1 = i3;
										j1 = k2;
										k1 = j3 % 2;
									}
								}
							}
						}
					}
				}
			}

			int k5 = l;
			int j2 = i1;
			k2 = j1;
			int l5 = k1 % 2;
			int l2 = 1 - l5;

			if (k1 % 4 >= 2) {
				l5 = -l5;
				l2 = -l2;
			}

			boolean flag;

			if (d0 < 0.0D) {
				if (i1 < 70) {
					i1 = 70;
				}

				if (i1 > this.worldServerInstance.getActualHeight() - 10) {
					i1 = this.worldServerInstance.getActualHeight() - 10;
				}

				j2 = i1;

				for (i3 = -1; i3 <= 1; ++i3) {
					for (j3 = 1; j3 < 3; ++j3) {
						for (k3 = -1; k3 < 3; ++k3) {
							l3 = k5 + (j3 - 1) * l5 + i3 * l2;
							i4 = j2 + k3;
							j4 = k2 + (j3 - 1) * l2 - i3 * l5;
							flag = k3 < 0;
							this.worldServerInstance.setBlock(l3, i4, j4, flag ? Blocks.hay_block : Blocks.air);
						}
					}
				}
			}

			for (i3 = 0; i3 < 4; ++i3) {
				for (j3 = 0; j3 < 4; ++j3) {
					for (k3 = -1; k3 < 4; ++k3) {
						l3 = k5 + (j3 - 1) * l5;
						i4 = j2 + k3;
						j4 = k2 + (j3 - 1) * l2;
						flag = j3 == 0 || j3 == 3 || k3 == -1 || k3 == 3;
						this.worldServerInstance.setBlock(l3, i4, j4, (Block) (flag ? Blocks.hay_block : portal), 0, 2);
					}
				}

				for (j3 = 0; j3 < 4; ++j3) {
					for (k3 = -1; k3 < 4; ++k3) {
						l3 = k5 + (j3 - 1) * l5;
						i4 = j2 + k3;
						j4 = k2 + (j3 - 1) * l2;
						this.worldServerInstance.notifyBlocksOfNeighborChange(l3, i4, j4, this.worldServerInstance.getBlock(l3, i4, j4));
					}
				}
			}

			return true;
		}

		/**
		 * called periodically to remove out-of-date portal locations from the
		 * cache list. Argument par1 is a WorldServer.getTotalWorldTime() value.
		 */
		public void removeStalePortalLocations(long par1) {
			if (par1 % 100L == 0L) {
				Iterator iterator = this.destinationCoordinateKeys.iterator();
				long j = par1 - 600L;

				while (iterator.hasNext()) {
					Long olong = (Long) iterator.next();
					Teleporter.PortalPosition portalposition = (Teleporter.PortalPosition) this.destinationCoordinateCache.getValueByKey(olong
							.longValue());

					if (portalposition == null || portalposition.lastUpdateTime < j) {
						iterator.remove();
						this.destinationCoordinateCache.remove(olong.longValue());
					}
				}
			}
		}

		public class PortalPosition extends ChunkCoordinates {
			/**
			 * The worldtime at which this PortalPosition was last verified
			 */
			public long lastUpdateTime;
			private static final String __OBFID = "CL_00000154";

			public PortalPosition(int par2, int par3, int par4, long par5) {
				super(par2, par3, par4);
				this.lastUpdateTime = par5;
			}
		}
	}

	// /FIRE BLOCK

	static class BlockFireMod extends Block {

		protected BlockFireMod() {
			super(Material.ground);
		}

		public void onBlockAdded(World par1World, int par2, int par3, int par4) {
			/* TutorialPortal.tryToCreatePortal(par1World, par2, par3, par4); */
		}

	}// fire block end

	// /PORTAL BLOCK

	public static class BlockTutorialPortal extends BlockBreakable {
		IIcon gor = null, dol = null, st1 = null, st2 = null, st3 = null, st4 = null;

		public BlockTutorialPortal() {
			super("", Material.portal, false);
			this.setTickRandomly(false);
			this.setHardness(-1.0F);
			this.setLightLevel(0.75F);
		}

		@SideOnly(Side.CLIENT)
		@Override
		public IIcon getIcon(int i, int par2) {

			if (i == 0)
				return gor;

			else if (i == 1)
				return dol;

			else if (i == 2)
				return st1;

			else if (i == 3)
				return st2;

			else if (i == 4)
				return st4;

			else if (i == 5)
				return st3;

			else
				return gor;

		}

		@SideOnly(Side.CLIENT)
		@Override
		public void registerBlockIcons(IIconRegister reg) {
			this.gor = reg.registerIcon("glass_lime");
			this.dol = reg.registerIcon("glass_lime");
			this.st1 = reg.registerIcon("glass_lime");
			this.st2 = reg.registerIcon("glass_lime");
			this.st3 = reg.registerIcon("glass_lime");
			this.st4 = reg.registerIcon("glass_lime");
		}

		/**
		 * Ticks the block if it's been scheduled
		 */
		public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
			super.updateTick(par1World, par2, par3, par4, par5Random);
			if (par1World.provider.isSurfaceWorld()) {
				int l;
				for (l = par3; !par1World.doesBlockHaveSolidTopSurface(par1World, par2, l, par4) && l > 0; --l) {
					;
				}
				if (l > 0 && !par1World.isBlockNormalCubeDefault(par2, l + 1, par4, true)) {
					Entity entity = ItemMonsterPlacer.spawnCreature(par1World, 57, (double) par2 + 0.5D, (double) l + 1.1D, (double) par4 + 0.5D);
					if (entity != null) {
						entity.timeUntilPortal = entity.getPortalCooldown();
					}
				}
			}
		}

		/**
		 * Returns a bounding box from the pool of bounding boxes (this means
		 * this box can change after the pool has been cleared to be reused)
		 */
		public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
			return null;
		}

		/**
		 * Updates the blocks bounds based on its current state. Args: world, x,
		 * y, z
		 */
		public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
			float f;
			float f1;
			if (par1IBlockAccess.getBlock(par2 - 1, par3, par4) != this && par1IBlockAccess.getBlock(par2 + 1, par3, par4) != this) {
				f = 0.125F;
				f1 = 0.5F;
				this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
			} else {
				f = 0.5F;
				f1 = 0.125F;
				this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
			}
		}

		/**
		 * Is this block (a) opaque and (B) a full 1m cube? This determines
		 * whether or not to render the shared face of two adjacent blocks and
		 * also whether the player can attach torches, redstone wire, etc to
		 * this block.
		 */
		public boolean isOpaqueCube() {
			return false;
		}

		/**
		 * If this block doesn't render as an ordinary block it will return
		 * False (examples: signs, buttons, stairs, etc)
		 */
		@Override
		public boolean renderAsNormalBlock() {
			return false;
		}

		/**
		 * Checks to see if this location is valid to create a portal and will
		 * return True if it does. Args: world, x, y, z
		 */
		public boolean tryToCreatePortal(World par1World, int par2, int par3, int par4) {
			byte b0 = 0;
			byte b1 = 0;
			if (par1World.getBlock(par2 - 1, par3, par4) == Blocks.hay_block || par1World.getBlock(par2 + 1, par3, par4) == Blocks.hay_block) {
				b0 = 1;
			}
			if (par1World.getBlock(par2, par3, par4 - 1) == Blocks.hay_block || par1World.getBlock(par2, par3, par4 + 1) == Blocks.hay_block) {
				b1 = 1;
			}
			if (b0 == b1) {
				return false;
			} else {
				if (par1World.getBlock(par2 - b0, par3, par4 - b1) == Blocks.air) {
					par2 -= b0;
					par4 -= b1;
				}
				int l;
				int i1;
				for (l = -1; l <= 2; ++l) {
					for (i1 = -1; i1 <= 3; ++i1) {
						boolean flag = l == -1 || l == 2 || i1 == -1 || i1 == 3;
						if (l != -1 && l != 2 || i1 != -1 && i1 != 3) {
							Block j1 = par1World.getBlock(par2 + b0 * l, par3 + i1, par4 + b1 * l);
							if (flag) {
								if (j1 != Blocks.hay_block) {
									return false;
								}
							}
							/*
							 * else if (j1 != 0 && j1 !=
							 * Main.TutorialFire.blockID) { return false; }
							 */
						}
					}
				}
				for (l = 0; l < 2; ++l) {
					for (i1 = 0; i1 < 3; ++i1) {
						par1World.setBlock(par2 + b0 * l, par3 + i1, par4 + b1 * l, this, 0, 2);
					}
				}
				return true;
			}
		}

		/**
		 * Lets the block know when one of its neighbor changes. Doesn't know
		 * which neighbor changed (coordinates passed are their own) Args: x, y,
		 * z, neighbor blockID
		 */
		public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
			byte b0 = 0;
			byte b1 = 1;
			if (par1World.getBlock(par2 - 1, par3, par4) == this || par1World.getBlock(par2 + 1, par3, par4) == this) {
				b0 = 1;
				b1 = 0;
			}
			int i1;
			for (i1 = par3; par1World.getBlock(par2, i1 - 1, par4) == this; --i1) {
				;
			}
			if (par1World.getBlock(par2, i1 - 1, par4) != Blocks.hay_block) {
				par1World.setBlockToAir(par2, par3, par4);
			} else {
				int j1;
				for (j1 = 1; j1 < 4 && par1World.getBlock(par2, i1 + j1, par4) == this; ++j1) {
					;
				}
				if (j1 == 3 && par1World.getBlock(par2, i1 + j1, par4) == Blocks.hay_block) {
					boolean flag = par1World.getBlock(par2 - 1, par3, par4) == this || par1World.getBlock(par2 + 1, par3, par4) == this;
					boolean flag1 = par1World.getBlock(par2, par3, par4 - 1) == this || par1World.getBlock(par2, par3, par4 + 1) == this;
					if (flag && flag1) {
						par1World.setBlockToAir(par2, par3, par4);
					} else {
						if ((par1World.getBlock(par2 + b0, par3, par4 + b1) != Blocks.hay_block || par1World.getBlock(par2 - b0, par3, par4 - b1) != this)
								&& (par1World.getBlock(par2 - b0, par3, par4 - b1) != Blocks.hay_block || par1World.getBlock(par2 + b0, par3, par4
										+ b1) != this)) {
							par1World.setBlockToAir(par2, par3, par4);
						}
					}
				} else {
					par1World.setBlockToAir(par2, par3, par4);
				}
			}
		}

		@SideOnly(Side.CLIENT)
		/**
		 * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
		 * coordinates. Args: blockAccess, x, y, z, side
		 */
		public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
			if (par1IBlockAccess.getBlock(par2, par3, par4) == this) {
				return false;
			} else {
				boolean flag = par1IBlockAccess.getBlock(par2 - 1, par3, par4) == this && par1IBlockAccess.getBlock(par2 - 2, par3, par4) != this;
				boolean flag1 = par1IBlockAccess.getBlock(par2 + 1, par3, par4) == this && par1IBlockAccess.getBlock(par2 + 2, par3, par4) != this;
				boolean flag2 = par1IBlockAccess.getBlock(par2, par3, par4 - 1) == this && par1IBlockAccess.getBlock(par2, par3, par4 - 2) != this;
				boolean flag3 = par1IBlockAccess.getBlock(par2, par3, par4 + 1) == this && par1IBlockAccess.getBlock(par2, par3, par4 + 2) != this;
				boolean flag4 = flag || flag1;
				boolean flag5 = flag2 || flag3;
				return flag4 && par5 == 4 ? true : (flag4 && par5 == 5 ? true : (flag5 && par5 == 2 ? true : flag5 && par5 == 3));
			}
		}

		/**
		 * Returns the quantity of items to drop on block destruction.
		 */
		public int quantityDropped(Random par1Random) {
			return 0;
		}

		/**
		 * Triggered whenever an entity collides with this block (enters into
		 * the block). Args: world, x, y, z, entity
		 */
		public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
			if ((par5Entity.ridingEntity == null) && (par5Entity.riddenByEntity == null) && ((par5Entity instanceof EntityPlayerMP))) {
				EntityPlayerMP thePlayer = (EntityPlayerMP) par5Entity;
				if (thePlayer.timeUntilPortal > 0) {
					thePlayer.timeUntilPortal = 10;
				} else if (thePlayer.dimension != DIMID) {
					thePlayer.timeUntilPortal = 10;
					thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, DIMID,
							new TeleporterDimensionMod(thePlayer.mcServer.worldServerForDimension(DIMID)));
				} else {
					thePlayer.timeUntilPortal = 10;
					thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, 0,
							new TeleporterDimensionMod(thePlayer.mcServer.worldServerForDimension(0)));
				}
			}
		}

		@SideOnly(Side.CLIENT)
		/**
		 * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
		 */
		public int getRenderBlockPass() {
			return 1;
		}

		@SideOnly(Side.CLIENT)
		/**
		 * A randomly called display update to be able to add particles or other items for display
		 */
		public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
			if (par5Random.nextInt(100) == 0) {
				par1World.playSound((double) par2 + 0.5D, (double) par3 + 0.5D, (double) par4 + 0.5D, "portal.portal", 0.5F,
						par5Random.nextFloat() * 0.4F + 0.8F, false);
			}
			for (int l = 0; l < 4; ++l) {
				double d0 = (double) ((float) par2 + par5Random.nextFloat());
				double d1 = (double) ((float) par3 + par5Random.nextFloat());
				double d2 = (double) ((float) par4 + par5Random.nextFloat());
				double d3 = 0.0D;
				double d4 = 0.0D;
				double d5 = 0.0D;
				int i1 = par5Random.nextInt(2) * 2 - 1;
				d3 = ((double) par5Random.nextFloat() - 0.5D) * 0.5D;
				d4 = ((double) par5Random.nextFloat() - 0.5D) * 0.5D;
				d5 = ((double) par5Random.nextFloat() - 0.5D) * 0.5D;
				if (par1World.getBlock(par2 - 1, par3, par4) != this && par1World.getBlock(par2 + 1, par3, par4) != this) {
					d0 = (double) par2 + 0.5D + 0.25D * (double) i1;
					d3 = (double) (par5Random.nextFloat() * 2.0F * (float) i1);
				} else {
					d2 = (double) par4 + 0.5D + 0.25D * (double) i1;
					d5 = (double) (par5Random.nextFloat() * 2.0F * (float) i1);
				}
				par1World.spawnParticle("portal", d0, d1, d2, d3, d4, d5);
			}
		}

		@SideOnly(Side.CLIENT)
		/**
		 * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
		 */
		public int idPicked(World par1World, int par2, int par3, int par4) {
			return 0;
		}
	}

	// portal block

	public static class ModTrigger extends Item {
		public ModTrigger() {
			super();
			this.maxStackSize = 1;
			setMaxDamage(64);
			setCreativeTab(CreativeTabs.tabTools);
		}

		public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7,
				float par8, float par9, float par10) {
			if (par7 == 0) {
				par5--;
			}
			if (par7 == 1) {
				par5++;
			}
			if (par7 == 2) {
				par6--;
			}
			if (par7 == 3) {
				par6++;
			}
			if (par7 == 4) {
				par4--;
			}
			if (par7 == 5) {
				par4++;
			}
			if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
				return false;
			}
			Block i1 = par3World.getBlock(par4, par5, par6);
			if (i1 == Blocks.air) {
				par3World.playSoundEffect(par4 + 0.5D, par5 + 0.5D, par6 + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
				portal.tryToCreatePortal(par3World, par4, par5, par6);
			}
			par1ItemStack.damageItem(1, par2EntityPlayer);
			return true;
		}
	}

	public static class ChunkProviderModded implements IChunkProvider {
		private Random rand;
		private NoiseGeneratorOctaves field_147431_j;
		private NoiseGeneratorOctaves field_147432_k;
		private NoiseGeneratorOctaves field_147429_l;
		private NoiseGeneratorPerlin field_147430_m;
		/**
		 * A NoiseGeneratorOctaves used in generating terrain
		 */
		public NoiseGeneratorOctaves noiseGen5;
		/**
		 * A NoiseGeneratorOctaves used in generating terrain
		 */
		public NoiseGeneratorOctaves noiseGen6;
		public NoiseGeneratorOctaves mobSpawnerNoise;
		/**
		 * Reference to the World object.
		 */
		private World worldObj;
		/**
		 * are map structures going to be generated (e.g. strongholds)
		 */
		private final boolean mapFeaturesEnabled;
		private WorldType field_147435_p;
		private final double[] field_147434_q;
		private final float[] parabolicField;
		private double[] stoneNoise = new double[256];
		private MapGenBase caveGenerator = new MapGenCaves();
		/**
		 * Holds Stronghold Generator
		 */
		private MapGenStronghold strongholdGenerator = new MapGenStronghold();
		/**
		 * Holds Village Generator
		 */
		private MapGenVillage villageGenerator = new MapGenVillage();
		/**
		 * Holds Mineshaft Generator
		 */
		private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
		private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
		/**
		 * Holds ravine generator
		 */
		private MapGenBase ravineGenerator = new MapGenRavine();
		/**
		 * The biomes that are used to generate the chunk
		 */
		private BiomeGenBase[] biomesForGeneration;
		double[] field_147427_d;
		double[] field_147428_e;
		double[] field_147425_f;
		double[] field_147426_g;
		int[][] field_73219_j = new int[32][32];
		private static final String __OBFID = "CL_00000396";

		{
			caveGenerator = TerrainGen.getModdedMapGen(caveGenerator, net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.CAVE);
			strongholdGenerator = (MapGenStronghold) TerrainGen.getModdedMapGen(strongholdGenerator,
					net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.STRONGHOLD);
			villageGenerator = (MapGenVillage) TerrainGen.getModdedMapGen(villageGenerator,
					net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.VILLAGE);
			mineshaftGenerator = (MapGenMineshaft) TerrainGen.getModdedMapGen(mineshaftGenerator,
					net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.MINESHAFT);
			scatteredFeatureGenerator = (MapGenScatteredFeature) TerrainGen.getModdedMapGen(scatteredFeatureGenerator,
					net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.SCATTERED_FEATURE);
			ravineGenerator = TerrainGen.getModdedMapGen(ravineGenerator, net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.RAVINE);
		}

		public ChunkProviderModded(World par1World, long par2) {
			this.worldObj = par1World;
			this.mapFeaturesEnabled = false;
			this.field_147435_p = par1World.getWorldInfo().getTerrainType();
			this.rand = new Random(par2);
			this.field_147431_j = new NoiseGeneratorOctaves(this.rand, 16);
			this.field_147432_k = new NoiseGeneratorOctaves(this.rand, 16);
			this.field_147429_l = new NoiseGeneratorOctaves(this.rand, 8);
			this.field_147430_m = new NoiseGeneratorPerlin(this.rand, 4);
			this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
			this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
			this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
			this.field_147434_q = new double[825];
			this.parabolicField = new float[25];

			for (int j = -2; j <= 2; ++j) {
				for (int k = -2; k <= 2; ++k) {
					float f = 10.0F / MathHelper.sqrt_float((float) (j * j + k * k) + 0.2F);
					this.parabolicField[j + 2 + (k + 2) * 5] = f;
				}
			}

			NoiseGenerator[] noiseGens = {field_147431_j, field_147432_k, field_147429_l, field_147430_m, noiseGen5, noiseGen6, mobSpawnerNoise};
			noiseGens = TerrainGen.getModdedNoiseGenerators(par1World, this.rand, noiseGens);
			this.field_147431_j = (NoiseGeneratorOctaves) noiseGens[0];
			this.field_147432_k = (NoiseGeneratorOctaves) noiseGens[1];
			this.field_147429_l = (NoiseGeneratorOctaves) noiseGens[2];
			this.field_147430_m = (NoiseGeneratorPerlin) noiseGens[3];
			this.noiseGen5 = (NoiseGeneratorOctaves) noiseGens[4];
			this.noiseGen6 = (NoiseGeneratorOctaves) noiseGens[5];
			this.mobSpawnerNoise = (NoiseGeneratorOctaves) noiseGens[6];
		}

		public void func_147424_a(int p_147424_1_, int p_147424_2_, Block[] p_147424_3_) {
			byte b0 = 63;
			this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, p_147424_1_ * 4 - 2,
					p_147424_2_ * 4 - 2, 10, 10);
			this.func_147423_a(p_147424_1_ * 4, 0, p_147424_2_ * 4);

			for (int k = 0; k < 4; ++k) {
				int l = k * 5;
				int i1 = (k + 1) * 5;

				for (int j1 = 0; j1 < 4; ++j1) {
					int k1 = (l + j1) * 33;
					int l1 = (l + j1 + 1) * 33;
					int i2 = (i1 + j1) * 33;
					int j2 = (i1 + j1 + 1) * 33;

					for (int k2 = 0; k2 < 32; ++k2) {
						double d0 = 0.125D;
						double d1 = this.field_147434_q[k1 + k2];
						double d2 = this.field_147434_q[l1 + k2];
						double d3 = this.field_147434_q[i2 + k2];
						double d4 = this.field_147434_q[j2 + k2];
						double d5 = (this.field_147434_q[k1 + k2 + 1] - d1) * d0;
						double d6 = (this.field_147434_q[l1 + k2 + 1] - d2) * d0;
						double d7 = (this.field_147434_q[i2 + k2 + 1] - d3) * d0;
						double d8 = (this.field_147434_q[j2 + k2 + 1] - d4) * d0;

						for (int l2 = 0; l2 < 8; ++l2) {
							double d9 = 0.25D;
							double d10 = d1;
							double d11 = d2;
							double d12 = (d3 - d1) * d9;
							double d13 = (d4 - d2) * d9;

							for (int i3 = 0; i3 < 4; ++i3) {
								int j3 = i3 + k * 4 << 12 | 0 + j1 * 4 << 8 | k2 * 8 + l2;
								short short1 = 256;
								j3 -= short1;
								double d14 = 0.25D;
								double d16 = (d11 - d10) * d14;
								double d15 = d10 - d16;

								for (int k3 = 0; k3 < 4; ++k3) {
									if ((d15 += d16) > 0.0D) {
										p_147424_3_[j3 += short1] = Blocks.diamond_block;
									} else if (k2 * 8 + l2 < b0) {
										p_147424_3_[j3 += short1] = Blocks.gold_block;
									} else {
										p_147424_3_[j3 += short1] = null;
									}
								}

								d10 += d12;
								d11 += d13;
							}

							d1 += d5;
							d2 += d6;
							d3 += d7;
							d4 += d8;
						}
					}
				}
			}
		}

		public void replaceBlocksForBiome(int p_147422_1_, int p_147422_2_, Block[] p_147422_3_, byte[] p_147422_4_, BiomeGenBase[] p_147422_5_) {
			ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, p_147422_1_, p_147422_2_, p_147422_3_,
					p_147422_5_);
			MinecraftForge.EVENT_BUS.post(event);
			if (event.getResult() == cpw.mods.fml.common.eventhandler.Event.Result.DENY)
				return;

			double d0 = 0.03125D;
			this.stoneNoise = this.field_147430_m.func_151599_a(this.stoneNoise, (double) (p_147422_1_ * 16), (double) (p_147422_2_ * 16), 16, 16,
					d0 * 2.0D, d0 * 2.0D, 1.0D);

			for (int k = 0; k < 16; ++k) {
				for (int l = 0; l < 16; ++l) {
					BiomeGenBase biomegenbase = p_147422_5_[l + k * 16];
					biomegenbase.genTerrainBlocks(this.worldObj, this.rand, p_147422_3_, p_147422_4_, p_147422_1_ * 16 + k, p_147422_2_ * 16 + l,
							this.stoneNoise[l + k * 16]);
				}
			}
		}

		/**
		 * loads or generates the chunk at the chunk location specified
		 */
		public Chunk loadChunk(int par1, int par2) {
			return this.provideChunk(par1, par2);
		}

		/**
		 * Will return back a chunk, if it doesn't exist and its not a MP client
		 * it will generates all the blocks for the specified chunk from the map
		 * seed and chunk seed
		 */
		public Chunk provideChunk(int par1, int par2) {
			this.rand.setSeed((long) par1 * 341873128712L + (long) par2 * 132897987541L);
			Block[] ablock = new Block[65536];
			byte[] abyte = new byte[65536];
			this.func_147424_a(par1, par2, ablock);
			this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, par1 * 16, par2 * 16,
					16, 16);
			this.replaceBlocksForBiome(par1, par2, ablock, abyte, this.biomesForGeneration);

			Chunk chunk = new Chunk(this.worldObj, ablock, abyte, par1, par2);
			byte[] abyte1 = chunk.getBiomeArray();

			for (int k = 0; k < abyte1.length; ++k) {
				abyte1[k] = (byte) this.biomesForGeneration[k].biomeID;
			}

			chunk.generateSkylightMap();
			return chunk;
		}

		private void func_147423_a(int p_147423_1_, int p_147423_2_, int p_147423_3_) {
			double d0 = 684.412D;
			double d1 = 684.412D;
			double d2 = 512.0D;
			double d3 = 512.0D;
			this.field_147426_g = this.noiseGen6.generateNoiseOctaves(this.field_147426_g, p_147423_1_, p_147423_3_, 5, 5, 200.0D, 200.0D, 0.5D);
			this.field_147427_d = this.field_147429_l.generateNoiseOctaves(this.field_147427_d, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5,
					8.555150000000001D, 4.277575000000001D, 8.555150000000001D);
			this.field_147428_e = this.field_147431_j.generateNoiseOctaves(this.field_147428_e, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5,
					684.412D, 684.412D, 684.412D);
			this.field_147425_f = this.field_147432_k.generateNoiseOctaves(this.field_147425_f, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5,
					684.412D, 684.412D, 684.412D);
			boolean flag1 = false;
			boolean flag = false;
			int l = 0;
			int i1 = 0;
			double d4 = 8.5D;

			for (int j1 = 0; j1 < 5; ++j1) {
				for (int k1 = 0; k1 < 5; ++k1) {
					float f = 0.0F;
					float f1 = 0.0F;
					float f2 = 0.0F;
					byte b0 = 2;
					BiomeGenBase biomegenbase = this.biomesForGeneration[j1 + 2 + (k1 + 2) * 10];

					for (int l1 = -b0; l1 <= b0; ++l1) {
						for (int i2 = -b0; i2 <= b0; ++i2) {
							BiomeGenBase biomegenbase1 = this.biomesForGeneration[j1 + l1 + 2 + (k1 + i2 + 2) * 10];
							float f3 = biomegenbase1.rootHeight;
							float f4 = biomegenbase1.heightVariation;

							if (this.field_147435_p == WorldType.AMPLIFIED && f3 > 0.0F) {
								f3 = 1.0F + f3 * 2.0F;
								f4 = 1.0F + f4 * 4.0F;
							}

							float f5 = this.parabolicField[l1 + 2 + (i2 + 2) * 5] / (f3 + 2.0F);

							if (biomegenbase1.rootHeight > biomegenbase.rootHeight) {
								f5 /= 2.0F;
							}

							f += f4 * f5;
							f1 += f3 * f5;
							f2 += f5;
						}
					}

					f /= f2;
					f1 /= f2;
					f = f * 0.9F + 0.1F;
					f1 = (f1 * 4.0F - 1.0F) / 8.0F;
					double d13 = this.field_147426_g[i1] / 8000.0D;

					if (d13 < 0.0D) {
						d13 = -d13 * 0.3D;
					}

					d13 = d13 * 3.0D - 2.0D;

					if (d13 < 0.0D) {
						d13 /= 2.0D;

						if (d13 < -1.0D) {
							d13 = -1.0D;
						}

						d13 /= 1.4D;
						d13 /= 2.0D;
					} else {
						if (d13 > 1.0D) {
							d13 = 1.0D;
						}

						d13 /= 8.0D;
					}

					++i1;
					double d12 = (double) f1;
					double d14 = (double) f;
					d12 += d13 * 0.2D;
					d12 = d12 * 8.5D / 8.0D;
					double d5 = 8.5D + d12 * 4.0D;

					for (int j2 = 0; j2 < 33; ++j2) {
						double d6 = ((double) j2 - d5) * 12.0D * 128.0D / 256.0D / d14;

						if (d6 < 0.0D) {
							d6 *= 4.0D;
						}

						double d7 = this.field_147428_e[l] / 512.0D;
						double d8 = this.field_147425_f[l] / 512.0D;
						double d9 = (this.field_147427_d[l] / 10.0D + 1.0D) / 2.0D;
						double d10 = MathHelper.denormalizeClamp(d7, d8, d9) - d6;

						if (j2 > 29) {
							double d11 = (double) ((float) (j2 - 29) / 3.0F);
							d10 = d10 * (1.0D - d11) + -10.0D * d11;
						}

						this.field_147434_q[l] = d10;
						++l;
					}
				}
			}
		}

		/**
		 * Checks to see if a chunk exists at x, y
		 */
		public boolean chunkExists(int par1, int par2) {
			return true;
		}

		/**
		 * Populates chunk with ores etc etc
		 */
		public void populate(IChunkProvider par1IChunkProvider, int par2, int par3) {
			BlockFalling.fallInstantly = true;
			int k = par2 * 16;
			int l = par3 * 16;
			BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(k + 16, l + 16);
			this.rand.setSeed(this.worldObj.getSeed());
			long i1 = this.rand.nextLong() / 2L * 2L + 1L;
			long j1 = this.rand.nextLong() / 2L * 2L + 1L;
			this.rand.setSeed((long) par2 * i1 + (long) par3 * j1 ^ this.worldObj.getSeed());
			boolean flag = false;

			MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(par1IChunkProvider, worldObj, rand, par2, par3, flag));

			int k1;
			int l1;
			int i2;

			if (biomegenbase != BiomeGenBase.desert
					&& biomegenbase != BiomeGenBase.desertHills
					&& !flag
					&& this.rand.nextInt(4) == 0
					&& TerrainGen.populate(par1IChunkProvider, worldObj, rand, par2, par3, flag,
							net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE)) {
				k1 = k + this.rand.nextInt(16) + 8;
				l1 = this.rand.nextInt(256);
				i2 = l + this.rand.nextInt(16) + 8;
				(new WorldGenLakes(Blocks.gold_block)).generate(this.worldObj, this.rand, k1, l1, i2);
			}

			if (TerrainGen.populate(par1IChunkProvider, worldObj, rand, par2, par3, flag,
					net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA) && !flag && this.rand.nextInt(8) == 0) {
				k1 = k + this.rand.nextInt(16) + 8;
				l1 = this.rand.nextInt(this.rand.nextInt(248) + 8);
				i2 = l + this.rand.nextInt(16) + 8;

				if (l1 < 63 || this.rand.nextInt(10) == 0) {
					(new WorldGenLakes(Blocks.gold_block)).generate(this.worldObj, this.rand, k1, l1, i2);
				}
			}
			int var4 = par2 * 16;
			int var5 = par3 * 16;

			biomegenbase.decorate(this.worldObj, this.rand, k, l);
			SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, k + 8, l + 8, 16, 16, this.rand);
			k += 8;
			l += 8;

			MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(par1IChunkProvider, worldObj, rand, par2, par3, flag));

			BlockFalling.fallInstantly = false;
		}

		/**
		 * Two modes of operation: if passed true, save all Chunks in one go. If
		 * passed false, save up to two chunks. Return true if all chunks have
		 * been saved.
		 */
		public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate) {
			return true;
		}

		/**
		 * Save extra data not associated with any Chunk. Not saved during
		 * autosave, only during world unload. Currently unimplemented.
		 */
		public void saveExtraData() {
		}

		/**
		 * Unloads chunks that are marked to be unloaded. This is not guaranteed
		 * to unload every such chunk.
		 */
		public boolean unloadQueuedChunks() {
			return false;
		}

		/**
		 * Returns if the IChunkProvider supports saving.
		 */
		public boolean canSave() {
			return true;
		}

		/**
		 * Converts the instance data to a readable string.
		 */
		public String makeString() {
			return "RandomLevelSource";
		}

		/**
		 * Returns a list of creatures of the specified type that can spawn at
		 * the given location.
		 */
		public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4) {
			BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(par2, par4);
			return par1EnumCreatureType == EnumCreatureType.monster && this.scatteredFeatureGenerator.func_143030_a(par2, par3, par4)
					? this.scatteredFeatureGenerator.getScatteredFeatureSpawnList()
					: biomegenbase.getSpawnableList(par1EnumCreatureType);
		}

		public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_) {
			return "Stronghold".equals(p_147416_2_) && this.strongholdGenerator != null ? this.strongholdGenerator.func_151545_a(p_147416_1_,
					p_147416_3_, p_147416_4_, p_147416_5_) : null;
		}

		public int getLoadedChunkCount() {
			return 0;
		}

		public void recreateStructures(int par1, int par2) {

		}
	}

	public static class WorldChunkManagerCustom extends WorldChunkManager {

		private GenLayer genBiomes;
		/** A GenLayer containing the indices into BiomeGenBase.biomeList[] */
		private GenLayer biomeIndexLayer;
		/** The BiomeCache object for this world. */
		private BiomeCache biomeCache;
		/** A list of biomes that the player can spawn in. */
		private List<BiomeGenBase> biomesToSpawnIn;

		@SuppressWarnings({"unchecked", "rawtypes"})
		public WorldChunkManagerCustom() {
			this.biomeCache = new BiomeCache(this);
			this.biomesToSpawnIn = new ArrayList();
			this.biomesToSpawnIn.addAll(allowedBiomes);
		}

		public WorldChunkManagerCustom(long seed, WorldType worldType) {
			this();
			// i changed this to my GenLayerTutorial
			GenLayer[] agenlayer = LightForestGenLayer.makeTheWorld(seed, worldType);

			agenlayer = getModdedBiomeGenerators(worldType, seed, agenlayer);
			this.genBiomes = agenlayer[0];
			this.biomeIndexLayer = agenlayer[1];

		}

		public WorldChunkManagerCustom(World world) {
			this(world.getSeed(), world.getWorldInfo().getTerrainType());

		}

		/**
		 * Gets the list of valid biomes for the player to spawn in.
		 */
		@Override
		public List<BiomeGenBase> getBiomesToSpawnIn() {
			return this.biomesToSpawnIn;
		}

		/**
		 * Returns a list of rainfall values for the specified blocks. Args:
		 * listToReuse, x, z, width, length.
		 */
		@Override
		public float[] getRainfall(float[] listToReuse, int x, int z, int width, int length) {
			IntCache.resetIntCache();

			if (listToReuse == null || listToReuse.length < width * length) {
				listToReuse = new float[width * length];
			}

			int[] aint = this.biomeIndexLayer.getInts(x, z, width, length);

			for (int i1 = 0; i1 < width * length; ++i1) {
				try {
					float f = (float) BiomeGenBase.getBiome(aint[i1]).getIntRainfall() / 65536.0F;

					if (f > 1.0F) {
						f = 1.0F;
					}

					listToReuse[i1] = f;
				} catch (Throwable throwable) {
					CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
					CrashReportCategory crashreportcategory = crashreport.makeCategory("DownfallBlock");
					crashreportcategory.addCrashSection("biome id", Integer.valueOf(i1));
					crashreportcategory.addCrashSection("downfalls[] size", Integer.valueOf(listToReuse.length));
					crashreportcategory.addCrashSection("x", Integer.valueOf(x));
					crashreportcategory.addCrashSection("z", Integer.valueOf(z));
					crashreportcategory.addCrashSection("w", Integer.valueOf(width));
					crashreportcategory.addCrashSection("h", Integer.valueOf(length));
					throw new ReportedException(crashreport);
				}
			}

			return listToReuse;
		}

		/**
		 * Return an adjusted version of a given temperature based on the y
		 * height
		 */
		@Override
		@SideOnly(Side.CLIENT)
		public float getTemperatureAtHeight(float par1, int par2) {
			return par1;
		}

		/**
		 * Returns an array of biomes for the location input.
		 */
		@Override
		public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] par1ArrayOfBiomeGenBase, int par2, int par3, int par4, int par5) {
			IntCache.resetIntCache();

			if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5) {
				par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
			}

			int[] aint = this.genBiomes.getInts(par2, par3, par4, par5);

			try {
				for (int i = 0; i < par4 * par5; ++i) {
					par1ArrayOfBiomeGenBase[i] = BiomeGenBase.getBiome(aint[i]);
				}

				return par1ArrayOfBiomeGenBase;
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
				crashreportcategory.addCrashSection("biomes[] size", Integer.valueOf(par1ArrayOfBiomeGenBase.length));
				crashreportcategory.addCrashSection("x", Integer.valueOf(par2));
				crashreportcategory.addCrashSection("z", Integer.valueOf(par3));
				crashreportcategory.addCrashSection("w", Integer.valueOf(par4));
				crashreportcategory.addCrashSection("h", Integer.valueOf(par5));
				throw new ReportedException(crashreport);
			}
		}

		/**
		 * Returns biomes to use for the blocks and loads the other data like
		 * temperature and humidity onto the WorldChunkManager Args:
		 * oldBiomeList, x, z, width, depth
		 */
		@Override
		public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] oldBiomeList, int x, int z, int width, int depth) {
			return this.getBiomeGenAt(oldBiomeList, x, z, width, depth, true);
		}

		/**
		 * Return a list of biomes for the specified blocks. Args: listToReuse,
		 * x, y, width, length, cacheFlag (if false, don't check biomeCache to
		 * avoid infinite loop in BiomeCacheBlock)
		 */
		@Override
		public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] listToReuse, int x, int y, int width, int length, boolean cacheFlag) {
			IntCache.resetIntCache();

			if (listToReuse == null || listToReuse.length < width * length) {
				listToReuse = new BiomeGenBase[width * length];
			}

			if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (y & 15) == 0) {
				BiomeGenBase[] abiomegenbase1 = this.biomeCache.getCachedBiomes(x, y);
				System.arraycopy(abiomegenbase1, 0, listToReuse, 0, width * length);
				return listToReuse;
			} else {
				int[] aint = this.biomeIndexLayer.getInts(x, y, width, length);

				for (int i = 0; i < width * length; ++i) {
					listToReuse[i] = BiomeGenBase.getBiome(aint[i]);
				}
				return listToReuse;
			}
		}

		/**
		 * checks given Chunk's Biomes against List of allowed ones
		 */
		@Override
		@SuppressWarnings("rawtypes")
		public boolean areBiomesViable(int x, int y, int z, List par4List) {
			IntCache.resetIntCache();
			int l = x - z >> 2;
			int i1 = y - z >> 2;
			int j1 = x + z >> 2;
			int k1 = y + z >> 2;
			int l1 = j1 - l + 1;
			int i2 = k1 - i1 + 1;
			int[] aint = this.genBiomes.getInts(l, i1, l1, i2);

			try {
				for (int j2 = 0; j2 < l1 * i2; ++j2) {
					BiomeGenBase biomegenbase = BiomeGenBase.getBiome(aint[j2]);

					if (!par4List.contains(biomegenbase)) {
						return false;
					}
				}

				return true;
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
				crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
				crashreportcategory.addCrashSection("x", Integer.valueOf(x));
				crashreportcategory.addCrashSection("z", Integer.valueOf(y));
				crashreportcategory.addCrashSection("radius", Integer.valueOf(z));
				crashreportcategory.addCrashSection("allowed", par4List);
				throw new ReportedException(crashreport);
			}
		}

		/**
		 * Finds a valid position within a range, that is in one of the listed
		 * biomes. Searches {par1,par2} +-par3 blocks. Strongly favors positive
		 * y positions.
		 */
		@Override
		@SuppressWarnings("rawtypes")
		public ChunkPosition findBiomePosition(int p_150795_1_, int p_150795_2_, int p_150795_3_, List p_150795_4_, Random p_150795_5_) {
			IntCache.resetIntCache();
			int l = p_150795_1_ - p_150795_3_ >> 2;
			int i1 = p_150795_2_ - p_150795_3_ >> 2;
			int j1 = p_150795_1_ + p_150795_3_ >> 2;
			int k1 = p_150795_2_ + p_150795_3_ >> 2;
			int l1 = j1 - l + 1;
			int i2 = k1 - i1 + 1;
			int[] aint = this.genBiomes.getInts(l, i1, l1, i2);
			ChunkPosition chunkposition = null;
			int j2 = 0;

			for (int k2 = 0; k2 < l1 * i2; ++k2) {
				int l2 = l + k2 % l1 << 2;
				int i3 = i1 + k2 / l1 << 2;
				BiomeGenBase biomegenbase = BiomeGenBase.getBiome(aint[k2]);

				if (p_150795_4_.contains(biomegenbase) && (chunkposition == null || p_150795_5_.nextInt(j2 + 1) == 0)) {
					chunkposition = new ChunkPosition(l2, 0, i3);
					++j2;
				}
			}

			return chunkposition;
		}

		/**
		 * Calls the WorldChunkManager's biomeCache.cleanupCache()
		 */
		@Override
		public void cleanupCache() {
			this.biomeCache.cleanupCache();
		}
	}

	public static class LightForestGenLayer extends GenLayer {

		public LightForestGenLayer(long seed) {
			super(seed);
		}

		public static GenLayer[] makeTheWorld(long seed, WorldType type) {
			GenLayer biomes = new GenLayerBiomes(1L);
			biomes = new GenLayerZoom(1000L, biomes);
			biomes = new GenLayerZoom(1001L, biomes);
			biomes = new GenLayerZoom(1002L, biomes);
			biomes = new GenLayerZoom(1003L, biomes);
			biomes = new GenLayerZoom(1004L, biomes);
			biomes = new GenLayerZoom(1005L, biomes);
			GenLayer genlayervoronoizoom = new GenLayerVoronoiZoom(10L, biomes);
			biomes.initWorldGenSeed(seed);
			genlayervoronoizoom.initWorldGenSeed(seed);
			return new GenLayer[]{biomes, genlayervoronoizoom};
		}

		@Override
		public int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_, int p_75904_4_) {
			return null;
		}
	}

	public static class GenLayerBiomes extends GenLayer {

		protected BiomeGenBase[] allowedBiomes = {BiomeGenBase.plains,};

		public GenLayerBiomes(long seed) {
			super(seed);
		}

		public GenLayerBiomes(long seed, GenLayer genlayer) {
			super(seed);
			this.parent = genlayer;
		}

		@Override
		public int[] getInts(int x, int z, int width, int depth) {
			int[] dest = IntCache.getIntCache(width * depth);
			for (int dz = 0; dz < depth; dz++) {
				for (int dx = 0; dx < width; dx++) {
					this.initChunkSeed(dx + x, dz + z);
					dest[(dx + dz * width)] = this.allowedBiomes[nextInt(this.allowedBiomes.length)].biomeID;
				}
			}
			return dest;
		}
	}

}
