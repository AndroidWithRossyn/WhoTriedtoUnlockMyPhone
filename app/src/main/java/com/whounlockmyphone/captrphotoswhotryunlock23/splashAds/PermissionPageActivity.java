package com.whounlockmyphone.captrphotoswhotryunlock23.splashAds;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.ads.AdsCommon;
import com.facebook.ads.NativeAdLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class PermissionPageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_page_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primarymain));
        }

        //one time call & load ads
        AdsCommon.OneTimeCall(this);


        //Reguler Banner Ads
        RelativeLayout admob_banner = (RelativeLayout) findViewById(R.id.Admob_Banner_Frame);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        AdsCommon.RegulerBanner(this, admob_banner, adContainer);


        //Reguler Native Ads
        FrameLayout admob_native_frame = (FrameLayout) findViewById(R.id.Admob_Native_Frame);
        NativeAdLayout nativeAdLayout = (NativeAdLayout) findViewById(R.id.native_ad_container);
        FrameLayout maxNative = (FrameLayout) findViewById(R.id.max_native_ad_layout);
        AdsCommon.RegulerBigNative(this, admob_native_frame, nativeAdLayout, maxNative);


        //Small Native Ads
        FrameLayout admob_small_native = (FrameLayout) findViewById(R.id.Admob_Small_Native);
        NativeAdLayout native_banner_ad_container = (NativeAdLayout) findViewById(R.id.native_banner_ad_container);
        AdsCommon.SmallNative(this, admob_small_native, native_banner_ad_container);


        checkMultiplePermissions(this);


        ((RelativeLayout) findViewById(R.id.btnPermission)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PermissionPageActivity.this, FirstPageMainActivity.class);
                AdsCommon.InterstitialAd(PermissionPageActivity.this, intent);
            }
        });

    }

    public static boolean checkMultiplePermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            if (!addPermission(activity, arrayList2, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                arrayList.add("Read Storage");
            }
            if (!addPermission(activity, arrayList2, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                arrayList.add("Write Storage");
            }
            if (arrayList2.size() > 0) {
                activity.requestPermissions((String[]) arrayList2.toArray(new String[arrayList2.size()]), 124);
                return false;
            }
        }
        return true;
    }

    public static boolean addPermission(Activity activity, List<String> list, String str) {
        if (Build.VERSION.SDK_INT >= 23 && activity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
            list.add(str);
            if (!activity.shouldShowRequestPermissionRationale(str)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
