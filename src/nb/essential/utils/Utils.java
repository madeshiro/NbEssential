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
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import zaesora.madeshiro.parser.json.JSONObject;

public class Utils {

    public static Location locFromJSON(JSONObject obj) {
        World world = Bukkit.getWorld(obj.<String>getObject("world"));
        double x, y, z;
        float yaw, pitch;

        x = obj.getObject("x");
        y = obj.getObject("y");
        z = obj.getObject("z");

        yaw = obj.<Double>getObject("yaw").floatValue();
        pitch = obj.<Double>getObject("pitch").floatValue();

        return world != null ? new Location(world, x, y, z, yaw, pitch) : null;
    }

    public static JSONObject jsonFromLoc(Location location) {
        JSONObject object = new JSONObject();

        object.put("world", location.getWorld().getName());
        object.put("x", location.getX());
        object.put("y", location.getY());
        object.put("z", location.getZ());
        object.put("yaw", location.getYaw());
        object.put("pitch", location.getPitch());

        return object;
    }

    /**
     * A simple function to replace all '&' + digit code by its equivalent
     * in order to colorize characters in the chat and others...
     * @param str The string to colorize
     * @return The colorize string.
     */
    public static String colorString(String str) {
        for (ChatColor color : ChatColor.values())
            str = str.replace("&" + color.getChar(), color.toString());
        return str;
    }

    public static String uncolorString(String str) {
        return uncolorString(str, '§');
    }

    public static String uncolorString(String str, char c) {
        for (ChatColor color : ChatColor.values())
            str = str.replace(Character.toString(c) + color.getChar(), "");
        return str;
    }

    public static String hideString(String str) {
        String hiddenString = "";
        for (char c : str.toCharArray())
            hiddenString += "§"+ Character.toString(c);
        return hiddenString;
    }

    public static String revealString(String str) {
        String revealString = "";
        for (char c : str.toCharArray()) {
            if (c != '§')
                revealString += Character.toString(c);
        }
        return revealString;
    }

    public static boolean isCommandBlock(Block block) {
        switch (block.getType()) {
            case COMMAND_BLOCK:case CHAIN_COMMAND_BLOCK: case REPEATING_COMMAND_BLOCK:
                return true;
            default:
                return false;
        }
    }

    public static boolean isDoor(Block block) {
        return block.getType().name().contains("DOOR");
    }

    public static boolean isFenceGate(Block block) {
        return block.getType().name().contains("FENCE_GATE");
    }

    public static boolean isTrapDoor(Block block) {
        return block.getType().name().contains("TRAPDOOR");
    }

    public static boolean isShulkerBox(Block block) {
        return block.getType().name().contains("SHULKER_BOX");
    }

    public static boolean isLockdoorSupported(Block block) {
        if (isDoor(block) || isShulkerBox(block) || isFenceGate(block) || isTrapDoor(block))
            return true;

        switch (block.getType()) {
            case DISPENSER:case CHEST:
            case TRAPPED_CHEST:
                return true;
            default: return false;
        }
    }
}
