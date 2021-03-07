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

import java.io.File;
import java.util.logging.Logger;

import nb.essential.loader.*;
import nb.script.ScriptManager;
import nb.script.type.objects.NbslNPC;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import nb.essential.utils.CustomTabCompleter;
import nb.essential.event.PluginShutdownEvent;
import zaesora.madeshiro.api.JSONAPI;

import static nb.essential.main.NbEssential.*;
import static nb.essential.player.PlayerSystem.*;

/**
 * This class is the plugin's main class. Inheriting from JavaPlugin, it represents
 * the plugin at Bukkit/Spigot. Also, it permits the data and file's loading by
 * using the differents loaders available. The loader will especially supports
 * commands, listeners and data of NbEssential and all these subsystems.
 *
 * @see nb.essential.loader Loader Package
 * @see #loadPlugin(boolean)
 *
 * @author MađeShirő ƵÆsora
 * @since NB 0.1.0 (Beta 1.0b)
 * @version NB 1.2
 */
public final class Main extends JavaPlugin {

    /**
     * a security - to disable interaction
     */
    static boolean disableInteraction = false;

    /**
     * a boolean to enable debug messages.
     */
    static boolean enableDebug = true;

    /**
     * The JavaPlugin's instance
     */
    static JavaPlugin nbessential_plugin;
    /**
     * The NbEssential's data loader
     */
    static DataLoader nbessential_dataloader;
    /**
     * The NbEssential's command loader
     */
    static CommandLoader nbessential_cmdloader;
    /**
     * The NbEssential's listener loader
     */
    static ListenerLoader nbessential_lstloader;

    // All methods inherit from JavaPlugin
    // @{

    /**
     * Called when this plugin is enabled
     */
    @Override
    public void onEnable() {
        // init essential var
        nbessential_plugin = this;

        logger().info("Loading operation in progress... this may take a long time, please wait.");
        long millis = System.currentTimeMillis();

        if (enableDebug)
            logger().info("DEBUG MODE: activated !");

        // Start the loading process
        // logDetails = true -> the plugin is currently in beta testing
        checkDependency();
        loadPlugin(enableDebug);

        logger().info("Plugin loaded (" + (System.currentTimeMillis() - millis) + " ms).");
    }

    /**
     * Called when this plugin is disabled
     */
    @Override
    public void onDisable() {

        // Call all methods which will register useful data
        // e.g: see the StickopInteractListener
        getServer().getPluginManager().callEvent(new PluginShutdownEvent(this));

        try {
            nbessential_dataloader.save();
        } catch (DataLoadingException e) {
            e.printStackTrace();
            getLogger().warning("Some errors occured when saving data..." +
                    " Check logs to get more details about it");
        }

        getLogger().info("Plugin NbEssential disabled... OK");
    }

    /**
     * Executes the given command, returning its success
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (disableInteraction) {
            switch (label.toLowerCase()) {
                case "nbsecurity":
                    // get NbSecurity command
                    if (!nbessential_cmdloader.call("nbsecurity").doCommand(sender, label, args))
                        sender.sendMessage("§c" + tl("s:command_no_permission"));
                    break;
                default:
                    sender.sendMessage("§cSorry, for security reasons, the NbEssential plugin has been disabled !");
                    if (sender.hasPermission("nbessential.operator"))
                        sender.sendMessage("§eTo reactivate interaction, do /nbsecurity disable");
                    else
                        sender.sendMessage("§cIf you think it's an error, called an operator...");
                    break;
            }
        } else {

            NbCommand cmd = nbessential_cmdloader.call(label);
            if (cmd == null)
                return false; // command doesn't exists ...
            try {
                if (sender instanceof Player && (!Nb_IsInstantiate((Player) sender))) {
                    if (cmd.getLabel().equals("profile")) {
                        if (!cmd.doCommand(sender, label, args))
                            sender.sendMessage("§c" + tl("s:command_no_permission"));
                    } else
                        sender.sendMessage("§c" + tl("s:unassigned_player_exception"));
                }
                else if (!cmd.doCommand(
                  (sender instanceof Player ? Nb_GetPlayer((Player) sender) : sender),
                        label, args))
                    sender.sendMessage("§c" + tl("s:command_no_permission"));
            } catch (Exception e) {
                e.printStackTrace();
                // sender.sendMessage("§c" + tl(e.getMessage()));
            }
        }

        return true;
    }

    // @}
    // -------------------------- //
    // All methods used when #onEnable() is called
    // @{

    /**
     * <p>
     * This function permit to load all differents plugin's components including
     * commands and listeners. For listeners, they will be stored in the Bukkit/Spigot
     * register.
     * <h5><u>Warning</u></h5>
     * If a critical error happens then the function will return false and the
     * exception's stack trace will be displayed.
     * </p>
     * <p>
     * If param <code>logDetails</code> equals true, all loaders mechanism details
     * will be logged in the console. We recommand to switch on this parameter only
     * when the plugin is in the debugging stage or if the current version is a
     * snapshot.
     * </p>
     *
     * @param logDetails A boolean to determine if all details must be logged in the console
     * @return True if the plugin was loaded with success, otherwise false;
     */
    private boolean loadPlugin(boolean logDetails) {
        nbessential_dataloader = new DataLoader(logDetails);
        nbessential_cmdloader = new CommandLoader(logDetails);
        nbessential_lstloader = new ListenerLoader(logDetails);

        // loadState : a boolean, if true, the loading process doesn't encounter error
        // otherwise, an exception occured and the function will return false;
        boolean loadState = loadData();

        Nb_LoadPlayerSystem();

        try {
            loadCommands();
        } catch (Exception e) {
            getLogger().severe("An exception occured when trying to load commands");
            if (logDetails)
                e.printStackTrace();
            else getLogger().severe("Error: " + e.getMessage());
            loadState = false;
        }

        try {
            loadListeners();
        } catch (Exception e) {
            getLogger().severe("An exception occured when trying to load listeners");
            if (logDetails)
                e.printStackTrace();
            else getLogger().severe("Error: " + e.getMessage());
            loadState = false;
        }

        if (JSONAPI.Nb_JAPI_Create(nbessential_dataloader.<MainData>getData("main").getAPIPort()) && JSONAPI.Nb_JAPI_Start())
            getLogger().info("[JSONAPI] Start JSONAPI on port " + JSONAPI.Nb_JAPI_GetPort());
        else
            getLogger().warning("[JSONAPI] Unable to start JSONAPI");

        return loadState;
    }

    /**
     * Called to load commands classes of NbEssential and these subplugins.
     *
     * @throws Exception If an exception occured when trying to load commands
     * or if the loader is null.
     */
    private void loadCommands() throws Exception {
        nbessential_cmdloader.load();

        for (NbCommand cmd : nbessential_cmdloader) {
            PluginCommand pluginCommand = getCommand(cmd.getLabel());
            if (pluginCommand != null)
                pluginCommand.setTabCompleter(new CustomTabCompleter(cmd));
            else
                getLogger().warning("The command '" + cmd.getLabel() + "' can't have an assigned TabCompleter");
        }
    }

    /**
     * Called to load listeners classes of NbEssential and thses subplugins.
     *
     * @throws Exception If an exception occured when trying to load listeners
     * or if the loader is null.
     */
    private void loadListeners() throws Exception {
        nbessential_lstloader.load();
    }

    /**
     * Called to load and enable the PluginData's classes of NbEssential and these
     * subplugins.
     *
     * @throws NullPointerException If the loader is null.
     * @return a Boolean, true if all data have been loaded correctly, otherwise false.
     */
    private boolean loadData() throws NullPointerException {
        try {
            nbessential_dataloader.load();
        } catch (Exception e) {
            if(e instanceof NullPointerException)
                throw (NullPointerException) e;

            e.printStackTrace();
            return false;
        }

        return true;
    }

    // TODO transfer function to FileReparator at utils package
    private void checkDependency() {
        File[] nb_essential_dir = new File[] {
                new File("plugins/NbEssential/"),
                new File("plugins/NbEssential/logs"),
                new File("plugins/NbEssential/player"),
                new File("plugins/NbEssential/blockmeta"),
                new File("plugins/NbEssential/defaultFiles")
        };
        for (File file : nb_essential_dir) {
            if (!file.exists())
                file.mkdir();
        }

        if (!getServer().getPluginManager().isPluginEnabled("Citizens")) {
            logger().severe("Citizens 2.0 not found or not enabled");
        } else {
            if (isPluginDebug())
                ScriptManager.Nb_SL_LogInfo("Register custom trait (NbslNPCTrait) in Citizens 2.0 Trait Factory");
            CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(NbslNPC.NbslNPCTrait.class));
        }
    }

    // @}
    // -------------------------- //
    // All static methods used by NbEssential
    // @{

    /**
     * Gets the logger associated with the NbEssential's JavaPlugin
     * @return the logger associated with NbEssential's JavaPlugin
     */
    protected static Logger logger() {
        return nbessential_plugin.getLogger();
    }

    /**
     * Gets the JavaPlugin's instance for NbEssential
     * @return the JavaPlugin's instance for NbEssential
     */
    protected static JavaPlugin plugin() {
        return nbessential_plugin;
    }

    /**
     * Gets the current plugin's version
     * @return the current NbEssential's version
     */
    protected static String getVersion() {
        return "1.~2 pre-release 2 for MC1.13 (1.2 18w7b2)";
    }

    // @}
}
