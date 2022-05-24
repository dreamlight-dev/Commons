package com.dreamlightbuilds.commons;

import java.util.logging.Level;

public class Logger {

    private static boolean isDebugEnabled = false;

    public static void info(final String... messages) {
        for (String message : messages) {
            Common.getInstance().getLogger().log(Level.INFO, message);
        }
    }

    public static void warning(final String... messages) {
        for (String message : messages) {
            Common.getInstance().getLogger().log(Level.WARNING, message);
        }
    }

    public static void error(final String... messages) {
        for (String message : messages) {
            Common.getInstance().getLogger().log(Level.SEVERE, message);
        }
    }

    public static void debug(final String... messages) {
        if (isDebugEnabled) {
            info(messages);
        }
    }

    public static void enableDebug(boolean state) {
        isDebugEnabled = state;
    }

}
