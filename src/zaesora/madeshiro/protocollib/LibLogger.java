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

package zaesora.madeshiro.protocollib;

import nb.essential.main.NbEssential;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 09/10/2016
 */
public class LibLogger {

    private FileOutputStream out;

    public LibLogger() throws IOException {
        String fileName = "[protocol-lib]_" + new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        File logDirectory = new File("plugins/NbEssential/logs");

        int countFile = 0;
        if (logDirectory != null && logDirectory.listFiles() != null) {
            for (File file : logDirectory.listFiles())
                if (file.getName().startsWith(fileName))
                    countFile++;
        }

        File logFile = new File("plugins/NbEssential/logs/" + fileName + (countFile > 0 ? " (" + countFile + ")" : "") + ".log");
        logFile.createNewFile();
        out = new FileOutputStream(logFile);
    }

    public void info(String msg) {
        if (out == null)
            NbEssential.getLogger().info(msg);
        else {
            try {
                out.write((new SimpleDateFormat("[HH:mm:ss]").
                        format(Calendar.getInstance().getTime()) + "[ProtocolLib/Info] " + msg).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                out = null;
            }
        }
    }

    public void warning(String msg) {
        if (out == null)
            NbEssential.getLogger().warning(msg);
        else {
            try {
                out.write((new SimpleDateFormat("[HH:mm:ss]").
                        format(Calendar.getInstance().getTime()) + "[ProtocolLib/Warning] " + msg).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                out = null;
            }
        }
    }

    public void severe(String msg) {
        try {
            if (out != null)
                out.write((new SimpleDateFormat("[HH:mm:ss]").
                    format(Calendar.getInstance().getTime()) + "[ProtocolLib/Error] " + msg).getBytes());
            NbEssential.getLogger().severe(msg);
        } catch (IOException e) {
            e.printStackTrace();
            out = null;
        }

    }
}
