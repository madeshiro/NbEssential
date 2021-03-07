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

package nb.essential.loader;

import org.bukkit.Bukkit;
import java.util.ArrayList;

/**
 * <p>
 * The class {@link DataLoadingException} represents exceptions which can be thrown
 * during loading of NbEssential and these subplugins' data.
 * </p>
 *
 * <p>
 * This class inheriting from {@link Exception} has a way to list all the bases
 * that caused the exception thrown. By using {@link #printStackTrace()}, you
 * will be able to determine the accident's origine. However, the exception is
 * thrown only if the loading method of a base failed by returning {@code false};
 * In other words, this exception will never be thrown by a loading component
 * of {@link DataLoader}
 * </p>
 * <p>
 * The {@link #printStackTrace()} method will send the regular message specified
 * by {@link Throwable#printStackTrace()} but also send this kind of message :
 * <code>
 *     <pre>
 * An error occured when trying to load database. The concerned database are :
 * !    > MainData@main
 * !    > RankData@rank
 *     </pre>
 * </code>
 * Each {@link PluginData} will be mentionned under the form of list with at least
 * their name assigned by the {@link Loader} annotation, see during the first
 * loading, the original class name ([class_name]@[base_name]).
 * </p>
 *
 * @see DataLoader
 * @see PluginData
 * @see Loader
 * @see Exception
 *
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (snapshot 12w0t6 'c')
 * @version NB 1.2
 */
public class DataLoadingException extends Exception {

    /**
     * The detail message of this exception
     */
    private String detailMessage;
    private ArrayList<String> concernedBase = new ArrayList<>();

    /**
     * Constructs a new exception with null as its detail message.
     */
    public DataLoadingException() {
        this("An error occured when trying to load database. The concerned database are :");
    }

    /**
     * Constructs a new exception with the specified detail message.
     * @param msg the detail message. The detail message is saved for later
     *            retrieval by the {@link Throwable#getMessage()} method.
     */
    public DataLoadingException(String msg) {
        super();
        detailMessage = msg;
    }

    /**
     * Constructs a new exception with the specified message and cause.
     * @param msg the detail message. The detail message is saved for later
     *            retrieval by the {@link Throwable#getMessage()} method.
     * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()}
     *              method). (A null value is permitted, and indicates that the
     *              cause is nonexistent or unknown.)
     */
    public DataLoadingException(String msg, Throwable cause) {
        super(msg, cause);
        detailMessage = super.getMessage();
    }

    /**
     * Constructs a new exception with the specified cause and a detail message
     * of (cause == null ? null : cause.toString()) (which typically contains the
     * class and detail message of cause).
     * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()}
     *              method). (A null value is permitted, and indicates that the
     *              cause is nonexistent or unknown.)
     */
    public DataLoadingException(Throwable cause) {
        super(cause);
        detailMessage = super.getMessage();
    }

    /**
     * Constructs a new exception with the specified base which occured the error.
     * The detail message is made automatically by the Exception.
     * @param concernedBase An array which contains all database concerned. However, you
     *                      can add the concerned database by using {@link #a(String)}.
     */
    public DataLoadingException(ArrayList<String> concernedBase) {
        this("An error occured when trying to load database. The concerned database are :");
        this . concernedBase = concernedBase;
    }

    public DataLoadingException(String msg, ArrayList<String> concernedBase) {
        this(msg);
        this . concernedBase = concernedBase;
    }

    /**
     * Adds the database's name which hasn't been loaded correctly.
     * @param base The database's name.
     */
    public void a(String base) {
        if (!concernedBase.contains(base))
            concernedBase.add(base);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void printStackTrace() {
        Bukkit.getLogger().warning(getLocalizedMessage());
        for ( String base : concernedBase ) {
            Bukkit.getLogger().warning("!\t> " + base);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getMessage() {
        return detailMessage;
    }
}
