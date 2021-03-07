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
import nb.essential.stick.LockDoor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static nb.essential.blockmeta.MetaManager.Nb_BlockHasMeta;
import static nb.essential.blockmeta.MetaManager.Nb_BlockGetMeta;
import static nb.essential.stick.LockDoor.Nb_LD_CreateKey;

public class LockDoorCmd extends NbCommand {

    private final AdvanceHelpLibrary helpLibrary = new AdvanceHelpLibrary() {

        @Override
        public List<String> tabComplete(CommandSender sender, String label, String[] args) {
            switch (args.length) {
                case 1:
                    return Arrays.asList("help", "lock", "unlock", "define", "remove", "key");
                case 5:
                    if (args[0].matches("lock|unlock|define"))
                        return Arrays.asList("true", "false");
            }

            return Collections.emptyList();
        }
    };

    private LockDoorCmd() {

        helpLibrary.put("help", helpLibrary.b(
                "/lockdoor help [<page>]",
                "s:c:lockdoor:help",
                "nbessential.lockdoor.*"
        ));

        helpLibrary.put("lock", helpLibrary.b(
                "/lockdoor lock (<id>|<x> <y> <z>) [world] [close]",
                "s:c:lockdoor:help_lock",
                "nbessential.lockdoor.lock"
        ));

        helpLibrary.put("unlock", helpLibrary.b(
                "/lockdoor unlock (<id>|<x> <y> <z> [world]) [open]",
                "s:c:lockdoor:help_unlock",
                "nbessential.lockdoor.unlock"
        ));

        helpLibrary.put("remove", helpLibrary.b(
                "/lockdoor remove <id>|<x> <y> <z> [world]",
                "s:c:lockdoor:help_remove",
                "nbessential.lockdoor.remove"
        ));

        helpLibrary.put("define", helpLibrary.b(
                "/lockdoor define <x> <y> <z> [lock]",
                "s:c:lockdoor:help_define",
                "nbessential.lockdoor.define"
        ));

        helpLibrary.put("key", helpLibrary.b(
                "/lockdoor key [<id>|<x> <y> <z> [world]]",
                "s:c:lockdoor:help_key",
                "nbessential.lockdoor.key"
        ));
    }

    @Override
    public boolean doCommand(CommandSender sender, String alias, String[] args) {

        switch (args.length) {
            case 0:
                if (!sender.hasPermission("nbessential.lockdoor.help"))
                    return false;
                getHelpLibrary().printHelpCmd(sender, "help");
                break;
            case 1:
                switch (args[0].toLowerCase()) {
                    case "help":
                        if (!sender.hasPermission("nbessential.lockdoor.help"))
                            return false;
                        getHelpLibrary().printHelpPage(sender, 0);
                        break;
                    case "key":
                        if (sender instanceof Player) {
                            if (get_key((NbPlayer) sender))
                                return true;
                        }
                    case "lock": case "unlock": case "remove": case "define":
                        return print_help(sender, args[0].toLowerCase());
                    default:
                        if (sender.hasPermission("nbessential.lockdoor.help"))
                            getHelpLibrary().printHelpCmd(sender, "help");
                        else
                            this.invalidArgs(sender, "lockdoor help");
                }
                break;
            case 2: case 3: case 4: case 5:
                if (args[0].toLowerCase().matches("lock|unlock|remove|define") &&
                        !sender.hasPermission("nbessential.lockdoor.".concat(args[0].toLowerCase())))
                    return false;

                switch (args[0].toLowerCase()) {
                    case "lock":
                        cmd_lock(args[0], a(args));
                        break;
                    case "unlock":
                        cmd_unlock(args[0], a(args));
                        break;
                    case "remove":
                        cmd_remove(args[0], a(args));
                        break;
                    case "define":
                        cmd_define(args[0], a(args));
                        break;
                    default:
                        invalidArgs(sender, "lockdoor help");
                        break;
                }
        }

        return true;
    }

    private boolean get_key(NbPlayer sender) {
        Block targetBlock = sender.getTargetBlock(null, 10);

        if (targetBlock != null && Nb_BlockHasMeta(targetBlock, "lockdoor")) {
            LockDoor.LockMetadataValue meta = Nb_BlockGetMeta(targetBlock, "lockdoor");
            sender.getInventory().addItem(Nb_LD_CreateKey(targetBlock, "A Key", null));
            sender.sendMessage(ctl("s:c:lockdoor:add_key"));
            return true;
        } else return false;
    }

    private void cmd_lock(String arg, String[] args) {
    }

    private void cmd_unlock(String arg, String[] args) {
    }

    private void cmd_define(String arg, String[] args) {
    }

    private void cmd_remove(String arg, String[] args) {
    }

    @Override
    public String getLabel() {
        return "lockdoor";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"ld"};
    }

    @Override
    protected HelpLibrary getHelpLibrary() {
        return new HelpLibrary();
    }

    @Override
    public List<String> getTabCompletion(CommandSender sender, String label, String[] args) {
        return helpLibrary.tabComplete(sender, label, args);
    }

    private boolean print_help(CommandSender sender, String helpCode) {
        if (!sender.hasPermission("nbessential.lockdoor." + helpCode))
            return false;

        getHelpLibrary().printHelpCmd(sender, helpCode);
        return true;
    }
}
