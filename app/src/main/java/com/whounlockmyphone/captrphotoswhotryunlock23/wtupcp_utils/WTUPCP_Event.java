package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils;

import android.location.Location;

public class WTUPCP_Event {
    private String imagePath;
    private Location location;

    public void setImagePath(String str) {
        this.imagePath = str;
    }

    public void setLocation(Location location2) {
        this.location = location2;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getImagePath() {
        return this.imagePath;
    }
}
