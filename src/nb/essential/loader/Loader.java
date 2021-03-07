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

import java.lang.annotation.*;
import static nb.essential.loader.LoaderPriority.Normal;

/**
 * <p>
 * This annotation will allow to load and store data from a class inheriting
 * from PluginData; The value represents the database's name which will be stored
 * in the DataLoader's map. The DataLoader is used by the {@link nb.essential.main.Main Main}
 * class while charging the plugin.
 * </p>
 * <p>
 * Annotation is used by the DataLoader class of the NbEssential NPCAPI and may
 * only be placed on class type. Moreover, this annotation will only work on
 * class which inherit from {@link PluginData}. Just because the loading system
 * will look first at PluginData's child class through the annotation. So if a
 * class doesn't inherit from PluginData then the class will not be loaded. <br/>
 * <i>More informations: see {@link DataLoader}.</i>
 * </p>
 * <p>
 * A priority is indicated in the annotation to determine if data must be loaded
 * at first or last. All of these priorities are defined in the {@link LoaderPriority}
 * enum. As indicated in documentation, more the priority is high, more the data
 * is likely to be loaded in last.
 * </p>
 * <h5>Note:</h5>
 * Put this annotation on a class which does not respect the conditions mentionned
 * above will not cause an error during the runtime. The annotation will be
 * just useless.
 * <br/>
 *
 * @see DataLoader
 * @see PluginData
 * @see nb.essential.main.Main#loadPlugin(boolean) Main.LoadPlugin(boolean)
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (snapshot 12w0t6 'c')
 * @version NB 1.2
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loader {

    /**
     * Returns the name of the Database to put in the DataLoader base of NbEssential
     * @return le nom de la base de données à stocket dans la base du DataLoader de NbEssential.
     */
    String value();

    /**
     * The priority of the PluginData. Put an higher priority will make that data will be
     * loaded later and vice-versa.
     * @return The pluginData's priority.
     */
    LoaderPriority priority() default Normal;
}
