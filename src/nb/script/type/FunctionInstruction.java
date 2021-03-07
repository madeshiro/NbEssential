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
package nb.script.type;

import nb.script.exception.ScriptException;
import nb.script.functions.Function;
import nb.script.parser.ParseInfo;
import nb.script.type.interfaces.Conditionalizable;
import nb.script.type.interfaces.RuntimeArgument;

import java.util.HashMap;

/**
 * Class of NbEssential in package nb.script
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 */
public class FunctionInstruction implements Conditionalizable {

    private int line;
    private String fileName;
    private String scriptName;

    private Function function;
    private Object returnv;

    private HashMap<String, Object> compile_args;
    private HashMap<String, RuntimeArgument> runtime_args;

    public FunctionInstruction(Function function, ParseInfo info) {
        this.line = info.getLine();
        this.fileName = info.getFileName();
        this.scriptName = info.getRoot().getName();

        this.function = function;

        this . compile_args = new HashMap<>();
        this . runtime_args = new HashMap<>();
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getScriptName() {
        return scriptName;
    }

    public Object getReturnValue() {
        return returnv;
    }

    public boolean isBooleanFunction() {
        return false;
    }

    public Object addArg(String identifier, Object value) {
        return compile_args.put(identifier, value);
    }

    public Object addArg(String identifier, RuntimeArgument argument) {
        return runtime_args.put(identifier, argument);
    }

    @Override
    public void run(CodeChunk parent) throws ScriptException {
        HashMap<String, Object> args = new HashMap<>(compile_args);

        for (String str : runtime_args.keySet())
            args.put(str, runtime_args.get(str).compile(parent));

        returnv = function.call(parent, args);
    }
}
