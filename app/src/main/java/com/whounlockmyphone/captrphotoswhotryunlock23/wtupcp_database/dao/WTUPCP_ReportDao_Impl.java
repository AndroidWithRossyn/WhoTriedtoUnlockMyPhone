package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_ReportEntity;
import java.util.ArrayList;
import java.util.List;

public final class WTUPCP_ReportDao_Impl implements WTUPCP_ReportDao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter __deletionAdapterOfWTUPCP_ReportEntity;
    private final EntityInsertionAdapter __insertionAdapterOfWTUPCP_ReportEntity;
    private final SharedSQLiteStatement __preparedStmtOfDeleteAllRows;
    private final EntityDeletionOrUpdateAdapter __updateAdapterOfWTUPCP_ReportEntity;

    public WTUPCP_ReportDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfWTUPCP_ReportEntity = new EntityInsertionAdapter<WTUPCP_ReportEntity>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR ABORT INTO `WTUPCP_ReportEntity`(`id`,`REPORT_ID`,`REPORT_TIME`,`END_TIME`,`DEVICE_UNLOCK_FAIL`,`PHOTO_PATH`) VALUES (nullif(?, 0),?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, WTUPCP_ReportEntity wTUPCP_ReportEntity) {
                supportSQLiteStatement.bindLong(1, (long) wTUPCP_ReportEntity.id);
                supportSQLiteStatement.bindLong(2, (long) wTUPCP_ReportEntity.getREPORT_ID());
                supportSQLiteStatement.bindLong(3, wTUPCP_ReportEntity.getREPORT_TIME());
                supportSQLiteStatement.bindLong(4, wTUPCP_ReportEntity.getEND_TIME());
                supportSQLiteStatement.bindLong(5, wTUPCP_ReportEntity.isDEVICE_UNLOCK_FAIL() ? 1 : 0);
                if (wTUPCP_ReportEntity.getPHOTO_PATH() == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindString(6, wTUPCP_ReportEntity.getPHOTO_PATH());
                }
            }
        };
        this.__deletionAdapterOfWTUPCP_ReportEntity = new EntityDeletionOrUpdateAdapter<WTUPCP_ReportEntity>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `WTUPCP_ReportEntity` WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, WTUPCP_ReportEntity wTUPCP_ReportEntity) {
                supportSQLiteStatement.bindLong(1, (long) wTUPCP_ReportEntity.id);
            }
        };
        this.__updateAdapterOfWTUPCP_ReportEntity = new EntityDeletionOrUpdateAdapter<WTUPCP_ReportEntity>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `WTUPCP_ReportEntity` SET `id` = ?,`REPORT_ID` = ?,`REPORT_TIME` = ?,`END_TIME` = ?,`DEVICE_UNLOCK_FAIL` = ?,`PHOTO_PATH` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, WTUPCP_ReportEntity wTUPCP_ReportEntity) {
                supportSQLiteStatement.bindLong(1, (long) wTUPCP_ReportEntity.id);
                supportSQLiteStatement.bindLong(2, (long) wTUPCP_ReportEntity.getREPORT_ID());
                supportSQLiteStatement.bindLong(3, wTUPCP_ReportEntity.getREPORT_TIME());
                supportSQLiteStatement.bindLong(4, wTUPCP_ReportEntity.getEND_TIME());
                supportSQLiteStatement.bindLong(5, wTUPCP_ReportEntity.isDEVICE_UNLOCK_FAIL() ? 1 : 0);
                if (wTUPCP_ReportEntity.getPHOTO_PATH() == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindString(6, wTUPCP_ReportEntity.getPHOTO_PATH());
                }
                supportSQLiteStatement.bindLong(7, (long) wTUPCP_ReportEntity.id);
            }
        };
        this.__preparedStmtOfDeleteAllRows = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM WTUPCP_ReportEntity";
            }
        };
    }

    public long insert(WTUPCP_ReportEntity wTUPCP_ReportEntity) {
        this.__db.beginTransaction();
        try {
            long insertAndReturnId = this.__insertionAdapterOfWTUPCP_ReportEntity.insertAndReturnId(wTUPCP_ReportEntity);
            this.__db.setTransactionSuccessful();
            return insertAndReturnId;
        } finally {
            this.__db.endTransaction();
        }
    }

    public int delete(WTUPCP_ReportEntity wTUPCP_ReportEntity) {
        this.__db.beginTransaction();
        try {
            int handle = this.__deletionAdapterOfWTUPCP_ReportEntity.handle(wTUPCP_ReportEntity) + 0;
            this.__db.setTransactionSuccessful();
            return handle;
        } finally {
            this.__db.endTransaction();
        }
    }

    public int update(WTUPCP_ReportEntity wTUPCP_ReportEntity) {
        this.__db.beginTransaction();
        try {
            int handle = this.__updateAdapterOfWTUPCP_ReportEntity.handle(wTUPCP_ReportEntity) + 0;
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

    public List<WTUPCP_ReportEntity> getAllData() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM WTUPCP_ReportEntity", 0);
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("id");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow("REPORT_ID");
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow("REPORT_TIME");
            int columnIndexOrThrow4 = query.getColumnIndexOrThrow("END_TIME");
            int columnIndexOrThrow5 = query.getColumnIndexOrThrow("DEVICE_UNLOCK_FAIL");
            int columnIndexOrThrow6 = query.getColumnIndexOrThrow("PHOTO_PATH");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                WTUPCP_ReportEntity wTUPCP_ReportEntity = new WTUPCP_ReportEntity();
                wTUPCP_ReportEntity.id = query.getInt(columnIndexOrThrow);
                wTUPCP_ReportEntity.setREPORT_ID(query.getInt(columnIndexOrThrow2));
                wTUPCP_ReportEntity.setREPORT_TIME(query.getLong(columnIndexOrThrow3));
                wTUPCP_ReportEntity.setEND_TIME(query.getLong(columnIndexOrThrow4));
                wTUPCP_ReportEntity.setDEVICE_UNLOCK_FAIL(query.getInt(columnIndexOrThrow5) != 0);
                wTUPCP_ReportEntity.setPHOTO_PATH(query.getString(columnIndexOrThrow6));
                arrayList.add(wTUPCP_ReportEntity);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
