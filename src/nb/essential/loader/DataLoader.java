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
import sun.plugin.dom.exception.InvalidStateException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 23/08/2016
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (snapshot 12w0t6 'c')
 * @version NB 1.2
 */
public final class DataLoader extends AbstractLoader {

    /**
     * The {@link java.util.Map Map} which contains all loaded {@link PluginData}.
     */
    private HashMap<String, PluginData> data;

    /**
     * Creates a new DataLoader with release-mode activated per default.
     */
    public DataLoader() {
        this(NbEssential.isPluginDebug());
    }

    /**
     * Creates a new DataLoader with the specified mode (debug/release)
     * @param debugMode A boolean, if true, the debug mode is activated and
     *                  details will be in the logger.
     */
    public DataLoader(boolean debugMode) {
        super(debugMode);
        data = new HashMap<>();
    }

    /**
     * Loads all database which have the {@link Loader} annotation.
     *
     * @throws DataLoadingException If an exception occured when trying to load a {@link PluginData}
     */
    @Override
    protected void loadComponents() throws DataLoadingException, InvalidStateException {
        ArrayList<Class<? super PluginData>> queue = new ArrayList<>();
        ArrayList<String> var1 = new ArrayList<>();

        try {
            JarFile jar = new JarFile("plugins/" + new File(DataLoader.class.getProtectionDomain().getCodeSource().getLocation()
                    .getPath()).getName());
            Enumeration<JarEntry> e = jar.entries();
            for (JarEntry je = e.nextElement(); e.hasMoreElements(); je = e.nextElement()) {
                if(!je.getName().endsWith(".class"))
                    continue;
                Class c = Class.forName(je.getName().substring(0, je.getName().length() - 6).replace('/', '.'));
                if (c.isAnnotationPresent(Loader.class) && PluginData.class.isAssignableFrom(c)) {
                    Loader loader = extractAnnotation(c);
                    if (queue.size() > 0) {
                        for (int i = 0; i < queue.size(); i++) {
                            if(queue.get(i).getAnnotation(Loader.class).priority().isHigherThan(loader.priority())) {
                                queue.add(i, c);
                                if(isDebug())
                                    NbEssential.getLogger().info("[DataLoader] Add " + c.getName() + " with priority = " + loader.priority().
                                            toString() + " at index = " + i);
                                break;
                            }
                            else if(i == queue.size() - 1) {
                                queue.add(c);
                                if (isDebug())
                                    NbEssential.getLogger().info("[DataLoader] Add " + c.getName() + " at the queue's end !");
                                break;
                            }
                        }
                    } else {
                        queue.add(c);
                        if (isDebug())
                            NbEssential.getLogger().info("[DataLoader] Add " + c.getName() + " in the queue ! (first class found !)");
                    }
                } else if (c.isAnnotationPresent(Loader.class)) {
                    if (isDebug())
                        NbEssential.getLogger().warning("An useless 'Loader' annotation has been founded at " + c.getName() + " class");
                }
            }

            if (isDebug())
                NbEssential.getLogger().info("[DataLoader] No error ! Starting instanciation process...");
            for(Class c : queue) {
                Loader l = extractAnnotation(c);
                if(isDebug())
                    NbEssential.getLogger().info("[DataLoader] Loads " + c.getName() + "@" + l.value() + "...");
                try {
                    data.put(l.value(), (PluginData) c.newInstance());
                    if (!data.get(l.value()).load())
                        var1.add(c.getName() + "@" + l.value());
                } catch(Exception ie) {
                    NbEssential.getLogger().severe("[DataLoader] Error when loading '" + c.getName() + "' -> " + ie.getMessage());
                    var1.add(c.getName() + "@" + l.value());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (!var1.isEmpty())
            throw new DataLoadingException(var1);
    }

    /**
     * Extract the {@link Loader} annotation from the specified class.
     * @param cls the class that must extract the annotation.
     * @return The {@link Loader} annotation of the class or null if
     * the specified class doesn't have this annotation.
     */
    private Loader extractAnnotation(Class cls) {
        return (Loader) cls.getAnnotation(Loader.class);
    }

    /**
     * Reloads all PluginData. If an error occured during one reload, the processus
     * won't stop but an exception will be thrown when the function will be ended.
     * @throws DataLoadingException If an exception occured when trying to reload a
     * {@link PluginData}.
     */
    public void reload() throws DataLoadingException {
        ArrayList<String> var1 = new ArrayList<>();

        for (String key : data.keySet()) {
            if (!data.get(key).reload())
                var1.add(key); // An error occured, add the database's name in the error list.
        }

        if (!var1.isEmpty())
            throw new DataLoadingException(var1);
    }

    /**
     * Saves all PluginData. If an error occured during one save, the processus
     * won't stop but an exception will be thrown when the function will be ended.
     * @throws DataLoadingException If an exception occured when trying to reload a
     * {@link PluginData}.
     */
    public void save() throws DataLoadingException {
        ArrayList<String> var1 = new ArrayList<>();

        for (String key : data.keySet()) {
            if (!data.get(key).save())
                var1.add(key); // An error occured, add the database's name in the error list.
        }

        if (!var1.isEmpty())
            throw new DataLoadingException("An error occured when trying to save database. The concerned database are :", var1);
    }

    /**
     * Gets the database using his name indicated by {@link Loader}.
     *
     * @param name The database's name
     * @param <T> The truth type of data
     * @return the database if exists, else <code>null</code>
     */
    public <T extends PluginData> T getData(String name) {
        return exists(name) ? (T) data.get(name) : null;
    }

    /**
     * Verify if a database has been loaded (or exists) in the {@link DataLoader}.
     * @param name The database's name to verify.
     * @return A boolean, true if the database exists else false.
     */
    public boolean exists(String name) {
        return data.containsKey(name);
    }
}