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
package nb.script;

import nb.script.listeners.ListenerHandler;
import nb.script.type.Event;

/**
 * Class of NbEssential in package nb.script
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public class EventManager {
    private final ListenerHandler handler = new ListenerHandler();

    /**
     * Start to listen the {@link Event} CodeChunk.
     * @param event The {@link Event CodeChunk} to listen.
     */
    public void listenEvent(Event event) {
        handler.listen(event);
    }

    /**
     * Stop to listen the {@link Event} CodeChunk
     * @param event The {@link Event CodeChunk} to unregister.
     */
    public void unregisterEvent(Event event) {
        handler.unregister(event);
    }

    /**
     * Gets the NbSL {@link ListenerHandler} used by NbSL Class {@link org.bukkit.event.Listener Listener}.
     * @return The NbSL {@link ListenerHandler}
     */
    public ListenerHandler getHandler() {
        return handler;
    }

    public enum SupportedEvent {
        /* Player Event */
        OnPlayerChatEvent(false),

        OnPlayerRightClickAtBlockEvent(true),
        OnPlayerRightClickAtEntityEvent(false),
        OnPlayerHitEntityEvent(false),
        OnPlayerHitNPCEvent(true),
        OnPlayerHitPlayerEvent(false),

        OnPlayerMoveEvent(false),
        OnPlayerGameOverEvent(false),
        OnPlayerBreakBlockEvent(false),

        /* Entity Event */
        OnEntityDeathEvent(false)

        /* Npc Event */

        /* Script Event */
        ;
        private boolean supportSelectors;

        SupportedEvent(boolean supportSelectors) {
            this . supportSelectors = supportSelectors;
        }

        /**
         * Specify if this event supports selectors. <br/>
         * <h3>Note:</h3>
         * <p>
         *     A selector is a special var included directly in the Event declaration to focus the event
         *     on an entity (or others) in particular.
         * </p>
         * <p>
         *     If the selector isn't supported, that means that the event is unable to do the focus.
         * </p>
         * @return
         */
        public boolean isSupportSelectors() {
            return supportSelectors;
        }
    }
}
