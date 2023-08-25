package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_view;

import android.content.Context;
import android.util.AttributeSet;
import androidx.cardview.widget.CardView;

public class WTUPCP_SquareLayout extends CardView {
    public WTUPCP_SquareLayout(Context context) {
        super(context);
    }

    public WTUPCP_SquareLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public WTUPCP_SquareLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(i, i);
    }
}
