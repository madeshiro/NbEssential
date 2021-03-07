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
package nb.script.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Class of NbEssential in package nb.script.parser
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (pre-release 1)
 * @version NB 1.2
 */
public class ScriptReader {
    private int cursor, undo;
    private String buffer;

    public ScriptReader(File file) throws IOException {
        this . cursor = 0;
        this . buffer = read(file);
    }

    public ScriptReader(ScriptReader copy) {
        this . undo = copy.undo;
        this . cursor = copy.cursor;
        this . buffer = copy.buffer;
    }

    private String read(File file) throws IOException {
        String read = "";
        FileInputStream stream = new FileInputStream(file);

        int r;
        while ((r = stream.read()) != -1)
            read += (char) r;

        return read;
    }

    public String getBuffer() {
        return buffer;
    }

    public String read(int from, int to) {
        return buffer.substring(from, to);
    }

    public int length() {
        return buffer.length();
    }

    public int available() {
        return buffer.length() - cursor;
    }

    public int setCursor(int pos) {
        undo = cursor;
        return cursor = pos;
    }

    public int getCursor() {
        return cursor;
    }

    public char gotoLastChar(char c) {
        undo = cursor;

        do {
            cursor--;
        } while (buffer.charAt(cursor) != c);

        return current();
    }

    public char gotoLastChar(char c, String... ignored) {
        undo = cursor;

        do {
            cursor--;
        } while (buffer.charAt(cursor) != c || CurrentMatches(ignored));

        return current();
    }

    public char gotoNextChar(char c) {
        undo = cursor;

        do {
            cursor++;
        } while (buffer.charAt(cursor) != c);

        return current();
    }

    public char gotoNextChar(char c, String... ignored) {
        undo = cursor;

        do {
            cursor++;
        } while (buffer.charAt(cursor) != c || CurrentMatches(ignored));
        return current();
    }

    public char ignoreCharacters(boolean ignoreSpace, String... ignored) {
        undo = cursor;

        if (ignoreSpace) {
            do {
                cursor++;
            } while (buffer.charAt(cursor) == ' ' || CurrentMatches(ignored));
        } else {
            do {
                cursor++;
            } while (CurrentMatches(ignored));
        }

        return current();
    }

    public char prev() {
        undo = cursor;
        return buffer.charAt(cursor--);
    }

    public char current() {
        return buffer.charAt(cursor);
    }

    public char next() {
        undo = cursor;
        return buffer.charAt(cursor++);
    }

    public char undo() {
        int var1 = cursor;
        cursor = undo;
        undo = var1;

        return current();
    }

    private boolean CurrentMatches(String[] ignored) {
        for (String str : ignored) {
            if (buffer.substring(cursor-str.length(), cursor).equals(str))
                return true;
        }

        return false;
    }
}
