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
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 02/10/2016
 */
public class ProfileCmd extends NbCommand {

    private final HelpLibrary helpLibrary = new HelpLibrary();
    public ProfileCmd() {
        helpLibrary.put("login", new String[] {
                "/login <profile>",
                "Login in the wanted profile",
                "nbessential.profile.login"
            }
        );
        helpLibrary.put("logout", new String[] {
                "/logout",
                "Sign out of your profile (and may make you quit the game)",
                "nbessential.profile.logout"
            }
        );
        helpLibrary.put("profile current", new String[] {
                "/profile current",
                "Gets information about your profile",
                "nbessential.profile.current"
            }
        );
        helpLibrary.put("profile login", new String[] {
                "/profile login <profile> [player]",
                "Login you (or another player) in a specific profile",
                "nbessential.profile.login[.other]"
            }
        );
        helpLibrary.put("profile logout", new String[] {
                "/profile logout [player] [forceQuit=true]",
                "Sign out you (or another player)",
                "nbessential.profile.logout[.other]"
            }
        );
        helpLibrary.put("profile create", new String[] {
                "/profile create <name> [player]",
                "Creates a new profile",
                "nbessential.profile.create"
            }
        );
        helpLibrary.put("profile remove", new String[] {
                "/profile remove <profile> [player]",
                "Removes a profile",
                "nbessential.profile.remove"
            }
        );
        helpLibrary.put("profile ask", new String[] {
                "/profile ask <player>",
                "Asks a player to choose one of its profiles",
                "nbessential.profile.ask"
            }
        );
    }

    @Override
    public boolean doCommand(CommandSender sender, String alias, String[] args) {
        return true;
    }

    @Override
    public String getLabel() {
        return "profile";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"nbprofile", "login", "logout"};
    }

    @Override
    protected HelpLibrary getHelpLibrary() {
        return helpLibrary;
    }

    @Override
    public List<String> getTabCompletion(CommandSender sender, String label, String[] args) {
        switch (args.length) {
            case 1:
                switch (label) {
                    case "profile":
                        return Arrays.asList("current", "login", "logout", "create", "remove", "ask");
                    case "login":
                    case "logout":
                        return Collections.emptyList();
                }
            default:
                return Collections.emptyList();
        }
    }
}
