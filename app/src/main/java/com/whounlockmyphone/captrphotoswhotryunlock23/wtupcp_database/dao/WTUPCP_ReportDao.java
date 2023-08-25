package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.dao;

import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_ReportEntity;
import java.util.List;

public interface WTUPCP_ReportDao {
    int delete(WTUPCP_ReportEntity wTUPCP_ReportEntity);

    void deleteAllRows();

    List<WTUPCP_ReportEntity> getAllData();

    long insert(WTUPCP_ReportEntity wTUPCP_ReportEntity);

    int update(WTUPCP_ReportEntity wTUPCP_ReportEntity);
}
