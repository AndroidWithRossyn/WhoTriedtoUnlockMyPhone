package com.whounlockmyphone.captrphotoswhotryunlock23.splashAds;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.ads.AdsCommon;
import com.facebook.ads.NativeAdLayout;

import androidx.appcompat.app.AppCompatActivity;

public class AppThankYouActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou_app);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorlight));
        }



        //Reguler Native Ads
        FrameLayout admob_native_frame = (FrameLayout) findViewById(R.id.Admob_Native_Frame);
        NativeAdLayout nativeAdLayout = (NativeAdLayout) findViewById(R.id.native_ad_container);
        FrameLayout maxNative = (FrameLayout) findViewById(R.id.max_native_ad_layout);
        AdsCommon.RegulerBigNative(this, admob_native_frame, nativeAdLayout, maxNative);


        //Small Native Ads
        FrameLayout admob_small_native = (FrameLayout) findViewById(R.id.Admob_Small_Native);
        NativeAdLayout native_banner_ad_container = (NativeAdLayout) findViewById(R.id.native_banner_ad_container);
        AdsCommon.SmallNative(this, admob_small_native, native_banner_ad_container);


        RelativeLayout Exit = (RelativeLayout) findViewById(R.id.exitapp);
        RelativeLayout Rate = (RelativeLayout) findViewById(R.id.rate);

        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String rateapp = getPackageName();
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + rateapp));
                startActivity(intent1);
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

}