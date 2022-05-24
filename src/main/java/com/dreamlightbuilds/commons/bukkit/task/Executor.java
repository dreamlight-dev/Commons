package com.dreamlightbuilds.commons.bukkit.task;

import com.dreamlightbuilds.commons.Common;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Executor {

    public static BukkitTask sync(final Runnable runnable) {
        return Bukkit.getScheduler().runTask(Common.getInstance(), runnable);
    }

    public static BukkitTask syncLater(final long delay, final Runnable runnable) {
        return Bukkit.getScheduler().runTaskLater(Common.getInstance(), runnable, delay);
    }

    public static BukkitTask syncTimer(final long delay, final long runEvery, final Runnable runnable) {
        return Bukkit.getScheduler().runTaskTimer(Common.getInstance(), runnable, delay, runEvery);
    }

    public static BukkitTask async(final Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(Common.getInstance(), runnable);
    }

    public static BukkitTask asyncLater(final long delay, final Runnable runnable) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(Common.getInstance(), runnable, delay);
    }

    public static BukkitTask asyncTimer(final long delay, final long runEvery, final Runnable runnable) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(Common.getInstance(), runnable, delay, runEvery);
    }
    
}
