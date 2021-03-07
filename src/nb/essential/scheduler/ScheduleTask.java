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

package nb.essential.scheduler;

import java.util.TimerTask;

import static nb.essential.main.NbEssential.getLogger;

/**
 * Class of NbEssential created by MađeShirő ƵÆsora on 28/08/2016
 */
public class ScheduleTask extends TimerTask {

    private int taskID;
    private IScheduleRunnable runnable;

    /**
     * Creates a new Schedule task with the specified runnable and its ID.
     * @param id The task's id assigned by the {@link NbScheduler}.
     * @param task The runnable to execute when {@link #run()} is called.
     */
    public ScheduleTask(int id, IScheduleRunnable task) {
        this . taskID = id;
        this . runnable = task;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void run() {
        try {
            runnable.run(taskID);
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().severe("Task cancelled ! (id = " + taskID + ")");
            cancel();
        }
    }
}
