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
package nb.essential.files;

import java.io.*;

import nb.essential.main.NbEssential;
import zaesora.madeshiro.parser.json.JSONArray;
import zaesora.madeshiro.parser.json.JSONParser;
import zaesora.madeshiro.parser.json.JSONValue;

/**
 * Class of NbEssential created by maᴅeliƒe Æský on 10/12/2016
 */
public class JSONFile extends ResourceFile {

    private JSONValue value;

    public JSONFile(String filename) {
        this(new File(filename));
    }

    public JSONFile(File file) {
        super(file);
    }

    @Override
    public boolean load() {

        if (!file.exists())
            return false;

        try {
            FileReader reader = new FileReader(file);

            int r;
            String buf = "";
            while ((r = reader.read()) != -1) {
                buf += (char) r;
            }

            value = new JSONParser().parse(buf);
        } catch (IOException e) {
            if (NbEssential.isPluginDebug())
                e.printStackTrace();

            return false;
        }

        return true;
    }

    @Override
    public boolean reload() {
        return load();
    }

    @Override
    public boolean save() {
        try {
            FileWriter writer = new FileWriter(file);
            value.writeForFile(writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            if (NbEssential.isPluginDebug())
                e.printStackTrace();

            return false;
        }

        return true;
    }

    @Override
    public <E> E get(Object... path) {
        return (E) value.getObject(path);
    }

    @Override
    public <E> void set(E obj, Object... path) {
        try {
            JSONValue container = value;
            for (int i = 0; i < path.length - 1; i++) {
                container = container.getObject(path[i]);
            }
            if (path[path.length-1] instanceof String)
                container.addJValue((String) path[path.length-1], obj);
            else
                ((JSONArray) container).set((Integer) path[path.length-1], obj);
        } catch (Exception e) {
            if (NbEssential.isPluginDebug())
                e.printStackTrace();
        }
    }

    public <E> void add(E obj, Object... path) {
        try {
            JSONValue container = value;
            for (int i = 0; i < path.length - 1; i++) {
                container = container.getObject(path[i]);
            }
            if (path[path.length-1] instanceof String)
                container.addJValue((String) path[path.length-1], obj);
            else
                ((JSONArray) container).set((Integer) path[path.length-1], obj);
        } catch (Exception e) {
            if (NbEssential.isPluginDebug())
                e.printStackTrace();
        }
    }

    public JSONValue getContainer() {
        return value;
    }

    public static Object DefaultValueOf(JSONResourceFiles file, Object... path) {
        return file.default_file != null ? file.default_file.get(path) : null;
    }

    public enum JSONResourceFiles {
        Config("config.json"),
        Groups("groups.json"),
        Jails("jails.json"),
        Player("player.json"),
        Warps("warps.json"),
        Zones("zones.json"),

        TranslateCurrent("translate.json"),
        TranslateCommands("translate_cmd.json")
        ;

        private String filename;
        private JSONFile default_file;

        JSONResourceFiles(String filename) {
            this.filename = filename;
            this.default_file = GenerateDefault();
        }

        private JSONFile GenerateDefault() {
            File file = new File("plugins/NbEssential/defaultFiles/" + filename);
            return copyFromResource(filename, "plugins/NbEssential/defaultFiles/" + filename) ?
                        new JSONFile(file) : null;
        }

        public JSONFile getDefault() {
            return default_file;
        }
    }
}
