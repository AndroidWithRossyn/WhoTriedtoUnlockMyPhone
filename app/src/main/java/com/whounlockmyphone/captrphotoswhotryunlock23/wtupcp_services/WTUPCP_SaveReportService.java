package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_services;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_activity.WTUPCP_HomeActivity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.db.WTUPCP_DatabaseClient;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_ReportEntity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_extra.WTUPCP_IntentFactory;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_SharePreferenceUtils;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Utils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WTUPCP_SaveReportService extends Service {
    public static String ALarm_CHANNEL_NAME = "Notification_alarm";
    public static final String BROADCAST_ACTION = "Hello World";
    public static String Sound_CHANNEL_ID = "notification_alarm.CHANNEL_ID_FOREGROUND";
    public static NotificationManager mNotificationManager;
    public static NotificationManager mNotificationmanager_alarm;
    public static MediaPlayer mPlayer;
    public static Vibrator vibrator;
    boolean booleanExtra2;
    public Camera cam;
    String cameraId;
    CameraManager cameraManager;
    KeyguardManager check_lockscreen_enabled;
    Thread flash_thread = new Thread(new Runnable() {
        public void run() {
            if (Build.VERSION.SDK_INT >= 23) {
                WTUPCP_SaveReportService wTUPCP_SaveReportService = WTUPCP_SaveReportService.this;
                wTUPCP_SaveReportService.cameraManager = (CameraManager) wTUPCP_SaveReportService.getSystemService("camera");
                try {
                    WTUPCP_SaveReportService wTUPCP_SaveReportService2 = WTUPCP_SaveReportService.this;
                    wTUPCP_SaveReportService2.cameraId = wTUPCP_SaveReportService2.cameraManager.getCameraIdList()[0];
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                SystemClock.sleep(200);
                boolean z = true;
                while (!WTUPCP_Utils.flash_stop) {
                    System.out.println("flashing...");
                    if (z) {
                        try {
                            WTUPCP_SaveReportService.this.cameraManager.setTorchMode(WTUPCP_SaveReportService.this.cameraId, true);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                        z = false;
                    } else {
                        try {
                            WTUPCP_SaveReportService.this.cameraManager.setTorchMode(WTUPCP_SaveReportService.this.cameraId, false);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                        z = true;
                    }
                    SystemClock.sleep(1000);
                }
                try {
                    WTUPCP_SaveReportService.this.cameraManager.setTorchMode(WTUPCP_SaveReportService.this.cameraId, false);
                } catch (Exception unused) {
                }
            } else {
                WTUPCP_SaveReportService.this.cam = Camera.open();
                WTUPCP_SaveReportService.this.cam.getParameters();
                SystemClock.sleep(200);
                boolean z2 = true;
                while (!WTUPCP_Utils.flash_stop) {
                    if (z2) {
                        Camera.Parameters parameters = WTUPCP_SaveReportService.this.cam.getParameters();
                        parameters.setFlashMode("torch");
                        WTUPCP_SaveReportService.this.cam.setParameters(parameters);
                        WTUPCP_SaveReportService.this.cam.startPreview();
                        z2 = false;
                    } else {
                        Camera.Parameters parameters2 = WTUPCP_SaveReportService.this.cam.getParameters();
                        parameters2.setFlashMode("off");
                        WTUPCP_SaveReportService.this.cam.setParameters(parameters2);
                        WTUPCP_SaveReportService.this.cam.stopPreview();
                        z2 = true;
                    }
                    SystemClock.sleep(1000);
                }
                Camera.Parameters parameters3 = WTUPCP_SaveReportService.this.cam.getParameters();
                parameters3.setFlashMode("off");
                WTUPCP_SaveReportService.this.cam.setParameters(parameters3);
                WTUPCP_SaveReportService.this.cam.stopPreview();
                WTUPCP_SaveReportService.this.cam.release();
            }
        }
    });
    Handler handler;
    boolean isEmailEnabled;
    WTUPCP_SharePreferenceUtils sharePreferenceUtils;
    Thread vibrator_thread = new Thread(new Runnable() {
        public void run() {
            try {
                WTUPCP_SaveReportService.vibrator = (Vibrator) WTUPCP_SaveReportService.this.getSystemService("vibrator");
                while (!WTUPCP_Utils.vibrate_stop) {
                    WTUPCP_SaveReportService.vibrator.vibrate(new long[]{100, 1000, 1000, 1000}, 0, new AudioAttributes.Builder().setContentType(4).setUsage(4).build());
                    try {
                        SystemClock.sleep(2000);
                    } catch (IllegalThreadStateException e) {
                        Log.e("AlertService", "IllegalThreadStateException " + e);
                    }
                }
            } catch (Exception e2) {
                Log.e("AlertService", "Exception_main " + e2);
            }
        }
    });

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean isServiceRunning(Context context) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (WTUPCP_SaveReportService.class.getName().equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Log.e("WT__SERVICE", "SAVE REPORT RUNNING.....");
        this.sharePreferenceUtils = new WTUPCP_SharePreferenceUtils(this, WTUPCP_Constants.PREFERENCE_NAME);
        mNotificationManager = (NotificationManager) getSystemService("notification");
        this.check_lockscreen_enabled = (KeyguardManager) getSystemService("keyguard");
        mNotificationmanager_alarm = (NotificationManager) getSystemService("notification");
        boolean booleanExtra = intent.getBooleanExtra(WTUPCP_Constants.SAVE_REPORT_EXTRA, false);
        this.booleanExtra2 = intent.getBooleanExtra(WTUPCP_Constants.WRONG_PATTERN_EXTRA, false);
        intent.getBooleanExtra(WTUPCP_Constants.DEVICE_UNLOCKED_EXTRA, false);
        this.isEmailEnabled = this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_ALERT_ON_EMAIL, Boolean.valueOf(WTUPCP_Constants.SETTING_ALERT_ON_EMAIL_DEFAULT));
        if (booleanExtra && this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.APP_SESSION_IS_RECORDING, false)) {
            PowerManager.WakeLock newWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, "");
            newWakeLock.acquire();
            this.sharePreferenceUtils.getLong(WTUPCP_Constants.CURRENT_SESSION_BEGIN, Long.valueOf(System.currentTimeMillis() - 60000)).longValue();
            System.currentTimeMillis();
            new SimpleDateFormat(WTUPCP_Constants.SHORT_DATE_FORMAT, Locale.US);
            if (this.booleanExtra2) {
                if (this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_ENABLE_NOTIFICATION, Boolean.valueOf(WTUPCP_Constants.SETTING_ENABLE_NOTIFICATION_DEFAULT))) {
                    show_image_notification(R.mipmap.ic_launcher, "Anti Theft Alarm and Notification", this.sharePreferenceUtils.getString(WTUPCP_Constants.CURRENT_SESSION_PHOTO, ""));
                }
                if (this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_ALARM_ON_WRONG_PASSWORD, Boolean.valueOf(WTUPCP_Constants.SETTING_ALARM_ON_WRONG_PASSWORD_DEFAULT))) {
                    play_sound();
                }
                if (this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_VIBRATE_ON_WRONG_PASSWORD, Boolean.valueOf(WTUPCP_Constants.SETTING_VIBRATE_ON_WRONG_PASSWORD_DEFAULT)) && !WTUPCP_Utils.vibrate_stop) {
                    this.vibrator_thread.start();
                }
                if (this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_FLASH_ON_WRONG_PASSWORD, Boolean.valueOf(WTUPCP_Constants.SETTING_FLASH_ON_WRONG_PASSWORD_DEFAULT))) {
                    this.flash_thread.start();
                }
                new InsertReport(this, false, this.sharePreferenceUtils.getString(WTUPCP_Constants.CURRENT_SESSION_PHOTO, "")).execute(new Void[0]);
            } else if (Build.VERSION.SDK_INT < 23 || this.check_lockscreen_enabled.isDeviceSecure() || this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_SAVE_REPORT_IF_NO_UNLOCK_ATTEMPSTS, Boolean.valueOf(WTUPCP_Constants.SETTING_SETTING_SAVE_REPORT_IF_NO_UNLOCK_ATTEMPSTS_DEFAULT))) {
                if (this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_SUCCESSFUL_UNLOCK_ATTEMPSTS, Boolean.valueOf(WTUPCP_Constants.SETTING_SUCCESSFUL_UNLOCK_ATTEMPSTS_DEFAULT))) {
                    new InsertReport(this, true, this.sharePreferenceUtils.getString(WTUPCP_Constants.CURRENT_SESSION_PHOTO, "")).execute(new Void[0]);
                }
                WTUPCP_Utils.stop_alarm();
            } else {
                stopSelf();
                return 2;
            }
            this.sharePreferenceUtils.putLong(WTUPCP_Constants.CURRENT_SESSION_BEGIN, Long.valueOf(System.currentTimeMillis()));
            this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.APP_SESSION_IS_RECORDING, false);
            newWakeLock.release();
        }
        stopSelf();
        return 2;
    }

    public void onCreate() {
        super.onCreate();
        WTUPCP_Utils.vibrate_stop = false;
        WTUPCP_Utils.flash_stop = false;
    }

    public void play_sound() {
        try {
            if (mPlayer != null) {
                Log.e("Alert_Service", "mPlayer.isplaying  " + mPlayer.isPlaying());
                if (!mPlayer.isPlaying()) {
                    try {
                        NotificationManager notificationManager = mNotificationmanager_alarm;
                        if (notificationManager != null) {
                            notificationManager.cancel(Sound_CHANNEL_ID, 1);
                        }
                    } catch (Exception e) {
                        Log.e("Alert_Service", "Exception_alarm_notfication  " + e);
                    }
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mPlayer = mediaPlayer;
                    mediaPlayer.setAudioStreamType(4);
                    mPlayer.setDataSource(this, Uri.parse(this.sharePreferenceUtils.getString(WTUPCP_Constants.SETTING_ALARM_TONE, WTUPCP_Constants.SETTING_ALARM_TONE_DEFAULT)));
                    try {
                        String uri = RingtoneManager.getDefaultUri(4).toString();
                        mPlayer.setAudioStreamType(4);
                        mPlayer.setDataSource(this, Uri.parse(uri));
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    mPlayer.prepare();
                    mPlayer.start();
                    mPlayer.setLooping(true);
                    return;
                }
                return;
            }
            MediaPlayer mediaPlayer2 = new MediaPlayer();
            mPlayer = mediaPlayer2;
            mediaPlayer2.setAudioStreamType(4);
            mPlayer.setDataSource(this, Uri.parse(this.sharePreferenceUtils.getString(WTUPCP_Constants.SETTING_ALARM_TONE, WTUPCP_Constants.SETTING_ALARM_TONE_DEFAULT)));
            try {
                String uri2 = RingtoneManager.getDefaultUri(4).toString();
                mPlayer.setAudioStreamType(4);
                mPlayer.setDataSource(this, Uri.parse(uri2));
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setLooping(true);
        } catch (Exception unused) {
        }
    }

    public void show_alarm_notification(int i, String str) {
        PendingIntent activity = PendingIntent.getActivity(this, 1234001, new Intent(this, WTUPCP_HomeActivity.class), PendingIntent.FLAG_IMMUTABLE);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(Sound_CHANNEL_ID, ALarm_CHANNEL_NAME, 4);
            notificationChannel.enableVibration(false);
            notificationChannel.setLightColor(-16776961);
            notificationChannel.enableLights(true);
            notificationChannel.setSound((Uri) null, (AudioAttributes) null);
            notificationChannel.setShowBadge(true);
            mNotificationmanager_alarm.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder contentText = new NotificationCompat.Builder(getApplicationContext(), Sound_CHANNEL_ID).setVibrate((long[]) null).setSound((Uri) null, 0).setPriority(2).setLights(-16776961, 3000, 3000).setAutoCancel(true).setSound((Uri) null).setOngoing(true).setContentIntent(activity).setContentTitle(getResources().getString(R.string.app_name)).setSmallIcon(R.mipmap.ic_launcher).setContentText(str);
        Notification build = contentText.build();
        if (Build.VERSION.SDK_INT >= 26) {
            contentText.setChannelId(Sound_CHANNEL_ID);
            startForeground(1, build);
            mNotificationmanager_alarm.notify(Sound_CHANNEL_ID, 1, build);
            return;
        }
        mNotificationmanager_alarm.notify(Sound_CHANNEL_ID, 1, build);
    }

    public void geoTag(String str) {
        new Thread(new Runnable() {
            public void run() {
            }
        }).start();
    }

    public void show_image_notification(int i, String str, String str2) {
        new File(str2).getPath();
        new File(str2).getName();
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        Intent intent = new Intent(this, WTUPCP_HomeActivity.class);
        intent.setFlags(603979776);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(str2));
            bigPictureStyle.setBigContentTitle(str);
            bigPictureStyle.setSummaryText("Someone tried to unlock your device");
            bigPictureStyle.bigPicture(bitmap);
        } catch (Exception unused) {
        }
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("notification.CHANNEL_ID_FOREGROUND", "Notification", 4);
            notificationChannel.enableVibration(false);
            notificationChannel.setLightColor(-16776961);
            notificationChannel.enableLights(true);
            notificationChannel.setSound((Uri) null, (AudioAttributes) null);
            notificationChannel.setShowBadge(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder contentText = new NotificationCompat.Builder(getApplicationContext(), "notification.CHANNEL_ID_FOREGROUND").setVibrate((long[]) null).setSound((Uri) null, 0).setPriority(2).setLights(-16776961, 3000, 3000).setAutoCancel(true).setSound((Uri) null).setContentIntent(activity).setContentTitle(getResources().getString(R.string.app_name)).setSmallIcon(R.mipmap.ic_launcher).setStyle(bigPictureStyle).setContentText(str);
        Notification build = contentText.build();
        if (Build.VERSION.SDK_INT >= 26) {
            contentText.setChannelId("notification.CHANNEL_ID_FOREGROUND");
            startForeground(1, build);
            mNotificationManager.notify("notification.CHANNEL_ID_FOREGROUND", 1, build);
            return;
        }
        mNotificationManager.notify("notification.CHANNEL_ID_FOREGROUND", 1, build);
    }

    
    public void sendUpdateUiBroadcast() {
        sendBroadcast(new WTUPCP_IntentFactory(getApplicationContext()).getUpdateUiIntent(true));
    }

    private class InsertReport extends AsyncTask<Void, Void, Void> {
        long check;
        Context context;
        String imagePath;
        boolean isDEVICE_UNLOCK_FAIL;
        WTUPCP_ReportEntity reportEntity;

        public InsertReport(Context context2, boolean z, String str) {
            this.context = context2;
            this.imagePath = str;
            this.isDEVICE_UNLOCK_FAIL = z;
        }

        
        public void onPreExecute() {
            super.onPreExecute();
            WTUPCP_ReportEntity wTUPCP_ReportEntity = new WTUPCP_ReportEntity();
            this.reportEntity = wTUPCP_ReportEntity;
            wTUPCP_ReportEntity.setREPORT_TIME(Calendar.getInstance().getTimeInMillis());
            this.reportEntity.setDEVICE_UNLOCK_FAIL(this.isDEVICE_UNLOCK_FAIL);
            this.reportEntity.setREPORT_ID(WTUPCP_SaveReportService.this.addReportId(this.context));
            this.reportEntity.setPHOTO_PATH(this.imagePath);
            this.reportEntity.setEND_TIME(0);
        }

        
        public Void doInBackground(Void... voidArr) {
            this.check = WTUPCP_DatabaseClient.getInstance(this.context).getAppDatabase().reportDao().insert(this.reportEntity);
            return null;
        }

        
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (this.check > 0) {
                Log.e("WT__ERROR", "SAVED IMAGE");
            } else {
                Toast.makeText(this.context, "ERROR", 0).show();
                Log.e("WT__ERROR", "ERROR IMAGE");
            }
            WTUPCP_SaveReportService.this.sharePreferenceUtils.putString(WTUPCP_Constants.CURRENT_SESSION_PHOTO, "");
            WTUPCP_SaveReportService.this.sendUpdateUiBroadcast();
        }
    }

    
    public int addReportId(Context context) {
        WTUPCP_SharePreferenceUtils wTUPCP_SharePreferenceUtils = new WTUPCP_SharePreferenceUtils(context, WTUPCP_Constants.PREFERENCE_NAME);
        int i = wTUPCP_SharePreferenceUtils.getInt(context.getResources().getString(R.string.pref_report_id), 0) + 1;
        wTUPCP_SharePreferenceUtils.putInt(context.getResources().getString(R.string.pref_report_id), i);
        Log.e("REPORT_ID", "ID =>" + i);
        return i;
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
