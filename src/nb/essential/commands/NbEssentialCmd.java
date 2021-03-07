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
import nb.essential.player.NbPlayer;
import nb.essential.stick.OpStick;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import static nb.essential.main.NbEssential.*;
import static nb.essential.player.PlayerSystem.Nb_GetPlayer;

/**
 * Class of NbEssential created by maᴅeliƒe Æský on 12/12/2016
 */
public class NbEssentialCmd extends NbCommand {

    private final HelpLibrary helpLibrary = new HelpLibrary();

    NbEssentialCmd() {
        helpLibrary.put("nb help", new String[] {
                "/nb <save|reload|stick>",
                "s:c:nb:nb_help",
                "nbessential.operator"
        });

        helpLibrary.put("nbsecurity help", new String[] {
                "/nbsecurity <enable|disable>",
                "s:c:nb:nbsecurity_help",
                "nbessential.operator"
        });
    }

    @Override
    public boolean doCommand(CommandSender sender, String alias, String[] args) {

        if (alias.equalsIgnoreCase("nbsecurity"))
            return cmd_nbsecurity(sender, args);
        else {
            if (args.length == 0 || args[0].matches("help|'?'")) {
                if (!sender.hasPermission("nbessential.operator"))
                    return false;

                getHelpLibrary().printHelpCmd(sender, "nb help");
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "stick":
                    return cmd_stick(sender);
                case "reload":
                    return cmd_reload(sender);
                case "save":
                    return cmd_save(sender);
            }
        }

        return true;
    }

    @Override
    public String getLabel() {
        return "nb";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"nbessential", "nbsecurity"};
    }

    @Override
    protected HelpLibrary getHelpLibrary() {
        return helpLibrary;
    }

    @Override
    public List<String> getTabCompletion(CommandSender sender, String label, String[] args) {
        if (label.equals("nbsecurity") && args.length == 1)
            return Arrays.asList("enable", "disable");
        switch (args.length) {
            case 1:
                return Arrays.asList("save", "reload", "stick");
            default: return Collections.emptyList();
        }
    }

    private boolean cmd_nbsecurity(CommandSender sender, String[] args) {
        if (!sender.hasPermission("nbessential.operator"))
            return false;

        if (args.length == 0 || args[0].matches("help|'?'")) {
            getHelpLibrary().printHelpCmd(sender, "nbsecurity help");
        } else {
            switch (args[0].toLowerCase()) {
                case "enable":
                    setSecurityOn(true);
                    break;
                case "disable":
                    setSecurityOn(false);
                    break;
                default:
                    sender.sendMessage("§cInvalid arguments sended ! (waiting 'enable' or 'disable')");
                    break;
            }
        }

        return true;
    }

    private boolean cmd_reload(CommandSender sender) {
        if (!sender.hasPermission("nbessential.operator"))
            return false;
        sender.sendMessage(ctl("s:c:nb:reload_" + (reloadPlugin() ? "succeed" :  "fail")));
        return true;
    }

    private boolean cmd_save(CommandSender sender) {
        if (!sender.hasPermission("nbessential.operator"))
            return false;
        sender.sendMessage(ctl("s:c:nb:save_" + (savePlugin() ? "succeed" :  "fail")));
        return true;
    }

    private boolean cmd_stick(CommandSender sender) {
        if (!sender.hasPermission("nbessential.stick"))
            return false;

        if (!(sender instanceof Player)) {
            invalidSenderWplayer(sender);
        } else {
            NbPlayer player = Nb_GetPlayer((Player) sender);
            if (player == null)
                sender.sendMessage(ctl("s:c:nb:nb_stick_wnbplayer"));
            else
                ((Player) sender).getInventory().addItem(new OpStick(player));
        }

        return true;
    }
}
