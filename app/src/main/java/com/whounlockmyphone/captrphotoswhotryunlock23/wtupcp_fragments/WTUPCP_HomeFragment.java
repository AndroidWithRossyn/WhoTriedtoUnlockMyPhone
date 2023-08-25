package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_fragments;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.florent37.shapeofview.shapes.RoundRectView;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.ads.AdsCommon;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_activity.WTUPCP_ViewIntruderActivity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_adapters.WTUPCP_RecentActivityAdapter;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.db.WTUPCP_DatabaseClient;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_ReportEntity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_interfaces.WTUPCP_OnReportCLick;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_manager.WTUPCP_PolicyManager;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_PermissionUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pub.devrel.easypermissions.EasyPermissions;

public class WTUPCP_HomeFragment extends Fragment implements View.OnClickListener {
    public static WTUPCP_HomeFragment homeFragment;
    Activity activity;
    Context context;
    ImageView ivServiceOnOff;
    WTUPCP_OnReportCLick onReportCLick = new WTUPCP_OnReportCLick() {
        public void onDeleteClick(WTUPCP_ReportEntity wTUPCP_ReportEntity) {
        }

        public void onClick(WTUPCP_ReportEntity wTUPCP_ReportEntity) {
            Intent intent = new Intent(WTUPCP_HomeFragment.this.context, WTUPCP_ViewIntruderActivity.class);
            intent.putExtra(WTUPCP_Constants.REPORT_ENTITY, wTUPCP_ReportEntity);
            //WTUPCP_HomeFragment.this.startActivity(intent);
            AdsCommon.InterstitialAd(context, intent);
        }
    };
    WTUPCP_PolicyManager policyManager;
    WTUPCP_RecentActivityAdapter reportAdapter;
    List<WTUPCP_ReportEntity> reportEntityList = new ArrayList();
    RoundRectView rrSeeAll;
    RecyclerView rvReports;
    onSomeEventListener someEventListener;
    TextView tvNoRecentActivity;
    TextView tvServiceStatus;
    private BroadcastReceiver updateUiBroadcast;
    View view1;
    View view2;

    public interface onSomeEventListener {
        void someEvent(int i);
    }

    public static WTUPCP_HomeFragment getInstance() {
        if (homeFragment == null) {
            homeFragment = new WTUPCP_HomeFragment();
        }
        return homeFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.context = getContext();
        this.activity = getActivity();
        return layoutInflater.inflate(R.layout.wtupcp_fragment_home, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        inIt(view);
        clickListener();
        adapterSetup();
        new getData().execute(new Void[0]);
        if (Build.VERSION.SDK_INT > 28) {
            if (EasyPermissions.hasPermissions(this.context, WTUPCP_Constants.camera_storage_Permission_new) && isAccessGranted() && this.policyManager.isAdminActive() && WTUPCP_PermissionUtils.isSysAlertWndwGranted(this.context)) {
                this.someEventListener.someEvent(2);
            }
        } else if (EasyPermissions.hasPermissions(this.context, WTUPCP_Constants.camera_storage_Permission) && isAccessGranted() && this.policyManager.isAdminActive() && WTUPCP_PermissionUtils.isSysAlertWndwGranted(this.context)) {
            this.someEventListener.someEvent(2);
        }
    }

    private boolean isAccessGranted() {
        try {
            ApplicationInfo applicationInfo = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 0);
            if ((Build.VERSION.SDK_INT > 19 ? ((AppOpsManager) this.context.getSystemService("appops")).checkOpNoThrow("android:get_usage_stats", applicationInfo.uid, applicationInfo.packageName) : 0) == 0) {
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private class getData extends AsyncTask<Void, Void, Void> {
        private getData() {
        }

        
        public Void doInBackground(Void... voidArr) {
            List<WTUPCP_ReportEntity> allData = WTUPCP_DatabaseClient.getInstance(WTUPCP_HomeFragment.this.context).getAppDatabase().reportDao().getAllData();
            WTUPCP_HomeFragment.this.reportEntityList.clear();
            WTUPCP_HomeFragment.this.reportEntityList.addAll(allData);
            Collections.reverse(WTUPCP_HomeFragment.this.reportEntityList);
            return null;
        }

        
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (WTUPCP_HomeFragment.this.reportEntityList.size() > 0) {
                WTUPCP_HomeFragment.this.tvNoRecentActivity.setVisibility(8);
                WTUPCP_HomeFragment.this.rvReports.setVisibility(0);
            } else {
                WTUPCP_HomeFragment.this.tvNoRecentActivity.setVisibility(0);
                WTUPCP_HomeFragment.this.rvReports.setVisibility(8);
            }
            WTUPCP_HomeFragment.this.reportAdapter.notifyDataSetChanged();
        }
    }

    private void adapterSetup() {
        this.reportAdapter = new WTUPCP_RecentActivityAdapter(this.context, this.reportEntityList, this.onReportCLick);
        this.rvReports.setLayoutManager(new LinearLayoutManager(this.context, 0, false));
        this.rvReports.setAdapter(this.reportAdapter);
    }

    private void registerUIBroadCastReceiver() {
        BroadcastReceiver r0 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                new getData().execute(new Void[0]);
            }
        };
        this.updateUiBroadcast = r0;
        this.context.registerReceiver(r0, new IntentFilter(WTUPCP_Constants.BROADCAST_UPDATE_UI));
    }

    private void clickListener() {
        this.ivServiceOnOff.setOnClickListener(this);
        this.rrSeeAll.setOnClickListener(this);
    }

    private void inIt(View view) {
        this.view1 = view.findViewById(R.id.view1);
        this.view2 = view.findViewById(R.id.view2);
        this.ivServiceOnOff = (ImageView) view.findViewById(R.id.ivServiceOnOff);
        this.rvReports = (RecyclerView) view.findViewById(R.id.rvReports);
        this.rrSeeAll = (RoundRectView) view.findViewById(R.id.rrSeeAll);
        this.tvNoRecentActivity = (TextView) view.findViewById(R.id.tvNoRecentActivity);
        this.tvServiceStatus = (TextView) view.findViewById(R.id.tvServiceStatus);
        this.policyManager = new WTUPCP_PolicyManager(this.context);
        registerUIBroadCastReceiver();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ivServiceOnOff) {
            this.someEventListener.someEvent(0);
        } else if (id == R.id.rrSeeAll) {
            this.someEventListener.someEvent(1);
        }
    }

    public void onAttach(Activity activity2) {
        super.onAttach(activity2);
        try {
            this.someEventListener = (onSomeEventListener) activity2;
        } catch (ClassCastException unused) {
            throw new ClassCastException(activity2.toString() + " must implement onSomeEventListener");
        }
    }

    public void setOnOffSwitch(boolean z) {
        if (z) {
            this.view1.setVisibility(8);
            this.view2.setVisibility(0);
            this.ivServiceOnOff.setImageResource(R.drawable.ic_switch_on);
            this.tvServiceStatus.setText("ACTIVATED");
            return;
        }
        this.view1.setVisibility(0);
        this.view2.setVisibility(8);
        this.ivServiceOnOff.setImageResource(R.drawable.ic_switch_off);
        this.tvServiceStatus.setText("DEACTIVATED");
    }
}
