package com.example.securescan;

import android.graphics.drawable.Drawable;
import java.util.List;

public class AppItem {
    private final String name;
    private final String packageName;
    private final Drawable icon;
    private final List<String> permissions;
    private final int riskLevel;
    private final int targetSdkVersion; // New field for SDK version

    public AppItem(String name, String packageName, Drawable icon, List<String> permissions, int riskLevel, int targetSdkVersion) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
        this.permissions = permissions;
        this.riskLevel = riskLevel;
        this.targetSdkVersion = targetSdkVersion;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public int getPermissionCount() {
        return (permissions != null) ? permissions.size() : 0;
    }

    public int getRiskLevel() {
        return riskLevel;
    }

    public int getTargetSdkVersion() {
        return targetSdkVersion;
    }
}