package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_activity;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
//import com.google.android.gms.drive.DriveFile;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.ads.AdsCommon;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_ReportEntity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_extra.WTUPCP_IntentFactory;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_fragments.WTUPCP_HistoryFragment;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_fragments.WTUPCP_HomeFragment;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_fragments.WTUPCP_SettingsFragment;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_interfaces.WTUPCP_OnHistoryClick;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_manager.WTUPCP_PolicyManager;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_services.WTUPCP_MainService;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_PermissionUtils;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_RateAppDialog;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_SharePreferenceUtils;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Sort;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Utils;
import java.util.List;
import pub.devrel.easypermissions.EasyPermissions;

public class WTUPCP_HomeActivity extends WTUPCP_BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks, WTUPCP_HomeFragment.onSomeEventListener, WTUPCP_SettingsFragment.OnSettingsClick, WTUPCP_OnHistoryClick {
    public static MutableLiveData<Integer> mutableLiveData = new MutableLiveData<>();
    private int SYS_ALERT_WNDW_REQUEST = 2;
    BottomSheetBehavior bottomSheetBehavior;
    BottomSheetBehavior bottomSheetBehaviorUnlockAttempt;
    int currentFragmentPosition = 0;
    FrameLayout flConstainer;
    ImageView ivHistory;
    ImageView ivHistoryDot;
    ImageView ivHome;
    ImageView ivHomeDot;
    ImageView ivSettings;
    ImageView ivSettingsDot;
    LinearLayout ll1FailAttempt;
    LinearLayout ll2FailAttempt;
    LinearLayout ll3FailAttempt;
    LinearLayout ll4FailAttempt;
    LinearLayout llBottomOption;
    LinearLayout llSortAll;
    LinearLayout llSortBottomsheet;
    LinearLayout llSortFail;
    LinearLayout llSortRecent;
    LinearLayout llSortSuccess;
    LinearLayout llUnlockAttempsBottomsheet;
    View mView;
    WTUPCP_PolicyManager policyManager;
    RadioButton rb1FailAttempt;
    RadioButton rb2FailAttempt;
    RadioButton rb3FailAttempt;
    RadioButton rb4FailAttempt;
    RadioButton rbSortAll;
    RadioButton rbSortFail;
    RadioButton rbSortRecent;
    RadioButton rbSortSuccess;
    RelativeLayout rlHistory;
    RelativeLayout rlHome;
    RelativeLayout rlSettings;
    
    public WTUPCP_SharePreferenceUtils sharedPreferencesUtils;
    Animation slide_down;
    Animation slide_up;

    public void onMenuClick(View view, WTUPCP_ReportEntity wTUPCP_ReportEntity) {
    }

    public void onPermissionsDenied(int i, List<String> list) {
    }

    public boolean shouldShowRequestPermissionRationale(String str) {
        return false;
    }

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.wtupcp_activity_home);

        //Reguler Banner Ads
        RelativeLayout admob_banner = (RelativeLayout) findViewById(R.id.Admob_Banner_Frame);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        AdsCommon.RegulerBanner(this, admob_banner, adContainer);


        inIt();
        clickListener();
        getSupportFragmentManager().beginTransaction().replace(R.id.flConstainer, WTUPCP_HomeFragment.getInstance()).commit();
        bottomsheetCallBack();
        mutableLiveData.observe(this, new Observer<Integer>() {
            public void onChanged(Integer num) {
                if (num.intValue() == 0 && WTUPCP_HomeActivity.this.llBottomOption.getVisibility() == 0) {
                    WTUPCP_HomeActivity.this.llBottomOption.setVisibility(8);
                    WTUPCP_HomeActivity.this.llBottomOption.startAnimation(WTUPCP_HomeActivity.this.slide_down);
                } else if (num.intValue() == 1 && WTUPCP_HomeActivity.this.llBottomOption.getVisibility() != 0) {
                    WTUPCP_HomeActivity.this.llBottomOption.setVisibility(0);
                    WTUPCP_HomeActivity.this.llBottomOption.startAnimation(WTUPCP_HomeActivity.this.slide_up);
                }
            }
        });
    }

    private void bottomsheetCallBack() {
        this.bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            public void onSlide(View view, float f) {
            }

            public void onStateChanged(View view, int i) {
                if (WTUPCP_HomeActivity.this.bottomSheetBehavior.getState() == 3) {
                    WTUPCP_HomeActivity.this.mView.setVisibility(0);
                } else {
                    WTUPCP_HomeActivity.this.mView.setVisibility(8);
                }
            }
        });
        this.bottomSheetBehaviorUnlockAttempt.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            public void onSlide(View view, float f) {
            }

            public void onStateChanged(View view, int i) {
                if (WTUPCP_HomeActivity.this.bottomSheetBehaviorUnlockAttempt.getState() == 3) {
                    WTUPCP_HomeActivity.this.mView.setVisibility(0);
                } else {
                    WTUPCP_HomeActivity.this.mView.setVisibility(8);
                }
            }
        });
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }

    
    public void checkUsagesPermission() {
        if (isAccessGranted()) {
            checkAdminPermission();
        } else {
            startActivityForResult(new Intent("android.settings.USAGE_ACCESS_SETTINGS"), WTUPCP_Constants.REQUEST_USAGE_APP_PERMISSION);
        }
    }

    
    public boolean isAccessGranted() {
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), 0);
            if ((Build.VERSION.SDK_INT > 19 ? ((AppOpsManager) getSystemService("appops")).checkOpNoThrow("android:get_usage_stats", applicationInfo.uid, applicationInfo.packageName) : 0) == 0) {
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != -1) {
            return;
        }
        if (i == WTUPCP_Constants.REQUEST_USAGE_APP_PERMISSION) {
            checkUsagesPermission();
        } else if (i == 101) {
            checkAdminPermission();
        }
    }

    private void checkAdminPermission() {
        if (this.policyManager.isAdminActive()) {
            checkOverlayPermission();
            return;
        }
        Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
        intent.putExtra("android.app.extra.DEVICE_ADMIN", this.policyManager.getAdminComponent());
        intent.putExtra("android.app.extra.ADD_EXPLANATION", getResources().getString(R.string.app_name) + " needs device administrator rights to look out incorrect unlock attempts.");
        startActivityForResult(intent, 101);
    }

    private void checkOverlayPermission() {
        if (!WTUPCP_PermissionUtils.isSysAlertWndwGranted(this)) {
            try {
                Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName()));
                intent.setFlags(268435456);
                startActivityForResult(intent, this.SYS_ALERT_WNDW_REQUEST);
            } catch (ActivityNotFoundException unused) {
                startActivityForResult(new Intent("android.settings.SETTINGS"), this.SYS_ALERT_WNDW_REQUEST);
            }
        } else if (WTUPCP_MainService.isServiceRunning(this)) {
            ContextCompat.startForegroundService(this, new WTUPCP_IntentFactory(getApplicationContext()).getMonitorServiceIntent(true));
            WTUPCP_HomeFragment.getInstance().setOnOffSwitch(false);
        } else {
            ContextCompat.startForegroundService(this, new WTUPCP_IntentFactory(getApplicationContext()).getMonitorServiceIntent(false));
            WTUPCP_HomeFragment.getInstance().setOnOffSwitch(true);
        }
    }

    public void onPermissionsGranted(int i, List<String> list) {
        if (i == WTUPCP_Constants.REQUEST_CAMERA_PERMISSION) {
            startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        }
    }

    private void clickListener() {
        this.rlHome.setOnClickListener(this);
        this.rlHistory.setOnClickListener(this);
        this.rlSettings.setOnClickListener(this);
        this.llSortAll.setOnClickListener(this);
        this.llSortRecent.setOnClickListener(this);
        this.llSortFail.setOnClickListener(this);
        this.llSortSuccess.setOnClickListener(this);
        this.ll1FailAttempt.setOnClickListener(this);
        this.ll2FailAttempt.setOnClickListener(this);
        this.ll3FailAttempt.setOnClickListener(this);
        this.ll4FailAttempt.setOnClickListener(this);
    }

    private void inIt() {
        this.slide_up = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        this.slide_down = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        this.rlHome = (RelativeLayout) findViewById(R.id.rlHome);
        this.rlHistory = (RelativeLayout) findViewById(R.id.rlHistory);
        this.rlSettings = (RelativeLayout) findViewById(R.id.rlSettings);
        this.ivHome = (ImageView) findViewById(R.id.ivHome);
        this.ivHistory = (ImageView) findViewById(R.id.ivHistory);
        this.ivSettings = (ImageView) findViewById(R.id.ivSettings);
        this.ivHomeDot = (ImageView) findViewById(R.id.ivHomeDot);
        this.ivHistoryDot = (ImageView) findViewById(R.id.ivHistoryDot);
        this.ivSettingsDot = (ImageView) findViewById(R.id.ivSettingsDot);
        this.flConstainer = (FrameLayout) findViewById(R.id.flConstainer);
        this.policyManager = new WTUPCP_PolicyManager(this);
        this.sharedPreferencesUtils = new WTUPCP_SharePreferenceUtils(this, WTUPCP_Constants.PREFERENCE_NAME);
        this.llSortBottomsheet = (LinearLayout) findViewById(R.id.llSortBottomsheet);
        this.mView = findViewById(R.id.view);
        this.bottomSheetBehavior = BottomSheetBehavior.from(this.llSortBottomsheet);
        this.rbSortAll = (RadioButton) findViewById(R.id.rbSortAll);
        this.rbSortRecent = (RadioButton) findViewById(R.id.rbSortRecent);
        this.rbSortFail = (RadioButton) findViewById(R.id.rbSortFail);
        this.rbSortSuccess = (RadioButton) findViewById(R.id.rbSortSuccess);
        this.llSortAll = (LinearLayout) findViewById(R.id.llSortAll);
        this.llSortRecent = (LinearLayout) findViewById(R.id.llSortRecent);
        this.llSortFail = (LinearLayout) findViewById(R.id.llSortFail);
        this.llSortSuccess = (LinearLayout) findViewById(R.id.llSortSuccess);
        this.llBottomOption = (LinearLayout) findViewById(R.id.llBottomOption);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llUnlockAttempsBottomsheet);
        this.llUnlockAttempsBottomsheet = linearLayout;
        this.bottomSheetBehaviorUnlockAttempt = BottomSheetBehavior.from(linearLayout);
        this.ll1FailAttempt = (LinearLayout) findViewById(R.id.ll1FailAttempt);
        this.ll2FailAttempt = (LinearLayout) findViewById(R.id.ll2FailAttempt);
        this.ll3FailAttempt = (LinearLayout) findViewById(R.id.ll3FailAttempt);
        this.ll4FailAttempt = (LinearLayout) findViewById(R.id.ll4FailAttempt);
        this.rb1FailAttempt = (RadioButton) findViewById(R.id.rb1FailAttempt);
        this.rb2FailAttempt = (RadioButton) findViewById(R.id.rb2FailAttempt);
        this.rb3FailAttempt = (RadioButton) findViewById(R.id.rb3FailAttempt);
        this.rb4FailAttempt = (RadioButton) findViewById(R.id.rb4FailAttempt);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.llSortAll) {
            switch (id) {
                case R.id.ll1FailAttempt:
                    WTUPCP_SettingsFragment.getInstance().setFailAttempt(1);
                    this.bottomSheetBehaviorUnlockAttempt.setState(4);
                    return;
                case R.id.ll2FailAttempt:
                    WTUPCP_SettingsFragment.getInstance().setFailAttempt(2);
                    this.bottomSheetBehaviorUnlockAttempt.setState(4);
                    return;
                case R.id.ll3FailAttempt:
                    WTUPCP_SettingsFragment.getInstance().setFailAttempt(3);
                    this.bottomSheetBehaviorUnlockAttempt.setState(4);
                    return;
                case R.id.ll4FailAttempt:
                    WTUPCP_SettingsFragment.getInstance().setFailAttempt(4);
                    this.bottomSheetBehaviorUnlockAttempt.setState(4);
                    return;
                default:
                    switch (id) {
                        case R.id.llSortFail:
                            WTUPCP_HistoryFragment.getInstance().setSortOption(WTUPCP_Sort.Fail);
                            this.bottomSheetBehavior.setState(4);
                            return;
                        case R.id.llSortRecent:
                            WTUPCP_HistoryFragment.getInstance().setSortOption(WTUPCP_Sort.Recent);
                            this.bottomSheetBehavior.setState(4);
                            return;
                        case R.id.llSortSuccess:
                            WTUPCP_HistoryFragment.getInstance().setSortOption(WTUPCP_Sort.Success);
                            this.bottomSheetBehavior.setState(4);
                            return;
                        default:
                            switch (id) {
                                case R.id.rlHistory:
                                    if (this.llBottomOption.getVisibility() != 0) {
                                        this.llBottomOption.setVisibility(0);
                                        this.llBottomOption.startAnimation(this.slide_up);
                                    }
                                    this.ivHome.setImageResource(R.drawable.ic_home);
                                    this.ivHistory.setImageResource(R.drawable.ic_user_fill);
                                    this.ivSettings.setImageResource(R.drawable.ic_settings);
                                    this.ivHomeDot.setVisibility(8);
                                    this.ivHistoryDot.setVisibility(0);
                                    this.ivSettingsDot.setVisibility(8);
                                    this.currentFragmentPosition = 1;
                                    WTUPCP_HistoryFragment.historyFragment = new WTUPCP_HistoryFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.flConstainer, WTUPCP_HistoryFragment.historyFragment).commit();
                                    CallAds();
                                    return;
                                case R.id.rlHome:
                                    if (this.llBottomOption.getVisibility() != 0) {
                                        this.llBottomOption.setVisibility(0);
                                        this.llBottomOption.startAnimation(this.slide_up);
                                    }
                                    this.ivHome.setImageResource(R.drawable.ic_home_fill);
                                    this.ivHistory.setImageResource(R.drawable.ic_user);
                                    this.ivSettings.setImageResource(R.drawable.ic_settings);
                                    this.ivHomeDot.setVisibility(0);
                                    this.ivHistoryDot.setVisibility(8);
                                    this.ivSettingsDot.setVisibility(8);
                                    this.currentFragmentPosition = 0;
                                    getSupportFragmentManager().beginTransaction().replace(R.id.flConstainer, WTUPCP_HomeFragment.getInstance()).commit();
                                    CallAds();
                                    return;
                                case R.id.rlSettings:
                                    if (this.llBottomOption.getVisibility() != 0) {
                                        this.llBottomOption.setVisibility(0);
                                        this.llBottomOption.startAnimation(this.slide_up);
                                    }
                                    this.ivHome.setImageResource(R.drawable.ic_home);
                                    this.ivHistory.setImageResource(R.drawable.ic_user);
                                    this.ivSettings.setImageResource(R.drawable.ic_settings_fill);
                                    this.ivHomeDot.setVisibility(8);
                                    this.ivHistoryDot.setVisibility(8);
                                    this.ivSettingsDot.setVisibility(0);
                                    this.currentFragmentPosition = 2;
                                    WTUPCP_SettingsFragment.settingsFragment = new WTUPCP_SettingsFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.flConstainer, WTUPCP_SettingsFragment.settingsFragment).commit();
                                    CallAds();
                                    return;
                                default:
                                    return;
                            }
                    }
            }
        } else {
            WTUPCP_HistoryFragment.getInstance().setSortOption(WTUPCP_Sort.All);
            this.bottomSheetBehavior.setState(4);
        }
    }

    private void CallAds() {
        AdsCommon.InterstitialAdsOnly(WTUPCP_HomeActivity.this);
    }

    public void someEvent(int i) {
        if (i == 0) {
            requestMainFolderAccess(getResources().getString(R.string.captured_image), new StorageCallbacks() {
                public void onFolderAccessGranted(DocumentFile documentFile) {
                    if (Build.VERSION.SDK_INT > 28) {
                        if (!EasyPermissions.hasPermissions(WTUPCP_HomeActivity.this, WTUPCP_Constants.camera_storage_Permission_new) || !WTUPCP_HomeActivity.this.isAccessGranted() || !WTUPCP_HomeActivity.this.policyManager.isAdminActive() || !WTUPCP_PermissionUtils.isSysAlertWndwGranted(WTUPCP_HomeActivity.this)) {
                            if (Build.VERSION.SDK_INT > 28) {
                                if (EasyPermissions.hasPermissions(WTUPCP_HomeActivity.this, WTUPCP_Constants.camera_storage_Permission_new)) {
                                    WTUPCP_HomeActivity.this.checkUsagesPermission();
                                } else {
                                    EasyPermissions.requestPermissions((Activity) WTUPCP_HomeActivity.this, "", WTUPCP_Constants.REQUEST_CAMERA_PERMISSION, WTUPCP_Constants.camera_storage_Permission_new);
                                }
                            } else if (EasyPermissions.hasPermissions(WTUPCP_HomeActivity.this, WTUPCP_Constants.camera_storage_Permission)) {
                                WTUPCP_HomeActivity.this.checkUsagesPermission();
                            } else {
                                EasyPermissions.requestPermissions((Activity) WTUPCP_HomeActivity.this, "", WTUPCP_Constants.REQUEST_CAMERA_PERMISSION, WTUPCP_Constants.camera_storage_Permission);
                            }
                        } else if (WTUPCP_HomeActivity.this.sharedPreferencesUtils.getBoolean(WTUPCP_Constants.SERVICE_ON, Boolean.valueOf(WTUPCP_Constants.SERVICE_ON_DEFAULT))) {
                            if (WTUPCP_MainService.isServiceRunning(WTUPCP_HomeActivity.this)) {
                                ContextCompat.startForegroundService(WTUPCP_HomeActivity.this, new WTUPCP_IntentFactory(WTUPCP_HomeActivity.this.getApplicationContext()).getMonitorServiceIntent(true));
                                WTUPCP_HomeFragment.getInstance().setOnOffSwitch(false);
                                WTUPCP_HomeActivity.this.sharedPreferencesUtils.putBoolean(WTUPCP_Constants.SERVICE_ON, false);
                                return;
                            }
                            WTUPCP_HomeFragment.getInstance().setOnOffSwitch(false);
                            WTUPCP_HomeActivity.this.sharedPreferencesUtils.putBoolean(WTUPCP_Constants.SERVICE_ON, false);
                        } else if (!WTUPCP_MainService.isServiceRunning(WTUPCP_HomeActivity.this)) {
                            ContextCompat.startForegroundService(WTUPCP_HomeActivity.this, new WTUPCP_IntentFactory(WTUPCP_HomeActivity.this.getApplicationContext()).getMonitorServiceIntent(false));
                            WTUPCP_HomeFragment.getInstance().setOnOffSwitch(true);
                            WTUPCP_HomeActivity.this.sharedPreferencesUtils.putBoolean(WTUPCP_Constants.SERVICE_ON, true);
                        } else {
                            WTUPCP_HomeFragment.getInstance().setOnOffSwitch(true);
                            WTUPCP_HomeActivity.this.sharedPreferencesUtils.putBoolean(WTUPCP_Constants.SERVICE_ON, true);
                        }
                    } else if (!EasyPermissions.hasPermissions(WTUPCP_HomeActivity.this, WTUPCP_Constants.camera_storage_Permission) || !WTUPCP_HomeActivity.this.isAccessGranted() || !WTUPCP_HomeActivity.this.policyManager.isAdminActive() || !WTUPCP_PermissionUtils.isSysAlertWndwGranted(WTUPCP_HomeActivity.this)) {
                        if (Build.VERSION.SDK_INT > 28) {
                            if (EasyPermissions.hasPermissions(WTUPCP_HomeActivity.this, WTUPCP_Constants.camera_storage_Permission_new)) {
                                WTUPCP_HomeActivity.this.checkUsagesPermission();
                            } else {
                                EasyPermissions.requestPermissions((Activity) WTUPCP_HomeActivity.this, "", WTUPCP_Constants.REQUEST_CAMERA_PERMISSION, WTUPCP_Constants.camera_storage_Permission_new);
                            }
                        } else if (EasyPermissions.hasPermissions(WTUPCP_HomeActivity.this, WTUPCP_Constants.camera_storage_Permission)) {
                            WTUPCP_HomeActivity.this.checkUsagesPermission();
                        } else {
                            EasyPermissions.requestPermissions((Activity) WTUPCP_HomeActivity.this, "", WTUPCP_Constants.REQUEST_CAMERA_PERMISSION, WTUPCP_Constants.camera_storage_Permission);
                        }
                    } else if (WTUPCP_HomeActivity.this.sharedPreferencesUtils.getBoolean(WTUPCP_Constants.SERVICE_ON, Boolean.valueOf(WTUPCP_Constants.SERVICE_ON_DEFAULT))) {
                        if (WTUPCP_MainService.isServiceRunning(WTUPCP_HomeActivity.this)) {
                            ContextCompat.startForegroundService(WTUPCP_HomeActivity.this, new WTUPCP_IntentFactory(WTUPCP_HomeActivity.this.getApplicationContext()).getMonitorServiceIntent(true));
                            WTUPCP_HomeFragment.getInstance().setOnOffSwitch(false);
                            WTUPCP_HomeActivity.this.sharedPreferencesUtils.putBoolean(WTUPCP_Constants.SERVICE_ON, false);
                            return;
                        }
                        WTUPCP_HomeFragment.getInstance().setOnOffSwitch(false);
                        WTUPCP_HomeActivity.this.sharedPreferencesUtils.putBoolean(WTUPCP_Constants.SERVICE_ON, false);
                    } else if (!WTUPCP_MainService.isServiceRunning(WTUPCP_HomeActivity.this)) {
                        ContextCompat.startForegroundService(WTUPCP_HomeActivity.this, new WTUPCP_IntentFactory(WTUPCP_HomeActivity.this.getApplicationContext()).getMonitorServiceIntent(false));
                        WTUPCP_HomeFragment.getInstance().setOnOffSwitch(true);
                        WTUPCP_HomeActivity.this.sharedPreferencesUtils.putBoolean(WTUPCP_Constants.SERVICE_ON, true);
                    } else {
                        WTUPCP_HomeFragment.getInstance().setOnOffSwitch(true);
                        WTUPCP_HomeActivity.this.sharedPreferencesUtils.putBoolean(WTUPCP_Constants.SERVICE_ON, true);
                    }
                }
            });
        } else if (i == 1) {
            this.rlHistory.performClick();
        } else if (i != 2) {
        } else {
            if (this.sharedPreferencesUtils.getBoolean(WTUPCP_Constants.SERVICE_ON, Boolean.valueOf(WTUPCP_Constants.SERVICE_ON_DEFAULT))) {
                if (!WTUPCP_MainService.isServiceRunning(this)) {
                    ContextCompat.startForegroundService(this, new WTUPCP_IntentFactory(getApplicationContext()).getMonitorServiceIntent(false));
                    WTUPCP_HomeFragment.getInstance().setOnOffSwitch(true);
                    this.sharedPreferencesUtils.putBoolean(WTUPCP_Constants.SERVICE_ON, true);
                    return;
                }
                WTUPCP_HomeFragment.getInstance().setOnOffSwitch(true);
                this.sharedPreferencesUtils.putBoolean(WTUPCP_Constants.SERVICE_ON, true);
            } else if (WTUPCP_MainService.isServiceRunning(this)) {
                ContextCompat.startForegroundService(this, new WTUPCP_IntentFactory(getApplicationContext()).getMonitorServiceIntent(true));
                WTUPCP_HomeFragment.getInstance().setOnOffSwitch(false);
                this.sharedPreferencesUtils.putBoolean(WTUPCP_Constants.SERVICE_ON, false);
            } else {
                WTUPCP_HomeFragment.getInstance().setOnOffSwitch(false);
                this.sharedPreferencesUtils.putBoolean(WTUPCP_Constants.SERVICE_ON, false);
            }
        }
    }

    public void onSettingsClickBack() {
        this.rlHome.performClick();
    }

    public void onSettingsUnlockAttemptsLimit(int i) {
        int i2 = i - 1;
        if (i2 == 0) {
            this.rb1FailAttempt.setChecked(true);
            this.rb2FailAttempt.setChecked(false);
            this.rb3FailAttempt.setChecked(false);
            this.rb4FailAttempt.setChecked(false);
        } else if (i2 == 1) {
            this.rb1FailAttempt.setChecked(false);
            this.rb2FailAttempt.setChecked(true);
            this.rb3FailAttempt.setChecked(false);
            this.rb4FailAttempt.setChecked(false);
        } else if (i2 == 2) {
            this.rb1FailAttempt.setChecked(false);
            this.rb2FailAttempt.setChecked(false);
            this.rb3FailAttempt.setChecked(true);
            this.rb4FailAttempt.setChecked(false);
        } else if (i2 == 3) {
            this.rb1FailAttempt.setChecked(false);
            this.rb2FailAttempt.setChecked(false);
            this.rb3FailAttempt.setChecked(false);
            this.rb4FailAttempt.setChecked(true);
        }
        this.bottomSheetBehaviorUnlockAttempt.setState(3);
    }

    public void onHistoryClickBack() {
        this.rlHome.performClick();
    }

    
    public void onResume() {
        super.onResume();
        WTUPCP_Utils.stop_alarm();
    }

    static class AnonymousClass5 {
        static final int[] $SwitchMap$com$whotryunlockphone$captrphotos$wtupcp_utils$WTUPCP_Sort;
        static {
            int[] iArr = new int[WTUPCP_Sort.values().length];
            $SwitchMap$com$whotryunlockphone$captrphotos$wtupcp_utils$WTUPCP_Sort = iArr;
            iArr[WTUPCP_Sort.All.ordinal()] = 1;
            $SwitchMap$com$whotryunlockphone$captrphotos$wtupcp_utils$WTUPCP_Sort[WTUPCP_Sort.Recent.ordinal()] = 2;
            $SwitchMap$com$whotryunlockphone$captrphotos$wtupcp_utils$WTUPCP_Sort[WTUPCP_Sort.Fail.ordinal()] = 3;
            try {
                $SwitchMap$com$whotryunlockphone$captrphotos$wtupcp_utils$WTUPCP_Sort[WTUPCP_Sort.Success.ordinal()] = 4;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public void OnSortClick(WTUPCP_Sort wTUPCP_Sort) {
        int i = AnonymousClass5.$SwitchMap$com$whotryunlockphone$captrphotos$wtupcp_utils$WTUPCP_Sort[wTUPCP_Sort.ordinal()];
        if (i == 1) {
            this.rbSortAll.setChecked(true);
            this.rbSortRecent.setChecked(false);
            this.rbSortFail.setChecked(false);
            this.rbSortSuccess.setChecked(false);
        } else if (i == 2) {
            this.rbSortAll.setChecked(false);
            this.rbSortRecent.setChecked(true);
            this.rbSortFail.setChecked(false);
            this.rbSortSuccess.setChecked(false);
        } else if (i == 3) {
            this.rbSortAll.setChecked(false);
            this.rbSortRecent.setChecked(false);
            this.rbSortFail.setChecked(true);
            this.rbSortSuccess.setChecked(false);
        } else if (i == 4) {
            this.rbSortAll.setChecked(false);
            this.rbSortRecent.setChecked(false);
            this.rbSortFail.setChecked(false);
            this.rbSortSuccess.setChecked(true);
        }
        this.bottomSheetBehavior.setState(3);
    }

    public void onBackPressed() {
        if (this.bottomSheetBehaviorUnlockAttempt.getState() == 3) {
            this.bottomSheetBehaviorUnlockAttempt.setState(4);
        } else if (this.bottomSheetBehavior.getState() == 3) {
            this.bottomSheetBehavior.setState(4);
        } else if (this.currentFragmentPosition != 0) {
            this.rlHome.performClick();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("app_rater", 0);
            int i = sharedPreferences.getInt("total_launch_count", 1);
            int i2 = sharedPreferences.getInt("never_count", 1);
            int i3 = sharedPreferences.getInt("rate_count", 1);
            long j = sharedPreferences.getLong("first_launch_date_time", 0);
            long j2 = sharedPreferences.getLong("launch_date_time", 0);
            if (j == 0) {
                WTUPCP_RateAppDialog.AppLaunch(this, R.layout.wtupcp_dialog_rate_us, R.id.tvAskLater, R.id.tvRateUs, "main");
            } else if (System.currentTimeMillis() < j2 + 86400000 || i > 5 || i2 > 2 || i3 > 2) {
                finishAffinity();
            } else {
                WTUPCP_RateAppDialog.AppLaunch(this, R.layout.wtupcp_dialog_rate_us, R.id.tvAskLater, R.id.tvRateUs, "main");
            }
        }
    }
}
