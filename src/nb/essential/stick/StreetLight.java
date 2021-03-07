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

package nb.essential.stick;

import nb.essential.blockmeta.MetadataValue;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static nb.essential.blockmeta.MetaManager.*;

/**
 * Class of NbEssential
 */
public class StreetLight implements StickAction {

    private OpStick stick;

    StreetLight(OpStick stick) {
        this . stick = stick;
    }

    @Override
    public boolean onStickLeftClickAir(PlayerInteractEvent event) {
        return true;
    }

    @Override
    public boolean onStickLeftClickBlock(PlayerInteractEvent event) {
        if (!(event.getClickedBlock().getType() == Material.REDSTONE_LAMP)) {
            return true;
        }

        if (Nb_BlockHasMeta(event.getClickedBlock(), "streetlight")) {
            Nb_BlockRemoveMeta(event.getClickedBlock(), "streetlight");
            event.getPlayer().sendMessage(stick.ctl("s:c:stick:streetlight_remove_meta"));
            event.getClickedBlock().setType(Material.REDSTONE_LAMP);
        }

        event.setCancelled(true);
        return true;
    }

    @Override
    public boolean onStickRightClickAir(PlayerInteractEvent event) {
        return true;
    }

    @Override
    public boolean onStickRightClickBlock(PlayerInteractEvent event) {
        if (!(event.getClickedBlock().getType() == Material.REDSTONE_LAMP))
            return true;

        if (Nb_BlockHasMeta(event.getClickedBlock(), "streetlight")) {
            MetadataValue value = Nb_BlockGetMeta(event.getClickedBlock(), "streetlight");
            LightFunction function = LightFunction.valueOf(value.asString());
            int ordinal = function.ordinal();
            ordinal = (ordinal == LightFunction.values().length -1) ? 0 : ordinal+1;
            function = LightFunction.values()[ordinal];
            Nb_BlockSetMeta(event.getClickedBlock(), "streetlight", new MetadataValue(function.toString(), true));
            updateRedstoneLamp(event.getClickedBlock());
            event.getPlayer().sendMessage(stick.ctl("s:c:stick:streetlight_set_meta").replace("%value%", function.toString()));
        } else {
            Nb_BlockSetMeta(event.getClickedBlock(), "streetlight", new MetadataValue("always", true));
            event.getPlayer().sendMessage(stick.ctl("s:c:stick:streetlight_set_meta").replace("%value%", "always"));
            updateRedstoneLamp(event.getClickedBlock());
        }
        return true;
    }

    private void updateRedstoneLamp(Block clickedBlock) {
        clickedBlock.setType(Material.REDSTONE_LAMP, false);

        Location loc = clickedBlock.getLocation(); loc.setY(loc.getY()+1);
        Block block = loc.getBlock();
        Material material = block.getType();
        block.setType(Material.REDSTONE_BLOCK);
        block.setType(material);
    }

    @Override
    public boolean onStickRightClickOnEntity(PlayerInteractAtEntityEvent event) {
        return false;
    }

    public enum LightFunction {
        always,

        day_only,
        night_only,

        rain_only,
        clear_only,
        thunder_only,
    }
}
