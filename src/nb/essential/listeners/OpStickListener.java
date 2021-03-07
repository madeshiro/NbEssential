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
import nb.essential.stick.OpStick;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static nb.essential.main.NbEssential.tl;
import static nb.essential.player.PlayerSystem.Nb_GetPlayer;

/**
 * Class of NbEssential
 */
@EventListener
public class OpStickListener implements Listener {

    public static boolean isOpStick(ItemStack stack) {
        return stack != null && stack.getType() == Material.STICK
                && stack.hasItemMeta()
                && stack.getItemMeta().getDisplayName().contains("OpStick");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        if (isOpStick(event.getItem())) {
            OpStick stick = new OpStick(event.getItem(), Nb_GetPlayer(event.getPlayer()), 0);

            if (!stick.doAction(event))
                event.getPlayer().sendMessage(tl("s:command_no_permission", event.getPlayer()));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClickOpStickInventory(InventoryClickEvent event) {

        if (isOpStick(event.getCurrentItem()) && event.isLeftClick()) {
            Player player = Bukkit.getPlayer(event.getWhoClicked().getName());
            if (player == null)
                return;
            NbPlayer whoClick = Nb_GetPlayer(player);
            if (whoClick == null)
                return;

            OpStick stick = new OpStick(event.getCurrentItem(), whoClick, event.getSlot());
            stick.openInterface(event.getCurrentItem());
            event.setCancelled(true);
        }
    }
}
