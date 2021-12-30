package com.gmail.picono435.spawnerprogression;

import com.gmail.picono435.spawnerprogression.config.SpawnerConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.configurate.ConfigurateException;

import java.util.HashMap;
import java.util.Map;

@Mod(
        modid = SpawnerProgression.MOD_ID,
        name = SpawnerProgression.MOD_NAME,
        version = SpawnerProgression.VERSION
)
public class SpawnerProgression {

    public static final String MOD_ID = "spawnerprogression";
    public static final String MOD_NAME = "SpawnerProgression";
    public static final String VERSION = "1.0.0";
    private static final RegistryNamespacedDefaultedByKey<ResourceLocation, Block> BLOCK_REGISTRY = net.minecraftforge.registries.GameData.getWrapperDefaulted(Block.class);
    public static Map<ResourceLocation, Integer> spawnerProgressions = new HashMap<>();

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static SpawnerProgression INSTANCE;


    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        try {
            MinecraftForge.EVENT_BUS.register(SpawnerProgression.class);
            System.out.println("Setting up SpawnerProgression config...");
            SpawnerConfig.init();
            SpawnerConfig.retrieveSpawnerProgressions();
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        if(event.getWorld().getBlockState(event.getPos()).getBlock() != Blocks.MOB_SPAWNER) return;
        EntityPlayerMP entityPlayer = (EntityPlayerMP) event.getPlayer();
        int mobKills = entityPlayer.getStatFile().readStat(StatList.MOB_KILLS);
        TileEntityMobSpawner tileEntityMobSpawner = (TileEntityMobSpawner) event.getWorld().getTileEntity(event.getPos());
        if(tileEntityMobSpawner == null) return;
        NBTTagCompound nbtTagCompound = tileEntityMobSpawner.getUpdateTag();
        ResourceLocation resourceLocation = new ResourceLocation(nbtTagCompound.getCompoundTag("SpawnData").getString("id"));
        System.out.println(spawnerProgressions);
        if(!spawnerProgressions.containsKey(resourceLocation)) return;
        if(mobKills < spawnerProgressions.get(resourceLocation)) {
            entityPlayer.sendStatusMessage(new TextComponentString("§cYou need to kill " + spawnerProgressions.get(resourceLocation) + " mobs to break this spawner."), true);
            event.setCanceled(true);
        }
    }

}
