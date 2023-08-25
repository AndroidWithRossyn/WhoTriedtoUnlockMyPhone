package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_ReportEntity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_interfaces.WTUPCP_OnReportCLick;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import java.util.Date;
import java.util.List;

public class WTUPCP_RecentActivityAdapter extends RecyclerView.Adapter<WTUPCP_RecentActivityAdapter.RecentActivityViewHolder> {
    Context context;
    WTUPCP_OnReportCLick onReportCLick;
    List<WTUPCP_ReportEntity> reportEntityList;

    public WTUPCP_RecentActivityAdapter(Context context2, List<WTUPCP_ReportEntity> list, WTUPCP_OnReportCLick wTUPCP_OnReportCLick) {
        this.context = context2;
        this.reportEntityList = list;
        this.onReportCLick = wTUPCP_OnReportCLick;
    }

    public RecentActivityViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecentActivityViewHolder(LayoutInflater.from(this.context).inflate(R.layout.wtupcp_layout_recent_activity_item, viewGroup, false));
    }

    public void onBindViewHolder(RecentActivityViewHolder recentActivityViewHolder, int i) {
        final WTUPCP_ReportEntity wTUPCP_ReportEntity = this.reportEntityList.get(i);
        ((RequestBuilder) Glide.with(this.context).load(wTUPCP_ReportEntity.getPHOTO_PATH()).placeholder((int) R.drawable.placeholder)).into(recentActivityViewHolder.ivImage);
        recentActivityViewHolder.tvTime.setText(WTUPCP_Constants.simpleDateFormat2.format(new Date(wTUPCP_ReportEntity.getREPORT_TIME())));
        recentActivityViewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                WTUPCP_RecentActivityAdapter.this.onReportCLick.onClick(wTUPCP_ReportEntity);
            }
        });
    }

    public int getItemCount() {
        return this.reportEntityList.size();
    }

    public class RecentActivityViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTime;

        public RecentActivityViewHolder(View view) {
            super(view);
            this.ivImage = (ImageView) view.findViewById(R.id.ivImage);
            this.tvTime = (TextView) view.findViewById(R.id.tvTime);
        }
    }
}
