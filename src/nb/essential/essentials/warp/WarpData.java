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

package nb.essential.essentials.warp;

import nb.essential.files.JSONFile;
import nb.essential.files.ResourceFile;
import nb.essential.loader.Loader;
import nb.essential.loader.PluginData;
import nb.essential.utils.Utils;
import org.bukkit.Location;
import zaesora.madeshiro.parser.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 16/10/2016
 */
@Loader("warps")
public class WarpData implements PluginData {

    private JSONFile warpFile;
    HashMap<String, Warp> warps = new HashMap<>();

    @Override
    public boolean save() {
        warpFile.<JSONObject>get("warps").clear();
        for (Warp warp : warps.values())
            warpFile.set(Utils.jsonFromLoc(warp.getLocation()), "warps", warp.getName());

        return warpFile.save();
    }

    @Override
    public boolean load() {
        File file = new File("plugins/NbEssential/warps.json");
        if (!file.exists() &&
                !ResourceFile.copyFromResource("warps.json", "plugins/NbEssential/warps.json"))
            return false;

        if ((warpFile = new JSONFile(file)).load()) {
            JSONObject obj = warpFile.get("warps");
            for (String key : obj.keySet()) {
// TODO FIX NOT DEFINE WORLD (report)
                Location location = Utils.locFromJSON(obj.getObject(key));
                if (location != null)
                    warps.put(key, new Warp(key, location));
            }
        }

        return true;
    }

    @Override
    public boolean reload() {
        return load();
    }

    public Warp getWarp(String name) {
        return warps.get(name);
    }
}
