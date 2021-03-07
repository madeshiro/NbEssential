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

import java.io.File;

import nb.essential.main.CraftNbServer;
import nb.essential.main.IServer;
import nb.essential.main.NbEssential;
import static nb.essential.main.NbEssential.getServer;

/**
 * Class of NbEssential created by maᴅeliƒe Æský on 10/12/2016
 */
public class TranslationFile extends JSONFile {

    public static final TranslationFile originalFile;
    public static final TranslationFile commandFile;
    static {
        originalFile = new TranslationFile("translate.json");
        commandFile = new TranslationFile("translate_cmd.json");
    }

    private TranslationFile(String filename) {
        super(new File("plugins/NbEssential/" + filename));

        if (!getFile().exists()) {
            if (!copyFromResource(filename, "plugins/NbEssential/" + filename)) {
                NbEssential.getLogger().severe("An error occured when trying to create the translation file");
            }
        }

        if (!load())
            NbEssential.getLogger().severe("An error occured when trying to parse the translation file");
    }

    public String getSentence(String repCode) {
        return getSentence(repCode, ((CraftNbServer) getServer()).getServerLanguage());
    }

    public String getSentence(String repCode, IServer.SupportedLanguage lang) {
        String sentence = get(repCode, lang.toString().toLowerCase());

        return sentence == null ? repCode : sentence;
    }
}
