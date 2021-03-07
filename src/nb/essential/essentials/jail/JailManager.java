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

package nb.essential.essentials.jail;

import nb.essential.main.NbEssential;
import nb.essential.player.NbPlayer;
import org.bukkit.Location;

import java.util.ArrayList;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 25/09/2016
 */
public class JailManager {

    private ArrayList<NbPlayer> jailedPlayer;

    public static JailData getData() {
        return NbEssential.getDataLoader().getData("jails");
    }

    public static Jail getJail(String name) {
        return getData().get(name);
    }

    public static boolean isJailExists(String name) {
        return getData().get(name) != null;
    }

    public boolean createJail(String name, Location location) {
        if (isJailExists(name))
            return false;

        getData().add(new Jail(name, location));
        return true;
    }
}
