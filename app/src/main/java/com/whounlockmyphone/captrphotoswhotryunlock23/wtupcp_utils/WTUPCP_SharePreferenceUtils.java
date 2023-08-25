package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils;

import android.content.Context;
import android.content.SharedPreferences;

public class WTUPCP_SharePreferenceUtils {
    private Context context;
    private SharedPreferences mPreferences;

    public WTUPCP_SharePreferenceUtils(Context context2, String str) {
        this.context = context2;
        this.mPreferences = context2.getSharedPreferences(str, 4);
    }

    private String isExists() {
        return this.context.getFilesDir().toString();
    }

    public Boolean contains(String str) {
        return Boolean.valueOf(this.mPreferences.contains(str));
    }

    public String getString(String str, String str2) {
        return this.mPreferences.getString(str, str2);
    }

    public void putInt(String str, int i) {
        SharedPreferences.Editor edit = this.mPreferences.edit();
        edit.putInt(str, i);
        apply(edit);
    }

    public int getInt(String str, int i) {
        return this.mPreferences.getInt(str, i);
    }

    public void putString(String str, String str2) {
        SharedPreferences.Editor edit = this.mPreferences.edit();
        edit.putString(str, str2);
        apply(edit);
    }

    public Long getLong(String str, Long l) {
        return Long.valueOf(this.mPreferences.getLong(str, l.longValue()));
    }

    public void putLong(String str, Long l) {
        SharedPreferences.Editor edit = this.mPreferences.edit();
        edit.putLong(str, l.longValue());
        apply(edit);
    }

    public boolean getBoolean(String str, Boolean bool) {
        return this.mPreferences.getBoolean(str, bool.booleanValue());
    }

    public void putBoolean(String str, boolean z) {
        SharedPreferences.Editor edit = this.mPreferences.edit();
        edit.putBoolean(str, z);
        apply(edit);
    }

    private void apply(SharedPreferences.Editor editor) {
        editor.commit();
        editor.apply();
    }
}
