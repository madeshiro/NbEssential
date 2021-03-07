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

import nb.essential.essentials.jail.JailManager;
import nb.essential.files.TranslationFile;
import nb.essential.loader.DataLoader;
import nb.essential.loader.DataLoadingException;
import nb.essential.loader.NbCommand;
import nb.essential.player.NbPlayer;
import nb.essential.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.util.Calendar;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import nb.essential.scheduler.NbScheduler;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 23/08/2016
 *
 * @author MađeShirő ƵÆsora
 * @since NB 0.1.0 (Beta 1.0b)
 * @version NB 1.2
 */
public class NbEssential {

    private static final CraftNbServer nbServer = new CraftNbServer();
    private static final JailManager jailManager = new JailManager();

    /**
     * Gets the logger for this plugin.
     * @return the logger of NbEssential's plugin.
     */
    public static Logger getLogger() {
        return Main.logger();
    }

    /**
     * Gets the NbEssential's plugin instance.
     * @return The NbEssential's plugin instance.
     */
    public static JavaPlugin getPlugin() {
        return Main.plugin();
    }

    /**
     * Gets if the plugin is currently in DEBUG-MODE
     * @see Main#enableDebug
     * @return True if debug is enabled, false otherwise.
     */
    public static boolean isPluginDebug() {
        return Main.enableDebug;
    }

    /**
     * <p>
     * Enabled or disabled the {@code NbSecurity}. The {@code NbSecurity} is a
     * simple system used to disable interaction between the player and the world
     * via the Plugin. In other words, all NbEssential's listeners and commands
     * are disabled.
     * </p>
     * <p>
     * Generally, the security is activated when an error occured in a listener like
     * the {@link org.bukkit.event.player.PlayerMoveEvent PlayerMoveEvent}'s listener
     * or {@link org.bukkit.event.player.PlayerInteractEntityEvent PlayerInteractEntityEvent}'s
     * listener per example.
     * </p>
     *
     * @see #getSecurityStatus()
     *
     * @param flag A boolean, if on, the security is activated else, the security is disabled.
     * @since NB 1.2 (snapshot 12w0t6 'c')
     */
    public static void setSecurityOn(boolean flag) {
        Main.disableInteraction = flag;
    }

    /**
     * Gets the NbSecurity's system status.
     *
     * @see #setSecurityOn(boolean)
     * @return The NbSecurity's system status.
     * @since NB 1.2 (snapshot 12w0t6 'c')
     */
    public boolean getSecurityStatus() {
        return Main.disableInteraction;
    }

    /**
     * Translates the sentence passed as argument using the {@code Translator}.
     * If the sentence isn't registered in the {@code Translator}, the command
     * will return {@code str}.
     *
     * <h5>Note:</h5>
     * This method will never return null. However, if the argument is {@code null},
     * the function will return an empty string.
     *
     * @param str The sentence to translate.
     * @return The translation of the passed argument if exists, else return the argument.
     */
    public static String tl(String str) {
        return tl(str, TranslationFile.originalFile);
    }

    /**
     * Translates the sentence passed as argument using the {@code Translator}.
     * If the sentence isn't registered in the {@code Translator}, the command
     * will return {@code str}.
     *
     * <h5>Note:</h5>
     * This method will never return null. However, if the argument is {@code null},
     * the function will return an empty string.
     *
     * @param str The sentence to translate.
     * @param player The player used to replace specific string to anothers according
     *               to player's caracteristics.
     * @return The translation of the passed argument if exists, else return the argument.
     */
    public static String tl(String str, Player player) {
        return tl(str, player, TranslationFile.originalFile);
    }

    /**
     * Translates the sentence passed as argument using the {@code Translator}.
     * If the sentence isn't registered in the {@code Translator}, the command
     * will return {@code str}.
     *
     * <h5>Note:</h5>
     * This method will never return null. However, if the argument is {@code null},
     * the function will return an empty string.
     *
     * @param str The sentence to translate.
     * @param tf The translation file to use.
     * @return The translation of the passed argument if exists, else return the argument.
     */
    public static String tl(String str, TranslationFile tf) {
        return Utils.colorString(tf.getSentence(str).replace("%server%", getServer().getName()));
    }

    public static String tl(String str, TranslationFile tf, IServer.SupportedLanguage lang) {
        return Utils.colorString(tf.getSentence(str, lang).replace("%server%", getServer().getName()));
    }

    /**
     * Translates the sentence passed as argument using the {@code Translator}.
     * If the sentence isn't registered in the {@code Translator}, the command
     * will return {@code str}.
     *
     * <h5>Note:</h5>
     * This method will never return null. However, if the argument is {@code null},
     * the function will return an empty string.
     *
     * @param str The sentence to translate.
     * @param player The player used to replace specific string to anothers according
     *               to player's caracteristics.
     * @param tf The translation file to use.
     * @return The translation of the passed argument if exists, else return the argument.
     */
    public static String tl(String str, Player player, TranslationFile tf) {
        String s = tl(str, tf);
        return s.replace("%player%", player.getDisplayName())
                .replace("%server%", getServer().getName())
                .replace("%uuid%", player.getUniqueId().toString());
    }

    /**
     * Translates the sentence passed as argument using the {@code Translator}.
     * If the sentence isn't registered in the {@code Translator}, the command
     * will return {@code str}.
     *
     * <h5>Note:</h5>
     * This method will never return null. However, if the argument is {@code null},
     * the function will return an empty string.
     *
     * @param str The sentence to translate.
     * @param player The player used to replace specific string to anothers according
     *               to player's caracteristics.
     * @return The translation of the passed argument if exists, else return the argument.
     */
    public static String tl(String str, NbPlayer player) {
        return tl(str, player, TranslationFile.originalFile);
    }

    /**
     * Translates the sentence passed as argument using the {@code Translator}.
     * If the sentence isn't registered in the {@code Translator}, the command
     * will return {@code str}.
     *
     * <h3>Note:</h3>
     * This method will never return null. However, if the argument is {@code null},
     * the function will return an empty string.
     *
     * @param str The sentence to translate.
     * @param player The player used to replace specific string to anothers according
     *               to player's caracteristics.
     * @param tf The translation file to use.
     * @return The translation of the passed argument if exists, else return the argument.
     */
    public static String tl(String str, NbPlayer player, TranslationFile tf) {
        String s = tl(str, tf, player.getProfileDescriptor().getLang());
        return s.replace("%player%", player.getDisplayName())
                .replace("%uuid%", player.getUniqueId().toString())
                .replace("%prefix%", player.getPrefix() == null ? "" : player.getPrefix())
                .replace("%nickname%", player.getNickname());
    }

    /**
     * Gets the scheduler of the NbEssential plugin.
     *
     * @see NbScheduler
     * @see nb.essential.scheduler.IScheduler IScheduler
     *
     * @return the scheduler of NbEssential
     * @since NB 1.0
     */
    public static NbScheduler getScheduler() {
        return NbScheduler.getInstance();
    }

    /**
     * Gets the server's representation for NbEssential.
     *
     * @see IServer
     * @see #getBukkitServer()
     * @return the server's representation for NbEssential.
     */
    public static IServer getServer() {
        return nbServer;
    }

    /**
     * Gets the server's representation for Bukkit/Spigot
     * @return the server's representation for Bukkit/Spigot
     */
    public static Server getBukkitServer() {
        return Bukkit.getServer();
    }

    /**
     * Gets the NbEssential's data loader. This one contains all "database" of
     * the plugin.
     * @return The {@link DataLoader} of NbEssential.
     */
    public static DataLoader getDataLoader() {
        return Main.nbessential_dataloader;
    }

    /**
     * Gets the NbEssential's jail manager.
     *
     * @see JailManager
     * @see nb.essential.essentials.jail.Jail Jail
     * @return the server's jail manager.
     */
    public static JailManager getJailManager() {
        return jailManager;
    }

    /**
     * Reloads the entire plugins including all config files and data in
     * the {@link nb.essential.loader.PluginData PluginData}.
     * @return A boolean, true if the reload mechanism worked great otherwise, false.
     */
    public static boolean reloadPlugin() {
        boolean status = getServer().reload();
        status = TranslationFile.originalFile.reload() && status;
        status = TranslationFile.commandFile.reload() && status;
        try {
            getDataLoader().reload();
        } catch (DataLoadingException e) {
            e.printStackTrace();
            return false;
        }

        return status;
    }

    /**
     * Saves the plugin.
     * @return True if the save process totally worked fine, false otherwise.
     */
    public static boolean savePlugin() {
        boolean status = getServer().save();
        try {
            getDataLoader().save();
        } catch (DataLoadingException e) {
            e.printStackTrace();
            return false;
        }

        return status;
    }

    /**
     * Gets the current day as String with format = 'jj-mm-yyyy'
     * @return The current day as String.
     */
    public static String getDayString() {
        String day = Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        if (day.length() == 1) day = "0" + day;

        String month = Integer.toString(Calendar.getInstance().get(Calendar.MONTH) + 1);
        if (month.length() == 1) month = "0" + month;

        String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));

        return day + "-" + month + "-" + year;
    }

    /**
     * Gets an NbCommand instance.
     * @param alias The alias associated with the command.
     * @return A {@link NbCommand} if founded, null otherwise.
     */
    public static NbCommand getNbCommand(String alias) {
        return Main.nbessential_cmdloader.call(alias);
    }

    /**
     * Gets the {@link MainData}.
     * @return {@link MainData}.
     */
    public static MainData getMainData() {
        return getDataLoader().getData("main");
    }
}
