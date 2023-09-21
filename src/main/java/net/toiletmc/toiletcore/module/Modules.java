package net.toiletmc.toiletcore.module;

import net.toiletmc.toiletcore.module.authmehook.AuthmeHook;
import net.toiletmc.toiletcore.module.placeholderapihook.PlaceholderAPIHook;

public enum Modules {
    AUTHMEHOOK(AuthmeHook.class),
    PlaceholderAPIHook(PlaceholderAPIHook.class);

    Modules(Object className) {

    }
}
