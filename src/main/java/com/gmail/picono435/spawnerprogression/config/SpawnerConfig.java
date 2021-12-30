package com.gmail.picono435.spawnerprogression.config;

import com.gmail.picono435.spawnerprogression.SpawnerProgression;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;

public class SpawnerConfig {

    private static YamlConfigurationLoader loader;
    private static ConfigurationNode root;

    public static void init() throws ConfigurateException {
        System.out.println(new File("config/spawnerprogression.yml").toPath().toAbsolutePath());
        loader = YamlConfigurationLoader.builder()
                .indent(2)
                .path(new File("config/spawnerprogression.yml").toPath().toAbsolutePath())
                .build();
        root = loader.load();
        if(root.node("spawners").virtual()) {
            root.node("spawners").node("minecraft:skeleton").set(150);
            root.node("spawners").node("minecraft:zombie").set(100);
            root.node("spawners").node("minecraft:spider").set(250);
            root.node("spawners").node("minecraft:cave_spider").set(300);
            root.node("spawners").node("minecraft:enderman").set(500);
            root.node("spawners").node("minecraft:silverfish").set(800);
            root.node("spawners").node("minecraft:husk").set(120);
            loader.save(root);
        }
    }

    public static void retrieveSpawnerProgressions() {
        SpawnerProgression.spawnerProgressions.clear();
        System.out.println("Retrieving spawners from config...");
        for(Object spawnerObject : root.node("spawners").childrenMap().keySet()) {
            String spawner = (String) spawnerObject;
            int hostileMobs = root.node("spawners").node(spawner).getInt();
            ResourceLocation resourceLocation = new ResourceLocation(spawner.split(":")[0], spawner.split(":")[1]);
            SpawnerProgression.spawnerProgressions.put(resourceLocation, hostileMobs);
            System.out.println("Retrieved spawner " + resourceLocation.toString() + " | " + hostileMobs);
        }
    }

}
