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
import nb.essential.zone.Rules;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

import static nb.essential.zone.ZoneManager.Nb_ZN_GetZone;

/**
 * Class of NbEssential
 */
@EventListener
public class MobsEventListener implements Listener {

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        if (ignoreGriefPrevention(event)) return;

        if (!Nb_ZN_GetZone(event.getLocation()).getRules().rule(Rules.Rule.MobsSpawn))
            event.setCancelled(true);
    }

    @EventHandler
    public void onMobChangeBlock(EntityChangeBlockEvent event) {
        if (ignoreGriefPrevention(event)) return;

        if (!Nb_ZN_GetZone(event.getBlock().getLocation()).getRules().rule(Rules.Rule.MobsGrief))
            event.setCancelled(true);
    }

    @EventHandler
    public void onMobBreakDoor(EntityBreakDoorEvent event) {
        if (ignoreGriefPrevention(event)) return;

        if (!Nb_ZN_GetZone(event.getBlock().getLocation()).getRules().rule(Rules.Rule.MobsGrief))
            event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (ignoreGriefPrevention(event)) return;

        if (!Nb_ZN_GetZone(event.getLocation()).getRules().rule(Rules.Rule.MobsGrief))
            event.setCancelled(true);
    }

    private boolean ignoreGriefPrevention(EntityEvent event) {
        switch (event.getEntityType()) {
            case FALLING_BLOCK: case DROPPED_ITEM: case PLAYER:
                return true;
            default: return false;
        }
    }
}

