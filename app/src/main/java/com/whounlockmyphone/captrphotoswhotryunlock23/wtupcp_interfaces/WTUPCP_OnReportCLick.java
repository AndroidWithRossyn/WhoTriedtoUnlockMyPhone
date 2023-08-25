package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_interfaces;

import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_ReportEntity;

public interface WTUPCP_OnReportCLick {
    void onClick(WTUPCP_ReportEntity wTUPCP_ReportEntity);

    void onDeleteClick(WTUPCP_ReportEntity wTUPCP_ReportEntity);
}
