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

import java.io.*;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 20/09/2016
 */
public abstract class ResourceFile {

    protected File file;
    protected ResourceFile(File file) {
        this . file = file;
    }

    public abstract boolean load();
    public abstract boolean reload();
    public abstract boolean save();

    public static boolean copyFromResource(String file, String target) {
        InputStream input = ResourceFile.class.getResourceAsStream("/resources/" + file);
        File fileTarget = new File(target);

        return copyFromResource(input, fileTarget);
    }

    public static boolean copyFromResource(InputStream input, File target) {
        try {
            if (!target.exists())
                if(!target.createNewFile())
                    return false;

            OutputStream outstream = new FileOutputStream(target);

            int length;
            byte[] buffer = new byte[1024];

            while((length = input.read(buffer)) > 0)
                outstream.write(buffer, 0, length);

            outstream.close();
            input.close();
        } catch (IOException e) {
            NbEssential.getLogger().warning(e.getLocalizedMessage());
            return false;
        }

        return true;
    }

    public abstract <E> E get(Object... path);
    public abstract <E> void set(E obj, Object... path);

    public File getFile() {
        return file;
    }
}