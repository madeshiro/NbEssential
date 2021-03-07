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

package nb.essential.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Arrays;
import java.util.Base64;

/**
 * Class of NbEssential created by maᴅeliƒe Æský on 11/12/2016
 */
@SuppressWarnings("deprecation")
public class InventoryUtil {

    public static String toStrB64(Inventory inventory) {

        int index = 0;
        String str = "";
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack == null)
                continue;

            if (index != 0)
                str += ";"; else index++;

            str += itemToString(stack) + ",s:" + i;
        }

        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    public static Inventory toInventory(String encodeStr) {
        String decode = new String(Base64.getDecoder().decode(encodeStr));
        String[] var1 = decode.split(";");

        Inventory inventory = Bukkit.getServer().createInventory(null, InventoryType.PLAYER);

        for (String item : var1) {
            ItemStackStruct stackStruct = strToItem(item);
            inventory.setItem(stackStruct.slot, stackStruct.stack);
        }

        return inventory;
    }


    private static String itemToString(ItemStack stack) {

        String str = "t:".concat(stack.getType().toString() + ":" + stack.getData().getData());

        if (stack.getDurability() != 0)
            str += ",d:".concat(String.valueOf(stack.getDurability()));
        if (stack.getEnchantments().size() > 0) {
            int index = 0;
            str += ",e:";
            for (Enchantment enchantment : stack.getEnchantments().keySet()) {
                if (index == 0) {
                    str += (enchantment.getName() + "@")
                            .concat(String.valueOf(stack.getEnchantmentLevel(enchantment)));
                    index++;
                } else
                    str += "-".concat(enchantment.getName() + "@")
                            .concat(String.valueOf(stack.getEnchantmentLevel(enchantment)));
            }
        }
        if (stack.getAmount() > 1)
            str += ",a:" + stack.getAmount();
        if (stack.hasItemMeta()) {

            if (stack.getItemMeta().hasLore()) {
                str += ",l:";

                int index = 0;
                for (String lore : stack.getItemMeta().getLore()) {
                    if (index == 0)
                        index++;
                    else
                        str += "/#/";

                    str += lore;
                }
            }

            if (stack.getItemMeta().getItemFlags().size() > 0) {
                str += ",f:";

                int index = 0;
                for (ItemFlag flag : stack.getItemMeta().getItemFlags()) {
                    if (index == 0)
                        index++;
                    else
                        str += "-";

                    str += flag.toString();
                }
            }
        }

        return str;
    }

    private static class ItemStackStruct {

        ItemStackStruct(ItemStack stack, int slot) {
            this . slot = slot;
            this . stack = stack;
        }

        public int slot;
        public ItemStack stack;
    }

    private static ItemStackStruct strToItem(String decodeStr) {

        int slot = 0;
        ItemStack stack = new ItemStack(Material.STICK); // per dafault
        String[] split = decodeStr.split(",");

        for (String data : split) {
            String identifier = data.substring(0,2);
            String others = data.substring(2);

            switch (identifier) {
                case "t:":
                    String[] var1;
                    if (others.contains(":"))
                        var1 = others.split(":");
                    else var1 = new String[]{others};
                    stack = new ItemStack(Material.getMaterial(var1[0]));
                    if (var1.length > 1)
                        stack.setData(new MaterialData(stack.getType(), Byte.valueOf(var1[1])));
                    break;
                case "s:":
                    slot = Integer.valueOf(others);
                    break;
                case "e:":
                    var1 = others.split("-");
                    for (String enchant : var1) {
                        String[] var2 = enchant.split("@");
                        Enchantment enchantment = Enchantment.getByName(var2[0]);
                        int enchantLevel = Integer.parseInt(var2[1]);

                        stack.addEnchantment(enchantment, enchantLevel);
                    }
                    break;
                case "a:":
                    stack.setAmount(Integer.valueOf(others));
                    break;
                case "n:":
                    stack.getItemMeta().setDisplayName(others);
                    break;
                case "l:":
                    var1 = others.split("/#/");
                    if (var1.length > 0)
                        stack.getItemMeta().setLore(Arrays.asList(var1));
                    break;
                case "f:":
                    var1 = others.split("-");
                    for (String attribute : var1) {
                        ItemFlag flag = ItemFlag.valueOf(attribute.toUpperCase());
                        if (flag != null)
                            stack.getItemMeta().addItemFlags(flag);
                    }
                    break;
                case "d:":
                    stack.setDurability(Short.valueOf(others));
                    break;
            }
        }

        return new ItemStackStruct(stack, slot);
    }
}
