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

import nb.essential.blockmeta.MetaManager;
import nb.essential.blockmeta.MetadataValue;
import nb.essential.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import zaesora.madeshiro.parser.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static nb.essential.blockmeta.MetaManager.*;
import static nb.essential.utils.Utils.hideString;
import static nb.essential.utils.Utils.isLockdoorSupported;
import static nb.essential.utils.Utils.revealString;

public class LockDoor implements StickAction {

    private OpStick stick;
    private static String generateID(Location location) {
        return "x:" + location.getBlockX() + "y:" + location.getBlockY() + "z:" + location.getBlockZ() + "w:" + location.getWorld().getName();
    }

    public static ItemStack Nb_LD_CreateKey(Block door, String keyName, List<String> lore) {
        if (!isLockdoorSupported(door) || !Nb_BlockHasMeta(door, "lockdoor"))
            return null;

        return Nb_LD_CreateKey(MetaManager.<LockMetadataValue>Nb_BlockGetMeta(door, "lockdoor").getID(), keyName, lore);
    }

    public static boolean Nb_LD_IsKeyFor(Block door, ItemStack key) {
        if (!Nb_BlockHasMeta(door, "lockdoor") || !key.getType().equals(Material.DRAGON_EGG))
            return false;

        String keyLock = Nb_LD_GetKeyLock(key);
        return keyLock != null && MetaManager.<LockMetadataValue>Nb_BlockGetMeta(door,"lockdoor").getID().equals(keyLock);
    }

    public static ItemStack Nb_LD_CreateKey(String id, String keyName, List<String> lore) {
        ItemStack stack = new ItemStack(Material.DRAGON_EGG, 1);
        ItemMeta meta = stack.getItemMeta();
        if (lore == null) lore = new ArrayList<>();
        lore.add(hideString(id));

        meta.setLore(lore);
        meta.setDisplayName(keyName);
        stack.setItemMeta(meta);

        return stack;
    }

    public static String Nb_LD_GetKeyLock(ItemStack item) {
        int loreSize = item.getItemMeta().getLore().size();
        return loreSize > 0 ? revealString(item.getItemMeta().getLore().get(loreSize-1)) : null;
    }

    public static void Nb_LD_DefineLockDoor(Block block, boolean lock) {
        if (!isLockdoorSupported(block))
            return;

        String id = Nb_BlockHasMeta(block, "lockdoor") ?
                MetaManager.<LockMetadataValue>Nb_BlockGetMeta(block, "lockdoor").getID() :
                generateID(block.getLocation());
        Nb_BlockSetMeta(block, "lockdoor", LockMetadataValue.a(lock, id));
        if (Utils.isDoor(block)) {
            for (int i = -1; i <= 1; i+=2) {
                Location location = block.getLocation();
                Block _block = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY()+i, location.getBlockZ());
                if (Utils.isDoor(_block)) {
                    Nb_BlockSetMeta(_block, "lockdoor", LockMetadataValue.a(lock, id));
                    break;
                }
            }
        }

    }

    public static void Nb_LD_RemoveLockDoor(Block block) {
        if (!isLockdoorSupported(block))
            return;

        Nb_BlockRemoveMeta(block, "lockdoor");
        if (Utils.isDoor(block)) {
            for (int i = -1; i <= 1; i+=2) {
                Location location = block.getLocation();
                Block _block = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY()+i, location.getBlockZ());
                if (Utils.isDoor(_block)) {
                    Nb_BlockRemoveMeta(_block, "lockdoor");
                    break;
                }
            }
        }
    }

    public static boolean Nb_LD_IsDoorLock(Block block) {
        return Nb_BlockHasMeta(block,"lockdoor") && MetaManager.<LockMetadataValue>Nb_BlockGetMeta(block, "lockdoor").isLock();
    }

    public LockDoor(OpStick opStick) {
        this.stick = opStick;
    }

    @Override
    public boolean onStickLeftClickAir(PlayerInteractEvent event) {
        return true;
    }

    @Override
    public boolean onStickLeftClickBlock(PlayerInteractEvent event) {
        if (!Utils.isLockdoorSupported(event.getClickedBlock()))
            return false;

        if (Nb_BlockHasMeta(event.getClickedBlock(), "lockdoor")) {
            Nb_LD_RemoveLockDoor(event.getClickedBlock());
            event.getPlayer().sendMessage(stick.ctl("s:c:stick:lockdoor_remove_meta"));
        }

        return true;
    }

    @Override
    public boolean onStickRightClickAir(PlayerInteractEvent event) {
        return true;
    }

    @Override
    public boolean onStickRightClickBlock(PlayerInteractEvent event) {
        if (!Utils.isLockdoorSupported(event.getClickedBlock()))
            return false;

        boolean lock = true;
        Location location = event.getClickedBlock().getLocation();
        String id = generateID(location);

        if (Nb_BlockHasMeta(event.getClickedBlock(), "lockdoor")) {
            lock = !((LockMetadataValue) Nb_BlockGetMeta(event.getClickedBlock(), "lockdoor")).isLock();
        } else {
            event.getPlayer().getInventory().addItem(Nb_LD_CreateKey(generateID(event.getClickedBlock().getLocation()), "A Key", null));
        }

        Nb_LD_DefineLockDoor(event.getClickedBlock(), lock);
        event.getPlayer().sendMessage(stick.ctl("s:c:stick:lockdoor_define") + "§7[§c" + Boolean.toString(lock) + "§7]");
        return true;
    }

    @Override
    public boolean onStickRightClickOnEntity(PlayerInteractAtEntityEvent event) {
        return true;
    }

    public static class LockMetadataValue extends MetadataValue {

        public static LockMetadataValue a(boolean value, String id) {
            JSONObject object = new JSONObject();
            object.put("lock", value);
            object.put("id", id);

            return new LockMetadataValue(object);
        }

        public LockMetadataValue(JSONObject object) {
            super(object);
        }

        @Override
        public JSONObject value() {
            return (JSONObject) super.value();
        }

        public boolean isLock() {
            return value().getObject("lock");
        }

        public String getID() {
            return value().getObject("id");
        }
    }
}
