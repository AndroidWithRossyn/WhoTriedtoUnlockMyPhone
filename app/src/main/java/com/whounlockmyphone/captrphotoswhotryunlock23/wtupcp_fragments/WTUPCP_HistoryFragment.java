package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_activity.WTUPCP_HomeActivity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_adapters.WTUPCP_HistoryAdapter;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.db.WTUPCP_DatabaseClient;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_ReportEntity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_extra.WTUPCP_IntentFactory;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_interfaces.WTUPCP_OnHistoryClick;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Sort;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class WTUPCP_HistoryFragment extends Fragment implements View.OnClickListener {
    public static WTUPCP_HistoryFragment historyFragment;
    Activity activity;
    Context context;
    WTUPCP_HistoryAdapter historyAdapter;
    ImageView ivBack;
    ImageView ivSort;
    LinearLayout llEmpty;
    WTUPCP_OnHistoryClick onHistoryClick;
    WTUPCP_OnHistoryClick onHistoryClick1 = new WTUPCP_OnHistoryClick() {
        public void OnSortClick(WTUPCP_Sort wTUPCP_Sort) {
        }

        public void onHistoryClickBack() {
        }

        public void onMenuClick(View view, final WTUPCP_ReportEntity wTUPCP_ReportEntity) {
            PopupMenu popupMenu = new PopupMenu(WTUPCP_HistoryFragment.this.context, view);
            popupMenu.inflate(R.menu.history_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int itemId = menuItem.getItemId();
                    if (itemId == R.id.delete) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(WTUPCP_HistoryFragment.this.context);
                        builder.setMessage((CharSequence) "Do you want to delete?");
                        builder.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new DeleteReport(wTUPCP_ReportEntity).execute(new Void[0]);
                            }
                        });
                        builder.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.create().show();
                        return false;
                    } else if (itemId != R.id.share) {
                        return false;
                    } else {
                        String packageName = WTUPCP_HistoryFragment.this.context.getPackageName();
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.putExtra("android.intent.extra.TEXT", WTUPCP_HistoryFragment.this.getResources().getString(R.string.app_name) + "\n\nCheck out the App at: https://play.google.com/store/apps/details?id=" + packageName);
                        intent.setType("Image/*");
                        intent.putExtra("android.intent.extra.STREAM", Uri.parse(wTUPCP_ReportEntity.getPHOTO_PATH()));
                        WTUPCP_HistoryFragment.this.startActivity(Intent.createChooser(intent, "Send Image:"));
                        return false;
                    }
                }
            });
            popupMenu.show();
        }
    };
    List<WTUPCP_ReportEntity> reportEntityList = new ArrayList();
    RecyclerView rvHistory;
    WTUPCP_Sort sort = WTUPCP_Sort.All;
    BroadcastReceiver updateUiBroadcast;

    public static WTUPCP_HistoryFragment getInstance() {
        WTUPCP_HistoryFragment wTUPCP_HistoryFragment = historyFragment;
        if (wTUPCP_HistoryFragment != null) {
            return wTUPCP_HistoryFragment;
        }
        WTUPCP_HistoryFragment wTUPCP_HistoryFragment2 = new WTUPCP_HistoryFragment();
        historyFragment = wTUPCP_HistoryFragment2;
        return wTUPCP_HistoryFragment2;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.context = getContext();
        this.activity = getActivity();
        return layoutInflater.inflate(R.layout.wtupcp_fragment_history, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        inIt(view);
        clickListener();
        adapterSetup();
        new getData().execute(new Void[0]);
    }

    public void onResume() {
        super.onResume();
    }

    private void adapterSetup() {
        this.historyAdapter = new WTUPCP_HistoryAdapter(this.context, this.reportEntityList, this.onHistoryClick1);
        this.rvHistory.setLayoutManager(new GridLayoutManager(this.context, 2, 1, false));
        this.rvHistory.setAdapter(this.historyAdapter);
        registerUIBroadCastReceiver();
    }

    static class AnonymousClass4 {
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

    public void setSortOption(WTUPCP_Sort wTUPCP_Sort) {
        this.sort = wTUPCP_Sort;
        int i = AnonymousClass4.$SwitchMap$com$whotryunlockphone$captrphotos$wtupcp_utils$WTUPCP_Sort[wTUPCP_Sort.ordinal()];
        if (i == 1) {
            new getData().execute(new Void[0]);
        } else if (i == 2) {
            new getRecentData().execute(new Void[0]);
        } else if (i == 3) {
            new getFailData().execute(new Void[0]);
        } else if (i == 4) {
            new getSuccessData().execute(new Void[0]);
        }
    }

    private class getRecentData extends AsyncTask<Void, Void, Void> {
        private getRecentData() {
        }

        
        public Void doInBackground(Void... voidArr) {
            List<WTUPCP_ReportEntity> allData = WTUPCP_DatabaseClient.getInstance(WTUPCP_HistoryFragment.this.context).getAppDatabase().reportDao().getAllData();
            WTUPCP_HistoryFragment.this.reportEntityList.clear();
            for (int i = 0; i < allData.size(); i++) {
                long abs = Math.abs(new Date(System.currentTimeMillis()).getTime() - new Date(allData.get(i).getREPORT_TIME()).getTime());
                long j = (abs / 60000) % 60;
                long j2 = (abs / 1000) % 60;
                if ((abs / 3600000) % 24 <= 0 && j >= 0 && j2 >= 0) {
                    WTUPCP_HistoryFragment.this.reportEntityList.add(allData.get(i));
                }
            }
            Collections.reverse(WTUPCP_HistoryFragment.this.reportEntityList);
            return null;
        }

        
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (WTUPCP_HistoryFragment.this.historyAdapter != null) {
                WTUPCP_HistoryFragment.this.historyAdapter.notifyDataSetChanged();
            }
        }
    }

    
    public void checkEmptyOrNot() {
        if (this.reportEntityList.size() > 0) {
            this.rvHistory.setVisibility(0);
            this.llEmpty.setVisibility(8);
            this.ivSort.setVisibility(0);
            return;
        }
        this.rvHistory.setVisibility(8);
        this.llEmpty.setVisibility(0);
        this.ivSort.setVisibility(8);
    }

    private class getFailData extends AsyncTask<Void, Void, Void> {
        private getFailData() {
        }

        
        public Void doInBackground(Void... voidArr) {
            List<WTUPCP_ReportEntity> allData = WTUPCP_DatabaseClient.getInstance(WTUPCP_HistoryFragment.this.context).getAppDatabase().reportDao().getAllData();
            WTUPCP_HistoryFragment.this.reportEntityList.clear();
            for (WTUPCP_ReportEntity next : allData) {
                if (!next.isDEVICE_UNLOCK_FAIL()) {
                    WTUPCP_HistoryFragment.this.reportEntityList.add(next);
                }
            }
            Collections.reverse(WTUPCP_HistoryFragment.this.reportEntityList);
            return null;
        }

        
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            WTUPCP_HistoryFragment.this.historyAdapter.notifyDataSetChanged();
        }
    }

    private class getSuccessData extends AsyncTask<Void, Void, Void> {
        private getSuccessData() {
        }

        
        public Void doInBackground(Void... voidArr) {
            List<WTUPCP_ReportEntity> allData = WTUPCP_DatabaseClient.getInstance(WTUPCP_HistoryFragment.this.context).getAppDatabase().reportDao().getAllData();
            WTUPCP_HistoryFragment.this.reportEntityList.clear();
            for (WTUPCP_ReportEntity next : allData) {
                if (next.isDEVICE_UNLOCK_FAIL()) {
                    WTUPCP_HistoryFragment.this.reportEntityList.add(next);
                }
            }
            Collections.reverse(WTUPCP_HistoryFragment.this.reportEntityList);
            return null;
        }

        
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            WTUPCP_HistoryFragment.this.historyAdapter.notifyDataSetChanged();
        }
    }

    private class DeleteReport extends AsyncTask<Void, Void, Void> {
        WTUPCP_ReportEntity reportEntity;

        public DeleteReport(WTUPCP_ReportEntity wTUPCP_ReportEntity) {
            this.reportEntity = wTUPCP_ReportEntity;
        }

        
        public Void doInBackground(Void... voidArr) {
            WTUPCP_DatabaseClient.getInstance(WTUPCP_HistoryFragment.this.context).getAppDatabase().reportDao().delete(this.reportEntity);
            return null;
        }

        
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            WTUPCP_HistoryFragment.this.context.sendBroadcast(new WTUPCP_IntentFactory(WTUPCP_HistoryFragment.this.context).getUpdateUiIntent(true));
        }
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

    private class getData extends AsyncTask<Void, Void, Void> {
        private getData() {
        }

        
        public Void doInBackground(Void... voidArr) {
            List<WTUPCP_ReportEntity> allData = WTUPCP_DatabaseClient.getInstance(WTUPCP_HistoryFragment.this.context).getAppDatabase().reportDao().getAllData();
            WTUPCP_HistoryFragment.this.reportEntityList.clear();
            WTUPCP_HistoryFragment.this.reportEntityList.addAll(allData);
            Collections.reverse(WTUPCP_HistoryFragment.this.reportEntityList);
            return null;
        }

        
        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            WTUPCP_HistoryFragment.this.checkEmptyOrNot();
            WTUPCP_HistoryFragment.this.historyAdapter.notifyDataSetChanged();
        }
    }

    private void clickListener() {
        this.ivBack.setOnClickListener(this);
        this.ivSort.setOnClickListener(this);
    }

    private void inIt(View view) {
        this.ivBack = (ImageView) view.findViewById(R.id.ivBack);
        this.ivSort = (ImageView) view.findViewById(R.id.ivSort);
        this.llEmpty = (LinearLayout) view.findViewById(R.id.llEmpty);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvHistory);
        this.rvHistory = recyclerView;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                if (i2 > 0) {
                    WTUPCP_HomeActivity.mutableLiveData.setValue(0);
                } else {
                    WTUPCP_HomeActivity.mutableLiveData.setValue(1);
                }
            }
        });
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ivBack) {
            this.rvHistory.getLayoutManager().scrollToPosition(0);
            this.onHistoryClick.onHistoryClickBack();
        } else if (id == R.id.ivSort) {
            this.onHistoryClick.OnSortClick(this.sort);
        }
    }

    public void onAttach(Activity activity2) {
        super.onAttach(activity2);
        try {
            this.onHistoryClick = (WTUPCP_OnHistoryClick) activity2;
        } catch (Exception unused) {
        }
    }
}
