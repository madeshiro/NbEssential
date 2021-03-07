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

import nb.citizens.NPCAPI;
import nb.essential.loader.NbCommand;
import nb.essential.player.NbPlayer;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CitizensCmd extends NbCommand {

    private static NPC npc;

    @Override
    public boolean doCommand(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player) || !sender.isOp())
            return false;

        switch (args.length) {
            case 1:
                switch (args[0]) {
                    case "test-1":
                        npc = NPCAPI.createNPC(((Player) sender).getLocation(), (Player) sender);
                        break;
                    case "test-2":
                        npc.getNavigator().setTarget(((NbPlayer) sender).getBukkitPlayer(), false);
                        npc.getEntity().setCustomName("gjeajôj");
                        break;
                }
        }

        return true;
    }

    @Override
    public String getLabel() {
        return "citizens";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"npc", "citi"};
    }

    @Override
    protected HelpLibrary getHelpLibrary() {
        return new HelpLibrary();
    }

    @Override
    public List<String> getTabCompletion(CommandSender sender, String label, String[] args) {
        switch (args.length) {
            case 1:
                return Arrays.asList("test-1", "test-2");
            default:
                return Collections.emptyList();
        }
    }
}
