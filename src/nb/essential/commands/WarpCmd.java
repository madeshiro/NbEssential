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

import nb.essential.essentials.warp.Warp;
import nb.essential.loader.NbCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static nb.essential.essentials.warp.WarpManager.*;

/**
 * Class of NbEssential in package nb.essential.commands
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 */
public class WarpCmd extends NbCommand {
    /**
     * Called when the commad is executed.
     *
     * @param sender Source of the command
     * @param alias  Alias of the command which was used
     * @param args   Passed command arguments
     * @return A boolean, true if the player was allowed to execute the specific
     * command, otherwise false.
     */
    @Override
    public boolean doCommand(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player))
            return false;

        switch (alias) {
            case "wtp":
                if (!sender.hasPermission("nbessential.warp.tp"))
                    return false;
                if (args.length != 1) {
                    sender.sendMessage(ctl("s:c:invalid_args_length", sender));
                } else {
                    wtp((Player) sender, args[0]);
                }
                break;
            default:
                switch (args.length) {
                    case 2:
                        if (!sender.hasPermission("nbessential.warp." + args[0].toLowerCase()))
                            return false;
                        switch (args[0].toLowerCase()) {
                            case "create":
                                if (Nb_WP_CreateWarp(args[1], ((Player) sender).getLocation()) != null) {
                                    sender.sendMessage(ctl("s:c:warp_create_success", sender, args[1]));
                                } else {
                                    sender.sendMessage(ctl("s:c:warp_create_fail", sender, args[1]));
                                }
                                break;
                            case "tp":
                                wtp((Player) sender, args[1]);
                                break;
                            case "remove":
                                if (Nb_WP_RemoveWarp(args[1])) {
                                    sender.sendMessage(ctl("s:c:warp_remove_success", sender, args[1]));
                                } else {
                                    sender.sendMessage(ctl("s:c:warp_remove_fail", sender, args[1]));
                                }
                        }
                        break;
                    default: // TODO print help
                }
        }

        return true;
    }

    private void wtp(Player player, String warpName) {
        Warp warp = Nb_WP_GetWarp(warpName);
        if (warp == null)
            player.sendMessage(ctl("s:c:warp_no_exist", player, warpName));
        else {
            player.teleport(warp.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            player.sendMessage(ctl("s:c:warp_tp_success"));
        }
    }

    /**
     * Gets the label of this command.
     *
     * @return the command's label
     */
    @Override
    public String getLabel() {
        return "warp";
    }

    /**
     * Gets the aliases for this command.
     *
     * @return the aliases assigned to this command.
     */
    @Override
    public String[] getAliases() {
        return new String[] {"wp", "wtp"};
    }

    /**
     * Gets the HelpLibrary for this command
     *
     * @return The HelpLibrary for this Command.
     */
    @Override
    protected HelpLibrary getHelpLibrary() {
        return new AdvanceHelpLibrary() {
            @Override
            public List<String> tabComplete(CommandSender sender, String label, String[] args) {
                switch (label) {
                    case "wtp":
                        if (args.length == 1 && sender.hasPermission("nbessential.warp.tp"))
                            return Nb_WP_GetWarpsList();
                        break;
                    default:
                        switch (args.length) {
                            case 1:
                                return Arrays.asList("create", "tp", "remove");
                            case 2:
                                if (!sender.hasPermission("nbessential.warp."+args[0].toLowerCase()))
                                    break;
                                switch (args[0].toLowerCase()) {
                                    case "tp": case "remove":
                                        return Nb_WP_GetWarpsList();
                                    default:
                                        break;
                                }
                                break;
                        }
                        break;
                }
                return Collections.emptyList();
            }
        };
    }

    /**
     * Gets an array of available argument when a {@link CommandSender} press
     * the Tab button to complete his command.
     *
     * @param sender Source of the request
     * @param label  Label of the command which was used
     * @param args   Passed command arguments
     * @return An array of avaiable arguments to complete the command or an empty list.
     * @implNote The method will never return null.
     */
    @Override
    public List<String> getTabCompletion(CommandSender sender, String label, String[] args) {
        return ((AdvanceHelpLibrary)getHelpLibrary()).tabComplete(sender, label, args);
    }
}
