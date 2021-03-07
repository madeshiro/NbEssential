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

package nb.script.events;

import nb.script.exception.ScriptException;
import nb.script.type.Script;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NbslScriptExceptionEvent extends Event {

    private Script script;
    private final ScriptException exception;
    private final static HandlerList handlers = new HandlerList();

    public NbslScriptExceptionEvent(Script script, final ScriptException ex) {
        this.script = script;
        this.exception = ex;
    }

    public Script getScript() {
        return script;
    }

    public ScriptException getException() {
        return exception;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
