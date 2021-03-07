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

import org.bukkit.*;
import nb.essential.utils.Utils;
import zaesora.madeshiro.parser.json.JSONObject;

public class WorldTrait {

    private JSONObject data;
    private final World world;

    JSONObject getData() {
        return data;
    }

    WorldTrait(String name, JSONObject data) {
        this.data = data;
        this.world = Bukkit.getWorld(name);
    }

    WorldTrait(World world) {
        this.world = world;
        this.data = new JSONObject();

        data.put("spawn", Utils.jsonFromLoc(world.getSpawnLocation()));
        data.put("default-gamemode", "CREATIVE");
        data.put("force-gamemmode", true);
        data.put("keep-load", true);
        data.put("difficulty", "EASY");

        JSONObject descriptor = new JSONObject();
        descriptor.put("environment", world.getEnvironment().toString());
        descriptor.put("seed", world.getSeed());
        descriptor.put("generator", null);
        descriptor.put("type", world.getWorldType().getName());

        data.put("descriptor", descriptor);
    }

    public String getName() {
        return world.getName();
    }

    public World getWorld() {
        return world;
    }

    public World.Environment getEnvironment() {
        return World.Environment.valueOf(data.getObject("descriptor", "environment"));
    }

    public long getSeed() {
        return data.getObject("descriptor", "seed");
    }

    public GameMode getDefaultGamemode() {
        return GameMode.valueOf(data.getObject("default-gamemode"));
    }

    public boolean keepLoad() {
        return data.getObject("keep-load");
    }

    public Difficulty getDifficulty() {
        return Difficulty.valueOf(data.getObject("difficulty"));
    }

    public boolean forceGamemode() {
        return data.getObject("force-gamemmode");
    }

    public Location getSpawn() {
        return Utils.locFromJSON(data.getObject("spawn"));
    }

    public WorldType getWorldType() {
        return WorldType.valueOf(data.getObject("descriptor", "type"));
    }
}
