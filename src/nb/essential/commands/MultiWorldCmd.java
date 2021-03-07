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
import nb.essential.main.NbEssential;
import nb.essential.player.NbPlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static nb.essential.multiworld.MultiWorldManager.Nb_MW_CreateWorld;

public class MultiWorldCmd extends NbCommand {

    @Override
    public boolean doCommand(CommandSender sender, String alias, String[] args) {

        switch (args.length) {
            case 2:
                switch (args[0]) {
                    case "create":
                        Nb_MW_CreateWorld(args[1], World.Environment.NORMAL);
                        break;
                    case "tp":
                        ((NbPlayer) sender).teleport(Bukkit.getWorld(args[1]).getSpawnLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
                        break;
                }
                break;
        }

        return true;
    }

    @Override
    public String getLabel() {
        return "multiworld";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"mw", "mworld"};
    }

    @Override
    protected HelpLibrary getHelpLibrary() {
        return new HelpLibrary();
    }

    @Override
    public List<String> getTabCompletion(CommandSender sender, String label, String[] args) {
        switch (args.length) {
            case 2:
                switch (args[0]) {
                    case "tp":
                        return NbEssential.getServer().getWorldList();
                    case "create":
                        List<String> list = NbEssential.getServer().getWorldList();
                        for (int i = 0; i < list.size(); i++)
                            list.set(i, "§m"+list.get(i));
                        return list;
                }
            default:
                return Collections.emptyList();
            case 1:
            return Arrays.asList("tp", "create");
        }
    }
}
