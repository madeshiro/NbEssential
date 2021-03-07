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

import org.bukkit.entity.Player;
import nb.essential.player.NbPlayer;
import nb.essential.loader.NbCommand;
import org.bukkit.command.CommandSender;
import nb.essential.player.BasicNbPlayer;
import nb.essential.permissions.PermissionGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import static nb.essential.main.NbEssential.*;
import static nb.essential.player.PlayerSystem.*;
import static nb.essential.permissions.GroupManager.*;

/**
 * Class of NbEssential
 */
public class PermissionCmd extends NbCommand {

    private final AdvanceHelpLibrary helpLibrary = new AdvanceHelpLibrary() {
        @Override
        public List<String> tabComplete(CommandSender sender, String label, String[] args) {
            switch (args.length) {
                case 1:
                    return Arrays.asList("save", "reload", "group", "player");
                case 2:
                    switch (args[0]) {
                        case "group":
                            return Nb_GetGroupList();
                        case "player":
                            return Nb_GetPlayersList();
                        default:
                            return Collections.emptyList();
                    }
                case 3:
                    switch (args[0]) {
                        default:
                            return Collections.emptyList();
                        case "group":
                            return Arrays.asList("set", "addmember", "delmember", "create");
                        case "player":
                            if (!args[1].matches("set|unset"))
                                return Arrays.asList("set", "unset");
                            else {
                                if (!(sender instanceof Player))
                                    return Collections.emptyList();
                                String[] tempArgs = new String[args.length+1];
                                tempArgs[0] = args[0];
                                tempArgs[1] = Nb_GetPlayer((Player) sender).getComposedNickname();
                                tempArgs[2] = args[1];
                                tempArgs[3] = args[2];

                                args = tempArgs;
                            }
                    }
                case 4:
                    switch (args[0]) {
                        case "group":
                            switch (args[2].toLowerCase()) {
                                case "addmember":
                                    ArrayList<String> list = new ArrayList<>(Nb_GetPlayersList());
                                    PermissionGroup group = Nb_GetGroup(args[1]);
                                    if (group != null) {
                                        for (String member : group.getMembers())
                                            list.remove(member);
                                        return list;
                                    } else
                                        return Collections.emptyList();
                                case "delmember":
                                    group = Nb_GetGroup(args[1]);
                                    if (group != null)
                                        return new ArrayList<>(group.getMembers());
                                    else
                                        return Collections.emptyList();
                                case "set":
                                    return Arrays.asList("permission", "inherit", "prefix");
                                default:
                                    return Collections.emptyList();
                            }
                        case "player":
                            switch (args[2].toLowerCase()) {
                                case "unset":case "set":
                                    return Arrays.asList("permission", "group");
                                case "group":
                                    if (args[1].toLowerCase().equalsIgnoreCase("set"))
                                        return Nb_GetGroupList();
                                    else
                                        return Collections.emptyList();
                            }
                            break;
                    }
                    break;
                case 5:
                    switch (args[0].toLowerCase()) {
                        case "player":
                            switch (args[2].toLowerCase()) {
                                case "set":
                                    if (args[3].equalsIgnoreCase("group"))
                                        return Nb_GetGroupList();
                                default:
                                    return Collections.emptyList();
                            }
                    }
                default:
                    return Collections.emptyList();
            }
            return Collections.emptyList();
        }
    };

    PermissionCmd() {
        // set HelpLibrary
        helpLibrary.put("perm help", helpLibrary.b(
                "/perm [player] <group|player|reload|save> ...",
                "s:c:perm:perm_help",
                "nbessential.permission.*"
        ));

        helpLibrary.put("perm player help", helpLibrary.b(
                "/perm player [player] <set|unset> ...",
                "s:c:perm:perm_player_help",
                "nbessential.permission.player.*"
        ));

        helpLibrary.put("perm player set help", helpLibrary.b(
                "/perm player [player] set <group|permission> ...",
                "s:c:perm:perm_player_set_help",
                "nbessential.permission.player.set.*"
        ));

        helpLibrary.put("perm player set permission help", helpLibrary.b(
                "/perm player [player] set permission <perm> <boolean>",
                "s:c:perm:perm_player_set_permission_help",
                "nbessential.permission.player.set.permission"
        ));

        helpLibrary.put("perm player unset help", helpLibrary.b(
                "/perm player [player] unset <permission|group> [...]",
                "s:c:perm:perm_player_unset_help",
                "nbessential.permission.player.unset.*"
        ));

        helpLibrary.put("perm player unset permission help", helpLibrary.b(
                "/perm player [player] unset permission <perm>",
                "s:c:perm:perm_player_unset_permission_help",
                "nbessential.permission.player.unset.permission"
        ));

        helpLibrary.put("perm group help", helpLibrary.b(
                "/perm group <group> create [prefix]\n" +
                    "/perm group <group> (add|del)member <player>\n" +
                    "/perm group <group> set <prefix|permission|inherit> ...",
                "s:c:perm:perm_group_help",
                "nbessential.permission.group.*"
        ));

        helpLibrary.put("perm group set help", helpLibrary.b(
                "/perm group <group> set <prefix|inherit> <value>\n" +
                        "/perm group <group> set permission <perm> <boolean>",
                "s:c:perm:perm_group_set_help",
                "nbessential.permission.group.set.*"
        ));

        helpLibrary.put("perm group addmember help", helpLibrary.b(
                "/perm group <group> addmember <player>",
                "s:c:perm:perm_group_addmember_help",
                "nbessential.permission.group.addmember"
        ));

        helpLibrary.put("perm group delmember help", helpLibrary.b(
                "/perm group <group> delmember <player>",
                "s:c:perm:perm_group_delmember_help",
                "nbessential.permission.group.delmember"
        ));

        helpLibrary.put("perm group set inherit help", helpLibrary.b(
                "/perm group <group> set inherit <other_gr>",
                "s:c:perm:perm_group_set_inherit_help",
                "nbessential.permission.group.set.inherit"
        ));

        helpLibrary.put("perm group set prefix help", helpLibrary.b(
                "/perm group <group> set prefix help",
                "s:c:perm:perm_group_set_prefix_help",
                "nbessential.permission.group.set.prefix"
        ));

        helpLibrary.put("perm group set permission help", helpLibrary.b (
                "/perm group <group> set permission <perm> <boolean>",
                "s:c:perm:perm_group_set_permission_help",
                "nbessential.permission.group.set.permission"
        ));
    }

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

        if (args.length == 0)
            args = new String[] {"help"};

        switch (args[0].toLowerCase()) {
            case "group":
                return cmd_group(sender, a(args));
            case "player":
                return cmd_player(sender, a(args));
            case "save":
                return cmd_save(sender);
            case "reload":
                return cmd_reload(sender);
            case "?":case "help":
            default:
                if (!sender.hasPermission("nbessential.permission.help"))
                    return false;
                getHelpLibrary().printHelpCmd(sender, "perm help");
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
        return "permission";
    }

    /**
     * Gets the aliases for this command.
     *
     * @return the aliases assigned to this command.
     */
    @Override
    public String[] getAliases() {
        return new String[] {"perm"};
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
        return helpLibrary.tabComplete(sender, label, args);
    }

    /*
        /perm save
        /perm reload

        -> note: group's name "help" will be forbidden
        /perm group <gr> create [prefix]                #ok
        /perm group <gr> set <prefix|inherit> <value>   #ok
        /perm group <gr> set pemission <perm_name> <boolean>    #ok
        /perm group <gr> addmember <name or nickname of future member> [profile]    #ok
        /perm group <gr> delmember <name of nickname of ancient member> [profile]   #ok

        /perm player [player] unset group
        /perm player [player] unset permission <perm_name>
        /perm player [player] set group <name>
        /perm player [player] set permission <perm_name> <boolean>
    */

    private boolean cmd_player(CommandSender sender, String[] args) {
        if (args.length == 0 || args[0].matches("help|'?'")) {
            if (!sender.hasPermission("nbessential.permission.player.help"))
                return false;
            getHelpLibrary().printHelpCmd(sender, "perm player help");
            return true;
        }

        if (args.length <= 1)
            invalidArgsLength(sender);
        else if (args[0].toLowerCase().matches("unset|set") &&
                !args[1].matches("unset|set")) {
            if (sender instanceof Player) {
                switch (args[0].toLowerCase()) {
                    case "set":
                        return cmd_player_set(sender, getServer().getNbPlayer((Player) sender), a(args));
                    case "unset":
                        return cmd_player_unset(sender, getServer().getNbPlayer((Player) sender), a(args));
                    case "help":
                    case "?":
                    default:
                        if (!sender.hasPermission("nbessential.permission.player.help"))
                            return false;
                        getHelpLibrary().printHelpCmd(sender, "perm player help");
                }
            } else
                invalidSenderWplayer(sender);
        } else {
            BasicNbPlayer player = Nb_GetBasicPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(ctl("s:c:player_not_found", args[0]));
                return true;
            }
            args = a(args);
            switch (args[0]) {
                case "set":
                    return cmd_player_set(sender, player, a(args));
                case "unset":
                    return cmd_player_unset(sender, player, a(args));
                case "help":
                case "?":
                default:
                    if (!sender.hasPermission("nbessential.permission.player.help"))
                        return false;
                    getHelpLibrary().printHelpCmd(sender, "perm player help");
            }
        }
        return true;
    }

    private boolean cmd_player_set(CommandSender sender, BasicNbPlayer player, String[] args) {

        if (args.length == 0 || args[0].matches("help|'?'")) {
            if (!sender.hasPermission("nbessential.permission.player.set.help"))
                return false;
            getHelpLibrary().printHelpCmd(sender, "perm player set help");
            return true;
        }

        if (!sender.hasPermission("nbessential.permission.player.set." + args[0].toLowerCase()))
            return false;

        switch (args[0].toLowerCase()) {
            case "group":
                if (args.length < 2)
                    getHelpLibrary().printHelpCmd(sender, "perm player set group help");
                else {
                    PermissionGroup group;
                    if ((group = Nb_GetGroup(args[1])) == null)
                        sender.sendMessage(ctl("s:c:perm:nonexistent_group", args[1]));
                    else {
                        boolean status = player.setPermissionGroup(group);
                        if (status) {
                            sender.sendMessage(ctl("s:c:perm:change_group_succeed", player.getNickname(), group.getName()));
                            if (player.isOnline())
                                ((NbPlayer) player).sendMessage(ctl("s:c:perm:player_change_group", group.getName()));
                        } else
                            sender.sendMessage(ctl("s:c:perm:change_group_fail", player.getNickname(), group.getName()));
                    }
                }

                break;
            case "permission":
                if (args.length < 3)
                    getHelpLibrary().printHelpCmd(sender, "perm player set permission help");
                else {
                    String permission = args[1];
                    String value = args[2];
                    if (!value.toLowerCase().matches("true|false"))
                        sender.sendMessage(ctl("s:c:invalid_boolean", args[2]));
                    else {
                        player.setPermission(permission, Boolean.parseBoolean(value));
                        sender.sendMessage(ctl("s:c:perm:change_player_permission",
                                  player.getNickname(), permission, value));
                    }
                }
                break;
        }

        return true;
    }

    private boolean cmd_player_unset(CommandSender sender, BasicNbPlayer player, String[] args) {

        if (args.length == 0 || args[0].matches("help|'?'")) {
            if (!sender.hasPermission("nbessential.permission.player.set.help"))
                return false;
            getHelpLibrary().printHelpCmd(sender, "perm player unset help");
            return true;
        }

        if (!sender.hasPermission("nbessential.permission.player.unset." + args[0].toLowerCase()))
            return false;

        switch (args[0].toLowerCase()) {
            case "group":
                player.setPermissionGroup(null);
                if (player.isOnline())
                    ((NbPlayer) player).sendMessage(ctl("s:c:perm:player_group_unset"));
                sender.sendMessage(ctl("s:c:perm:unset_player_group", player.getNickname()));
                break;
            case "permission":
                if (args.length < 2)
                    getHelpLibrary().printHelpCmd(sender, "perm player unset permission help");
                else {
                    player.unsetPermission(args[1]);
                    sender.sendMessage(ctl("s:c:perm:unset_player_permission",
                            player.getNickname(), args[1]));
                }
                break;
            default:
                getHelpLibrary().printHelpCmd(sender, "perm player unset help");
        }

        return true;
    }

    private boolean cmd_group(CommandSender sender, String[] args) {
        if (args.length == 0 || args[0].equals("help")) {
            if (!sender.hasPermission("nbessential.permission.group.help"))
                return false;
            getHelpLibrary().printHelpCmd(sender, "perm group help");
            return true;
        }

        if (args.length == 1)
            invalidArgsLength(sender);
        else {
            PermissionGroup group = Nb_GetGroup(args[0]);
            if (!args[1].equalsIgnoreCase("create") && !assertGroupNonnull(group, sender))
                return true;

            switch (args[1]) {
                case "create":
                    return cmd_group_create(sender, args[0], a(args, 2));
                case "set":
                    return cmd_group_set(sender, group, a(args, 2));
                case "addmember":
                    return cmd_group_addmember(sender, group, a(args, 2));
                case "delmember":
                    return cmd_group_delmember(sender, group, a(args, 2));
            }
        }
        return true;
    }

    private boolean cmd_group_create(CommandSender sender, String name, String[] args) {
        if (!sender.hasPermission("nbessential.permission.group.create"))
            return false;

        String prefix = args.length > 0 ? args[0] : "";
        if (Nb_IsGroupExist(name))
            sender.sendMessage(ctl("s:c:perm:group_already_exists", sender, name));
        else
            sender.sendMessage(ctl("s:c:perm:create_group_" +
                    (Nb_CreateGroup(name, prefix) == null ? "fail" : "succeed"), sender, name));
        return true;
    }

    private boolean cmd_group_addmember(CommandSender sender, PermissionGroup group, String[] args) {
        if (!sender.hasPermission("nbessential.permission.group.addmember"))
            return false;

        if (args.length == 0 || args[0].matches("help|'?'")) {
            getHelpLibrary().printHelpCmd(sender, "perm group addmember help");
        } else {
            BasicNbPlayer player = Nb_GetBasicPlayer(args[0]);
            if (player == null)
                sender.sendMessage(ctl("s:c:player_not_found", args[0], group.getName()));
            else {
                if (player.setPermissionGroup(group)) {
                    sender.sendMessage(ctl("s:c:perm:change_group_succeed", player.getNickname(), group.getName()));
                    if (player.isOnline())
                        ((NbPlayer) player).sendMessage(ctl("s:c:perm:player_change_group", group.getName()));
                } else
                    sender.sendMessage(ctl("s:c:perm:change_group_fail", player.getNickname(), group.getName()));
            }
        }

        return true;
    }

    private boolean cmd_group_delmember(CommandSender sender, PermissionGroup group, String[] args) {
        if (!sender.hasPermission("nbessential.permission.group.delmember"))
            return false;

        if (args.length == 0 || args[0].matches("help|'?'")) {
            getHelpLibrary().printHelpCmd(sender, "perm group delmember help");
        } else {
            BasicNbPlayer player = Nb_GetBasicPlayer(args[0]);
            if (player == null)
                sender.sendMessage(ctl("s:c:player_not_found", args[0], group.getName()));
            else {
                if (group.hasMember(player.getComposedNickname())) {
                    player.setPermissionGroup(null);
                    if (player.isOnline())
                        ((NbPlayer) player).sendMessage(ctl("s:c:perm:player_group_unset"));
                    sender.sendMessage(ctl("s:c:perm:group_delmember_succeed", player.getNickname(), group.getName()));
                } else
                    sender.sendMessage(ctl("s:c:perm:group_delmember_fail", player.getNickname(), group.getName()));
            }
        }

        return true;
    }

    private boolean cmd_group_set(CommandSender sender, PermissionGroup group, String[] args) {
        if (args.length == 0 || args[0].matches("help|'?'")) {
            if (!sender.hasPermission("nbessential.permission.group.set.help"))
                return false;
            else {
                getHelpLibrary().printHelpCmd(sender, "perm group set help");
                return true;
            }
        }

        if (!args[0].toLowerCase().matches("prefix|inherit|permission")) {
            if (!sender.hasPermission("nbessential.permission.group.set.help"))
                return false;
            invalidArgs(sender, "permission group " + group.getName() + " set <prefix|ìnherit|permission> ...");
            return true;
        }

        else if (args.length < 2 || (args.length < 3 && args[0].equals("permission"))) {
            if (!sender.hasPermission("nbessential.permission.group.set." +
                    args[0].toLowerCase() + ".help"))
                return false;
            getHelpLibrary().printHelpCmd(sender, "perm group set " + args[0].toLowerCase() + " help");
            return true;
        }

        if (!sender.hasPermission("nbessential.permission.set." + args[0]))
            return false; // verify permission for the specific command

        switch (args[0]) {
            case "prefix":
                String prefix = "";
                for (int i = 1; i < args.length; i++)
                    prefix += args[i] + (i == args.length -1 ? "" : " ");
                group.setPrefix(prefix);
                sender.sendMessage(ctl("s:c:perm:set_prefix", sender, group.getName(), prefix));
                break;
            case "inherit":
                if (!group.setInheritence(args[1]))
                    sender.sendMessage(ctl("s:c:perm:set_inherit_failed", sender, args[1])); // group not found
                else
                    sender.sendMessage(ctl("s:c:perm:set_inherit_succeed", sender, group.getName(), args[1]));
                break;
            case "permission":
                if (!args[2].toLowerCase().matches("true|false")) {
                    sender.sendMessage(ctl("s:c:invalid_boolean", args[2]));
                } else {
                    group.setPermission(args[1], Boolean.valueOf(args[2]));
                    sender.sendMessage(ctl("s:c:perm:set_permission", sender, group.getName(), args[1], args[2]));
                }
                break;
        }

        return true;
    }

    private boolean cmd_reload(CommandSender sender) {
        if (!sender.hasPermission("nbessential.permission.reload"))
            return false;
        sender.sendMessage(ctl("s:c:perm:reload_" +
                (Nb_GM_Reload() ? "succeed" : "fail"), sender));
        return true;
    }

    /**
     *
     * @param sender Source of the request
     * @return A boolean, true if the sender has the permission to execute this command,
     *         false otherwise.
     */
    private boolean cmd_save(CommandSender sender) {
        if (!sender.hasPermission("nbessential.permission.save"))
            return false;

        sender.sendMessage(ctl("s:c:perm:save_" +
                (Nb_GM_Save() ? "succeed" : "fail"), sender));
        return true;
    }

    private boolean assertGroupNonnull(PermissionGroup group, CommandSender sender) {
        if (group == null) {
            sender.sendMessage(ctl(
                    sender.hasPermission("nbessential.permission.grouplist") ?
                            "s:c:perm:nonexistent_group" :
                            "s:c:invalid_args_send", sender));
            return false;
        } else return true;
    }
}