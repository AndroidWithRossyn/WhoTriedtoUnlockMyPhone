package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_services;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
//import com.google.android.gms.drive.DriveFile;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_activity.WTUPCP_HomeActivity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_extra.WTUPCP_IntentFactory;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_receivers.WTUPCP_ScreenStateReceiver;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_NotificationUtils;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_SharePreferenceUtils;

public class WTUPCP_MainService extends Service {
    boolean checkOffOrNot;
    String mNotificationTitle;
    WTUPCP_ScreenStateReceiver mScreenStateReceiver;
    boolean mServiceIsStarted = false;
    WTUPCP_SharePreferenceUtils sharePreferenceUtils;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean isServiceRunning(Context context) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (WTUPCP_MainService.class.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void onCreate() {
        super.onCreate();
        this.sharePreferenceUtils = new WTUPCP_SharePreferenceUtils(this, WTUPCP_Constants.PREFERENCE_NAME);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        this.mNotificationTitle = getResources().getString(R.string.running_bg);
        WTUPCP_ScreenStateReceiver wTUPCP_ScreenStateReceiver = new WTUPCP_ScreenStateReceiver();
        this.mScreenStateReceiver = wTUPCP_ScreenStateReceiver;
        registerReceiver(wTUPCP_ScreenStateReceiver, intentFilter);
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mScreenStateReceiver);
        if (!this.checkOffOrNot) {
            WTUPCP_IntentFactory wTUPCP_IntentFactory = new WTUPCP_IntentFactory(getApplicationContext());
            ContextCompat.startForegroundService(this, wTUPCP_IntentFactory.getMonitorServiceIntent(false));
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(wTUPCP_IntentFactory.getMonitorServiceIntent(false));
            } else {
                startService(wTUPCP_IntentFactory.getMonitorServiceIntent(false));
            }
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        try {
            boolean booleanExtra = intent.getBooleanExtra(WTUPCP_Constants.OFF_SERVICE_EXTRA, false);
            this.checkOffOrNot = booleanExtra;
            if (booleanExtra) {
                stopForeground(true);
                stopSelf();
            } else {
                goForeground();
            }
        } catch (Exception unused) {
        }
        return 1;
    }

    private void goForeground() {
        Intent intent = new Intent(this, WTUPCP_HomeActivity.class);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.addFlags(268435456);
        NotificationCompat.Builder contentIntent = new NotificationCompat.Builder(this, WTUPCP_NotificationUtils.CHANNEL_FOREGROUND).setSmallIcon(R.mipmap.ic_launcher).setStyle(new NotificationCompat.DecoratedCustomViewStyle()).setAutoCancel(true).setPriority(-2).setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE));
        if (!this.mNotificationTitle.isEmpty()) {
            contentIntent.setContentTitle(this.mNotificationTitle);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            WTUPCP_NotificationUtils.createNotificationChannels(this);
        }
        if (!this.mServiceIsStarted) {
            startForeground(WTUPCP_NotificationUtils.FOREGROUND_NOTIFICATION_ID, contentIntent.build());
            this.mServiceIsStarted = true;
        }
    }
}
