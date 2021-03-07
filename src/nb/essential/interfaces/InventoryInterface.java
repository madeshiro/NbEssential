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

package nb.essential.interfaces;

import nb.essential.player.CraftNbPlayer;
import nb.essential.player.NbPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 29/09/2016
 */
public abstract class InventoryInterface extends UserInterface {

    protected Inventory inventory;
    private HashMap<Integer, InterfaceAction> actions;

    public InventoryInterface(NbPlayer player, Inventory inventory) {
        super(player);
        this . inventory = inventory;
        actions = new HashMap<>();
    }

    @Override
    public void displayInterface() {
        this.user.openInventory(inventory);
        ((CraftNbPlayer) user).getVariables().setOpenedInterface(this);
    }

    public void doAction(int slot, ItemStack stack) {
        InterfaceAction action;
        if ((action = actions.get(slot)) != null && inventory.getItem(slot).equals(stack))
            action.doAction(this, user);
    }

    protected void setAction(int slot, InterfaceAction action) {
        actions.put(slot, action);
    }

    @Override
    public String getName() {
        return inventory.getName();
    }

    protected final ItemStack createItem(Material material, String... lores) {
        return createItem(material, 1, lores);
    }

    @SuppressWarnings("SameParameterValue")
    protected final ItemStack createItem(Material material, int amount, String... lores) {
        ItemStack stack = new ItemStack(material, amount);

        ItemMeta meta = stack.getItemMeta();
        meta.setLore(Arrays.asList(lores));
        stack.setItemMeta(meta);

        return stack;
    }

    @SuppressWarnings("SameParameterValue")
    protected final ItemStack createItem(Material material, String name, int amount, String... lores) {
        ItemStack stack = new ItemStack(material, amount);

        ItemMeta meta = stack.getItemMeta();

        meta.setLore(Arrays.asList(lores));
        meta.setDisplayName(name);

        stack.setItemMeta(meta);

        return stack;
    }
}
