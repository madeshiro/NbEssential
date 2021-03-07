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

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import static nb.essential.main.NbEssential.tl;
import static nb.essential.main.NbEssential.getServer;

/**
 * Class of NbEssential created by maᴅeliƒe Æský on 24/12/2016
 */
public class FlyCmd extends NbCommand {

    /**
     * The Help library assigned to the command.
     */
    private final HelpLibrary helpLibrary = new HelpLibrary();

    /**
     * Creates a new {@code FlyCmd}.
     */
    FlyCmd() {
        helpLibrary.put("fly help", new String[] {
                "/fly [player] | [boolean]",
                "s:c:fly:fly_help",
                "nbessential.fly"
            }
        );
    }

    /**
     * Called when the commad is executed.
     *
     * @param sender Source of the command
     * @param alias Alias of the command which was used
     * @param args Passed command arguments
     * @return A boolean, true if the player was allowed to execute the specific
     * command, otherwise false.
     */
    @Override
    public boolean doCommand(CommandSender sender, String alias, String[] args) {

        if (!sender.hasPermission("nbessential.fly"))
            return false;

        switch (args.length) {
            case 0:
                if (!(sender instanceof NbPlayer)) {
                    invalidSenderWplayer(sender);
                    break;
                }

                ((NbPlayer) sender).setAllowFlight(!((NbPlayer) sender).getAllowFlight());
                sender.sendMessage(tl("s:c:fly:change_fly_mode", (NbPlayer) sender, getTlFile())
                        .replace("%arg{0}%", String.valueOf(((NbPlayer) sender).getAllowFlight())));
                break;
            case 1:
                NbPlayer player = getServer().getNbPlayer(args[0]);
                if (player == null) {
                    sender.sendMessage("§c" + ctl("s:c:player_not_found", args[0]));
                    break;
                }

                player.setAllowFlight(!player.getAllowFlight());
                sender.sendMessage(ctl("s:c:fly:change_fly_mode_other", sender,
                player.getDisplayName(), String.valueOf(player.getAllowFlight())));
                player.sendMessage(
                        ctl("s:c:fly:change_fly_mode", sender,
                             String.valueOf(((NbPlayer) sender).getAllowFlight())));
                break;
            default:
                invalidArgsLength(sender);
                break;
        }

        return true;
    }

    @Override
    public String getLabel() {
        return "fly";
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

        if (!sender.hasPermission("nbessential.fly"))
            return Collections.emptyList();

        switch (args.length) {
            case 1:
                return Arrays.asList(getServer().getMCServer().getPlayers());
            default:
                return Collections.emptyList();
        }
    }
}
