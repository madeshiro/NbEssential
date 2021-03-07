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

import nb.essential.loader.NbCommand;
import nb.essential.utils.Utils;
import org.bukkit.command.CommandSender;
import nb.essential.player.NbPlayer;

import java.util.ArrayList;
import java.util.List;

import static nb.essential.player.PlayerSystem.Nb_GetPlayer;
import static nb.essential.player.PlayerSystem.Nb_GetPlayersList;

/**
 * Class of NbEssential
 */
public class NicknameCmd extends NbCommand {

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
        if (!sender.hasPermission("nbessential.nickname"))
            return false;

        if (args.length < 2)
            sender.sendMessage(ctl("s:c:invalid_args_length", sender));
        else {
            NbPlayer player = Nb_GetPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(ctl("s:c:player_not_found", sender));
            } else {
                String nick = args[1];
                for (int i = 2; i < args.length; i++)
                    nick += " " + args[i];

                if (player.getProfileDescriptor().getHandler().setNickname(nick)) {
                    sender.sendMessage(ctl("s:c:nick:change_success", sender, Utils.colorString(nick)));
                } else
                    sender.sendMessage(ctl("s:c:nick:already_used", sender, args[0]));
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
        return "nickname";
    }

    /**
     * Gets the aliases for this command.
     *
     * @return the aliases assigned to this command.
     */
    @Override
    public String[] getAliases() {
        return new String[] {"nick"};
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
        switch (args.length) {
            case 1:
                return Nb_GetPlayersList();
            default:
                return new ArrayList<>();
        }
    }
}
