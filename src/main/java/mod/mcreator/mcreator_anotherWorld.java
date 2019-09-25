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
		public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
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
                   thePlayer.addExperienceLevel(0);
                   //thePlayer.removeExperienceLevel(1);
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
