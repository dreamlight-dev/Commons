package com.dreamlightbuilds.commons;

import com.dreamlightbuilds.commons.bukkit.inventory.CustomInventoryManager;
import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

public final class Common {

    private static JavaPlugin INSTANCE;

    public static void init(final JavaPlugin plugin) {
        INSTANCE = plugin;

        CustomInventoryManager.register(plugin);
    }

    public static JavaPlugin getInstance() {
        return INSTANCE;
    }

    public static void sendMessage(final CommandSender sender, final List<String> messages) {
        messages.forEach(message -> sendMessage(sender, message));
    }

    public static void sendMessage(final CommandSender sender, final String message) {
        if (message.isEmpty() || message.equalsIgnoreCase("none")) {
            return;
        }
        sender.sendMessage(color(message));
    }

    public static List<String> color(final List<String> messages) {
        return messages.stream().map(Common::color).collect(Collectors.toList());
    }

    public static String color(final String message) {
        return BaseComponent.toLegacyText(MineDown.parse(message));
    }

}
