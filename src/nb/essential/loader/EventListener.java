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

/**
 * <p>
 * This annotation will allow to load a listener's class into the Bukkit/Spigot NPCAPI.
 * </p>
 * <p>
 * Annotation is used by the ListenerLoader class of the NbEssential NPCAPI and may
 * only be placed on class type. Moreover, this annotation will only work on
 * class which inherit from {@link org.bukkit.event.Listener}. Just because the loading system
 * will look first at Listener's child class through the annotation. So if a
 * class doesn't inherit from Listener interface, then the class will not be loaded. <br/>
 * <i>More informations: see {@link ListenerLoader}.</i>
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
public @interface EventListener {
}
