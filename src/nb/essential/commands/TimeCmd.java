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

import org.bukkit.World;
import org.bukkit.entity.Player;
import nb.essential.loader.NbCommand;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Collections;

import static nb.essential.main.NbEssential.getServer;
import static nb.essential.main.NbEssential.tl;

/**
 * Class of NbEssential created by maᴅeliƒe Æský on 30/12/2016
 */
public class TimeCmd extends NbCommand {

    private final HelpLibrary helpLibrary = new HelpLibrary();

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

        if(!sender.hasPermission("nbessential.time"))
            return false;

        alias = alias.toLowerCase();
        switch (args.length) {
            case 0:
                if (!(sender instanceof Player))
                    invalidSenderWplayer(sender);
                else {
                    ((Player) sender).getWorld().setTime(alias.equals("day") ? 1000 : 13000);
                    sender.sendMessage("§e" + tl("s:c:time:time_set_" + alias));
                }
                break;
            case 1:
                World world = getServer().getWorld(args[0]);
                if (world == null)
                    sender.sendMessage("§c" + tl("s:c:time:invalid_world"));
                else {
                    world.setTime(alias.equals("day") ? 1000 : 13000);
                    sender.sendMessage("§e" + tl("s:c:time:time_set_" + alias));
                }
                break;
            default:
                invalidArgsLength(sender);
                break;
        }

        return true;
    }

    /**
     * Gets the label of this command.
     *
     * @return the command's label
     */
    @Override
    public String getLabel() {
        return "day";
    }

    /**
     * Gets the aliases for this command.
     *
     * @return the aliases assigned to this command.
     */
    @Override
    public String[] getAliases() {
        return new String[] {"night"};
    }

    /**
     * Gets the HelpLibrary for this command
     *
     * @return The HelpLibrary for this Command.
     */
    @Override
    protected HelpLibrary getHelpLibrary() {
        return helpLibrary;
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
        if(!sender.hasPermission("nbessential.time"))
            return Collections.emptyList();

        switch (args.length) {
            case 1:
                return getServer().getWorldList();
            default:
                return Collections.emptyList();
        }
    }
}
