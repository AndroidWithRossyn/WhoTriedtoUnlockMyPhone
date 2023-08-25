package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationManagerCompat;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;

public class WTUPCP_NotificationUtils {
    public static String CHANNEL_FOREGROUND = "WTMP_APP_CHANNEL_FG";
    public static String CHANNEL_OTHER = "WTMP_APP_CHANNEL_OTHER";
    public static int FOREGROUND_NOTIFICATION_ID = 1123;

    public static void createNotificationChannels(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (notificationManager != null && notificationManager.getNotificationChannel(CHANNEL_FOREGROUND) == null) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_FOREGROUND, context.getString(R.string.services_notifications), 1);
            notificationChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        if (notificationManager != null && notificationManager.getNotificationChannel(CHANNEL_OTHER) == null) {
            notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_OTHER, context.getString(R.string.other_notifications), 3));
        }
    }

    public static boolean isForegroundNotificationsEnabled(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return NotificationManagerCompat.from(context).areNotificationsEnabled();
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        NotificationChannel notificationChannel = notificationManager.getNotificationChannel(CHANNEL_FOREGROUND);
        boolean z = true;
        if (notificationChannel == null) {
            return true;
        }
        if (notificationChannel.getImportance() == 0) {
            z = false;
        }
        return notificationManager.areNotificationsEnabled() & z;
    }
}
