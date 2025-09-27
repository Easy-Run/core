// com.easyrun.core.AppContext.java
package com.easyrun.core;

import java.nio.file.Path;

public final class AppContext {
    private static volatile String appId = "default";
    private static volatile String profile = "default";
    private static volatile String baseDirOverride = null; // optionnel (EASYRUN_HOME)

    private AppContext() {}

    public static void set(String appIdParam, String profileParam, String baseDirParam) {
        if (appIdParam != null && !appIdParam.isEmpty()) appId = appIdParam;
        if (profileParam != null && !profileParam.isEmpty()) profile = profileParam;
        if (baseDirParam != null && !baseDirParam.isEmpty()) baseDirOverride = baseDirParam;
    }

    public static String getAppId() { return appId; }
    public static String getProfile() { return profile; }
    public static String getBaseDirOverride() { return baseDirOverride; }

    public static Path appendNamespace(Path root) {
        return root.resolve(appId).resolve(profile);
    }
}
