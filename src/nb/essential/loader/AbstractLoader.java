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

package nb.essential.loader;

import java.util.logging.Level;
import nb.essential.main.NbEssential;

/**
 * <p>
 * An abstract representation of a Loader. A loader is (in the {@link nb.essential.loader}
 * package) a system to load data, classes, etc... and store it for a specific usage
 * in the NbEssential's plugin and for these subsystems.
 * </p>
 * <p>
 * Currently, there are three known implementations of the {@code AbstractLoader} class. <br/>
 *   <ul>
 *     <li>The {@link CommandLoader} is a system to load the NbEssential's commands.</li>
 *     <li>The {@link ListenerLoader} is a system to load the NbEssential's listeners.</li>
 *     <li>The {@link DataLoader} is a system to load NbEssential's data.</li>
 *   </ul>
 * To get more informations about these classes, we recommand you to check
 * their documentation.
 * @apiNote The {@code AbstractLoader} was made for the version <code>'NB 1.2'</code> of
 * the plugin to make a better code more organized and more understandable with
 * specific functions of a {@code Loader}.
 * </p>
 *
 * @see CommandLoader
 * @see ListenerLoader
 * @see DataLoader
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (snapshot 12w0t6 'c')
 * @version NB 1.2
 */
public abstract class AbstractLoader {

    /**
     * A boolean to tell if the plugin is in a debugMode. If {@code TRUE}, all
     * details during loading will be displayed.
     */
    private final boolean debugMode;

    /**
     * A boolean which captured the current loader's status. If {@code isLoad == true},
     * then, an error will occure if the {@link #load()} is called.
     */
    private boolean isLoad;

    /**
     * Constructs a new {@code AbstractLoader} with the specified debug mode.
     * @param debugMode A boolean, if true, the debug mode is enabled. See {@link #debugMode}
     */
    protected AbstractLoader(final boolean debugMode) {
        this . debugMode = debugMode;
        this . isLoad = false;
    }

    /**
     * Called to start the loading process.
     * @throws Exception Throws when an exception occured during the process.
     * @throws IllegalStateException Throws if the {@code load()} method is called
     * while the {@link AbstractLoader Loader} is already loaded.
     */
    public void load() throws Exception {
        if (isLoad)
            throw new IllegalStateException();

        isLoad = true;
        loadComponents();
    }

    /**
     * Called by the {@link #load()} method to start the loading process.
     * @throws Exception Throws when an exception occured during the process.
     */
    protected abstract void loadComponents() throws Exception;

    /**
     * Gets the current mode of the plugin (debug/release). If the function
     * return true, the plugin is currently in debug mode. See {@link #debugMode}
     * for more details.
     *
     * @return the current plugin's mode (debug/release).
     */
    protected boolean isDebug() {
        return debugMode;
    }

    /**
     * Prints a message in the logger with the specified {@link Level}. However, the
     * message will be printed only if <code>{@link #debugMode} == true</code>.
     *
     * @param msg The message to log.
     * @param level One of the message level identifiers, e.g., SEVERE
     */
    protected final void debug(String msg, Level level) {
        if (isDebug())
            NbEssential.getLogger().log(level, msg);
    }
}
