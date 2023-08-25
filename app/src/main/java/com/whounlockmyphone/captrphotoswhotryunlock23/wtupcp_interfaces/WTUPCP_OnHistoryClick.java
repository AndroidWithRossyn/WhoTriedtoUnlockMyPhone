package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_interfaces;

import android.view.View;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_ReportEntity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Sort;

public interface WTUPCP_OnHistoryClick {
    void OnSortClick(WTUPCP_Sort wTUPCP_Sort);

    void onHistoryClickBack();

    void onMenuClick(View view, WTUPCP_ReportEntity wTUPCP_ReportEntity);
}
