package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_interfaces.WTUPCP_PermissionEventListener;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_manager.WTUPCP_PolicyManager;

public class WTUPCP_ActivateAdminPermissionFragment extends Fragment implements View.OnClickListener {
    WTUPCP_PermissionEventListener permissionEventListener;
    WTUPCP_PolicyManager policyManager;
    TextView tvAllowAccess;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.wtupcp_fragment_activate, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.tvAllowAccess = (TextView) view.findViewById(R.id.tvAllowAccess);
        this.policyManager = new WTUPCP_PolicyManager(getActivity());
        this.tvAllowAccess.setOnClickListener(this);
        if (this.policyManager.isAdminActive()) {
            this.tvAllowAccess.setText("Activated");
        } else {
            this.tvAllowAccess.setText("Allow Access");
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.tvAllowAccess) {
            if (!this.policyManager.isAdminActive()) {
                Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
                intent.putExtra("android.app.extra.DEVICE_ADMIN", this.policyManager.getAdminComponent());
                intent.putExtra("android.app.extra.ADD_EXPLANATION", getActivity().getString(R.string.app_name) + " needs device administrator rights to look out incorrect unlock attempts.");
                startActivityForResult(intent, 101);
                return;
            }
            Toast.makeText(getActivity(), "Administration Activated!", 0).show();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 101 && i2 == -1) {
            Toast.makeText(getActivity(), "Administration Activated!", 0).show();
            this.tvAllowAccess.setText("Activated");
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.permissionEventListener = (WTUPCP_PermissionEventListener) getActivity();
        } catch (Exception unused) {
        }
    }
}
