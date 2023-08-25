package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity;

import java.io.Serializable;

public class WTUPCP_ReportEntity implements Serializable {
    private boolean DEVICE_UNLOCK_FAIL;
    private long END_TIME;
    private String PHOTO_PATH;
    private int REPORT_ID;
    private long REPORT_TIME;
    public int id;

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

    public long getEND_TIME() {
        return this.END_TIME;
    }

    public void setEND_TIME(long j) {
        this.END_TIME = j;
    }

    public long getREPORT_TIME() {
        return this.REPORT_TIME;
    }

    public void setREPORT_TIME(long j) {
        this.REPORT_TIME = j;
    }

    public boolean isDEVICE_UNLOCK_FAIL() {
        return this.DEVICE_UNLOCK_FAIL;
    }

    public void setDEVICE_UNLOCK_FAIL(boolean z) {
        this.DEVICE_UNLOCK_FAIL = z;
    }

    public String getPHOTO_PATH() {
        return this.PHOTO_PATH;
    }

    public void setPHOTO_PATH(String str) {
        this.PHOTO_PATH = str;
    }
}
