package com.dreamlightbuilds.commons.bukkit.inventory;

import com.dreamlightbuilds.commons.bukkit.task.Executor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Manager for CustomInventory listeners.
 *
 * @author MrMicky
 */
public final class CustomInventoryManager {

    private static final AtomicBoolean REGISTERED = new AtomicBoolean(false);

    private CustomInventoryManager() {
        throw new UnsupportedOperationException();
    }

    /**
     * Register listeners for CustomInventory.
     *
     * @param plugin plugin to register
     * @throws NullPointerException  if plugin is null
     * @throws IllegalStateException if CustomInventory is already registered
     */
    public static void register(final Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin");

        if (REGISTERED.getAndSet(true)) {
            throw new IllegalStateException("CustomInventory is already registered");
        }

        Bukkit.getPluginManager().registerEvents(new InventoryListener(plugin), plugin);
    }

    /**
     * Close all open CustomInventory inventories.
     */
    public static void closeAll() {
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.getOpenInventory().getTopInventory().getHolder() instanceof CustomInventory)
                .forEach(Player::closeInventory);
    }

    public record InventoryListener(
            Plugin plugin
    ) implements Listener {

        @EventHandler
        public void onInventoryClick(final InventoryClickEvent event) {
            if (event.getInventory().getHolder() instanceof CustomInventory inv &&
                    event.getClickedInventory() != null) {

                final boolean wasCancelled = event.isCancelled();
                event.setCancelled(true);

                inv.handleClick(event);

                // This prevents un-canceling the event if another plugin canceled it before
                if (!wasCancelled && !event.isCancelled()) {
                    event.setCancelled(false);
                }
            }
        }

        @EventHandler
        public void onInventoryOpen(final InventoryOpenEvent event) {
            if (event.getInventory().getHolder() instanceof CustomInventory inv) {
                inv.handleOpen(event);
            }
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {
            if (event.getInventory().getHolder() instanceof CustomInventory inv &&
                    inv.handleClose(event)) {

                Executor.sync(() -> inv.open((Player) event.getPlayer()));
            }
        }

        @EventHandler
        public void onPluginDisable(PluginDisableEvent event) {
            if (event.getPlugin() == this.plugin) {
                closeAll();

                REGISTERED.set(false);
            }
        }
    }
}
