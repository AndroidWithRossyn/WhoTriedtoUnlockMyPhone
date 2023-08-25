package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_interfaces.WTUPCP_PermissionEventListener;

public class WTUPCP_SelectFolderPermissionFragment extends Fragment {
    public static TextView tvSelectFolder;
    WTUPCP_PermissionEventListener permissionEventListener;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.wtupcp_fragment_select_folder, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        TextView textView = (TextView) view.findViewById(R.id.tvSelectFolder);
        tvSelectFolder = textView;
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                WTUPCP_SelectFolderPermissionFragment.this.permissionEventListener.onEventCall(200);
            }
        });
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.permissionEventListener = (WTUPCP_PermissionEventListener) activity;
        } catch (Exception unused) {
        }
    }
}
