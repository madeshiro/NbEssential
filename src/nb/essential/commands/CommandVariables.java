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
package nb.essential.commands;

import java.util.HashSet;

import nb.essential.essentials.jail.CustomJail;
import nb.essential.interfaces.UserInterface;
import nb.essential.stick.Chairs;
import nb.essential.zone.Zone;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

/**
 * Variables used by all commands to interact with the player
 */
public final class CommandVariables {

    long lastAction = 0; // in seconds
    CommandSender whisperMsgReply;
    int musicTaskId = 0;
    String currentMusic = "";

    Location ZLoc1, ZLoc2;
    Location CBLoc1, CBLoc2;
    HashSet<Location> CBSel = new HashSet<>();

    int CBChangeStatus = 0;
    String CBStringTarget;
    String CBStringReplacement;

    CustomJail jail;
    Location lastLocation;
    Zone zoneLocation;

    private Chairs chairs;

    private UserInterface openedInterface;

    boolean isStealing, isBeingRobbed;

    public boolean isStealing() {
        return isStealing;
    }

    public boolean isBeingRobbed() {
        return isBeingRobbed;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public Chairs getChairs() {
        return chairs;
    }

    public void setChairs(Chairs chairs) {
        this . chairs = chairs;
    }

    boolean selectCBZone() {
        if (CBLoc1 == null || CBLoc2 == null ||
                !CBLoc1.getWorld().equals(CBLoc2.getWorld()))
            return false;

        int minX = Math.min(CBLoc1.getBlockX(), CBLoc2.getBlockX()),
                maxX = Math.max(CBLoc1.getBlockX(), CBLoc2.getBlockX());
        int minY = Math.min(CBLoc1.getBlockY(), CBLoc2.getBlockY()),
                maxY = Math.max(CBLoc1.getBlockY(), CBLoc2.getBlockY());
        int minZ = Math.min(CBLoc1.getBlockZ(), CBLoc2.getBlockZ()),
                maxZ = Math.max(CBLoc1.getBlockZ(), CBLoc2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = new Location(CBLoc1.getWorld(), x, y, z).getBlock();
                    switch (block.getType()) {
                        case COMMAND_BLOCK: case CHAIN_COMMAND_BLOCK: case REPEATING_COMMAND_BLOCK:
                            CBSel.add(block.getLocation());
                    }
                }
            }
        }

        CBLoc1 = null; CBLoc2 = null;
        return true;
    }


    public void clearCBSelection() {
        CBLoc1 = CBLoc2 = null;
        CBSel.clear();
    }

    public boolean addUnitCB(Location loc) {
        if (CBSel.contains(loc))
            return false;
        else
            CBSel.add(loc);
        return true;
    }

    public Location getCBLoc1() {
        return CBLoc1;
    }

    public void setCBLoc1(Location loc1) {
        CBLoc1 = loc1;
    }

    public Location getCBLoc2() {
        return CBLoc2;
    }

    public void setCBLoc2(Location loc2) {
        CBLoc2 = loc2;
    }

    public boolean removeUnitCB(Location loc) {
        return CBSel.remove(loc);
    }

    public Zone getZoneLocation() {
        return zoneLocation;
    }

    public void setZoneLocation(Zone zone) {
        Zone lastZone = zoneLocation;
        this . zoneLocation = zone;
    }

    public UserInterface getOpenedInterface() {
        return openedInterface;
    }

    public void setOpenedInterface(UserInterface userInterface) {
        this . openedInterface = userInterface;
    }

    public boolean hasOpenedInterface() {
        return openedInterface != null;
    }
}
