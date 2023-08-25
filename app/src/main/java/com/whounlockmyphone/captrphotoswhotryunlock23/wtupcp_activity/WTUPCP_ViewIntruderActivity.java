package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.facebook.ads.NativeAdLayout;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.ads.AdsCommon;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_adapters.WTUPCP_AppListAdapter;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.db.WTUPCP_DatabaseClient;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_AppListEntity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_ReportEntity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_extra.WTUPCP_IntentFactory;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class WTUPCP_ViewIntruderActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = "adsLog";
    WTUPCP_AppListAdapter appListAdapter;
    List<WTUPCP_AppListEntity> appListEntityList = new ArrayList();
    CardView cvApplicationList;
    Intent intent;
    ImageView ivBack;
    ImageView ivDelete;
    ImageView ivImage;
    ImageView ivShare;
    WTUPCP_ReportEntity reportEntity;
    RecyclerView rvAppList;
    TextView tvDateTime;
    TextView tvStatus;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.wtupcp_activity_view_intruder);


        //Reguler Banner Ads
        RelativeLayout admob_banner = (RelativeLayout) findViewById(R.id.Admob_Banner_Frame);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        AdsCommon.RegulerBanner(this, admob_banner, adContainer);


        //Reguler Native Ads
        FrameLayout admob_native_frame = (FrameLayout) findViewById(R.id.Admob_Native_Frame);
        NativeAdLayout nativeAdLayout = (NativeAdLayout) findViewById(R.id.native_ad_container);
        FrameLayout maxNative = (FrameLayout) findViewById(R.id.max_native_ad_layout);
        AdsCommon.RegulerBigNative(this, admob_native_frame, nativeAdLayout, maxNative);


        inIt();
        clickListener();

        viewSetup();
        new getAppData().execute(new Void[0]);
    }

    private void viewSetup() {
        Glide.with((FragmentActivity) this).load(this.reportEntity.getPHOTO_PATH()).into(this.ivImage);
        this.tvDateTime.setText(WTUPCP_Constants.simpleDateFormat2.format(new Date(this.reportEntity.getREPORT_TIME())));
        if (this.reportEntity.isDEVICE_UNLOCK_FAIL()) {
            this.tvStatus.setText(getResources().getString(R.string.successful_attempt));
            this.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.success_color));
        } else {
            this.tvStatus.setText(getResources().getString(R.string.fail_attempt));
            this.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.fail_color));
        }
        this.appListAdapter = new WTUPCP_AppListAdapter(this, this.appListEntityList);
        this.rvAppList.setLayoutManager(new LinearLayoutManager(this, 1, false));
        this.rvAppList.setAdapter(this.appListAdapter);
    }

    private void clickListener() {
        this.ivBack.setOnClickListener(this);
        this.ivShare.setOnClickListener(this);
        this.ivDelete.setOnClickListener(this);
    }

    private void inIt() {
        this.intent = getIntent();
        this.ivImage = (ImageView) findViewById(R.id.ivImage);
        this.ivBack = (ImageView) findViewById(R.id.ivBack);
        this.ivShare = (ImageView) findViewById(R.id.ivShare);
        this.ivDelete = (ImageView) findViewById(R.id.ivDelete);
        this.tvDateTime = (TextView) findViewById(R.id.tvDateTime);
        this.tvStatus = (TextView) findViewById(R.id.tvStatus);
        this.rvAppList = (RecyclerView) findViewById(R.id.rvAppList);
        this.cvApplicationList = (CardView) findViewById(R.id.cvApplicationList);
        this.reportEntity = (WTUPCP_ReportEntity) this.intent.getSerializableExtra(WTUPCP_Constants.REPORT_ENTITY);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                return;
            case R.id.ivDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage((CharSequence) "Do you want to delete?");
                builder.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WTUPCP_ViewIntruderActivity wTUPCP_ViewIntruderActivity = WTUPCP_ViewIntruderActivity.this;
                        new DeleteReport(wTUPCP_ViewIntruderActivity.reportEntity).execute(new Void[0]);
                    }
                });
                builder.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.create().show();
                return;
            case R.id.ivShare:
                String packageName = getPackageName();
                Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.putExtra("android.intent.extra.TEXT", getResources().getString(R.string.app_name) + "\n\nCheck out the App at: https://play.google.com/store/apps/details?id=" + packageName);
                intent2.setType("Image/*");
                intent2.putExtra("android.intent.extra.STREAM", Uri.parse(this.reportEntity.getPHOTO_PATH()));
                startActivity(Intent.createChooser(intent2, "Send Image:"));
                return;
            default:
                return;
        }
    }

    private class DeleteReport extends AsyncTask<Void, Void, Void> {
        WTUPCP_ReportEntity reportEntity;

        public DeleteReport(WTUPCP_ReportEntity wTUPCP_ReportEntity) {
            this.reportEntity = wTUPCP_ReportEntity;
        }

        
        public Void doInBackground(Void... voidArr) {
            WTUPCP_DatabaseClient.getInstance(WTUPCP_ViewIntruderActivity.this).getAppDatabase().reportDao().delete(this.reportEntity);
            return null;
        }

        
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            WTUPCP_ViewIntruderActivity.this.sendBroadcast(new WTUPCP_IntentFactory(WTUPCP_ViewIntruderActivity.this).getUpdateUiIntent(true));
            WTUPCP_ViewIntruderActivity.this.finish();
        }
    }

    private class getAppData extends AsyncTask<Void, Void, Void> {
        private getAppData() {
        }

        
        public Void doInBackground(Void... voidArr) {
            List<WTUPCP_AppListEntity> allDataForSingleReport = WTUPCP_DatabaseClient.getInstance(WTUPCP_ViewIntruderActivity.this).getAppDatabase().appListDao().getAllDataForSingleReport(WTUPCP_ViewIntruderActivity.this.reportEntity.getREPORT_ID());
            WTUPCP_ViewIntruderActivity.this.appListEntityList.clear();
            WTUPCP_ViewIntruderActivity.this.appListEntityList.addAll(allDataForSingleReport);
            Collections.sort(WTUPCP_ViewIntruderActivity.this.appListEntityList, new Comparator<WTUPCP_AppListEntity>() {
                public int compare(WTUPCP_AppListEntity wTUPCP_AppListEntity, WTUPCP_AppListEntity wTUPCP_AppListEntity2) {
                    return (int) (wTUPCP_AppListEntity.getAPP_TIME() - wTUPCP_AppListEntity2.getAPP_TIME());
                }
            });
            Collections.reverse(WTUPCP_ViewIntruderActivity.this.appListEntityList);
            return null;
        }

        
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (WTUPCP_ViewIntruderActivity.this.appListEntityList.size() > 0) {
                WTUPCP_ViewIntruderActivity.this.cvApplicationList.setVisibility(0);
            } else {
                WTUPCP_ViewIntruderActivity.this.cvApplicationList.setVisibility(8);
            }
            WTUPCP_ViewIntruderActivity.this.appListAdapter.notifyDataSetChanged();
        }
    }
}
