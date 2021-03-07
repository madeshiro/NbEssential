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

import nb.essential.files.JSONFile;
import nb.essential.loader.Loader;
import nb.essential.loader.LoaderPriority;
import nb.essential.loader.PluginData;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import zaesora.madeshiro.parser.json.JSONObject;

import java.io.File;
import java.util.HashMap;

@Loader(value = "multiworld", priority = LoaderPriority.Low)
public class MultiWorldData implements PluginData {

    private JSONFile mw_file;
    private HashMap<String, WorldTrait> worldTraits;

    @Override
    public String getName() {
        return "MultiWorld";
    }

    @Override
    public boolean save() {
        for (World world : Bukkit.getWorlds()) {
            mw_file.set(getWorldTrait(world).getData(), "worlds", world.getName());
            loginfo("Save world '" + world.getName() + "' !");
        }
        return mw_file.save();
    }

    @Override
    public boolean load() {
        File file = PluginData.checkrcfile("multiworld.json");
        if (file == null) return false;
        mw_file = new JSONFile(file);
        worldTraits = new HashMap<>();

        if (mw_file.load()) {
            for (World world : Bukkit.getWorlds()) {
                if (mw_file.getContainer().<JSONObject>getObject("worlds").containsKey(world.getName())) {
                    worldTraits.put(world.getName(), new WorldTrait(world.getName(), mw_file.get("worlds", world.getName())));
                } else {
                    worldTraits.put(world.getName(), new WorldTrait(world));
                    mw_file.set(worldTraits.get(world.getName()).getData(), "worlds", world.getName());
                    mw_file.save();
                }
                loginfo("World '" + world.getName() + "' has been loaded (main-world) !");
            }
            for (String key : mw_file.getContainer().<JSONObject>getObject("worlds").keySet()) {
                if (Bukkit.getWorld(key) != null) continue;
                if (new WorldCreator(key).createWorld() != null) {
                    worldTraits.put(key, new WorldTrait(key, mw_file.get("worlds", key)));
                    loginfo("World '" + key + "' has been loaded !");
                } else
                    logsevere("Unable to load world '" + key + "'...");
            }
            return true;
        } else {
            logsevere("An error occured when loading <multiworld.json> data file !");
            return false;
        }
    }

    @Override
    public boolean reload() {
        return mw_file.reload();
    }

    public WorldTrait getWorldTrait(World world) {
        return worldTraits.get(world.getName());
    }

    void addWorldTrait(WorldTrait trait) {
        worldTraits.put(trait.getName(), trait);
    }

    public boolean isWorldExists(String world) {
        return worldTraits.containsKey(world);
    }
}
