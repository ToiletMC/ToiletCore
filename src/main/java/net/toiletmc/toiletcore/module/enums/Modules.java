package net.toiletmc.toiletcore.module.enums;

import net.toiletmc.toiletcore.module.impl.authme.AuthmeModule;
import net.toiletmc.toiletcore.module.impl.placeholder.PlaceholderModule;
import net.toiletmc.toiletcore.module.interfaces.Module;

public enum Modules {
    AUTHME("authme", AuthmeModule.class, "Authme 密码算法支持"),
    PLACEHOLDER("placeholder-api", PlaceholderModule.class, "PlaceholderAPI 支持");

    public final String name;
    public final Class<? extends Module> moduleClass;
    public final String description;

    Modules(String name, Class<? extends Module> moduleClass, String description) {
        this.name = name;
        this.moduleClass = moduleClass;
        this.description = description;
    }
}
