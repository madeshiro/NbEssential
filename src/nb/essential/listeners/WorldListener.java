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
import nb.essential.player.NbPlayer;
import nb.essential.player.PlayerSystem;
import nb.essential.zone.ZoneType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.WorldLoadEvent;

import static nb.essential.blockmeta.MetaManager.Nb_BlockHasMeta;
import static nb.essential.main.NbEssential.getLogger;
import static nb.essential.main.NbEssential.isPluginDebug;
import static nb.essential.zone.Rules.Rule.*;
import static nb.essential.zone.ZoneManager.*;

/**
 * Class of NbEssential
 */
@EventListener
public class WorldListener implements Listener {

    @EventHandler
    public void RedstoneLampUpdate(BlockRedstoneEvent event) {
        if (event.getBlock().getType() == Material.REDSTONE_LAMP &&
                Nb_BlockHasMeta(event.getBlock(), "streetlight"))
            event.setNewCurrent(15);
    }

    @EventHandler
    public void onBlockGrowEvent(BlockGrowEvent event) {
        if (!Nb_ZN_GetZone(event.getBlock().getLocation()).getRules().rule(NaturePlantsGrow))
            event.setCancelled(true);
    }

    @EventHandler
    public void onStructureGrowEvent(StructureGrowEvent event) {
        if (!Nb_ZN_GetZone(event.getLocation()).getRules().rule(NaturePlantsGrow) && !event.isFromBonemeal())
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause().equals(BlockIgniteEvent.IgniteCause.SPREAD) &&
                !Nb_ZN_GetZone(event.getIgnitingBlock().getLocation()).getRules().rule(NatureFirePropagation))
            event.setCancelled(true);
    }

    @EventHandler
    public void onTntDamage(ExplosionPrimeEvent event) {
        if (event.getEntityType() == EntityType.PRIMED_TNT &&
                !Nb_ZN_GetZone(event.getEntity().getLocation()).getRules().rule(TNT_BlockDamage))
            event.setCancelled(true);
    }


    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        NbPlayer player = PlayerSystem.Nb_GetPlayer(event.getPlayer());
        if (player == null || !Nb_ZN_GetZone(event.getBlock().getLocation()).getRules().rule(PlayerBreakBlock, player))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInterract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }

        switch (event.getAction()) {
            case PHYSICAL: case LEFT_CLICK_BLOCK: case LEFT_CLICK_AIR:
                return;
            default:
                NbPlayer player = PlayerSystem.Nb_GetPlayer(event.getPlayer());
                if (player == null || !Nb_ZN_GetZone(event.getClickedBlock().getLocation()).getRules().rule(PlayerInterract, player))
                    event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        if (!Nb_ZN_IsZoneExists(event.getWorld().getName(), ZoneType.Realm)) {
            if (Nb_ZN_CreateZone(event.getWorld().getName(), ZoneType.Realm) != null && isPluginDebug())
                getLogger().info("[ZoneManager] Successfully create realm <" + event.getWorld().getName() + ">");
        }
    }
}
