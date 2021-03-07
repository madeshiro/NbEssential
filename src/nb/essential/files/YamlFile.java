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

import nb.essential.main.NbEssential;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;


/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 20/09/2016
 */
public class YamlFile extends ResourceFile {

    private FileConfiguration fileConfiguration;

    protected YamlFile(File file) {
        super(file);
    }

    @Override
    public boolean load() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        return fileConfiguration != null;
    }

    @Override
    public boolean reload() {
        return load();
    }

    @Override
    public boolean save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            if (NbEssential.isPluginDebug())
                e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public <E> E get(Object... path) {
        return (E) fileConfiguration.get(GenerateYamlPath(path));
    }

    @Override
    public void set(Object obj, Object... path) {
        fileConfiguration.set(GenerateYamlPath(path), obj);
    }

    public FileConfiguration getContainer() {
        return fileConfiguration;
    }

    public static String GenerateYamlPath(Object... path) {
        String yamlpath = "";
        for (Object identifier : path)
            yamlpath += identifier + ".";
        return yamlpath.substring(0, yamlpath.length()-1);
    }
}
