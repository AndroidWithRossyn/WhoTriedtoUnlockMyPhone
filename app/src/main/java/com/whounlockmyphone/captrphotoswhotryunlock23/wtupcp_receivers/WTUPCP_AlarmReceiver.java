package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.db.WTUPCP_DatabaseClient;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_AppListEntity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_ReportEntity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_SharePreferenceUtils;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_StorageUtills;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WTUPCP_AlarmReceiver extends BroadcastReceiver {
    List<WTUPCP_AppListEntity> appListEntityArrayList = new ArrayList();
    List<List<WTUPCP_AppListEntity>> appListEntityArrayListNew = new ArrayList();

    List<WTUPCP_ReportEntity> reportEntityArrayList = new ArrayList();
    List<WTUPCP_ReportEntity> reportEntityArrayListNew = new ArrayList();
    WTUPCP_SharePreferenceUtils sharePreferenceUtils;

    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras().getBoolean(WTUPCP_Constants.EXTRA_ALARM_OBJECT, false)) {
            Log.e("WT_", "RECEIVER CALLED");
            Toast.makeText(context, "Alarm Receiver Called", 0).show();
            WTUPCP_StorageUtills wTUPCP_StorageUtills = new WTUPCP_StorageUtills(context);
            File storeToPackageDirectory = wTUPCP_StorageUtills.storeToPackageDirectory("TextFiles", WTUPCP_Constants.simpleDateFormat1.format(new Date(System.currentTimeMillis())) + ".txt");
            WTUPCP_SharePreferenceUtils wTUPCP_SharePreferenceUtils = new WTUPCP_SharePreferenceUtils(context, WTUPCP_Constants.PREFERENCE_NAME);
            this.sharePreferenceUtils = wTUPCP_SharePreferenceUtils;
            if (wTUPCP_SharePreferenceUtils.getBoolean(context.getResources().getString(R.string.google_drive_sign_in), false)) {
                new getAllData(context, storeToPackageDirectory).execute(new Void[0]);
            }
        }
    }

    private class getAllData extends AsyncTask<Void, Void, Void> {
        Context context;
        File file;

        public getAllData(Context context2, File file2) {
            this.context = context2;
            this.file = file2;
        }

        
        public Void doInBackground(Void... voidArr) {
            WTUPCP_AlarmReceiver.this.reportEntityArrayList = WTUPCP_DatabaseClient.getInstance(this.context).getAppDatabase().reportDao().getAllData();
            WTUPCP_AlarmReceiver.this.appListEntityArrayList = WTUPCP_DatabaseClient.getInstance(this.context).getAppDatabase().appListDao().getAllData();
            for (WTUPCP_ReportEntity next : WTUPCP_AlarmReceiver.this.reportEntityArrayList) {
                if (next.isDEVICE_UNLOCK_FAIL() && WTUPCP_AlarmReceiver.isYesterday(next.getREPORT_TIME())) {
                    WTUPCP_AlarmReceiver.this.reportEntityArrayListNew.add(next);
                }
            }
            for (int i = 0; i < WTUPCP_AlarmReceiver.this.reportEntityArrayListNew.size(); i++) {
                WTUPCP_AlarmReceiver.this.appListEntityArrayListNew.add(WTUPCP_DatabaseClient.getInstance(this.context).getAppDatabase().appListDao().getAllDataForSingleReport(WTUPCP_AlarmReceiver.this.reportEntityArrayListNew.get(i).getREPORT_ID()));
            }
            return null;
        }

        
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < WTUPCP_AlarmReceiver.this.appListEntityArrayListNew.size(); i++) {
                sb.append(WTUPCP_Constants.simpleDateFormat3.format(new Date(WTUPCP_AlarmReceiver.this.reportEntityArrayListNew.get(i).getREPORT_TIME())));
                for (int i2 = 0; i2 < WTUPCP_AlarmReceiver.this.appListEntityArrayListNew.get(i).size(); i2++) {
                    Log.e("MAIN_UNLOCK", ((WTUPCP_AppListEntity) WTUPCP_AlarmReceiver.this.appListEntityArrayListNew.get(i).get(i2)).getAPP_NAME());
                    sb.append("\n");
                    sb.append("        " + ((WTUPCP_AppListEntity) WTUPCP_AlarmReceiver.this.appListEntityArrayListNew.get(i).get(i2)).getAPP_NAME() + "  " + WTUPCP_Constants.simpleDateFormat3.format(new Date(((WTUPCP_AppListEntity) WTUPCP_AlarmReceiver.this.appListEntityArrayListNew.get(i).get(i2)).getAPP_TIME())));
                }
                sb.append("\n");
                sb.append("\n");
            }
            WTUPCP_AlarmReceiver.this.writeToFile(sb.toString(), this.context, this.file);
            Log.e("TEXT_DATA", "DATA =>" + sb.toString());
        }
    }

    
    public void writeToFile(String str, final Context context, File file) {
        try {
            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
            fileWriter.write(str);
            fileWriter.close();
        } catch (IOException e) {
            Log.e("Exception12", "File write failed: " + e.toString());
        }
    }

    public static boolean isYesterday(long j) {
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        instance2.setTimeInMillis(j);
        instance.add(5, -1);
        if (instance.get(1) == instance2.get(1) && instance.get(2) == instance2.get(2) && instance.get(5) == instance2.get(5)) {
            return true;
        }
        return false;
    }
}
