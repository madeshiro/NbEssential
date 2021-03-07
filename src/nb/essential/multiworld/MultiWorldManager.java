/*
 * This file is a part of the NbEssential plugin, licensed under the GPL v3 License (GPL)
 * Copyright © 2015-2018 MađeShirő ƵÆsora <https://github.com/madeshiro>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package nb.essential.multiworld;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.Random;
import static nb.essential.main.NbEssential.getDataLoader;

public class MultiWorldManager {

    public static World Nb_MW_CreateWorld(String name, World.Environment environment) {
        World world;
        if ((world = Bukkit.getWorld(name)) != null) {
            Nb_MW_GetData().logwarning("World '" + name + "' already exists !");
        } else if ((world = new WorldCreator(name).seed(new Random().nextLong()).environment(environment).createWorld()) == null) {
            Nb_MW_GetData().logsevere("Unable to create world '" + name + "'");
        } else {
            Nb_MW_GetData().addWorldTrait(new WorldTrait(world));
            Nb_MW_GetData().loginfo("World '" +name + "' has been successfuly created !");
        }

        return world;
    }

    public static World Nb_MW_LoadWorld(String world) {
        World bukkitWorld = null;
        if (!Nb_MW_GetData().isWorldExists(world)) {
            Nb_MW_GetData().logsevere("Unable to load world '" + world + "' (non-existing)");
        } else if ((bukkitWorld = Bukkit.getWorld(world)) != null) {
            bukkitWorld = new WorldCreator(world).createWorld();
            Nb_MW_GetData().loginfo("Load world '" + world + "' !");
        } else {
            Nb_MW_GetData().logwarning("World '" + world + "' is already loaded !");
        }
        return bukkitWorld;
    }

    public static boolean Nb_MW_IsWorldExist(String world) {
        return Nb_MW_GetData().isWorldExists(world);
    }

    public static void Nb_MW_UnloadWorld(String world) {
        World bukkitWorld = Bukkit.getWorld(world);
        if (bukkitWorld == null)
            Nb_MW_GetData().logwarning("World '" + world + "' doesn't exists ! Unable to unload it...");
        else
            Nb_MW_UnloadWorld(bukkitWorld);
    }

    public static void Nb_MW_UnloadWorld(World world) {
        if (Bukkit.unloadWorld(world, true)) {
            Nb_MW_GetData().loginfo("Unload world '" + world.getName() + "' !");
        }
    }

    static MultiWorldData Nb_MW_GetData() {
        return getDataLoader().getData("multiworld");
    }

    static WorldTrait Nb_MW_BuildDefaultTrait(World world) {
        return new WorldTrait(world);
    }

    public static WorldTrait Nb_MW_GetWorldTrait(World world) {
        return Nb_MW_GetData().getWorldTrait(world);
    }
}
