package net.toiletmc.toiletcore.module.enums;

import net.toiletmc.toiletcore.module.impl.authme.AuthmeModule;
import net.toiletmc.toiletcore.module.impl.debugstick.DebugStickModule;
import net.toiletmc.toiletcore.module.impl.lagalert.LagAlertModule;
import net.toiletmc.toiletcore.module.impl.placeholder.PlaceholderModule;
import net.toiletmc.toiletcore.module.impl.premium.PremiumModule;
import net.toiletmc.toiletcore.module.impl.shart.ShartModule;

public enum Module {
    AUTHME("authme", AuthmeModule.class, "Authme 密码算法支持"),
    PLACEHOLDER("placeholder-api", PlaceholderModule.class, "PlaceholderAPI 支持"),
    DEBUGSTICK("debug-stick", DebugStickModule.class, "生存模式调试棒"),
    LAGALERT("lag-alert", LagAlertModule.class, "滞后监测程序"),
    SHART("shart", ShartModule.class, "排泄"),
    PREMIUM("premium", PremiumModule.class, "正版玩家奖励");

    public final String name;
    public final Class<? extends net.toiletmc.toiletcore.module.interfaces.Module> moduleClass;
    public final String description;

    Module(String name, Class<? extends net.toiletmc.toiletcore.module.interfaces.Module> moduleClass, String description) {
        this.name = name;
        this.moduleClass = moduleClass;
        this.description = description;
    }
}
