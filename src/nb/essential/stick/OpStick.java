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

import nb.essential.interfaces.InventoryInterface;
import nb.essential.loader.NbCommand;
import nb.essential.player.NbPlayer;
import nb.essential.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

import static nb.essential.main.NbEssential.tl;

/**
 * Class of NbEssential
 */
public class OpStick extends ItemStack {

    private class OpStickGUI extends InventoryInterface {

        public OpStickGUI() {
            super(player, Bukkit.createInventory(null, 9, "§6OpStick - Menu"));

            inventory.addItem(
                    createItem(Material.OAK_STAIRS, "§eChairs",1, "Switch to '§9Chairs§5' function"),
                    createItem(Material.COMMAND_BLOCK, "§eCB Replace", 1, "Switch to '§9CB Replace§5' function"),
                    createItem(Material.REDSTONE_LAMP, "§eStreetlight", 1, "Switch to '§9Streetlight§5' function"),
                    createItem(Material.DRAGON_EGG, "§eLockDoor", 1, "Switch to '§9LockDoor§5' function")
            );
            inventory.setItem(8, createItem(Material.BARRIER, "§cDelete", 1, "Remove the stick"));

            setAction(0, (userInterface, player1) -> {setFunction(OpStickFunction.CHAIRS); item.setItemMeta(getItemMeta());});
            setAction(1, (userInterface, player1) -> {setFunction(OpStickFunction.CB_REPLACE); item.setItemMeta(getItemMeta());});
            setAction(2, (userInterface, player1) -> {setFunction(OpStickFunction.STREETLIGHT); item.setItemMeta(getItemMeta());});
            setAction(3, (userInterface, player1) -> {setFunction(OpStickFunction.LOCKDOOR); item.setItemMeta(getItemMeta());});
            setAction(8, (userInterface, player1) -> {player1.getInventory().setItem(slot, null); player1.closeInventory();});
        }

        private ItemStack item;
        public void setItem(ItemStack item) {
            this.item = item;
        }
    }

    final OpStickGUI stickGui;
    final NbPlayer player;
    OpStickFunction stickFunction;

    public enum OpStickFunction {
        CHAIRS("s:c:stick:chairs", "nbessential.stick.chairs"),
        STREETLIGHT("s:c:stick:streetlight", "nbessential.stick.streetlight"),
        CB_REPLACE("s:c:stick:cb_replace", "unit", "nbessential.stick.cbreplace"),
        LOCKDOOR("s:c:stick:lookdoor", "nbessential.stick.lockdoor");

        OpStickFunction(String str, String permission) {
            this . description = str;
            this . permission = permission;
        }

        OpStickFunction(String description, String subfunction, String permission) {
            this . description = description;
            this . subfunction = subfunction;
            this . permission  = permission;
        }

        private String permission;
        private String description;
        private String subfunction;

        public String getDescription() {
            return description;
        }

        public boolean hasSubfunction() {
            return subfunction != null && !subfunction.isEmpty();
        }

        public String getSubfunction() {
            return subfunction;
        }

        public String getPermission() {
            return permission;
        }

        public void setSubfunction(String sub) {
            this . subfunction = sub;
        }
    }

    private int slot;

    public OpStick(ItemStack stack, NbPlayer player, int slot) {
        this (player);
        this . init(stack);
        this . slot = slot;
    }

    public OpStick(NbPlayer player) {
        super(Material.STICK);

        this . player = player;
        this . stickGui = new OpStickGUI();

        this . init();
    }

    private void init() {
        ItemMeta meta = getItemMeta();

        meta.setDisplayName("§cOpStick");
        setItemMeta(meta);

        setFunction(OpStickFunction.CHAIRS);
    }

    private void init(ItemStack stack) {
        if (stack.hasItemMeta()) {
            List<String> list = stack.getItemMeta().getLore();
            if (list.size() >= 3) {
                String function = list.get(1).replace("function: ", "");
                OpStickFunction function1 = OpStickFunction.valueOf(function);
                if (list.size() > 3) {
                    String subfunction = list.get(3).replace("subfunction: ", "");
                    function1.setSubfunction(subfunction);
                }

                setFunction(function1 == null ? OpStickFunction.CHAIRS : function1);
            } else
                setFunction(OpStickFunction.CHAIRS);
        } else
            setFunction(OpStickFunction.CHAIRS);
    }

    public void openInterface(ItemStack currentItem) {
        stickGui.setItem(currentItem);
        stickGui.displayInterface();
    }

    public boolean doAction(PlayerInteractEvent event) {
        StickAction action = getAction();
        assert action != null;

        if (!event.getPlayer().hasPermission(stickFunction.permission))
            return false;

        switch (event.getAction()) {
            case LEFT_CLICK_AIR:
                return action.onStickLeftClickAir(event);
            case LEFT_CLICK_BLOCK:
                return action.onStickLeftClickBlock(event);
            case RIGHT_CLICK_AIR:
                return action.onStickRightClickAir(event);
            case RIGHT_CLICK_BLOCK:
                return action.onStickRightClickBlock(event);
            default: return false;
        }
    }

    public boolean doAction(PlayerInteractAtEntityEvent event) {
        StickAction action = getAction();
        assert action != null;

        return action.onStickRightClickOnEntity(event);
    }

    private StickAction getAction() {
        switch (stickFunction) {
            case CHAIRS:
                return new Chairs(this);
            case STREETLIGHT:
                return new StreetLight(this);
            case CB_REPLACE:
                return new CbReplace(this);
            case LOCKDOOR:
                return new LockDoor(this);
            default:
                return new StickAction() {
                    @Override
                    public boolean onStickLeftClickAir(PlayerInteractEvent event) {
                        return false;
                    }

                    @Override
                    public boolean onStickLeftClickBlock(PlayerInteractEvent event) {
                        return false;
                    }

                    @Override
                    public boolean onStickRightClickAir(PlayerInteractEvent event) {
                        return false;
                    }

                    @Override
                    public boolean onStickRightClickBlock(PlayerInteractEvent event) {
                        return false;
                    }

                    @Override
                    public boolean onStickRightClickOnEntity(PlayerInteractAtEntityEvent event) {
                        return false;
                    }
                };
        }
    }

    public void nextFunction() {
        if (stickFunction.ordinal() == OpStickFunction.values().length-1)
            setFunction(OpStickFunction.CHAIRS);
        else
            setFunction(OpStickFunction.values()[stickFunction.ordinal()+1]);
    }

    public void setFunction(OpStickFunction function) {
        stickFunction = function;
        ItemMeta meta = getItemMeta();
        if (stickFunction.hasSubfunction())
            meta.setLore(Arrays.asList(ctl("s:c:stick:description"),
                    "function: " + function.toString(), ctl(stickFunction.description),
                    "subfunction: " + function.subfunction));
        else
            meta.setLore(Arrays.asList(ctl("s:c:stick:description"),
                    "function: " + function.toString(), ctl(stickFunction.description)));
        setItemMeta(meta);
    }

    String ctl(String str) {
        String s = tl(str, player, NbCommand.getTlFile());
        return Utils.colorString(s);
    }
}
