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

import nb.essential.player.NbPlayer;
import nb.essential.loader.NbCommand;
import org.bukkit.command.CommandSender;
import nb.essential.player.CraftNbPlayer;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;
import java.util.Collections;

/**
 * Class of NbEssential created by maᴅeliƒe Æský on 27/12/2016
 */
public class BackCmd extends NbCommand {

    private final HelpLibrary helpLibrary = new HelpLibrary();
    private BackCmd() {
    }

    @Override
    public boolean doCommand(CommandSender sender, String alias, String[] args) {

        if (!sender.hasPermission("nbessential.back"))
            return false;

        switch (args.length) {
            case 0:
                if (!(sender instanceof NbPlayer))
                    // If the sender isn't a Player
                    invalidSenderWplayer(sender);
                else if(!back((NbPlayer) sender))
                    // If any last location wasn't found
                    sender.sendMessage("§c" + ctl("s:c:back:back_fail_noloc", (NbPlayer) sender));
                break;
            case 1:
                if (args[0].equalsIgnoreCase("help"))
                    getHelpLibrary().printHelpCmd(sender, "back help");
                else
                    invalidArgs(sender, "back help");
                break;
            default:
                invalidArgsLength(sender);
        }
        return true;
    }

    boolean back(NbPlayer player) {
        if (((CraftNbPlayer) player).getVariables().lastLocation == null)
            return false;

        if (player.teleport(((CraftNbPlayer) player).getVariables().lastLocation,
                PlayerTeleportEvent.TeleportCause.COMMAND))
            player.sendMessage("§e" + ctl("s:c:back:back_success", player));
        return true;
    }

    @Override
    public String getLabel() {
        return "back";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    protected HelpLibrary getHelpLibrary() {
        return helpLibrary;
    }

    @Override
    public List<String> getTabCompletion(CommandSender sender, String label, String[] args) {
        return Collections.emptyList();
    }
}
