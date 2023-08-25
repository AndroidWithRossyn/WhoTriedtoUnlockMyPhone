package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.db;

import android.content.Context;
import androidx.room.Room;

public class WTUPCP_DatabaseClient {
    private static WTUPCP_DatabaseClient mInstance;
    private WTUPCP_AppDataBase appDatabase;
    private Context mCtx;

    private WTUPCP_DatabaseClient(Context context) {
        this.mCtx = context;
        this.appDatabase = Room.databaseBuilder(context, WTUPCP_AppDataBase.class, "ReportList").build();
    }

    public static synchronized WTUPCP_DatabaseClient getInstance(Context context) {
        WTUPCP_DatabaseClient wTUPCP_DatabaseClient;
        synchronized (WTUPCP_DatabaseClient.class) {
            if (mInstance == null) {
                mInstance = new WTUPCP_DatabaseClient(context);
            }
            wTUPCP_DatabaseClient = mInstance;
        }
        return wTUPCP_DatabaseClient;
    }

    public WTUPCP_AppDataBase getAppDatabase() {
        return this.appDatabase;
    }
}
