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
package nb.essential.listeners;

import nb.essential.event.PluginShutdownEvent;
import nb.essential.loader.EventListener;
import nb.essential.player.NbPlayer;
import nb.essential.player.OfflineNbPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static nb.essential.main.NbEssential.getLogger;
import static nb.essential.main.NbEssential.isPluginDebug;
import static nb.essential.player.PlayerSystem.Nb_GetOfflinePlayers;
import static nb.essential.player.PlayerSystem.Nb_GetPlayers;
import static zaesora.madeshiro.api.JSONAPI.Nb_JAPI_Stop;

/**
 * Class of NbEssential
 */
@EventListener
public class PluginShutdownListener implements Listener {

    @EventHandler
    public void onPluginShutdown(PluginShutdownEvent event) {
        for (OfflineNbPlayer offline : Nb_GetOfflinePlayers())
            if (!offline.saveData() && isPluginDebug())
                getLogger().warning("Unable to save offline player <" + offline.getName() + ":"+ offline.getComposedNickname() + ">");
        for (NbPlayer player : Nb_GetPlayers())
            player.saveData();

        Nb_JAPI_Stop();
    }
}
