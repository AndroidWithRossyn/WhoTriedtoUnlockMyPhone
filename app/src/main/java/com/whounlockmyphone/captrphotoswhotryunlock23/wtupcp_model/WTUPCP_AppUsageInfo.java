package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_model;

import android.graphics.drawable.Drawable;

public class WTUPCP_AppUsageInfo {
    Drawable appIcon;
    String appName;
    long lastUsageTime;
    String packageName;
    int reportId;

    public Drawable getAppIcon() {
        return this.appIcon;
    }

    public void setAppIcon(Drawable drawable) {
        this.appIcon = drawable;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }

    public long getTimeInForeground() {
        return this.lastUsageTime;
    }

    public void setTimeInForeground(long j) {
        this.lastUsageTime = j;
    }

    public int getReportId() {
        return this.reportId;
    }

    public void setReportId(int i) {
        this.reportId = i;
    }
}
