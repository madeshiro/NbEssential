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

package nb.essential.main;

import nb.essential.files.JSONFile;
import org.bukkit.plugin.PluginManager;
import zaesora.madeshiro.protocollib.ProtocolLib;
import nb.essential.player.NbPlayer;
import nb.essential.scheduler.IScheduleRunnable;
import nb.essential.scheduler.NbScheduler;
import net.minecraft.server.v1_13_R1.MinecraftServer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 28/08/2016
 */
public interface IServer {

    boolean save();
    boolean reload();

    Logger getLogger();
    Plugin getNbPlugin();
    List<World> getWorlds();
    Server getBukkitServer();
    MinecraftServer getMCServer();
    PluginManager getPluginManager();
    BukkitScheduler getBukkitScheduler();
    Collection<? extends Player> getOnlinePlayers();

    String getName();
    JSONFile getConfigFile();
    NbScheduler getScheduler();
    ProtocolLib getProtocolLib();

    int scheduleSyncTask(Runnable run);
    int scheduleSyncDelayedTask(Runnable run, long delay);
    int scheduleSyncRepeatingTask(Runnable run, long delay, long period);

    int nbScheduleSyncTask(IScheduleRunnable runnable);
    int nbScheduleSyncDelayedTask(IScheduleRunnable run, long delay);
    int nbScheduleSyncRepeatingTask(IScheduleRunnable run, long delay, long period);

    Player getPlayer(String name);
    Player getPlayerByNickname(String nickname);

    NbPlayer getNbPlayer(String name);
    NbPlayer getNbPlayer(Player player);
    NbPlayer getNbPlayerByNickname(String nickname);

    void broadcastMsg(String msg);
    void broadcastMsgExcept(String msg, List<Player> except);

    String getMOTD();
    void setMOTD(String motd);

    String getLanguage();

    List<String> getWorldList();

    World getWorld(String name);

    enum SupportedLanguage {
        FR_fr,
        // FR_ca,
        EN_en
        ;

        private static SupportedLanguage defaultLanguage = EN_en;
        public static void setDefaultLanguage(SupportedLanguage language) {
            defaultLanguage = language;
        }

        public static SupportedLanguage getDefaultLanguage() {
            return defaultLanguage;
        }

        public static String getCorrectString(String var1) {
            for (SupportedLanguage l : values())
                if (l.toString().equalsIgnoreCase(var1))
                    return l.toString();
            return defaultLanguage.toString();
        }
    }
}
