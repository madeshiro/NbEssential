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

import nb.essential.player.CraftNbPlayer;
import nb.essential.utils.Utils;
import org.bukkit.craftbukkit.v1_13_R1.block.CraftCommandBlock;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Class of NbEssential
 */
public class CbReplace implements StickAction {

    private final OpStick stick;

    CbReplace(OpStick stick) {
        this . stick = stick;
    }

    @Override
    public boolean onStickLeftClickAir(PlayerInteractEvent event) {
        OpStick.OpStickFunction function = stick.stickFunction;
        switch (function.getSubfunction().toLowerCase()) {
            case "unit":
                function.setSubfunction("zone");
            case "zone": default:
                function.setSubfunction("unit");
                break;
        }

        stick.setFunction(function);
        event.getItem().setItemMeta(stick.getItemMeta());
        event.getPlayer().sendMessage(stick.ctl("s:c:cbreplace:change_subfunction")
                + " §7(§9" + function.getSubfunction() + "§7)");
        return true;
    }

    @Override
    public boolean onStickLeftClickBlock(PlayerInteractEvent event) {
        switch (stick.stickFunction.getSubfunction().toLowerCase()) {
            case "unit":
                if (Utils.isCommandBlock(event.getClickedBlock())) {
                    CraftCommandBlock block = new CraftCommandBlock(event.getClickedBlock());
                    if (((CraftNbPlayer) stick.player).getVariables()
                            .removeUnitCB(event.getClickedBlock().getLocation()))
                        event.getPlayer().sendMessage(stick.ctl("s:c:cbreplace:remove_cmdblock"));
                }
                break;
            case "zone":
                if (((CraftNbPlayer) stick.player).getVariables().getCBLoc1() == null ||
                        !((CraftNbPlayer) stick.player).getVariables().getCBLoc1()
                        .equals(event.getClickedBlock().getLocation()))
                {
                    ((CraftNbPlayer)stick.player).getVariables().
                            setCBLoc1(event.getClickedBlock().getLocation());
                    event.getPlayer().sendMessage(stick.ctl("s:c:cbreplace:set_fist_loc"));
                }
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public boolean onStickRightClickAir(PlayerInteractEvent event) {
        return true;
    }

    @Override
    public boolean onStickRightClickBlock(PlayerInteractEvent event) {
        switch (stick.stickFunction.getSubfunction().toLowerCase()) {
            case "unit":
                if (Utils.isCommandBlock(event.getClickedBlock())) {
                    event.getPlayer().closeInventory();
                    event.getPlayer().sendMessage(stick.ctl("s:c:cbreplace:add_cmdblock_" +
                            (((CraftNbPlayer) stick.player).getVariables()
                                    .addUnitCB(event.getClickedBlock().getLocation())
                                    ? "succeed" : "fail")));
                }
                break;
            case "zone":
                if (((CraftNbPlayer) stick.player).getVariables().getCBLoc2() == null ||
                        !((CraftNbPlayer) stick.player).getVariables().getCBLoc2()
                        .equals(event.getClickedBlock().getLocation()))
                {
                    ((CraftNbPlayer)stick.player).getVariables().
                            setCBLoc2(event.getClickedBlock().getLocation());
                    event.getPlayer().sendMessage(stick.ctl("s:c:cbreplace:set_second_loc"));
                }
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public boolean onStickRightClickOnEntity(PlayerInteractAtEntityEvent event) {
        return true;
    }
}
