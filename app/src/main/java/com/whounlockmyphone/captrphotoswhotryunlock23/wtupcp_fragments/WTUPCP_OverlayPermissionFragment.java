package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_PermissionUtils;

public class WTUPCP_OverlayPermissionFragment extends Fragment {
    
    public int SYS_ALERT_WNDW_REQUEST = 2;
    Context context;
    TextView tvAllowAccess;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.context = getContext();
        return layoutInflater.inflate(R.layout.wtupcp_fragment_overlay_permission, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.tvAllowAccess = (TextView) view.findViewById(R.id.tvAllowAccess);
        if (WTUPCP_PermissionUtils.isSysAlertWndwGranted(this.context)) {
            this.tvAllowAccess.setText("Granted");
        } else {
            this.tvAllowAccess.setText("Allow Access");
        }
        this.tvAllowAccess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (WTUPCP_PermissionUtils.isSysAlertWndwGranted(WTUPCP_OverlayPermissionFragment.this.context)) {
                    Toast.makeText(WTUPCP_OverlayPermissionFragment.this.context, "Overlay Permission Granted!", 0).show();
                    return;
                }
                try {
                    Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + WTUPCP_OverlayPermissionFragment.this.context.getPackageName()));
                    WTUPCP_OverlayPermissionFragment wTUPCP_OverlayPermissionFragment = WTUPCP_OverlayPermissionFragment.this;
                    wTUPCP_OverlayPermissionFragment.startActivityForResult(intent, wTUPCP_OverlayPermissionFragment.SYS_ALERT_WNDW_REQUEST);
                } catch (ActivityNotFoundException unused) {
                    WTUPCP_OverlayPermissionFragment.this.startActivityForResult(new Intent("android.settings.SETTINGS"), WTUPCP_OverlayPermissionFragment.this.SYS_ALERT_WNDW_REQUEST);
                }
            }
        });
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Log.e("ACTIVITY_RESULT", "Request Code :" + i);
        Log.e("ACTIVITY_RESULT", "Result Code :" + i2);
        if (i != this.SYS_ALERT_WNDW_REQUEST) {
            return;
        }
        if (WTUPCP_PermissionUtils.isSysAlertWndwGranted(this.context)) {
            this.tvAllowAccess.setText("Granted");
        } else {
            this.tvAllowAccess.setText("Allow Access");
        }
    }
}
