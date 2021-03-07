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

package nb.essential.utils;

import nb.essential.files.ResourceFile;

import java.util.HashMap;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import static java.util.logging.Level.*;
import static nb.essential.main.NbEssential.getLogger;

/**
 * SaveMechanism est un systeme permettant de récupérer les variables sauvegardable dans
 * un fichier à l'aide de l'annotation @Savable(value = "path.to.save")
 *
 * Cette classe permet aussi le chargement en utilisant par contre que des setters 'conseiller protected'
 * avec l'annotation @Loadable(value = "path.to.load")
 *
 * Ne contient que des méthodes statiques
 *
 * @see Savable
 * @see Loadable
 * @see BothMechanism
 *
 * @version NB 1.2
 * @author MađeShirő ƵÆsora
 * @since NB 1.2 (snapshot 12w0t6 'c')
 */
@Deprecated
public final class SaveMechanism {

    /**
     * A boolean to tell if the plugin is in a debugMode. If {@code TRUE}, all
     * details during save or load operation will be displayed.
     */
    private static boolean isDebug;

    /**
     * Defines if the plugin is in debugMode.
     * @param flag A boolean, if TRUE, all details will be logged during critical
     *             operation.
     *
     * @see #logDetails(String, Level)
     */
    public static void setDebug(boolean flag) {
        isDebug = flag;
    }

    /**
     * A simple method to log message if the debugMode is enabled.
     * @param msg The message to log
     * @param lvl One of the message level identifiers, e.g., SEVERE
     */
    private static void logDetails(String msg, Level lvl) {
        if (isDebug)
            getLogger().log(lvl, msg);
    }

    public static <T extends SaveMechanismAdaptater> boolean loadClass(T instance, ResourceFile handler) {
        boolean status = true;
        for (Field field : instance.getClass().getDeclaredFields()) {
            status = extractAndLoad(field, instance, handler) && status;
        }
        for (Method method : instance.getClass().getDeclaredMethods()) {
            status = extractAndLoad(method, instance, handler) && status;
        }

        Class<?> cls = instance.getClass().getSuperclass();
        while(SaveMechanismAdaptater.class.isAssignableFrom(cls) && cls != null) {
            for (Field field : cls.getDeclaredFields()) {
                status = extractAndLoad(field, instance, handler) && status;
            }
            for (Method method : cls.getDeclaredMethods()) {
                status = extractAndLoad(method, instance, handler) && status;
            }

            cls = cls.getSuperclass();
        }

        return status;
    }

    public static <T extends SaveMechanismAdaptater> boolean saveClass(T instance, ResourceFile handler) {
        boolean status = true;

        for (Field field : instance.getClass().getDeclaredFields()) {
            status = extractAndSave(field, instance, handler) && status;
        }
        for (Method method : instance.getClass().getDeclaredMethods()) {
            status = extractAndSave(method, instance, handler) && status;
        }

        Class<?> cls = instance.getClass().getSuperclass();
        while(SaveMechanismAdaptater.class.isAssignableFrom(cls) && cls != null) {
            for (Field field : cls.getDeclaredFields()) {
                status = extractAndSave(field, instance, handler) && status;
            }
            for (Method method : cls.getDeclaredMethods()) {
                status = extractAndSave(method, instance, handler) && status;
            }

            cls = cls.getSuperclass();
        }

        return handler.save() && status;
    }

    private static <T extends SaveMechanismAdaptater> boolean extractAndLoad
            (Field field, T instance, ResourceFile handler) {
        boolean status = true;
        String[] loadable;
        if (field.isAnnotationPresent(Loadable.class))
            loadable = field.getDeclaredAnnotation(Loadable.class).value();
        else if (field.isAnnotationPresent(BothMechanism.class))
            loadable = field.getDeclaredAnnotation(BothMechanism.class).value();
        else
            return true; // stop function but no error happened

        boolean isAccessible;
        if (!(isAccessible = field.isAccessible())) {
            field.setAccessible(true);
            logDetails("Field '" + field.getName() + "' set accessible to true", INFO);
        }

        try {
            try {
                Object savedValue = handler.get((Object[]) SaveMechanismAdaptater.getPath(instance, loadable));
                    field.set(instance, savedValue);
                    logDetails("Field's value for '" + field.getName() + "' is: (" +
                            savedValue.getClass().getName() + ") " + savedValue.toString(), INFO);
            } catch (NullPointerException ex) {
                if (isDebug)
                    logDetails("Field '" + field.getName() + "' will be nulled", WARNING);
            }
        } catch (IllegalAccessException e) {
            if (isDebug)
                e.printStackTrace();
            status = false;
        } catch (IllegalArgumentException e) {
            logDetails("Field '" + field.getName() + "' cannot be loaded (invalid type)", WARNING);
            status = false;
        }

        if (!isAccessible) {
            field.setAccessible(false);
            logDetails("Field '" + field.getName() + "' back up accessible value", INFO);
        }

        return status;
    }

    private static <T extends SaveMechanismAdaptater> boolean extractAndLoad
            (Method method, T instance, ResourceFile handler) {
        boolean status = true;
        String[] loadable;
        if (method.isAnnotationPresent(Loadable.class))
            loadable = method.getDeclaredAnnotation(Loadable.class).value();
        else if (method.isAnnotationPresent(BothMechanism.class))
            loadable = method.getDeclaredAnnotation(BothMechanism.class).value();
        else
            return true; // stop function but no error happened
        boolean isAccessible;
        if (!(isAccessible = method.isAccessible())) {
            method.setAccessible(true);
            logDetails("Method'" + method.getName() + "' set accessible to true", INFO);
        }

        try {
            Object savedValue = handler.get((Object[]) SaveMechanismAdaptater.getPath(instance, loadable));
            if (method.getParameterCount() == 1)
                method.invoke(instance, savedValue);
            else
                logDetails("Method'" + method.getName() + "' must have only one parameter !", WARNING);
        } catch (IllegalArgumentException e) {
            logDetails("Method '" + method.getName() + "' cannot be invoked (invalid type)", WARNING);
            status = false;
        } catch (IllegalAccessException | InvocationTargetException e) {
            if (isDebug)
                e.getCause().printStackTrace();
            status = false;
        }

        if (!isAccessible) {
            method.setAccessible(false);
            logDetails("Method '" + method.getName() + "' back up accessible value", INFO);
        }

        return status;
    }

    private static <T extends SaveMechanismAdaptater> boolean extractAndSave
            (Field field, T instance, ResourceFile handler) {
        boolean status = true;
        String[] savable;
        if (field.isAnnotationPresent(Savable.class))
            savable = field.getDeclaredAnnotation(Savable.class).value();
        else if (field.isAnnotationPresent(BothMechanism.class))
            savable = field.getDeclaredAnnotation(BothMechanism.class).value();
        else
            return true; // stop function but no error happened
        boolean isAccessible;
        if (!(isAccessible = field.isAccessible())) {
            field.setAccessible(true);
            logDetails("Field '" + field.getName() + "' set accessible to true", INFO);
        }

        try {
            saveValue(field.get(instance), SaveMechanismAdaptater.getPath(instance, savable), handler);
        } catch (IllegalAccessException e) {
            if (isDebug)
                e.printStackTrace();
            status = false;
        }

        if (!isAccessible) {
            field.setAccessible(false);
            logDetails("Field '" + field.getName() + "' back up accessible value", INFO);
        }

        return status;
    }

    private static <T extends SaveMechanismAdaptater> boolean extractAndSave
            (Method method, T instance, ResourceFile handler) {
        boolean status = true;
        String[] savable;
        if (method.isAnnotationPresent(Savable.class))
            savable = method.getDeclaredAnnotation(Savable.class).value();
        else if (method.isAnnotationPresent(BothMechanism.class))
            savable = method.getDeclaredAnnotation(BothMechanism.class).value();
        else
            return true; // stop function but no error happened

        boolean isAccessible;
        if (!(isAccessible = method.isAccessible())) {
            method.setAccessible(true);
            logDetails("Method '" + method.getName() + "' set accessible to true", INFO);
        }

        try {
            if (method.getParameterCount() > 0)
                logDetails("Method '" + method.getName() + "' must not require parameter !", SEVERE);
            else
                saveValue(method.invoke(instance), SaveMechanismAdaptater.getPath(instance, savable), handler);
        } catch (IllegalAccessException | InvocationTargetException e) {
            if (isDebug)
                e.printStackTrace();
            status = false;
        }

        if (!isAccessible) {
            method.setAccessible(false);
            logDetails("Method '" + method.getName() + "' back up accessible value", INFO);
        }

        return status;
    }

    private static void saveValue(Object value, String[] path, ResourceFile handler) {
        if (value instanceof Enum || value instanceof SavableClassString)
            handler.set(value.toString(), (Object[]) path);
        else
            handler.set(value, (Object[]) path);
    }

    public interface SaveMechanismAdaptater {
        HashMap<String, String> getModPath();

        static String[] getPath(SaveMechanismAdaptater a, String[] e) {
            if (a.getModPath() == null)
                return e;

            for (String str : a.getModPath().keySet()) {
                for (int i = 0; i < e.length; i++) {
                    if (e[i] instanceof String)
                        e[i] = e[i].replace(str, a.getModPath().get(str));
                }
            }
            return e;
        }

        boolean save();
        boolean load();
    }

    /**
     *
     */
    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Savable {
        /**
         * Gets the path where saved the variable or the return value of the method.
         * @return the path where saved the variable or the return value of the method.
         */
        String[] value();
    }

    /**
     *
     */
    @Documented
    @Target({ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Loadable {
        /**
         * Gets the path of the value to passed as parameter in the setter's method.
         * @return the path of the value in the {@link ResourceFile handler}.
         */
        String[] value();
    }

    @Documented
    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface BothMechanism {

        String[] value();
    }

    public interface SavableClassString {
        @Override
        String toString();
    }
}
