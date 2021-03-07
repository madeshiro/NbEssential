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

package nb.essential.loader;

import nb.essential.files.TranslationFile;
import nb.essential.main.Main;
import nb.essential.player.NbPlayer;
import nb.essential.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import static nb.essential.main.NbEssential.*;
import static nb.essential.player.PlayerSystem.Nb_GetPlayer;

/**
 * <p>
 * Represents a command for NbEssential and these subplugins. Each inheriting
 * from the {@code NbCommand class} will be loaded automaticaly when the plugin
 * will be enabled.
 * </p>
 * <p>
 * A command can't have a constructor which requires a parameter to its constructor
 * otherwise, they will not be loaded by the {@link CommandLoader} system. Moreover,
 * the {@link #getLabel()} method must necessarily return a value <code>not null</code>
 * to avoid a {@link NullPointerException} come. However, the function {@link #getAliases()}
 * can return an empty array or {@code null} without risking to make an exception occure.
 * </p>
 * <p>
 * During command creation, it is recommended to create a <code>private static HelpLibrary</code>
 * and put all help messages in a static block like this:
 * <code>
 *     <pre>
 * private static final HelpLibrary helpLibrary = new HelpLibrary();
 * protected static HelpLibrary getHelpLibrary() {
 *     return helpLibrary;
 * }
 *
 * static {
 *     helpLibrary.put("mycommand args", new String[] {
 *         "/mycommand args [value]", // usage
 *         "A simple command which required a value", // description
 *         "nbessential.mycommand.args" // permission
 *     });
 * }
 *     </pre>
 * </code>
 * </p>
 *
 * @see Command
 * @see CommandLoader
 * @see Main#onCommand(CommandSender, Command, String, String[])
 *
 * @author MađeShirő ƵÆsora
 * @since NB 0.1.0 (first release 0.1)
 * @version NB 1.2
 */
public abstract class NbCommand {

    /**
     * Called when the commad is executed.
     *
     * @param sender Source of the command
     * @param alias Alias of the command which was used
     * @param args Passed command arguments
     * @return A boolean, true if the player was allowed to execute the specific
     * command, otherwise false.
     */
    public abstract boolean doCommand(CommandSender sender, String alias, String[] args);

    /**
     * Gets the label of this command.
     * @return the command's label
     */
    public abstract String getLabel();

    /**
     * Gets the aliases for this command.
     * @return the aliases assigned to this command.
     */
    public abstract String[] getAliases();

    /**
     * Tests if the current command has for alias (or label) the string passed as
     * argument.
     *
     * @param alias The alias (or label) to test
     * @return True if the command has for alias (or label) the following string,
     * else false.
     */
    public boolean hasAlias(String alias) {

        if (getAliases() != null)
            for (String str : getAliases())
                if (str.equalsIgnoreCase(alias))
                    return true;

        return getLabel().equalsIgnoreCase(alias);
    }

    /**
     * Gets the HelpLibrary for this command
     * @return The HelpLibrary for this Command.
     */
    protected abstract HelpLibrary getHelpLibrary();

    /**
     * Gets an array of available argument when a {@link CommandSender} press
     * the Tab button to complete his command.
     *
     * @implNote The method will never return null.
     *
     * @param sender Source of the request
     * @param label Label of the command which was used
     * @param args Passed command arguments
     * @return An array of avaiable arguments to complete the command or an empty list.
     */
    public abstract List<String> getTabCompletion(CommandSender sender, String label, String[] args);

    public static TranslationFile getTlFile() {
        return TranslationFile.commandFile;
    }

    protected String ctl(String str, String... args) {
        String s = tl(str, getTlFile());
        if (args == null || args.length == 0)
            return s;

        for (int i = 0; i < args.length; i++)
            s = s.replace("%arg{" + i + "}%", args[i]);
        return Utils.colorString(s);
    }

    protected String ctl(String str, CommandSender sender, String... args) {
        if (sender instanceof NbPlayer)
            return ctl(str, (NbPlayer) sender, args);
        else if (sender instanceof Player)
            return ctl(str, (Player) sender, args);
        else
            return ctl(str, args);
    }

    protected String ctl(String str, Player player, String... args) {
        String s;
        NbPlayer player1;
        if ((player1 = Nb_GetPlayer(player)) != null)
            s = tl(str, player1, getTlFile());
        else
            s = tl(str, player, getTlFile());
        if (args == null || args.length == 0)
            return s;

        for (int i = 0; i < args.length; i++)
            s = s.replace("%arg{" + i + "}%", args[i]);
        return Utils.colorString(s);
    }

    protected String ctl(String str, NbPlayer player, String... args) {
        String s = tl(str, player, getTlFile());
        if (args == null || args.length == 0)
            return s;

        for (int i = 0; i < args.length; i++)
            s = s.replace("%arg{" + i + "}%", args[i]);
        return Utils.colorString(s);
    }

    protected String[] a(String[] args) {
        return a(args, 1);
    }

    protected String[] a(String[] args, int firstIndex) {
        String[] var1 = new String[args.length - firstIndex];

        for (int i = firstIndex; i < args.length; i++)
            var1[i - firstIndex] = args[i];

        return var1;
    }

    /**
     * <pre>
     * String[] =
     *  [0] : usage
     *  [1] : description (to translate)
     *  [2] : permission
     * </pre>
     * @author MađeShirő ƵÆsora
     * @since NB 1.1.9 (snapshot 12w0t5 'b')
     * @version NB 1.2
     */
    public class HelpLibrary extends HashMap<String, String[]> {

        public void printHelpPage(CommandSender sender, int page) {
            if (page > getPageCount() || page < 1)
                sender.sendMessage("§c" + tl("Invalid help page index !") + " [1 -> " + getPageCount() + "]");
            else {
                page--;
                ArrayList<String> keys = new ArrayList<>(keySet());
                sender.sendMessage("§7----- Help §e[" + (page+1) + "/" + getPageCount() + "] §7-----");
                for (int i = 0; i < 6 && i + (6*page) < keys.size(); i++) {
                    sender.sendMessage("§6/" + keys.get(i) + ": §7" + tl(get(keys.get(i))[1], getTlFile()));
                }
            }
        }

        public void printHelpCmd(CommandSender sender, String cmd) {
            sender.sendMessage("§eHelp for «§9 " + getLabel() + "'s command§e » :");
            sender.sendMessage("§6Usage: §e" + get(cmd)[0]);
            sender.sendMessage("§7" + tl(get(cmd)[1], getTlFile()));
            sender.sendMessage("§4Permission: §c" + get(cmd)[2]);
        }

        private int getPageCount() {
            return (size() + (6 - (size() % 6))) / 6;
        }

        public String[] b(String usage, String desc, String perm) {
            return new String[] {usage, desc, perm};
        }
    }

    public abstract class AdvanceHelpLibrary extends HelpLibrary {
        public abstract List<String> tabComplete(CommandSender sender, String label, String[] args);
    }

    /**
     * Sends a message to tell the sender that he put invalid arguments when sending
     * a command.
     * @param sender The command's sender
     * @param tryThis The command to try instead
     */
    protected final void invalidArgs(CommandSender sender, String tryThis) {
        sender.sendMessage("§c" + ctl("s:c:invalid_args_send"));
        if (tryThis != null && !tryThis.isEmpty())
            sender.sendMessage("§eTry: /".concat(tryThis));
    }

    protected final void invalidSenderWplayer(CommandSender sender) {
        sender.sendMessage("§c" + ctl("s:c:waiting_player_sender"));
    }

    protected final void invalidSenderWcmd(CommandSender sender) {
        sender.sendMessage("§c" + ctl("s:c:waiting_console_sender"));
    }

    protected final void invalidArgsLength(CommandSender sender) {
        sender.sendMessage("§c" + ctl("s:c:invalid_args_length"));
    }

    public static class UnassignedPlayerException extends RuntimeException {
        public UnassignedPlayerException() {
            super();
        }
    }
}
