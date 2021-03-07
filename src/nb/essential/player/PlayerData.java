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
package nb.essential.player;

import nb.essential.loader.Loader;
import nb.essential.loader.LoaderPriority;
import nb.essential.loader.PluginData;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.UUID;
import java.util.HashMap;


/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 31/08/2016
 */
@Loader(value = "player", priority = LoaderPriority.Low)
// FIXME YAML/JSON
public class PlayerData implements PluginData, Iterable<PlayerProfile> {

    /**
     * The players profiles.
     */
    private HashMap<UUID, PlayerProfile> profiles;

    /**
     * Creates a new PlayerData.
     */

    @Override
    public boolean save() {
        boolean status = true;

        for (PlayerProfile profile : profiles.values())
            status = profile.save() && status;

        return status;
    }

    @Override
    public boolean load() {
        profiles = new HashMap<>();
        return true;
    }

    @Override
    public boolean reload() {
        boolean status = true;

        for (PlayerProfile profile : profiles.values())
            status = profile.reload() && status;

        return status;
    }

    protected void addPlayer(CraftNbPlayer player) {
        profiles.put(player.getUniqueId(), player.getProfile());
    }

    public NbPlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

    protected NbPlayer getPlayer(UUID uuid) {
        PlayerProfile profile = profiles.get(uuid);
        return profile != null ? profile.getPlayer() : null;
    }

    public void removePlayer(Player player) {
        profiles.remove(player.getUniqueId());
    }

    PlayerProfile getProfile(UUID playerUniqueId) {
        return profiles.get(playerUniqueId);
    }

    @Override
    public Iterator<PlayerProfile> iterator() {
        return profiles.values().iterator();
    }
}
