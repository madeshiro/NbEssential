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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * <p>
 * The {@code Commandloader} class is a system which permit the loading of all
 * NbEssential's commands and his subplugins commands by registering them in an
 * array. This array is then called by the method from the {@link nb.essential.main.Main Main}
 * class {@link nb.essential.main.Main#onCommand(CommandSender, Command, String, String[])}.
 * </p>
 * <p>
 * Each command inheriting from {@link NbCommand} are detected and listed
 * automaticaly when the loading function {@link #load()} is called. To permit
 * the smooth functioning of the loader, classes inheriting from {@link NbCommand}
 * should not ask parameters in their constructor otherwise, the class loading will
 * be ignored.
 * </p>
 *
 * @see AbstractLoader
 * @see ListenerLoader
 * @see NbCommand
 *
 * @author MađeShirő ƵÆsora
 * @since NB 0.1.0 (first release 0.1)
 * @version NB 1.2
 */
public final class CommandLoader extends AbstractLoader implements Iterable<NbCommand> {

    /**
     * An {@link ArrayList} which contains all {@link NbCommand} loaded.
     */
    private final ArrayList<NbCommand> commands;

    /**
     * Constructs a new {@code CommandLoader} with the specified debug mode.
     *
     * @param debugMode A boolean, if true, the debug mode is enabled. See {@link #debugMode}
     */
    public CommandLoader(boolean debugMode) {
        super(debugMode);

        commands = new ArrayList<>();
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
            if (!c.getName().equals("nb.essential.loader.NbCommand") && NbCommand.class.isAssignableFrom(c)) {
                try {
                    Constructor constructor = c.getDeclaredConstructor();

                    if (constructor == null)
                        continue;

                    boolean constructorAccess = constructor.isAccessible();
                    if (!constructorAccess)
                        constructor.setAccessible(true);
                    commands.add((NbCommand) constructor.newInstance());
                    if (!constructorAccess)
                        constructor.setAccessible(false);

                    if (isDebug())
                        NbEssential.getLogger().info("[CommandLoader] Command " + c.getName() + " has been loaded !");
                } catch (NullPointerException | InstantiationException e1) {
                    NbEssential.getLogger().severe("[CommandLoader] An error occured when " +
                            "trying to instanciate command " + c.getName() + " !");
                    if (isDebug())
                        NbEssential.getLogger().severe("[CommandLoader] cause by: " + e1.getMessage());
                }
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public Iterator<NbCommand> iterator() {
        return commands.iterator();
    }

    /**
     * Gets a commad from the CommandLoader
     * @param alias The label (or one of multiple available aliases) of the command
     * @return The command which match with the alias or {@code NULL}.
     */
    public NbCommand call(String alias) {
        for(NbCommand cmd : this) {
            if (cmd.hasAlias(alias))
                return cmd;
        }

        return null;
    }
}
