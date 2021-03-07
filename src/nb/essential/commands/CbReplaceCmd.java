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

import nb.essential.loader.EventListener;
import nb.essential.player.NbPlayer;
import nb.essential.utils.Utils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.craftbukkit.v1_13_R1.block.CraftCommandBlock;

import nb.essential.main.NbEssential;
import nb.essential.loader.NbCommand;
import nb.essential.player.CraftNbPlayer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class of NbEssential
 */
@EventListener
public class CbReplaceCmd extends NbCommand implements Listener {

    private final HelpLibrary helpLibrary;

    CbReplaceCmd() {
        helpLibrary = new HelpLibrary();

        helpLibrary.put("cbr help", new String[] {
                "/cbr <clear|replace|zconfirm>",
                "s:c:cbreplace:cbr_help",
                "nbessential.cbreplace.*"
        });
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

        switch (args[0]) {
            case "zconfirm":
                if (!sender.hasPermission("nbessential.cbreplace.zconfirm"))
                    return false;
                else if (!(sender instanceof NbPlayer))
                    invalidSenderWplayer(sender);
                else {
                    if (((CraftNbPlayer) sender).getVariables().selectCBZone())
                        sender.sendMessage(ctl("s:c:cbreplace:zconfirm_succeed", sender));
                    else
                        sender.sendMessage(ctl("s:c:cbreplace:zconfirm_fail", sender));
                }
                break;
            case "clear":
                if (!sender.hasPermission("nbessential.cbreplace.clear"))
                    return false;
                else if (!(sender instanceof NbPlayer))
                    invalidSenderWplayer(sender);
                else {
                    ((CraftNbPlayer) sender).getVariables().clearCBSelection();
                    sender.sendMessage(ctl("s:c:cbreplace:clear", sender));
                }
                break;
            case "replace":
                if (!sender.hasPermission("nbessential.cbreplace.replace"))
                    return false;
                else if (!(sender instanceof NbPlayer))
                    invalidSenderWplayer(sender);
                else {
                    cmd_replace((CraftNbPlayer) sender);
                }
                break;
            case "help":case "?":
            default:
                if (!sender.hasPermission("nbessential.cbreplace.help"))
                    return false;
                getHelpLibrary().printHelpCmd(sender, "cbr help");
        }

        return true;
    }

    private void cmd_replace(CraftNbPlayer sender) {
        if (sender.getVariables().CBChangeStatus != 0) {
            sender.sendMessage(ctl("s:c:cbreplace:already_working", sender));
        } else if ((!sender.getVariables().CBSel.isEmpty()
            || (sender.getVariables().CBLoc1 != null && sender.getVariables().CBLoc2 != null)))
        {
            sender.getVariables().CBChangeStatus++;
            sender.sendMessage(ctl("s:c:cbreplace:step_1"));
        } else
            sender.sendMessage(ctl("s:c:cbreplace:replace_fail"));
    }

    /**
     * Gets the label of this command.
     *
     * @return the command's label
     */
    @Override
    public String getLabel() {
        return "cbreplace";
    }

    /**
     * Gets the aliases for this command.
     *
     * @return the aliases assigned to this command.
     */
    @Override
    public String[] getAliases() {
        return new String[] {"cbr"};
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
                return Arrays.asList("zconfirm", "replace", "clear");
            default:
                return Collections.emptyList();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMakeChange(AsyncPlayerChatEvent event) {
        CraftNbPlayer player = (CraftNbPlayer) NbEssential.getServer().getNbPlayer(event.getPlayer());
        if (player == null || player.getVariables().CBChangeStatus == 0)
            return;
        else
            event.setCancelled(true);

        switch (player.getVariables().CBChangeStatus) {
            case 1:
                player.getVariables().CBStringTarget = event.getMessage();
                player.getVariables().CBChangeStatus++;
                player.sendMessage(ctl("s:c:cbreplace:step_2"));
                break;
            case 2:
                player.getVariables().CBStringReplacement = event.getMessage();
                player.getVariables().CBChangeStatus = 0;

                a(player);
                break;
        }
    }

    private void a(CraftNbPlayer player) {
        Location CBLoc1 = player.getVariables().CBLoc1,
                CBLoc2 = player.getVariables().CBLoc2;
        if (!(CBLoc1 == null || CBLoc2 == null ||
                !CBLoc1.getWorld().equals(CBLoc2.getWorld()))){

            int minX = Math.min(CBLoc1.getBlockX(), CBLoc2.getBlockX()),
                    maxX = Math.max(CBLoc1.getBlockX(), CBLoc2.getBlockX());
            int minY = Math.min(CBLoc1.getBlockY(), CBLoc2.getBlockY()),
                    maxY = Math.max(CBLoc1.getBlockY(), CBLoc2.getBlockY());
            int minZ = Math.min(CBLoc1.getBlockZ(), CBLoc2.getBlockZ()),
                    maxZ = Math.max(CBLoc1.getBlockZ(), CBLoc2.getBlockZ());

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        Block block = new Location(CBLoc1.getWorld(), x, y, z).getBlock();
                        if (Utils.isCommandBlock(block)) {
                            b(block,
                                player.getVariables().CBStringTarget,
                                player.getVariables().CBStringReplacement);
                        }
                    }
                }
            }
        }

        for (Location block : player.getVariables().CBSel)
            b(block.getBlock(), player.getVariables().CBStringTarget,
                    player.getVariables().CBStringReplacement);

        player.sendMessage(ctl("s:c:cbreplace:change_done", player));
    }

    private void b(Block block, String target, String replacement) {
        if (Utils.isCommandBlock(block)) {
            CraftCommandBlock cmdBlock = new CraftCommandBlock(block);
            String cmd = cmdBlock.getCommand();
            cmdBlock.setCommand(cmd.replace(target, replacement));
            cmdBlock.update();
        }
    }
}
