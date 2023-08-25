package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.ads.AdsCommon;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_fragments.WTUPCP_ActivateAdminPermissionFragment;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_fragments.WTUPCP_OverlayPermissionFragment;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_fragments.WTUPCP_RuntimePermissionFragment;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_fragments.WTUPCP_SelectFolderPermissionFragment;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_fragments.WTUPCP_UsageAppPermissionFragment;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_interfaces.WTUPCP_PermissionEventListener;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_manager.WTUPCP_PolicyManager;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_PermissionUtils;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_SharePreferenceUtils;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Utils;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_view.WTUPCP_CustomViewPager;
import java.util.ArrayList;
import java.util.List;
import pub.devrel.easypermissions.EasyPermissions;

public class WTUPCP_PermissionActivity extends WTUPCP_BaseActivity implements WTUPCP_PermissionEventListener, EasyPermissions.PermissionCallbacks, View.OnClickListener {
    ViewPagerAdapter adapter;
    FloatingActionButton fbNext;
    boolean firstTime = true;
    public final List<Fragment> mFragmentList = new ArrayList();
    public final List<String> mFragmentTitleList = new ArrayList();
    WTUPCP_PolicyManager policyManager;
    WTUPCP_SharePreferenceUtils sharePreferenceUtils;
    WTUPCP_CustomViewPager viewPager;

    public void onPermissionsDenied(int i, List<String> list) {
    }

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.wtupcp_activity_permission);


        //Reguler Banner Ads
        RelativeLayout admob_banner = (RelativeLayout) findViewById(R.id.Admob_Banner_Frame);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        AdsCommon.RegulerBanner(this, admob_banner, adContainer);



        inIt();
        setUpViewPager(this.viewPager);
    }

    public void onBackPressed() {
        finishAffinity();
    }

    public void setUpViewPager(ViewPager viewPager2) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        this.adapter = viewPagerAdapter;
        viewPagerAdapter.addFragment(new WTUPCP_ActivateAdminPermissionFragment(), "ActivateFragment");
        this.adapter.addFragment(new WTUPCP_UsageAppPermissionFragment(), "UsageAppFragment");
        if (Build.VERSION.SDK_INT >= 23) {
            this.adapter.addFragment(new WTUPCP_RuntimePermissionFragment(), "RuntimePermissionFragment");
            this.adapter.addFragment(new WTUPCP_OverlayPermissionFragment(), "OverlayPermissionFragment");
        }
        this.adapter.addFragment(new WTUPCP_SelectFolderPermissionFragment(), "SelectFolderFragment");
        viewPager2.setAdapter(this.adapter);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }

    public void onPermissionsGranted(int i, List<String> list) {
        if (i == WTUPCP_Constants.REQUEST_CAMERA_PERMISSION) {
            startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            WTUPCP_RuntimePermissionFragment.tvAllowAccess.setText("All permission granted");
        }
    }

    public void onEventCall(int i) {
        if (i == 200) {
            requestMainFolderAccess(getResources().getString(R.string.captured_image), new StorageCallbacks() {
                public void onFolderAccessGranted(DocumentFile documentFile) {
                    if (WTUPCP_SelectFolderPermissionFragment.tvSelectFolder != null) {
                        WTUPCP_SelectFolderPermissionFragment.tvSelectFolder.setText("Folder selected");
                    }
                    WTUPCP_PermissionActivity.this.viewPager.setCurrentItem(WTUPCP_PermissionActivity.this.viewPager.getCurrentItem() + 1);
                }
            });
        }
    }

    
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        for (Fragment onActivityResult : getSupportFragmentManager().getFragments()) {
            onActivityResult.onActivityResult(i, i2, intent);
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.fbNext) {
            if (this.viewPager.getCurrentItem() == 0) {
                if (this.policyManager.isAdminActive()) {
                    this.viewPager.setCurrentItem(1);
                } else {
                    Toast.makeText(this, "Please Activate Administrator Permission", 1).show();
                }
            } else if (this.viewPager.getCurrentItem() == 1) {
                if (WTUPCP_Utils.isUsageAppAccessGranted(this)) {
                    this.viewPager.setCurrentItem(2);
                } else {
                    Toast.makeText(this, "Please Activate Usage App Permission", 1).show();
                }
            } else if (this.viewPager.getCurrentItem() == 2) {
                if (Build.VERSION.SDK_INT > 28) {
                    if (EasyPermissions.hasPermissions(this, WTUPCP_Constants.camera_storage_Permission_new)) {
                        this.viewPager.setCurrentItem(3);
                    } else {
                        Toast.makeText(this, "Please Allow all three Permissions", 1).show();
                    }
                } else if (EasyPermissions.hasPermissions(this, WTUPCP_Constants.camera_storage_Permission)) {
                    this.viewPager.setCurrentItem(3);
                } else {
                    Toast.makeText(this, "Please Allow all three Permissions", 1).show();
                }
            } else if (this.viewPager.getCurrentItem() == 3) {
                if (WTUPCP_PermissionUtils.isSysAlertWndwGranted(this)) {
                    this.viewPager.setCurrentItem(4);
                } else {
                    Toast.makeText(this, "Please Allow Overlay Permissions", 1).show();
                }
            } else if (this.viewPager.getCurrentItem() != 4) {
            } else {
                if (this.sharePreferenceUtils.getString(WTUPCP_Constants.FOLDER_URI, "").length() > 0) {
                    startActivity(new Intent(this, WTUPCP_HomeActivity.class));
                    finish();
                    return;
                }
                Toast.makeText(this, "Please Select Folder", 1).show();
            }
        }
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public Fragment getItem(int i) {
            return WTUPCP_PermissionActivity.this.mFragmentList.get(i);
        }

        public int getCount() {
            return WTUPCP_PermissionActivity.this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String str) {
            WTUPCP_PermissionActivity.this.mFragmentList.add(fragment);
            WTUPCP_PermissionActivity.this.mFragmentTitleList.add(str);
        }

        public CharSequence getPageTitle(int i) {
            return WTUPCP_PermissionActivity.this.mFragmentTitleList.get(i);
        }
    }

    private void inIt() {
        this.viewPager = (WTUPCP_CustomViewPager) findViewById(R.id.viewPager);
        this.fbNext = (FloatingActionButton) findViewById(R.id.fbNext);
        this.sharePreferenceUtils = new WTUPCP_SharePreferenceUtils(this, WTUPCP_Constants.PREFERENCE_NAME);
        this.policyManager = new WTUPCP_PolicyManager(this);
        this.fbNext.setOnClickListener(this);
    }

    
    public void onResume() {
        super.onResume();
        if (this.firstTime) {
            this.firstTime = false;
            if (this.policyManager.isAdminActive()) {
                this.viewPager.setCurrentItem(1);
            } else {
                this.viewPager.setCurrentItem(0);
            }
            this.viewPager.setPagingEnabled(false);
        }
    }
}
