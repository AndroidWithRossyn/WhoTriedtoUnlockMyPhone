package com.whounlockmyphone.captrphotoswhotryunlock23.wtupcp_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.whounlockmyphone.captrphotoswhotryunlock23.R;

public class WTUPCP_CustomSwitchView extends LinearLayout {
    Bitmap bitmap1;
    int canvasHeight;
    int canvasWidth;
    Context context;
    ImageView imageViewBottom;
    ImageView imageViewTop;
    RelativeLayout.LayoutParams layoutParamsBottom;
    RelativeLayout.LayoutParams layoutParamsTop;
    SwitchMode switchMode;

    enum SwitchMode {
        ON,
        OFF
    }

    public WTUPCP_CustomSwitchView(Context context2) {
        super(context2);
        inIt(context2);
    }

    public WTUPCP_CustomSwitchView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        inIt(context2);
    }

    public WTUPCP_CustomSwitchView(Context context2, AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i);
        inIt(context2);
    }

    public WTUPCP_CustomSwitchView(Context context2, AttributeSet attributeSet, int i, int i2) {
        super(context2, attributeSet, i, i2);
    }

    private void inIt(Context context2) {
        this.layoutParamsTop = new RelativeLayout.LayoutParams(-1, -1);
        this.layoutParamsBottom = new RelativeLayout.LayoutParams(-1, -1);
        this.context = context2;
        this.imageViewTop = new ImageView(context2);
        this.imageViewBottom = new ImageView(context2);
        this.switchMode = SwitchMode.OFF;
        this.imageViewTop.setBackgroundResource(R.drawable.ic_switch_on);
        this.imageViewBottom.setBackgroundResource(R.drawable.ic_switch_off);
        this.bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_switch_on);
        addView(this.imageViewBottom, this.layoutParamsBottom);
    }

    
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.canvasWidth = i;
        this.canvasHeight = i2;
    }

    
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap.createBitmap(this.bitmap1, 0, 0, this.canvasWidth, this.canvasHeight);
    }
}
