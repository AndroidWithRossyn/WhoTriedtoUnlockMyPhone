package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_adapters;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_activity.WTUPCP_ViewIntruderActivity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_database.entity.WTUPCP_ReportEntity;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_interfaces.WTUPCP_OnHistoryClick;
import com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_utils.WTUPCP_Constants;
import java.util.Date;
import java.util.List;

public class WTUPCP_HistoryAdapter extends RecyclerView.Adapter<WTUPCP_HistoryAdapter.HistoryViewHolder> {
    Context context;
    DisplayMetrics displayMetrics;
    WTUPCP_OnHistoryClick onHistoryClick;
    List<WTUPCP_ReportEntity> reportEntityList;
    int width;

    public WTUPCP_HistoryAdapter(Context context2, List<WTUPCP_ReportEntity> list, WTUPCP_OnHistoryClick wTUPCP_OnHistoryClick) {
        this.context = context2;
        this.reportEntityList = list;
        this.onHistoryClick = wTUPCP_OnHistoryClick;
        DisplayMetrics displayMetrics2 = context2.getResources().getDisplayMetrics();
        this.displayMetrics = displayMetrics2;
        this.width = displayMetrics2.widthPixels / 2;
    }

    public HistoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new HistoryViewHolder(LayoutInflater.from(this.context).inflate(R.layout.wtupcp_layout_history_item, viewGroup, false));
    }

    public void onBindViewHolder(HistoryViewHolder historyViewHolder, int i) {
        historyViewHolder.llRoot.getLayoutParams().height = this.width;
        final WTUPCP_ReportEntity wTUPCP_ReportEntity = this.reportEntityList.get(i);
        ((RequestBuilder) Glide.with(this.context).load(wTUPCP_ReportEntity.getPHOTO_PATH()).placeholder((int) R.drawable.placeholder)).into(historyViewHolder.ivImage);
        historyViewHolder.tvTime.setText(WTUPCP_Constants.simpleDateFormat2.format(new Date(wTUPCP_ReportEntity.getREPORT_TIME())));
        if (wTUPCP_ReportEntity.isDEVICE_UNLOCK_FAIL()) {
            historyViewHolder.tvStatus.setText(this.context.getResources().getString(R.string.successful_attempt));
            historyViewHolder.tvStatus.setTextColor(ContextCompat.getColor(this.context, R.color.success_color));
        } else {
            historyViewHolder.tvStatus.setText(this.context.getResources().getString(R.string.fail_attempt));
            historyViewHolder.tvStatus.setTextColor(ContextCompat.getColor(this.context, R.color.fail_color));
        }
        historyViewHolder.ivMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                WTUPCP_HistoryAdapter.this.onHistoryClick.onMenuClick(view, wTUPCP_ReportEntity);
            }
        });
        historyViewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(WTUPCP_HistoryAdapter.this.context, WTUPCP_ViewIntruderActivity.class);
                intent.putExtra(WTUPCP_Constants.REPORT_ENTITY, wTUPCP_ReportEntity);
                WTUPCP_HistoryAdapter.this.context.startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return this.reportEntityList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        ImageView ivMenu;
        LinearLayout llRoot;
        TextView tvStatus;
        TextView tvTime;

        public HistoryViewHolder(View view) {
            super(view);
            this.ivImage = (ImageView) view.findViewById(R.id.ivImage);
            this.ivMenu = (ImageView) view.findViewById(R.id.ivMenu);
            this.tvTime = (TextView) view.findViewById(R.id.tvTime);
            this.tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            this.llRoot = (LinearLayout) view.findViewById(R.id.llRoot);
        }
    }
}
