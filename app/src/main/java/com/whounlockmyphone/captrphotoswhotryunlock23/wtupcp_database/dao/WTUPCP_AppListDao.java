package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.dao;

import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_AppListEntity;
import java.util.List;

public interface WTUPCP_AppListDao {
    int delete(WTUPCP_AppListEntity wTUPCP_AppListEntity);

    void deleteAllRows();

    List<WTUPCP_AppListEntity> getAllData();

    List<WTUPCP_AppListEntity> getAllDataForSingleReport(int i);

    long insert(WTUPCP_AppListEntity wTUPCP_AppListEntity);

    int update(WTUPCP_AppListEntity wTUPCP_AppListEntity);
}
