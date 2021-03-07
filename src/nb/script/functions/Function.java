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

package nb.script.functions;

import nb.script.exception.ScriptException;
import nb.script.type.CodeChunk;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Class of NbEssential in package nb.script.functions
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public class Function {

    private Method method;
    public Function(Method method) {
        this.method = method;
    }

    /* --- properties --- */

    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    public boolean isBoolean() {
        return getReturnType() == boolean.class;
    }

    @Override
    public String toString() {
        return method.toString();
    }

    /* --- call --- */

    public Object call(CodeChunk parent, Args... args) throws ScriptException {
        HashMap<String, Object> hashMap = new HashMap<>();
        for (Args arg : args)
            hashMap.put(arg.name, arg.value);
        return call(parent, hashMap);
    }

    public Object call(CodeChunk parent, HashMap<String,Object> args) throws ScriptException {
        try {
            return method.invoke(null, parent.getRoot(), args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ScriptException(parent.getRoot(), e.getMessage(), e.getCause());
        }
    }

    public static class Args {
        public String name;
        public final Object value;

        public Args(String name, Object object) {
            this.name = name;
            this.value = object;
        }
    }
}
