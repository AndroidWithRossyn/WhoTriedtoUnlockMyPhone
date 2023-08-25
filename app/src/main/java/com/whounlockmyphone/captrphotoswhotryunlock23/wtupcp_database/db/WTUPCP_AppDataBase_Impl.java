package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.db;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;
import androidx.room.RoomOpenHelper;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.dao.WTUPCP_AppListDao;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.dao.WTUPCP_AppListDao_Impl;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.dao.WTUPCP_ReportDao;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.dao.WTUPCP_ReportDao_Impl;
import java.util.HashMap;
import java.util.HashSet;

public final class WTUPCP_AppDataBase_Impl extends WTUPCP_AppDataBase {
    private volatile WTUPCP_AppListDao _wTUPCPAppListDao;
    private volatile WTUPCP_ReportDao _wTUPCPReportDao;

    
    public SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, new RoomOpenHelper.Delegate(1) {
            public void createAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `WTUPCP_ReportEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `REPORT_ID` INTEGER NOT NULL, `REPORT_TIME` INTEGER NOT NULL, `END_TIME` INTEGER NOT NULL, `DEVICE_UNLOCK_FAIL` INTEGER NOT NULL, `PHOTO_PATH` TEXT)");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `WTUPCP_AppListEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `REPORT_ID` INTEGER NOT NULL, `APP_ICON` INTEGER NOT NULL, `APP_TIME` INTEGER NOT NULL, `APP_NAME` TEXT, `PACKAGE_NAME` TEXT)");
                supportSQLiteDatabase.execSQL(RoomMasterTable.CREATE_QUERY);
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"510833f9708f1d27272779dc35e3f78c\")");
            }

            public void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `WTUPCP_ReportEntity`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `WTUPCP_AppListEntity`");
            }

            
            public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
                if (WTUPCP_AppDataBase_Impl.this.mCallbacks != null) {
                    int size = WTUPCP_AppDataBase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) WTUPCP_AppDataBase_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }

            public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
                SupportSQLiteDatabase unused = WTUPCP_AppDataBase_Impl.this.mDatabase = supportSQLiteDatabase;
                WTUPCP_AppDataBase_Impl.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (WTUPCP_AppDataBase_Impl.this.mCallbacks != null) {
                    int size = WTUPCP_AppDataBase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) WTUPCP_AppDataBase_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }

            
            public void validateMigration(SupportSQLiteDatabase supportSQLiteDatabase) {
                HashMap hashMap = new HashMap(6);
                hashMap.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
                hashMap.put("REPORT_ID", new TableInfo.Column("REPORT_ID", "INTEGER", true, 0));
                hashMap.put("REPORT_TIME", new TableInfo.Column("REPORT_TIME", "INTEGER", true, 0));
                hashMap.put("END_TIME", new TableInfo.Column("END_TIME", "INTEGER", true, 0));
                hashMap.put("DEVICE_UNLOCK_FAIL", new TableInfo.Column("DEVICE_UNLOCK_FAIL", "INTEGER", true, 0));
                hashMap.put("PHOTO_PATH", new TableInfo.Column("PHOTO_PATH", "TEXT", false, 0));
                TableInfo tableInfo = new TableInfo("WTUPCP_ReportEntity", hashMap, new HashSet(0), new HashSet(0));
                TableInfo read = TableInfo.read(supportSQLiteDatabase, "WTUPCP_ReportEntity");
                if (tableInfo.equals(read)) {
                    HashMap hashMap2 = new HashMap(6);
                    hashMap2.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
                    hashMap2.put("REPORT_ID", new TableInfo.Column("REPORT_ID", "INTEGER", true, 0));
                    hashMap2.put("APP_ICON", new TableInfo.Column("APP_ICON", "INTEGER", true, 0));
                    hashMap2.put("APP_TIME", new TableInfo.Column("APP_TIME", "INTEGER", true, 0));
                    hashMap2.put("APP_NAME", new TableInfo.Column("APP_NAME", "TEXT", false, 0));
                    hashMap2.put("PACKAGE_NAME", new TableInfo.Column("PACKAGE_NAME", "TEXT", false, 0));
                    TableInfo tableInfo2 = new TableInfo("WTUPCP_AppListEntity", hashMap2, new HashSet(0), new HashSet(0));
                    TableInfo read2 = TableInfo.read(supportSQLiteDatabase, "WTUPCP_AppListEntity");
                    if (!tableInfo2.equals(read2)) {
                        throw new IllegalStateException("Migration didn't properly handle WTUPCP_AppListEntity(com.whotryunlockphone.captrphotos.wtupcp_database.entity.WTUPCP_AppListEntity).\n Expected:\n" + tableInfo2 + "\n Found:\n" + read2);
                    }
                    return;
                }
                throw new IllegalStateException("Migration didn't properly handle WTUPCP_ReportEntity(com.whotryunlockphone.captrphotos.wtupcp_database.entity.WTUPCP_ReportEntity).\n Expected:\n" + tableInfo + "\n Found:\n" + read);
            }
        }, "510833f9708f1d27272779dc35e3f78c", "8399f52c24834b4e26e68449a082275d")).build());
    }

    
    public InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, "WTUPCP_ReportEntity", "WTUPCP_AppListEntity");
    }

    public void clearAllTables() {
        super.assertNotMainThread();
        SupportSQLiteDatabase writableDatabase = super.getOpenHelper().getWritableDatabase();
        try {
            super.beginTransaction();
            writableDatabase.execSQL("DELETE FROM `WTUPCP_ReportEntity`");
            writableDatabase.execSQL("DELETE FROM `WTUPCP_AppListEntity`");
            super.setTransactionSuccessful();
        } finally {
            super.endTransaction();
            writableDatabase.query("PRAGMA wal_checkpoint(FULL)").close();
            if (!writableDatabase.inTransaction()) {
                writableDatabase.execSQL("VACUUM");
            }
        }
    }

    public WTUPCP_ReportDao reportDao() {
        WTUPCP_ReportDao wTUPCP_ReportDao;
        if (this._wTUPCPReportDao != null) {
            return this._wTUPCPReportDao;
        }
        synchronized (this) {
            if (this._wTUPCPReportDao == null) {
                this._wTUPCPReportDao = new WTUPCP_ReportDao_Impl(this);
            }
            wTUPCP_ReportDao = this._wTUPCPReportDao;
        }
        return wTUPCP_ReportDao;
    }

    public WTUPCP_AppListDao appListDao() {
        WTUPCP_AppListDao wTUPCP_AppListDao;
        if (this._wTUPCPAppListDao != null) {
            return this._wTUPCPAppListDao;
        }
        synchronized (this) {
            if (this._wTUPCPAppListDao == null) {
                this._wTUPCPAppListDao = new WTUPCP_AppListDao_Impl(this);
            }
            wTUPCP_AppListDao = this._wTUPCPAppListDao;
        }
        return wTUPCP_AppListDao;
    }
}
