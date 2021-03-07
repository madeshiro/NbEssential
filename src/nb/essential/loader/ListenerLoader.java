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

import nb.essential.main.NbEssential;
import org.bukkit.event.Listener;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * <p>
 * The {@code Listenerloader} class is a system which permit the loading of all
 * NbEssential's listeners and his subplugins listener by registering them in the
 * Bukkit register. When the {@link Listener} is registered, Bukkit will called
 * function which have the {@link org.bukkit.event.EventHandler EventHandler}
 * annotation and a correct Bukkit {@link org.bukkit.event.Event Event} as parameter.
 * </p>
 * <p>
 * Each listener inheriting from {@link Listener} are detected and listed
 * automaticaly when the loading function {@link #load()} is called. To permit
 * the smooth functioning of the loader, classes inheriting from {@link Listener}
 * should not ask parameters in their constructor otherwise, the class loading will
 * be ignored.
 * </p>
 *
 * @see AbstractLoader
 * @see CommandLoader
 * @see Listener
 *
 * @author MađeShirő ƵÆsora
 * @since NB 0.1.0 (first release 0.1)
 * @version NB 1.2
 */
public final class ListenerLoader extends AbstractLoader implements Iterable<Listener> {

    /**
     * An {@link ArrayList} which contains all {@link Listener} loaded.
     */
    private final ArrayList<Listener> listeners;

    /**
     * Constructs a new {@code ListenerLoader} with the specified debug mode.
     *
     * @param debugMode A boolean, if true, the debug mode is enabled. See {@link #debugMode}
     */
    public ListenerLoader(boolean debugMode) {
        super(debugMode);

        listeners = new ArrayList<>();
    }

    /**
     * Called by the {@link #load()} method to start the loading process.
     * @throws Exception Throws when an exception occured during the process.
     */
    @Override
    protected void loadComponents() throws Exception {

        JarFile jar = new JarFile("plugins/" + new File(DataLoader.class.getProtectionDomain().getCodeSource().getLocation()
                .getPath()).getName());
        Enumeration<JarEntry> e = jar.entries();
        for (JarEntry je = e.nextElement(); e.hasMoreElements(); je = e.nextElement()) {
            if (!je.getName().endsWith(".class"))
                continue;

            Class c = Class.forName(je.getName().substring(0, je.getName().length() - 6).replace('/', '.'));
            if (Listener.class.isAssignableFrom(c) && c.isAnnotationPresent(EventListener.class)) {
                try {
                    Listener listener;
                    Constructor constructor = c.getDeclaredConstructor();

                    if (constructor == null)
                        continue;

                    boolean constructorAccess = constructor.isAccessible();
                    if (!constructorAccess)
                        constructor.setAccessible(true);
                    listener = (Listener) constructor.newInstance();
                    if (!constructorAccess)
                        constructor.setAccessible(false);

                    NbEssential.getPlugin().getServer().getPluginManager().registerEvents(listener, NbEssential.getPlugin());
                    listeners.add(listener);

                    if (isDebug())
                        NbEssential.getLogger().info("[ListenerLoader] Listener " + c.getName() + " has been loaded !");
                } catch (InstantiationException e1) {
                    NbEssential.getLogger().severe("[ListenerLoader] An error occured when " +
                            "trying to instancite listener " + c.getName() + " !");
                    if (isDebug())
                        NbEssential.getLogger().severe("[ListenerLoader] cause by: " + e1.getMessage());
                }
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public Iterator<Listener> iterator() {
        return listeners.iterator();
    }
}
