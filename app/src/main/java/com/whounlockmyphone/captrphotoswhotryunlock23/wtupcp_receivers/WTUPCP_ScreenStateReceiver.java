package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_receivers;

import android.app.KeyguardManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.db.WTUPCP_DatabaseClient;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_AppListEntity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_ReportEntity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_extra.WTUPCP_IntentFactory;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_model.WTUPCP_AppUsageInfo;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_services.WTUPCP_CameraService;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_services.WTUPCP_SaveReportService;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_SharePreferenceUtils;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Utils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WTUPCP_ScreenStateReceiver extends BroadcastReceiver {
    KeyguardManager check_lockscreen_enabled;
    Context context;
    private boolean isLogEnabled = false;
    private boolean isSessionRecording = false;
    private WTUPCP_SharePreferenceUtils mAppPrefs;
    private long mCurrEventTime;
    private long mLastScreenOnTime;
    ArrayList<String> packagenamesList = new ArrayList<>();
    WTUPCP_SharePreferenceUtils sharePreferenceUtils;
    private int timeDiffBetweenTriggering = 2000;
    ArrayList<Long> timesList = new ArrayList<>();

    public void onReceive(Context context2, Intent intent) {
        boolean z;
        this.context = context2;
        this.packagenamesList.clear();
        this.timesList.clear();
        if (intent != null) {
            this.sharePreferenceUtils = new WTUPCP_SharePreferenceUtils(context2, WTUPCP_Constants.PREFERENCE_NAME);
            this.mCurrEventTime = System.currentTimeMillis();
            WTUPCP_SharePreferenceUtils wTUPCP_SharePreferenceUtils = new WTUPCP_SharePreferenceUtils(context2, WTUPCP_Constants.PREFERENCE_NAME);
            this.mAppPrefs = wTUPCP_SharePreferenceUtils;
            this.isLogEnabled = false;
            this.isSessionRecording = wTUPCP_SharePreferenceUtils.getBoolean(WTUPCP_Constants.APP_SESSION_IS_RECORDING, false);
            this.mLastScreenOnTime = this.mAppPrefs.getLong(WTUPCP_Constants.LAST_SCREEN_ON_TIME, Long.valueOf((this.mCurrEventTime - ((long) this.timeDiffBetweenTriggering)) - 1)).longValue();
            if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                PowerManager powerManager = (PowerManager) context2.getSystemService("power");
                if (powerManager != null) {
                    if (Build.VERSION.SDK_INT >= 20) {
                        z = powerManager.isInteractive();
                    } else {
                        z = powerManager.isScreenOn();
                    }
                    if (z) {
                        return;
                    }
                }
                if (WTUPCP_CameraService.isServiceRunning(context2)) {
                    context2.stopService(new Intent(context2, WTUPCP_CameraService.class));
                }
                if (this.isSessionRecording && !WTUPCP_SaveReportService.isServiceRunning(context2)) {
                    context2.startService(new WTUPCP_IntentFactory(context2).getSaveRepServiceIntent(false, WTUPCP_Utils.checkDeviceWasUnlocked(context2, this.isLogEnabled)));
                }
                saveLastEventType("android.intent.action.SCREEN_OFF");
                if (Build.VERSION.SDK_INT > 28) {
                    new updateDatabase().execute(new Void[0]);
                } else if (this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_SAVE_REPORT_IF_NO_UNLOCK_ATTEMPSTS, Boolean.valueOf(WTUPCP_Constants.SETTING_SETTING_SAVE_REPORT_IF_NO_UNLOCK_ATTEMPSTS_DEFAULT)) || this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_SUCCESSFUL_UNLOCK_ATTEMPSTS, Boolean.valueOf(WTUPCP_Constants.SETTING_SUCCESSFUL_UNLOCK_ATTEMPSTS_DEFAULT))) {
                    new updateDatabase().execute(new Void[0]);
                }
            } else if ("android.intent.action.USER_PRESENT".equals(intent.getAction())) {
                startCameraService(context2, "USER_PRESENT");
                saveLastEventType("android.intent.action.USER_PRESENT");
                WTUPCP_Utils.stop_alarm();
            }
        }
    }

    private class updateDatabase extends AsyncTask<Void, Void, Void> {
        List<WTUPCP_AppUsageInfo> appUsageInfoList;

        private updateDatabase() {
            this.appUsageInfoList = new ArrayList();
        }

        
        public Void doInBackground(Void... voidArr) {
            List<WTUPCP_ReportEntity> allData = WTUPCP_DatabaseClient.getInstance(WTUPCP_ScreenStateReceiver.this.context).getAppDatabase().reportDao().getAllData();
            int size = allData.size();
            if (allData.size() < 1) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            int i = size - 1;
            sb.append(allData.get(i).isDEVICE_UNLOCK_FAIL());
            sb.append("");
            Log.e("APP_NAME", sb.toString());
            if (allData.get(i).isDEVICE_UNLOCK_FAIL()) {
                Log.e("APP_NAME", "UNLOCK YES");
                WTUPCP_ReportEntity wTUPCP_ReportEntity = allData.get(i);
                wTUPCP_ReportEntity.setEND_TIME(Calendar.getInstance().getTimeInMillis());
                WTUPCP_DatabaseClient.getInstance(WTUPCP_ScreenStateReceiver.this.context).getAppDatabase().reportDao().update(wTUPCP_ReportEntity);
                List<UsageStats> queryUsageStats = ((UsageStatsManager) WTUPCP_ScreenStateReceiver.this.context.getSystemService("usagestats")).queryUsageStats(4, wTUPCP_ReportEntity.getREPORT_TIME(), wTUPCP_ReportEntity.getEND_TIME());
                for (int i2 = 0; i2 < queryUsageStats.size(); i2++) {
                    Log.e("APP_NAME", "UNLOCK YES FOR---------" + queryUsageStats.get(i2).getPackageName());
                    String packageName = queryUsageStats.get(i2).getPackageName();
                    long lastTimeUsed = queryUsageStats.get(i2).getLastTimeUsed();
                    Log.e("CHECK_TIME", wTUPCP_ReportEntity.getREPORT_TIME() + "===" + lastTimeUsed);
                    if (wTUPCP_ReportEntity.getREPORT_TIME() < lastTimeUsed) {
                        WTUPCP_ScreenStateReceiver.this.packagenamesList.add(packageName);
                        WTUPCP_ScreenStateReceiver.this.timesList.add(Long.valueOf(lastTimeUsed));
                    }
                }
                if (!WTUPCP_ScreenStateReceiver.this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_SHOW_LAUNCHED_APP, Boolean.valueOf(WTUPCP_Constants.SETTING_SHOW_LAUNCHED_APP_DEFAULT))) {
                    return null;
                }
                Log.e("APP_NAME", "YES");
                for (int i3 = 0; i3 < WTUPCP_ScreenStateReceiver.this.packagenamesList.size(); i3++) {
                    try {
                        Log.e("APP_NAME", WTUPCP_Utils.getAppNameFromPkgName(WTUPCP_ScreenStateReceiver.this.context, WTUPCP_ScreenStateReceiver.this.packagenamesList.get(i3)));
                        WTUPCP_AppUsageInfo wTUPCP_AppUsageInfo = new WTUPCP_AppUsageInfo();
                        wTUPCP_AppUsageInfo.setAppName(WTUPCP_Utils.getAppNameFromPkgName(WTUPCP_ScreenStateReceiver.this.context, WTUPCP_ScreenStateReceiver.this.packagenamesList.get(i3)));
                        wTUPCP_AppUsageInfo.setAppIcon(WTUPCP_ScreenStateReceiver.this.context.getPackageManager().getApplicationIcon(WTUPCP_ScreenStateReceiver.this.packagenamesList.get(i3)));
                        wTUPCP_AppUsageInfo.setTimeInForeground(WTUPCP_ScreenStateReceiver.this.timesList.get(i3).longValue());
                        wTUPCP_AppUsageInfo.setPackageName(WTUPCP_ScreenStateReceiver.this.packagenamesList.get(i3));
                        wTUPCP_AppUsageInfo.setReportId(wTUPCP_ReportEntity.getREPORT_ID());
                        this.appUsageInfoList.add(wTUPCP_AppUsageInfo);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
            Log.e("APP_NAME", "UNLOCK NO");
            return null;
        }

        
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            new InsertUsedAppList(this.appUsageInfoList).execute(new Void[0]);
        }
    }

    public boolean isSystemApp(String str) {
        PackageManager packageManager = this.context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(str, 64);
            PackageInfo packageInfo2 = packageManager.getPackageInfo("android", 64);
            if (packageInfo == null || packageInfo.signatures == null || !packageInfo2.signatures[0].equals(packageInfo.signatures[0])) {
                return false;
            }
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private class InsertUsedAppList extends AsyncTask<Void, Void, Void> {
        List<WTUPCP_AppUsageInfo> appUsageInfoList;

        public InsertUsedAppList(List<WTUPCP_AppUsageInfo> list) {
            this.appUsageInfoList = list;
        }

        
        public Void doInBackground(Void... voidArr) {
            for (int i = 0; i < this.appUsageInfoList.size(); i++) {
                WTUPCP_AppUsageInfo wTUPCP_AppUsageInfo = this.appUsageInfoList.get(i);
                WTUPCP_AppListEntity wTUPCP_AppListEntity = new WTUPCP_AppListEntity();
                wTUPCP_AppListEntity.setAPP_NAME(wTUPCP_AppUsageInfo.getAppName());
                wTUPCP_AppListEntity.setPACKAGE_NAME(wTUPCP_AppUsageInfo.getPackageName());
                wTUPCP_AppListEntity.setAPP_TIME(wTUPCP_AppUsageInfo.getTimeInForeground());
                wTUPCP_AppListEntity.setREPORT_ID(wTUPCP_AppUsageInfo.getReportId());
                WTUPCP_DatabaseClient.getInstance(WTUPCP_ScreenStateReceiver.this.context).getAppDatabase().appListDao().insert(wTUPCP_AppListEntity);
            }
            return null;
        }

        
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            WTUPCP_ScreenStateReceiver.this.packagenamesList.clear();
            WTUPCP_ScreenStateReceiver.this.timesList.clear();
            WTUPCP_ScreenStateReceiver.this.sendUpdateUiBroadcast();
        }
    }

    
    public void sendUpdateUiBroadcast() {
        this.context.sendBroadcast(new WTUPCP_IntentFactory(this.context).getUpdateUiIntent(true));
    }

    private void saveLastEventType(String str) {
        WTUPCP_SharePreferenceUtils wTUPCP_SharePreferenceUtils = this.mAppPrefs;
        wTUPCP_SharePreferenceUtils.putString(WTUPCP_Constants.PRE_LAST_SCREEN_EVENT_TYPE, wTUPCP_SharePreferenceUtils.getString(WTUPCP_Constants.LAST_SCREEN_EVENT_TYPE, "android.intent.action.SCREEN_OFF"));
        this.mAppPrefs.putString(WTUPCP_Constants.LAST_SCREEN_EVENT_TYPE, str);
    }

    private void startCameraService(Context context2, String str) {
        this.mAppPrefs.putLong(WTUPCP_Constants.LAST_SCREEN_ON_TIME, Long.valueOf(this.mCurrEventTime));
        if (WTUPCP_CameraService.isServiceRunning(context2)) {
            Log.e("WT__CHECK_SERVICE", this.isSessionRecording + "");
            return;
        }
        KeyguardManager keyguardManager = (KeyguardManager) context2.getSystemService("keyguard");
        if (Build.VERSION.SDK_INT >= 23) {
            if (keyguardManager.isDeviceSecure()) {
                if (this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_SUCCESSFUL_UNLOCK_ATTEMPSTS, Boolean.valueOf(WTUPCP_Constants.SETTING_SUCCESSFUL_UNLOCK_ATTEMPSTS_DEFAULT)) && !WTUPCP_CameraService.isServiceRunning(context2)) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        context2.startForegroundService(new WTUPCP_IntentFactory(context2).getCameraServiceIntent(false));
                    } else {
                        context2.startService(new WTUPCP_IntentFactory(context2).getCameraServiceIntent(false));
                    }
                }
            } else if (this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_SAVE_REPORT_IF_NO_UNLOCK_ATTEMPSTS, Boolean.valueOf(WTUPCP_Constants.SETTING_SETTING_SAVE_REPORT_IF_NO_UNLOCK_ATTEMPSTS_DEFAULT)) && !WTUPCP_CameraService.isServiceRunning(context2)) {
                if (Build.VERSION.SDK_INT >= 26) {
                    context2.startForegroundService(new WTUPCP_IntentFactory(context2).getCameraServiceIntent(false));
                } else {
                    context2.startService(new WTUPCP_IntentFactory(context2).getCameraServiceIntent(false));
                }
            }
        } else if (this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_SAVE_REPORT_IF_NO_UNLOCK_ATTEMPSTS, Boolean.valueOf(WTUPCP_Constants.SETTING_SETTING_SAVE_REPORT_IF_NO_UNLOCK_ATTEMPSTS_DEFAULT)) || this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_SUCCESSFUL_UNLOCK_ATTEMPSTS, Boolean.valueOf(WTUPCP_Constants.SETTING_SUCCESSFUL_UNLOCK_ATTEMPSTS_DEFAULT))) {
            Log.e("WT__CHECK_SERVICE", "ELSE");
            if (WTUPCP_CameraService.isServiceRunning(context2)) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 26) {
                context2.startForegroundService(new WTUPCP_IntentFactory(context2).getCameraServiceIntent(false));
            } else {
                context2.startService(new WTUPCP_IntentFactory(context2).getCameraServiceIntent(false));
            }
        }
    }
}
