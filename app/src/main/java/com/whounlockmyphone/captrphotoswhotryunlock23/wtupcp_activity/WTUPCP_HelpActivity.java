package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.ads.AdsCommon;

public class WTUPCP_HelpActivity extends AppCompatActivity implements View.OnClickListener {
    
    public String TAG = "MyCreation";
    ImageView ivBack;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.wtupcp_activity_help);


        //Reguler Banner Ads
        RelativeLayout admob_banner = (RelativeLayout) findViewById(R.id.Admob_Banner_Frame);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        AdsCommon.RegulerBanner(this, admob_banner, adContainer);


        
        inIt();
        clickListener();
    }

    private void clickListener() {
        this.ivBack.setOnClickListener(this);
    }

    private void inIt() {
        this.ivBack = (ImageView) findViewById(R.id.ivBack);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.ivBack) {
            onBackPressed();
        }
    }
}
