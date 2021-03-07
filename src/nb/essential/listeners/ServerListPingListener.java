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

import nb.essential.loader.EventListener;
import nb.essential.utils.Utils;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

import static nb.essential.main.NbEssential.getServer;

/**
 * Class of NbEssential created by maᴅeliƒe Æský on 06/12/2016
 */
@EventListener
public class ServerListPingListener implements Listener {

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        event.setMotd(Utils.colorString(getServer().getMOTD()));
    }
}
