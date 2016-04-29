package fr.ludovic.vimont.carouselviewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class ScaledFrameLayout extends FrameLayout {
    private float scale = 1.0f;

    public ScaledFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaledFrameLayout(Context context) {
        super(context);
    }

    public void setScaleBoth(float scale) {
        this.scale = scale;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.scale(scale, scale, getWidth() / 2, getHeight() / 2);
    }
}