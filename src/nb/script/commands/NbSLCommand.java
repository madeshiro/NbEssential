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

package nb.script.commands;

import nb.essential.loader.NbCommand;
import nb.essential.main.NbEssential;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * Class of NbEssential in package nb.script.commands
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public class NbSLCommand extends NbCommand {
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
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "quest":
                    return NbEssential.getNbCommand("quest").doCommand(sender, "quest", a(args));
                case "script":
                    return NbEssential.getNbCommand("script").doCommand(sender, "script", a(args));
            }
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
        return "nbsl";
    }

    /**
     * Gets the aliases for this command.
     *
     * @return the aliases assigned to this command.
     */
    @Override
    public String[] getAliases() {
        return new String[0];
    }

    /**
     * Gets the HelpLibrary for this command
     *
     * @return The HelpLibrary for this Command.
     */
    @Override
    protected HelpLibrary getHelpLibrary() {
        return new HelpLibrary();
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
        return Collections.emptyList();
    }
}
