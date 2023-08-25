package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import pub.devrel.easypermissions.EasyPermissions;

public class WTUPCP_RuntimePermissionFragment extends Fragment {
    public static TextView tvAllowAccess;
    Context context;
    LinearLayout llStorage;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.context = getContext();
        return layoutInflater.inflate(R.layout.wtupcp_fragment_runtime_permission, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        tvAllowAccess = (TextView) view.findViewById(R.id.tvAllowAccess);
        this.llStorage = (LinearLayout) view.findViewById(R.id.llStorage);
        if (Build.VERSION.SDK_INT > 28) {
            this.llStorage.setVisibility(8);
            if (EasyPermissions.hasPermissions(this.context, WTUPCP_Constants.camera_storage_Permission_new)) {
                tvAllowAccess.setText("All permission granted");
            } else {
                tvAllowAccess.setText("Allow Access");
            }
        } else {
            this.llStorage.setVisibility(0);
            if (EasyPermissions.hasPermissions(this.context, WTUPCP_Constants.camera_storage_Permission)) {
                tvAllowAccess.setText("All permission granted");
            } else {
                tvAllowAccess.setText("Allow Access");
            }
        }
        tvAllowAccess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > 28) {
                    if (EasyPermissions.hasPermissions(WTUPCP_RuntimePermissionFragment.this.context, WTUPCP_Constants.camera_storage_Permission_new)) {
                        Toast.makeText(WTUPCP_RuntimePermissionFragment.this.context, "All permission granted", 0).show();
                    } else {
                        EasyPermissions.requestPermissions((Activity) WTUPCP_RuntimePermissionFragment.this.getActivity(), "", WTUPCP_Constants.REQUEST_CAMERA_PERMISSION, WTUPCP_Constants.camera_storage_Permission_new);
                    }
                } else if (EasyPermissions.hasPermissions(WTUPCP_RuntimePermissionFragment.this.context, WTUPCP_Constants.camera_storage_Permission)) {
                    Toast.makeText(WTUPCP_RuntimePermissionFragment.this.context, "All permission granted", 0).show();
                } else {
                    EasyPermissions.requestPermissions((Activity) WTUPCP_RuntimePermissionFragment.this.getActivity(), "", WTUPCP_Constants.REQUEST_CAMERA_PERMISSION, WTUPCP_Constants.camera_storage_Permission);
                }
            }
        });
    }
}
