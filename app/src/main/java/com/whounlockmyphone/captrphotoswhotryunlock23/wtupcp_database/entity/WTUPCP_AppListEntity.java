package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity;

import java.io.Serializable;

public class WTUPCP_AppListEntity implements Serializable {
    private int APP_ICON;
    private String APP_NAME;
    private long APP_TIME;
    private String PACKAGE_NAME;
    private int REPORT_ID;
    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public int getREPORT_ID() {
        return this.REPORT_ID;
    }

    public void setREPORT_ID(int i) {
        this.REPORT_ID = i;
    }

    public int getAPP_ICON() {
        return this.APP_ICON;
    }

    public void setAPP_ICON(int i) {
        this.APP_ICON = i;
    }

    public long getAPP_TIME() {
        return this.APP_TIME;
    }

    public void setAPP_TIME(long j) {
        this.APP_TIME = j;
    }

    public String getAPP_NAME() {
        return this.APP_NAME;
    }

    public void setAPP_NAME(String str) {
        this.APP_NAME = str;
    }

    public String getPACKAGE_NAME() {
        return this.PACKAGE_NAME;
    }

    public void setPACKAGE_NAME(String str) {
        this.PACKAGE_NAME = str;
    }
}
