package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_manager;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_receivers.WTUPCP_AdminReceiver;

public class WTUPCP_PolicyManager {
    public static final int PM_ACTIVATION_REQUEST_CODE = 101;
    private ComponentName _adminComponent; // = new ComponentName(this._mContext, WTUPCP_AdminReceiver.class);
    private Context _mContext;
    private DevicePolicyManager _mDPM;

    public WTUPCP_PolicyManager(Context context) {
        this._mContext = context;
        this._mDPM = (DevicePolicyManager) context.getSystemService("device_policy");
        _adminComponent = new ComponentName(this._mContext, WTUPCP_AdminReceiver.class);
    }

    public ComponentName getAdminComponent() {
        return this._adminComponent;
    }

    public boolean isAdminActive() {
        return this._mDPM.isAdminActive(this._adminComponent);
    }

    public boolean isLock() {
        return this._mDPM.isAdminActive(this._adminComponent);
    }
}
