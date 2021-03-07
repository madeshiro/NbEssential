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

package nb.essential.utils;

import nb.essential.loader.NbCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.ArrayList;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 30/08/2016
 */
public class CustomTabCompleter implements TabCompleter {

    /**
     * The assigned command.
     */
    private NbCommand assignedCommand;

    /**
     * Creates a new CustomTabCompleter for a specific {@link NbCommand}.
     * @param cmd The command to assign the {@code TabCompleter}.
     */
    public CustomTabCompleter(NbCommand cmd) {
        this . assignedCommand = cmd;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        String lastElementFind = "";
        ArrayList<String> list = new ArrayList<>();
        for (String str : assignedCommand.getTabCompletion(sender, label, args)) {
            if (str.startsWith(args[args.length - 1]) && (lastElementFind.isEmpty() || lastElementFind.length() < str.length())) {
                list.add(0, str);
                lastElementFind = str;
            } else if(str.startsWith(args[args.length-1]))
                list.add(1, str);
        }

        return list;
    }
}
