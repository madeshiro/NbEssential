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
import nb.essential.main.NbEssential;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.List;
import static nb.essential.blockmeta.MetaManager.*;

/**
 * Class of NbEssential
 */
public class Chairs implements StickAction {

    private Block block;
    private Arrow stand;
    private Player player;

    private OpStick stick;

    Chairs(OpStick stick) {
        this . stick = stick;
    }

    public Chairs(Block block, Player player) {
        this . block = block;
        this . player = player;
    }

    public void sit() {
        List<org.bukkit.metadata.MetadataValue> metadataValues = block.getMetadata("chairs");
        if (metadataValues == null || metadataValues.isEmpty())
            return;

        Location location = block.getLocation();
        location = new Location(location.getWorld(), location.getX()+0.5, location.getY(), location.getZ()+0.5);

        stand = block.getWorld().spawn(location, Arrow.class);
        stand.setVelocity(new Vector(0,0,0));
        stand.addPassenger(player);
    }

    public void standup() {
        stand.remove();
        stand = null;

    }

    private ChairsMetadataValue getMeta(List<org.bukkit.metadata.MetadataValue> list) {
        for (org.bukkit.metadata.MetadataValue value : list) {
            if (value.getOwningPlugin().equals(NbEssential.getPlugin()))
                return (ChairsMetadataValue) value;
        }

        return null;
    }

    @Override
    public boolean onStickLeftClickAir(PlayerInteractEvent event) {
        return true; // nothing to do here
    }

    @Override
    public boolean onStickLeftClickBlock(PlayerInteractEvent event) {
        if (Nb_BlockHasMeta(event.getClickedBlock(), "chairs")) {
            Nb_BlockRemoveMeta(event.getClickedBlock(), "chairs");
            event.getPlayer().sendMessage(stick.ctl("s:c:stick:chairs_remove_meta"));
        }

        event.setCancelled(true);
        return true;
    }

    @Override
    public boolean onStickRightClickAir(PlayerInteractEvent event) {
        return true; // nothing to do here
    }

    @Override
    public boolean onStickRightClickBlock(PlayerInteractEvent event) {
        if (Nb_BlockHasMeta(event.getClickedBlock(), "chairs"))
            event.getPlayer().sendMessage(stick.ctl("s:c:stick:chairs_already_set"));
        else {
            Nb_BlockSetMeta(event.getClickedBlock(), "chairs", new ChairsMetadataValue(true));
            event.getPlayer().sendMessage(stick.ctl("s:c:stick:chairs_set"));
        }
        return true;
    }

    @Override
    public boolean onStickRightClickOnEntity(PlayerInteractAtEntityEvent event) {
        return true; // nothing to do here
    }

    public Arrow getStand() {
        return stand;
    }

    public static class ChairsMetadataValue extends MetadataValue {
        public ChairsMetadataValue(boolean empty) {
            super(empty, true);
        }

        @Override
        protected Object saveValue() {
            return asBoolean();
        }
    }
}
