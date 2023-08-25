package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_fragments;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;

public class WTUPCP_UsageAppPermissionFragment extends Fragment implements View.OnClickListener {
    Context context;
    TextView tvAllowUsage;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.context = getContext();
        return layoutInflater.inflate(R.layout.wtupcp_fragment_usage_app_permission, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        inIt(view);
        this.tvAllowUsage.setOnClickListener(this);
        if (isAccessGranted(this.context)) {
            this.tvAllowUsage.setText("Granted");
        } else {
            this.tvAllowUsage.setText("Allow Permission");
        }
    }

    private void inIt(View view) {
        this.tvAllowUsage = (TextView) view.findViewById(R.id.tvAllowUsage);
    }

    private boolean isAccessGranted(Context context2) {
        try {
            ApplicationInfo applicationInfo = context2.getPackageManager().getApplicationInfo(context2.getPackageName(), 0);
            if ((Build.VERSION.SDK_INT > 19 ? ((AppOpsManager) context2.getSystemService("appops")).checkOpNoThrow("android:get_usage_stats", applicationInfo.uid, applicationInfo.packageName) : 0) == 0) {
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.tvAllowUsage) {
            if (isAccessGranted(this.context)) {
                this.tvAllowUsage.setText("Granted");
                Toast.makeText(this.context, "Usage App Permission Activated!", 0).show();
                return;
            }
            startActivityForResult(new Intent("android.settings.USAGE_ACCESS_SETTINGS"), WTUPCP_Constants.REQUEST_USAGE_APP_PERMISSION);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != WTUPCP_Constants.REQUEST_USAGE_APP_PERMISSION) {
            return;
        }
        if (isAccessGranted(this.context)) {
            this.tvAllowUsage.setText("Granted");
        } else {
            this.tvAllowUsage.setText("Allow Permission");
        }
    }
}
