package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.facebook.ads.NativeAdLayout;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.ads.AdsCommon;
import com.whounlockmyphone.captrphotoswhotryunlock23.ads.MyApplication;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_activity.WTUPCP_HelpActivity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_manager.WTUPCP_PolicyManager;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_RateAppDialog;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_SharePreferenceUtils;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class WTUPCP_SettingsFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CODE_SIGN_IN = 0;
    public static WTUPCP_SettingsFragment settingsFragment;
    Activity activity;
    Context context;
    String[] emailPermission = {"android.permission.GET_ACCOUNTS"};
    TextView etAlarmTone;
    TextView etEmail;
    ImageView ivBack;
    LinearLayout llAlarmTone;
    LinearLayout llEmailId;
    LinearLayout llHelp;
    LinearLayout llPrivacyPolicy;
    LinearLayout llRateUs;
    LinearLayout llSaveReport;
    LinearLayout llShare;
    LinearLayout llSuccessPassword;
    LinearLayout llUnInstallApp;
    LinearLayout llUnlockAttempstLimit;
    OnSettingsClick onSettingsClick;
    WTUPCP_PolicyManager policyManager;
    String ringtone;
    WTUPCP_SharePreferenceUtils sharePreferenceUtils;
    ToggleButton swAlarmOnWrongPassword;
    ToggleButton swAlertOnEmail;
    ToggleButton swEnableNotification;
    ToggleButton swFailUnlock;
    ToggleButton swFlashOnWrongPassword;
    ToggleButton swListOfLaunchedApp;
    ToggleButton swSaveReportNoLock;
    ToggleButton swSuccessfulUnlock;
    ToggleButton swVibrationOnWrongPassword;

    TextView tvNumberOfAttempts;

    public interface OnSettingsClick {
        void onSettingsClickBack();

        void onSettingsUnlockAttemptsLimit(int i);
    }

    public void onPermissionsDenied(int i, List<String> list) {
    }

    public static WTUPCP_SettingsFragment getInstance() {
        WTUPCP_SettingsFragment wTUPCP_SettingsFragment = settingsFragment;
        if (wTUPCP_SettingsFragment != null) {
            return wTUPCP_SettingsFragment;
        }
        WTUPCP_SettingsFragment wTUPCP_SettingsFragment2 = new WTUPCP_SettingsFragment();
        settingsFragment = wTUPCP_SettingsFragment2;
        return wTUPCP_SettingsFragment2;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void onAttach(Activity activity2) {
        super.onAttach(activity2);
        try {
            this.onSettingsClick = (OnSettingsClick) activity2;
        } catch (ClassCastException unused) {
            throw new ClassCastException(activity2.toString() + " must implement onSomeEventListener");
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.context = getContext();
        this.activity = getActivity();
        return layoutInflater.inflate(R.layout.wtupcp_fragment_settings, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);



        //Reguler Native Ads
        FrameLayout admob_native_frame = (FrameLayout) view.findViewById(R.id.Admob_Native_Frame);
        NativeAdLayout nativeAdLayout = (NativeAdLayout) view.findViewById(R.id.native_ad_container);
        FrameLayout maxNative = (FrameLayout) view.findViewById(R.id.max_native_ad_layout);
        AdsCommon.RegulerBigNative(getActivity(), admob_native_frame, nativeAdLayout, maxNative);



        inIt(view);
        this.llSuccessPassword = (LinearLayout) view.findViewById(R.id.llSuccessPassword);
        this.llSaveReport = (LinearLayout) view.findViewById(R.id.llSaveReport);
        clickListener();


        viewSetup();
        checkedChangeListener();
        if (Build.VERSION.SDK_INT >= 30) {
            this.llSaveReport.setVisibility(8);
        }
    }

    private void checkedChangeListener() {
        this.swEnableNotification.setOnCheckedChangeListener(this);
        this.swAlertOnEmail.setOnCheckedChangeListener(this);
        this.swAlarmOnWrongPassword.setOnCheckedChangeListener(this);
        this.swVibrationOnWrongPassword.setOnCheckedChangeListener(this);
        this.swFlashOnWrongPassword.setOnCheckedChangeListener(this);
        this.swListOfLaunchedApp.setOnCheckedChangeListener(this);
        this.swSaveReportNoLock.setOnCheckedChangeListener(this);
        this.swSuccessfulUnlock.setOnCheckedChangeListener(this);
        this.swFailUnlock.setOnCheckedChangeListener(this);

    }

    private void inIt(View view) {
        this.ivBack = (ImageView) view.findViewById(R.id.ivBack);
        this.llUnlockAttempstLimit = (LinearLayout) view.findViewById(R.id.llUnlockAttempstLimit);
        this.llShare = (LinearLayout) view.findViewById(R.id.llShare);
        this.llPrivacyPolicy = (LinearLayout) view.findViewById(R.id.llPrivacyPolicy);
        this.llRateUs = (LinearLayout) view.findViewById(R.id.llRateUs);
        this.llAlarmTone = (LinearLayout) view.findViewById(R.id.llAlarmTone);
        this.llEmailId = (LinearLayout) view.findViewById(R.id.llEmailId);
        this.llUnInstallApp = (LinearLayout) view.findViewById(R.id.llUnInstallApp);
        this.llHelp = (LinearLayout) view.findViewById(R.id.llHelp);
        this.swEnableNotification = (ToggleButton) view.findViewById(R.id.swEnableNotification);
        this.swAlertOnEmail = (ToggleButton) view.findViewById(R.id.swAlertOnEmail);
        this.tvNumberOfAttempts = (TextView) view.findViewById(R.id.tvNumberOfAttempts);
        this.etEmail = (TextView) view.findViewById(R.id.etEmail);
        this.etEmail = (TextView) view.findViewById(R.id.etEmail);
        this.etAlarmTone = (TextView) view.findViewById(R.id.etAlarmTone);
        this.swAlarmOnWrongPassword = (ToggleButton) view.findViewById(R.id.swAlarmOnWrongPassword);
        this.swVibrationOnWrongPassword = (ToggleButton) view.findViewById(R.id.swVibrationOnWrongPassword);
        this.swFlashOnWrongPassword = (ToggleButton) view.findViewById(R.id.swFlashOnWrongPassword);
        this.swListOfLaunchedApp = (ToggleButton) view.findViewById(R.id.swListOfLaunchedApp);
        this.swSaveReportNoLock = (ToggleButton) view.findViewById(R.id.swSaveReportNoLock);
        this.swSuccessfulUnlock = (ToggleButton) view.findViewById(R.id.swSuccessfulUnlock);
        this.swFailUnlock = (ToggleButton) view.findViewById(R.id.swFailUnlock);
        this.policyManager = new WTUPCP_PolicyManager(this.context);
        this.sharePreferenceUtils = new WTUPCP_SharePreferenceUtils(this.context, WTUPCP_Constants.PREFERENCE_NAME);
    }

    private void viewSetup() {
        this.swEnableNotification.setChecked(this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_ENABLE_NOTIFICATION, Boolean.valueOf(WTUPCP_Constants.SETTING_ENABLE_NOTIFICATION_DEFAULT)));
        int i = this.sharePreferenceUtils.getInt(WTUPCP_Constants.SETTING_NO_OF_ATTEMPS, WTUPCP_Constants.SETTING_NO_OF_ATTEMPS_DEFAULT) - 1;
        if (i == 0) {
            this.tvNumberOfAttempts.setText("1 Attempts");
        } else if (i == 1) {
            this.tvNumberOfAttempts.setText("2 Attempts");
        } else if (i == 2) {
            this.tvNumberOfAttempts.setText("3 Attempts");
        } else if (i == 3) {
            this.tvNumberOfAttempts.setText("4 Attempts");
        }
        this.swAlertOnEmail.setChecked(this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_ALERT_ON_EMAIL, Boolean.valueOf(WTUPCP_Constants.SETTING_ALERT_ON_EMAIL_DEFAULT)));
        this.etEmail.setText(this.sharePreferenceUtils.getString(WTUPCP_Constants.SETTING_EMAIL_ADDRESS, "Add email id"));
        this.swAlarmOnWrongPassword.setChecked(this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_ALARM_ON_WRONG_PASSWORD, Boolean.valueOf(WTUPCP_Constants.SETTING_ALARM_ON_WRONG_PASSWORD_DEFAULT)));
        String string = this.sharePreferenceUtils.getString(WTUPCP_Constants.SETTING_ALARM_TONE, WTUPCP_Constants.SETTING_ALARM_TONE_DEFAULT);
        this.ringtone = string;
        Ringtone ringtone2 = RingtoneManager.getRingtone(this.context, Uri.parse(string));
        String title = ringtone2.getTitle(this.context);
        if (title.contains("Default") || title.contains("default")) {
            this.etAlarmTone.setText("Default tone ");
        } else {
            this.etAlarmTone.setText(ringtone2.getTitle(this.context));
        }
        this.swVibrationOnWrongPassword.setChecked(this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_VIBRATE_ON_WRONG_PASSWORD, Boolean.valueOf(WTUPCP_Constants.SETTING_VIBRATE_ON_WRONG_PASSWORD_DEFAULT)));
        this.swFlashOnWrongPassword.setChecked(this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_FLASH_ON_WRONG_PASSWORD, Boolean.valueOf(WTUPCP_Constants.SETTING_FLASH_ON_WRONG_PASSWORD_DEFAULT)));
        this.swListOfLaunchedApp.setChecked(this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_SHOW_LAUNCHED_APP, Boolean.valueOf(WTUPCP_Constants.SETTING_SHOW_LAUNCHED_APP_DEFAULT)));
        this.swSaveReportNoLock.setChecked(this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_SAVE_REPORT_IF_NO_UNLOCK_ATTEMPSTS, Boolean.valueOf(WTUPCP_Constants.SETTING_SETTING_SAVE_REPORT_IF_NO_UNLOCK_ATTEMPSTS_DEFAULT)));
        this.swSuccessfulUnlock.setChecked(this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_SUCCESSFUL_UNLOCK_ATTEMPSTS, Boolean.valueOf(WTUPCP_Constants.SETTING_SUCCESSFUL_UNLOCK_ATTEMPSTS_DEFAULT)));
        this.swFailUnlock.setChecked(this.sharePreferenceUtils.getBoolean(WTUPCP_Constants.SETTING_FAIL_UNLOCK_ATTEMPSTS, Boolean.valueOf(WTUPCP_Constants.SETTING_FAIL_UNLOCK_ATTEMPSTS_DEFAULT)));
        if (this.sharePreferenceUtils.getBoolean(getResources().getString(R.string.google_drive_sign_in), false)) {
            return;
        }
    }

    private void clickListener() {
        this.ivBack.setOnClickListener(this);
        this.llUnlockAttempstLimit.setOnClickListener(this);
        this.llShare.setOnClickListener(this);
        this.llPrivacyPolicy.setOnClickListener(this);
        this.llRateUs.setOnClickListener(this);
        this.llEmailId.setOnClickListener(this);
        this.llAlarmTone.setOnClickListener(this);
        this.llUnInstallApp.setOnClickListener(this);
        this.llHelp.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                this.onSettingsClick.onSettingsClickBack();
                return;
            case R.id.llAlarmTone:
                selectRingtone();
                return;
            case R.id.llEmailId:
                Log.e("CHECK_CLICK", "YES");
                pickUserAccount();
                return;
            case R.id.llHelp:
                Intent intentHelp = new Intent(context, WTUPCP_HelpActivity.class);
                AdsCommon.InterstitialAd(context, intentHelp);
                return;
            case R.id.llPrivacyPolicy:
                Intent intentPrivacy = new Intent(Intent.ACTION_VIEW, Uri.parse(MyApplication.PrivacyPolicy));
                intentPrivacy.setPackage("com.android.chrome");
                startActivity(intentPrivacy);
                return;
            case R.id.llRateUs:
                SharedPreferences sharedPreferences = this.context.getSharedPreferences("app_rater", 0);
                final SharedPreferences.Editor edit = sharedPreferences.edit();
                sharedPreferences.getBoolean("do_not_show_again", false);
                final Dialog dialog = new Dialog(this.context);
                dialog.requestWindowFeature(1);
                dialog.getWindow().setBackgroundDrawableResource(17170445);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                View inflate = getLayoutInflater().inflate(R.layout.wtupcp_dialog_rate_us, (ViewGroup) null);
                ((TextView) inflate.findViewById(R.id.tvAskLater)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                ((TextView) inflate.findViewById(R.id.tvRateUs)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (WTUPCP_RateAppDialog.rate_count <= 2) {
                            SharedPreferences.Editor editor = edit;
                            if (editor != null) {
                                editor.putInt("rate_count", WTUPCP_RateAppDialog.rate_count + 1);
                                edit.commit();
                            }
                            try {
                                WTUPCP_SettingsFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + WTUPCP_SettingsFragment.this.context.getPackageName())));
                            } catch (ActivityNotFoundException unused) {
                                WTUPCP_SettingsFragment wTUPCP_SettingsFragment = WTUPCP_SettingsFragment.this;
                                wTUPCP_SettingsFragment.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + WTUPCP_SettingsFragment.this.context.getPackageName())));
                                Toast.makeText(WTUPCP_SettingsFragment.this.context, " unable to find market app", 1).show();
                            }
                        } else {
                            SharedPreferences.Editor editor2 = edit;
                            if (editor2 != null) {
                                editor2.putBoolean("do_not_show_again", true);
                                edit.commit();
                            }
                        }
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(inflate);
                dialog.show();
                return;
            case R.id.llShare:
                String packageName = this.context.getPackageName();
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
                intent.putExtra("android.intent.extra.TEXT", "Check out the App at: https://play.google.com/store/apps/details?id=" + packageName);
                intent.setType("text/plain");
                startActivity(intent);
                return;
            case R.id.llUnInstallApp:
                if (this.policyManager.isAdminActive()) {
                    ((DevicePolicyManager) this.context.getSystemService("device_policy")).removeActiveAdmin(this.policyManager.getAdminComponent());
                }
                Intent intent2 = new Intent("android.intent.action.UNINSTALL_PACKAGE");
                intent2.setData(Uri.parse("package:" + this.context.getPackageName()));
                startActivity(intent2);
                return;
            case R.id.llUnlockAttempstLimit:
                this.onSettingsClick.onSettingsUnlockAttemptsLimit(this.sharePreferenceUtils.getInt(WTUPCP_Constants.SETTING_NO_OF_ATTEMPS, WTUPCP_Constants.SETTING_NO_OF_ATTEMPS_DEFAULT));
                return;
            default:
                return;
        }
    }

    public void selectRingtone() {
        Intent intent = new Intent("android.intent.action.RINGTONE_PICKER");
        intent.putExtra("android.intent.extra.ringtone.TYPE", 4);
        intent.putExtra("android.intent.extra.ringtone.SHOW_SILENT", true);
        intent.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
        intent.putExtra("android.intent.extra.ringtone.TITLE", "Select Alarm Tone");
        intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", Uri.parse(this.ringtone));
        startActivityForResult(intent, WTUPCP_Constants.RC_ALARM_TONE);
    }

    public void setFailAttempt(int i) {
        int i2 = i - 1;
        if (i2 == 0) {
            this.tvNumberOfAttempts.setText("1 Attempts");
            this.sharePreferenceUtils.putInt(WTUPCP_Constants.SETTING_NO_OF_ATTEMPS, 1);
        } else if (i2 == 1) {
            this.tvNumberOfAttempts.setText("2 Attempts");
            this.sharePreferenceUtils.putInt(WTUPCP_Constants.SETTING_NO_OF_ATTEMPS, 2);
        } else if (i2 == 2) {
            this.tvNumberOfAttempts.setText("3 Attempts");
            this.sharePreferenceUtils.putInt(WTUPCP_Constants.SETTING_NO_OF_ATTEMPS, 3);
        } else if (i2 == 3) {
            this.tvNumberOfAttempts.setText("4 Attempts");
            this.sharePreferenceUtils.putInt(WTUPCP_Constants.SETTING_NO_OF_ATTEMPS, 4);
        }
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        switch (compoundButton.getId()) {
            case R.id.swAlarmOnWrongPassword:
                if (z) {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_ALARM_ON_WRONG_PASSWORD, true);
                    return;
                } else {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_ALARM_ON_WRONG_PASSWORD, false);
                    return;
                }
            case R.id.swAlertOnEmail:
                if (!z) {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_ALERT_ON_EMAIL, false);
                    return;
                } else if (!this.sharePreferenceUtils.getString(WTUPCP_Constants.SETTING_EMAIL_ADDRESS, "Add email id").equals("Add email id")) {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_ALERT_ON_EMAIL, true);
                    return;
                } else if (EasyPermissions.hasPermissions(this.context, this.emailPermission)) {
                    pickUserAccount();
                    return;
                } else {
                    EasyPermissions.requestPermissions(this.activity, "", WTUPCP_Constants.PC_EMAIL, this.emailPermission);
                    return;
                }
            case R.id.swEnableNotification:
                if (z) {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_ENABLE_NOTIFICATION, true);
                    return;
                } else {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_ENABLE_NOTIFICATION, false);
                    return;
                }
            case R.id.swFailUnlock:
                if (z) {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_FAIL_UNLOCK_ATTEMPSTS, true);
                    return;
                } else {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_FAIL_UNLOCK_ATTEMPSTS, false);
                    return;
                }
            case R.id.swFlashOnWrongPassword:
                if (z) {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_FLASH_ON_WRONG_PASSWORD, true);
                    return;
                } else {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_FLASH_ON_WRONG_PASSWORD, false);
                    return;
                }
            case R.id.swListOfLaunchedApp:
                if (z) {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_SHOW_LAUNCHED_APP, true);
                    return;
                } else {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_SHOW_LAUNCHED_APP, false);
                    return;
                }
            case R.id.swSaveReportNoLock:
                if (z) {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_SAVE_REPORT_IF_NO_UNLOCK_ATTEMPSTS, true);
                    return;
                } else {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_SAVE_REPORT_IF_NO_UNLOCK_ATTEMPSTS, false);
                    return;
                }
            case R.id.swSuccessfulUnlock:
                if (z) {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_SUCCESSFUL_UNLOCK_ATTEMPSTS, true);
                    return;
                } else {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_SUCCESSFUL_UNLOCK_ATTEMPSTS, false);
                    return;
                }
            case R.id.swVibrationOnWrongPassword:
                if (z) {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_VIBRATE_ON_WRONG_PASSWORD, true);
                    return;
                } else {
                    this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_VIBRATE_ON_WRONG_PASSWORD, false);
                    return;
                }
            default:
                return;
        }
    }

    private void pickUserAccount() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= 23) {
            intent = AccountManager.newChooseAccountIntent((Account) null, (List) null, new String[]{"com.google"}, (String) null, (String) null, (String[]) null, (Bundle) null);
        } else {
            intent = AccountManager.newChooseAccountIntent((Account) null, (ArrayList) null, new String[]{"com.google"}, false, (String) null, (String) null, (String[]) null, (Bundle) null);
        }
        startActivityForResult(intent, WTUPCP_Constants.RC_EMAIL_PIC);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }

    public void onPermissionsGranted(int i, List<String> list) {
        if (i == WTUPCP_Constants.PC_EMAIL) {
            pickUserAccount();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == WTUPCP_Constants.RC_EMAIL_PIC && i2 == -1) {
            this.sharePreferenceUtils.putBoolean(WTUPCP_Constants.SETTING_ALERT_ON_EMAIL, true);
            String stringExtra = intent.getStringExtra("authAccount");
            this.etEmail.setText(stringExtra);
            this.sharePreferenceUtils.putString(WTUPCP_Constants.SETTING_EMAIL_ADDRESS, stringExtra);
        } else if (i == WTUPCP_Constants.RC_EMAIL_PIC && i2 == 0) {
            if (this.sharePreferenceUtils.getString(WTUPCP_Constants.SETTING_EMAIL_ADDRESS, "Add email id").equals("Add email id")) {
                this.swAlertOnEmail.setChecked(false);
            } else {
                this.swAlertOnEmail.setChecked(true);
            }
        } else if (i == WTUPCP_Constants.RC_ALARM_TONE && i2 == -1) {
            Uri uri = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            if (uri != null) {
                this.sharePreferenceUtils.putString(WTUPCP_Constants.SETTING_ALARM_TONE, uri.toString());
            } else {
                this.sharePreferenceUtils.putString(WTUPCP_Constants.SETTING_ALARM_TONE, RingtoneManager.getDefaultUri(4).toString());
            }
            Ringtone ringtone2 = RingtoneManager.getRingtone(this.context, uri);
            String title = ringtone2.getTitle(this.context);
            if (title.contains("Default") || title.contains("default")) {
                this.etAlarmTone.setText("Default tone ");
            } else {
                this.etAlarmTone.setText(ringtone2.getTitle(this.context));
            }
        }
        if (i == 0 && i2 == -1) {
            Log.e("SIGNIN_CHECK", "DONE");
            this.sharePreferenceUtils.putBoolean(getResources().getString(R.string.google_drive_sign_in), true);
            return;
        }
        Log.e("SIGNIN_CHECK", "ERROR");
    }

}
