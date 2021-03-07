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

package zaesora.madeshiro.protocollib.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import zaesora.madeshiro.protocollib.dispatch.ShipmentContent;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 29/11/2016
 */
public final class DispatchPacketEvent extends Event implements Cancellable {

    private ShipmentContent content;
    private boolean isCancelled = false;
    private static HandlerList handlerList = new HandlerList();

    public DispatchPacketEvent(ShipmentContent content) {
        this . content = content;
    }

    public HashSet<Player> getTargets() {
        return content.getTargets();
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public boolean removePlayer(Player p) {
        return content.getTargets().remove(p);
    }

    public boolean addPlayers(Player... players) {
        return content.getTargets().addAll(Arrays.asList(players));
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }
}
