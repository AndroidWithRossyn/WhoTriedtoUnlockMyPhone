package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_AppListEntity;
import java.util.ArrayList;
import java.util.List;

public final class WTUPCP_AppListDao_Impl implements WTUPCP_AppListDao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter __deletionAdapterOfWTUPCP_AppListEntity;
    private final EntityInsertionAdapter __insertionAdapterOfWTUPCP_AppListEntity;
    private final SharedSQLiteStatement __preparedStmtOfDeleteAllRows;
    private final EntityDeletionOrUpdateAdapter __updateAdapterOfWTUPCP_AppListEntity;

    public WTUPCP_AppListDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfWTUPCP_AppListEntity = new EntityInsertionAdapter<WTUPCP_AppListEntity>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR ABORT INTO `WTUPCP_AppListEntity`(`id`,`REPORT_ID`,`APP_ICON`,`APP_TIME`,`APP_NAME`,`PACKAGE_NAME`) VALUES (nullif(?, 0),?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, WTUPCP_AppListEntity wTUPCP_AppListEntity) {
                supportSQLiteStatement.bindLong(1, (long) wTUPCP_AppListEntity.getId());
                supportSQLiteStatement.bindLong(2, (long) wTUPCP_AppListEntity.getREPORT_ID());
                supportSQLiteStatement.bindLong(3, (long) wTUPCP_AppListEntity.getAPP_ICON());
                supportSQLiteStatement.bindLong(4, wTUPCP_AppListEntity.getAPP_TIME());
                if (wTUPCP_AppListEntity.getAPP_NAME() == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindString(5, wTUPCP_AppListEntity.getAPP_NAME());
                }
                if (wTUPCP_AppListEntity.getPACKAGE_NAME() == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindString(6, wTUPCP_AppListEntity.getPACKAGE_NAME());
                }
            }
        };
        this.__deletionAdapterOfWTUPCP_AppListEntity = new EntityDeletionOrUpdateAdapter<WTUPCP_AppListEntity>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `WTUPCP_AppListEntity` WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, WTUPCP_AppListEntity wTUPCP_AppListEntity) {
                supportSQLiteStatement.bindLong(1, (long) wTUPCP_AppListEntity.getId());
            }
        };
        this.__updateAdapterOfWTUPCP_AppListEntity = new EntityDeletionOrUpdateAdapter<WTUPCP_AppListEntity>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `WTUPCP_AppListEntity` SET `id` = ?,`REPORT_ID` = ?,`APP_ICON` = ?,`APP_TIME` = ?,`APP_NAME` = ?,`PACKAGE_NAME` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, WTUPCP_AppListEntity wTUPCP_AppListEntity) {
                supportSQLiteStatement.bindLong(1, (long) wTUPCP_AppListEntity.getId());
                supportSQLiteStatement.bindLong(2, (long) wTUPCP_AppListEntity.getREPORT_ID());
                supportSQLiteStatement.bindLong(3, (long) wTUPCP_AppListEntity.getAPP_ICON());
                supportSQLiteStatement.bindLong(4, wTUPCP_AppListEntity.getAPP_TIME());
                if (wTUPCP_AppListEntity.getAPP_NAME() == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindString(5, wTUPCP_AppListEntity.getAPP_NAME());
                }
                if (wTUPCP_AppListEntity.getPACKAGE_NAME() == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindString(6, wTUPCP_AppListEntity.getPACKAGE_NAME());
                }
                supportSQLiteStatement.bindLong(7, (long) wTUPCP_AppListEntity.getId());
            }
        };
        this.__preparedStmtOfDeleteAllRows = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM WTUPCP_AppListEntity";
            }
        };
    }

    public long insert(WTUPCP_AppListEntity wTUPCP_AppListEntity) {
        this.__db.beginTransaction();
        try {
            long insertAndReturnId = this.__insertionAdapterOfWTUPCP_AppListEntity.insertAndReturnId(wTUPCP_AppListEntity);
            this.__db.setTransactionSuccessful();
            return insertAndReturnId;
        } finally {
            this.__db.endTransaction();
        }
    }

    public int delete(WTUPCP_AppListEntity wTUPCP_AppListEntity) {
        this.__db.beginTransaction();
        try {
            int handle = this.__deletionAdapterOfWTUPCP_AppListEntity.handle(wTUPCP_AppListEntity) + 0;
            this.__db.setTransactionSuccessful();
            return handle;
        } finally {
            this.__db.endTransaction();
        }
    }

    public int update(WTUPCP_AppListEntity wTUPCP_AppListEntity) {
        this.__db.beginTransaction();
        try {
            int handle = this.__updateAdapterOfWTUPCP_AppListEntity.handle(wTUPCP_AppListEntity) + 0;
            this.__db.setTransactionSuccessful();
            return handle;
        } finally {
            this.__db.endTransaction();
        }
    }

    public void deleteAllRows() {
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteAllRows.acquire();
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteAllRows.release(acquire);
        }
    }

    public List<WTUPCP_AppListEntity> getAllData() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM WTUPCP_AppListEntity", 0);
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow("REPORT_ID");
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow("APP_ICON");
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow("APP_TIME");
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow("APP_NAME");
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow("PACKAGE_NAME");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                WTUPCP_AppListEntity wTUPCP_AppListEntity = new WTUPCP_AppListEntity();
                wTUPCP_AppListEntity.setId(query.getInt(columnIndexOrThrow));
                wTUPCP_AppListEntity.setREPORT_ID(query.getInt(columnIndexOrThrow2));
                wTUPCP_AppListEntity.setAPP_ICON(query.getInt(columnIndexOrThrow3));
                wTUPCP_AppListEntity.setAPP_TIME(query.getLong(columnIndexOrThrow4));
                wTUPCP_AppListEntity.setAPP_NAME(query.getString(columnIndexOrThrow5));
                wTUPCP_AppListEntity.setPACKAGE_NAME(query.getString(columnIndexOrThrow6));
                arrayList.add(wTUPCP_AppListEntity);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public List<WTUPCP_AppListEntity> getAllDataForSingleReport(int i) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM WTUPCP_AppListEntity WHERE REPORT_ID=?", 1);
        acquire.bindLong(1, (long) i);
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow("REPORT_ID");
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow("APP_ICON");
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow("APP_TIME");
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow("APP_NAME");
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow("PACKAGE_NAME");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                WTUPCP_AppListEntity wTUPCP_AppListEntity = new WTUPCP_AppListEntity();
                wTUPCP_AppListEntity.setId(query.getInt(columnIndexOrThrow));
                wTUPCP_AppListEntity.setREPORT_ID(query.getInt(columnIndexOrThrow2));
                wTUPCP_AppListEntity.setAPP_ICON(query.getInt(columnIndexOrThrow3));
                wTUPCP_AppListEntity.setAPP_TIME(query.getLong(columnIndexOrThrow4));
                wTUPCP_AppListEntity.setAPP_NAME(query.getString(columnIndexOrThrow5));
                wTUPCP_AppListEntity.setPACKAGE_NAME(query.getString(columnIndexOrThrow6));
                arrayList.add(wTUPCP_AppListEntity);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
