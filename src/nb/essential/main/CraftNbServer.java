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
import nb.essential.utils.Utils;
import org.bukkit.plugin.PluginManager;
import zaesora.madeshiro.protocollib.ProtocolLib;
import nb.essential.player.NbPlayer;
import nb.essential.scheduler.IScheduleRunnable;
import nb.essential.scheduler.NbScheduler;
import net.minecraft.server.v1_13_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import static nb.essential.player.PlayerSystem.*;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 11/11/2016
 */
public class CraftNbServer implements IServer {

    private List<String> worldList;

    public MainData getMainData() {
        return NbEssential.getDataLoader().getData("main");
    }

    @Override
    public boolean save() {
        return getMainData().save();
    }

    @Override
    public boolean reload() {
        return getMainData().reload();
    }

    @Override
    public Logger getLogger() {
        return getNbPlugin().getLogger();
    }

    @Override
    public Plugin getNbPlugin() {
        return Main.plugin();
    }

    @Override
    public List<World> getWorlds() {
        return getBukkitServer().getWorlds();
    }

    @Override
    public Server getBukkitServer() {
        return Bukkit.getServer();
    }

    @Override
    public MinecraftServer getMCServer() {
        return ((CraftServer) getBukkitServer()).getServer();
    }

    @Override
    public PluginManager getPluginManager() {
        return getBukkitServer().getPluginManager();
    }

    @Override
    public BukkitScheduler getBukkitScheduler() {
        return Bukkit.getScheduler();
    }

    @Override
    public String getName() {
        return getMainData().getServerName();
    }

    @Override
    public JSONFile getConfigFile() {
        return getMainData().getConfigFile();
    }

    @Override
    public NbScheduler getScheduler() {
        return NbEssential.getScheduler();
    }

    @Override
    public ProtocolLib getProtocolLib() {
        return ProtocolLib.getInstance();
    }

    @Override
    public int scheduleSyncTask(Runnable run) {
        return getBukkitScheduler().scheduleSyncDelayedTask(getNbPlugin(), run);
    }

    @Override
    public int scheduleSyncDelayedTask(Runnable run, long delay) {
        return getBukkitScheduler().scheduleSyncDelayedTask(getNbPlugin(), run, delay);
    }

    @Override
    public int scheduleSyncRepeatingTask(Runnable run, long delay, long period) {
        return getBukkitScheduler().scheduleSyncRepeatingTask(getNbPlugin(), run, delay, period);
    }

    @Override
    public int nbScheduleSyncTask(IScheduleRunnable runnable) {
        return getScheduler().scheduleSyncTask(runnable);
    }

    @Override
    public int nbScheduleSyncDelayedTask(IScheduleRunnable run, long delay) {
        return getScheduler().scheduleSyncDelayedTask(run, delay);
    }

    @Override
    public int nbScheduleSyncRepeatingTask(IScheduleRunnable run, long delay, long period) {
        return getScheduler().scheduleSyncRepeatingTask(run, delay, period);
    }

    @Override
    public Player getPlayer(String name) {
        return Bukkit.getPlayer(name);
    }

    @Override
    public Player getPlayerByNickname(String nickname) {
        return Nb_GetPlayer(nickname).getBukkitPlayer();
    }

    @Override
    public NbPlayer getNbPlayer(String name) {
        return Nb_GetPlayer(Bukkit.getPlayer(name));
    }

    @Override
    public NbPlayer getNbPlayer(Player player) {
        return Nb_GetPlayer(player);
    }

    @Override
    public NbPlayer getNbPlayerByNickname(String nickname) {
        return Nb_GetPlayer(nickname);
    }

    @Override
    public void broadcastMsg(String msg) {
        Bukkit.broadcastMessage(msg);
    }

    @Override
    public void broadcastMsgExcept(String msg, List<Player> except) {
        for (Player player : getOnlinePlayers()) {
            NbPlayer nbPlayer = getNbPlayer(player);
            if (!except.contains(player)) {
                if (nbPlayer != null)
                    player.sendMessage(Utils.colorString(NbEssential.tl(msg, nbPlayer)));
                else
                    player.sendMessage(Utils.colorString(NbEssential.tl(msg, player)));
            }
        }
    }

    @Override
    public String getMOTD() {
        return getMainData().getMOTD();
    }

    @Override
    public void setMOTD(String motd) {
        getMainData().setMOTD(motd);
    }

    @Override
    public String getLanguage() {
        return SupportedLanguage.FR_fr.name();
    }

    public SupportedLanguage getServerLanguage() {
        return SupportedLanguage.valueOf(getLanguage());
    }

    @Override
    public List<String> getWorldList() {
        if (worldList == null || worldList.size() < getWorlds().size()) {
            worldList = new ArrayList<>();
            for (World world : getWorlds())
                worldList.add(world.getName());
        }

        return worldList;
    }

    @Override
    public World getWorld(String name) {
        return Bukkit.getWorld(name);
    }

    @Override
    public Collection<? extends Player> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers();
    }
}
