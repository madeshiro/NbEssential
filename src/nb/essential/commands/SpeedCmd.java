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
import nb.essential.loader.NbCommand;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import static nb.essential.main.NbEssential.getServer;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 02/10/2016
 */
public class SpeedCmd extends NbCommand {

    private final HelpLibrary helpLibrary = new HelpLibrary();

    // equivalent of old static block
    public SpeedCmd() {
        helpLibrary.put("speed help", new String[] {
                "/speed <float> [player]",
                "s:c:speed:speed_help",
                "nbessential.speed"
            }
        );
    }

    @Override
    public boolean doCommand(CommandSender sender, String alias, String[] args) {

        if (!sender.hasPermission("nbessential.speed"))
            return false;

        switch (args.length) {
            case 0:
                helpLibrary.printHelpCmd(sender, "speed help");
                break;
            case 1:
                if (args[0].equalsIgnoreCase("help"))
                    helpLibrary.printHelpCmd(sender, "speed help");
                else if (!(sender instanceof Player))
                    invalidSenderWplayer(sender);
                else {
                    try {
                        float[] speeds = args[0].equalsIgnoreCase("normal") ?
                                new float[] {0.1f, 0.2f} : toMCSpeed(Float.parseFloat(args[0]));
                        ((Player) sender).setFlySpeed(speeds[0]);
                        ((Player) sender).setWalkSpeed(speeds[1]);
                        sender.sendMessage("§e" + ctl("s:c:speed:player_change_speed", (Player) sender).replace("%arg{0}%", args[0]));
                    } catch (Exception e) {
                        sender.sendMessage("§c" + e.getMessage());
                    }
                }
                break;
            case 2:
                Player player = getServer().getPlayer(args[1]);
                if (player == null)
                    sender.sendMessage("§c" + ctl("s:c:player_not_found").replace("%arg{0}%", args[1]));
                else {
                    try {
                        float[] speeds = args[0].equalsIgnoreCase("normal") ?
                                new float[] {0.1f, 0.2f} : toMCSpeed(Float.parseFloat(args[0]));
                        player.setFlySpeed(speeds[0]);
                        player.setWalkSpeed(speeds[1]);
                        player.sendMessage("§e" + ctl("s:c:speed:player_change_speed", player).replace("%arg{0}%", args[0]));
                    } catch (Exception e) {
                        sender.sendMessage("§3" + e.getMessage());
                    }
                }
                break;
            default:
                invalidArgsLength(sender);
                break;
        }

        return true;
    }

    @Override
    public String getLabel() {
        return "speed";
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
        switch (args.length) {
            case 1:
                if (args[0].length() > 0 && "normal".startsWith(args[0]))
                    return Collections.singletonList("normal");
                else
                    return Collections.emptyList();
            case 2:
                return Arrays.asList(getServer().getMCServer().getPlayers());
            default:
                return Collections.emptyList();
        }
    }

    private float[] toMCSpeed(float sp) throws IllegalArgumentException {
        if (sp > 10 || sp < 0)
            throw new IllegalArgumentException("Invalid number. Waiting a value between 0 and 10");

        float[] speed = new float[2];
            speed[0] = (sp == 1 ? 0.1f : sp / 10);

        speed[1] = (sp == 1 ? 0.2f : ((sp * 8 / 10) + 2) / 10);
        return speed;
    }
}
