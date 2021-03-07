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

import org.bukkit.Location;

import java.util.ArrayList;

import static nb.essential.main.NbEssential.getDataLoader;

/**
 * Class of NbEssential in package nb.essential.essentials.warp
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 */
public class WarpManager {

    private static WarpData getData() {
        return getDataLoader().getData("warps");
    }

    public static Warp Nb_WP_GetWarp(String name) {
        return getData().getWarp(name);
    }

    public static Warp Nb_WP_CreateWarp(String name, Location location) {
        if (getData().getWarp(name) != null)
            return null;
        Warp warp = new Warp(name, location);
        getData().warps.put(name, warp);

        return warp;
    }

    public static boolean Nb_WP_RemoveWarp(String name) {
        return getData().warps.remove(name) != null;
    }

    public static ArrayList<String> Nb_WP_GetWarpsList() {
        return new ArrayList<>(getData().warps.keySet());
    }
}
