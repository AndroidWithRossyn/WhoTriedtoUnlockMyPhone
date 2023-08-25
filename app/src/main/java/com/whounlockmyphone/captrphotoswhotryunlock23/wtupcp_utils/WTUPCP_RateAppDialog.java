package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class WTUPCP_RateAppDialog {
    public static int DAYS_UNTIL_PROMPT = 1;
    public static final int MAX_NEVER_PROMPT = 2;
    public static final int MAX_RATE_PROMPT = 2;
    public static final int MAX_REMIND_PROMPT = 5;
    public static Context context;
    public static Long first_launch_date_time;
    public static Long launch_date_time;
    public static int never_count;
    public static int rate_count;
    public static int total_launch_count;

    public static void AppLaunch(Context context2, int i, int i2, int i3, String str) {
        context = context2;
        SharedPreferences sharedPreferences = context2.getSharedPreferences("app_rater", 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        total_launch_count = sharedPreferences.getInt("total_launch_count", 1);
        never_count = sharedPreferences.getInt("never_count", 1);
        rate_count = sharedPreferences.getInt("rate_count", 1);
        if (!sharedPreferences.getBoolean("do_not_show_again", false)) {
            Long valueOf = Long.valueOf(sharedPreferences.getLong("first_launch_date_time", 0));
            first_launch_date_time = valueOf;
            if (valueOf.longValue() == 0) {
                Long valueOf2 = Long.valueOf(System.currentTimeMillis());
                first_launch_date_time = valueOf2;
                edit.putLong("first_launch_date_time", valueOf2.longValue());
            }
            launch_date_time = Long.valueOf(sharedPreferences.getLong("launch_date_time", 0));
            if (System.currentTimeMillis() >= launch_date_time.longValue() + 86400000 && DAYS_UNTIL_PROMPT <= 5) {
                edit.putLong("launch_date_time", System.currentTimeMillis());
                DAYS_UNTIL_PROMPT++;
            }
            int i4 = total_launch_count;
            if (i4 <= 5) {
                if (edit != null) {
                    edit.putInt("total_launch_count", i4 + 1);
                    edit.commit();
                }
                if (total_launch_count == 1) {
                    showRateDialog(context, i, i2, i3, str);
                } else if (System.currentTimeMillis() >= launch_date_time.longValue() + 86400000) {
                    showRateDialog(context, i, i2, i3, str);
                }
            }
            edit.commit();
        }
    }

    public static void showRateDialog(final Context context2, int i, int i2, int i3, final String str) {
        SharedPreferences sharedPreferences = context2.getSharedPreferences("app_rater", 0);
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        if (!sharedPreferences.getBoolean("do_not_show_again", false)) {
            final Dialog dialog = new Dialog(context2);
            dialog.requestWindowFeature(1);
            dialog.getWindow().setBackgroundDrawableResource(17170445);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            View inflate = ((Activity) context2).getLayoutInflater().inflate(i, (ViewGroup) null);
            ((TextView) inflate.findViewById(i2)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (str.equalsIgnoreCase("main")) {
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                    }
                }
            });
            ((TextView) inflate.findViewById(i3)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (WTUPCP_RateAppDialog.rate_count <= 2) {
                        SharedPreferences.Editor editor = edit;
                        if (editor != null) {
                            editor.putInt("rate_count", WTUPCP_RateAppDialog.rate_count + 1);
                            edit.commit();
                        }
                        try {
                            context2.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + context2.getPackageName())));
                        } catch (ActivityNotFoundException unused) {
                            Context context = context2;
                            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + context2.getPackageName())));
                            Toast.makeText(context2, " unable to find market app", 1).show();
                        }
                    } else {
                        SharedPreferences.Editor editor2 = edit;
                        if (editor2 != null) {
                            editor2.putBoolean("do_not_show_again", true);
                            edit.commit();
                        }
                    }
                    dialog.dismiss();
                }
            });
            dialog.setContentView(inflate);
            dialog.show();
        }
    }
}
