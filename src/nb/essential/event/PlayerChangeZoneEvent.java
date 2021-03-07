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

package nb.essential.event;


import nb.essential.player.NbPlayer;
import nb.essential.zone.Zone;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Class of NbEssential
 */
public class PlayerChangeZoneEvent extends Event implements Cancellable {

    private boolean cancelled;
    private final Zone from, to;
    private final NbPlayer player;
    private final static HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public PlayerChangeZoneEvent(NbPlayer player, Zone lastZone, Zone newZone) {
        this . player = player;
        this . from = lastZone;
        this . to = newZone;
        this . cancelled = false;
    }

    public Zone getFrom() {
        return from;
    }

    public Zone getTo() {
        return to;
    }

    public NbPlayer getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this . cancelled = b;
    }
}