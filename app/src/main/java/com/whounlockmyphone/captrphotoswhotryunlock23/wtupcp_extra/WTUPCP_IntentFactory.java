package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_extra;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_services.WTUPCP_CameraService;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_services.WTUPCP_MainService;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_services.WTUPCP_SaveReportService;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;

public class WTUPCP_IntentFactory extends ContextWrapper {
    public WTUPCP_IntentFactory(Context context) {
        super(context);
    }

    public Intent getMonitorServiceIntent(boolean z) {
        Intent intent = new Intent(this, WTUPCP_MainService.class);
        intent.putExtra(WTUPCP_Constants.OFF_SERVICE_EXTRA, z);
        return intent;
    }

    public Intent getSaveRepServiceIntent(boolean z, boolean z2) {
        Intent intent = new Intent(this, WTUPCP_SaveReportService.class);
        intent.putExtra(WTUPCP_Constants.SAVE_REPORT_EXTRA, true);
        intent.putExtra(WTUPCP_Constants.WRONG_PATTERN_EXTRA, z);
        intent.putExtra(WTUPCP_Constants.DEVICE_UNLOCKED_EXTRA, z2);
        return intent;
    }

    public Intent getCameraServiceIntent(boolean z) {
        Intent intent = new Intent(this, WTUPCP_CameraService.class);
        intent.putExtra(WTUPCP_Constants.WRONG_PATTERN_EXTRA, z);
        return intent;
    }

    public Intent getUpdateUiIntent(boolean z) {
        Intent intent = new Intent(WTUPCP_Constants.BROADCAST_UPDATE_UI);
        intent.putExtra(WTUPCP_Constants.ANIM_FIRST_ROW_EXTRA, z);
        return intent;
    }
}
