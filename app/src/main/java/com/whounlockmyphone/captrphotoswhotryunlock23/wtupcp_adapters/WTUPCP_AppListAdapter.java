package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_AppListEntity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import java.util.Date;
import java.util.List;

public class WTUPCP_AppListAdapter extends RecyclerView.Adapter<WTUPCP_AppListAdapter.AppListViewHolder> {
    List<WTUPCP_AppListEntity> appListEntityList;
    Context context;

    public WTUPCP_AppListAdapter(Context context2, List<WTUPCP_AppListEntity> list) {
        this.context = context2;
        this.appListEntityList = list;
    }

    public AppListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AppListViewHolder(LayoutInflater.from(this.context).inflate(R.layout.wtupcp_layout_applist_item, viewGroup, false));
    }

    public void onBindViewHolder(AppListViewHolder appListViewHolder, int i) {
        WTUPCP_AppListEntity wTUPCP_AppListEntity = this.appListEntityList.get(i);
        try {
            ((RequestBuilder) Glide.with(this.context).load(this.context.getPackageManager().getApplicationIcon(wTUPCP_AppListEntity.getPACKAGE_NAME())).placeholder((int) R.mipmap.ic_launcher)).into(appListViewHolder.ivAppIcon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        appListViewHolder.tvAppName.setText(wTUPCP_AppListEntity.getAPP_NAME());
        appListViewHolder.tvAppTime.setText(WTUPCP_Constants.simpleDateFormat3.format(new Date(wTUPCP_AppListEntity.getAPP_TIME())));
    }

    public int getItemCount() {
        return this.appListEntityList.size();
    }

    public class AppListViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAppIcon;
        TextView tvAppName;
        TextView tvAppTime;

        public AppListViewHolder(View view) {
            super(view);
            this.ivAppIcon = (ImageView) view.findViewById(R.id.ivAppIcon);
            this.tvAppName = (TextView) view.findViewById(R.id.tvAppName);
            this.tvAppTime = (TextView) view.findViewById(R.id.tvAppTime);
        }
    }
}
