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

package nb.roleplay.jukebox;

import nb.essential.scheduler.IScheduler;
import nb.essential.scheduler.ScheduleTask;
import nb.essential.scheduler.IScheduleRunnable;

import java.util.HashMap;
import java.util.Timer;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 28/08/2016
 *
 * @see Timer
 * @see ScheduleTask
 * @see IScheduleRunnable
 * @see IScheduler
 *
 * @since NB 1.2
 * @version NB 1.2
 * @author MađeShirő ƵÆsora
 */
public class Scheduler implements IScheduler {

    private Timer timer;
    private int currentId;
    private HashMap<Integer, ScheduleTask> scheduleTaskList;
    private static final Scheduler instance = new Scheduler();

    /**
     * Constructs a new {@code Scheduler}.
     */
    protected Scheduler() {
        currentId = 0;
        timer = new Timer();
        scheduleTaskList = new HashMap<>();
    }

    /**
     * Gets the unique instance available of the {@link Scheduler}.
     * @return the unique instance of the {@link Scheduler}
     */
    public static Scheduler getInstance() {
        return instance;
    }

    void removeTaskFromList(int taskId) {
        scheduleTaskList.remove(taskId);
    }

    private ScheduleTask createTask(IScheduleRunnable runnable) {
        ScheduleTask task = new ScheduleTask(currentId++, runnable);
        scheduleTaskList.put(currentId, task);
        return task;
    }

    @Override
    public int scheduleSyncTask(IScheduleRunnable run) {
        timer.schedule(createTask(run), 0);
        return currentId;
    }

    @Override
    public int scheduleSyncDelayedTask(IScheduleRunnable run, long delay) {
        timer.schedule(createTask(run), delay);
        return currentId;
    }

    @Override
    public int scheduleSyncRepeatingTask(IScheduleRunnable run, long delay, long period) {
        timer.schedule(createTask(run), delay, period);
        return currentId;
    }

    @Override
    public boolean scheduleCancelTask(int id) {
        if (scheduleTaskList.containsKey(id)) {
            scheduleTaskList.get(id).cancel();
            scheduleTaskList.remove(id);
        } else
            return false;

        return true;
    }

    @Override
    public void cancelAllTasks() {
        timer.cancel();
        scheduleTaskList.clear();
    }
}
