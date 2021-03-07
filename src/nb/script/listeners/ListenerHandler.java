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
package nb.script.listeners;

import nb.script.EventManager;
import nb.script.exception.ScriptException;
import nb.script.functions.Function;
import nb.script.type.Event;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Class of NbEssential in package nb.script.listeners
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public class ListenerHandler {

    private HashMap<EventManager.SupportedEvent, HashSet<Event>> listeners;
    public ListenerHandler() {
        listeners = new HashMap<>();
        for (EventManager.SupportedEvent event : EventManager.SupportedEvent.values())
            listeners.put(event, new HashSet<>());
    }

    public void listen(Event event) {
        listeners.get(event.getEventType()).add(event);
    }

    public void unregister(Event event) {
        listeners.get(event.getEventType()).remove(event);
    }

    protected void doEvent(EventManager.SupportedEvent event, Function.Args... args) throws ScriptException {
        for (Event chunk : listeners.get(event))
            chunk.onEvent(args);
    }
}
