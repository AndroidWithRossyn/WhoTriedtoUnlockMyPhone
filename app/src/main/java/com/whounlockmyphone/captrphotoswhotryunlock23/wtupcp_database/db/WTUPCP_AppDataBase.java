package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.db;

import androidx.room.RoomDatabase;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.dao.WTUPCP_AppListDao;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.dao.WTUPCP_ReportDao;

public abstract class WTUPCP_AppDataBase extends RoomDatabase {
    public abstract WTUPCP_AppListDao appListDao();

    public abstract WTUPCP_ReportDao reportDao();
}
