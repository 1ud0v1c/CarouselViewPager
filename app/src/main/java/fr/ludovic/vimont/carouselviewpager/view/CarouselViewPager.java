package fr.ludovic.vimont.carouselviewpager.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CarouselViewPager extends ViewPager {
    private static final int DEFAULT_PADDING_BETWEEN_ITEM = 16;
    private static final int DEFAULT_ANIMATION_DURATION = 2500;

    private Animation animation;
    private int animationDurationInMs = DEFAULT_ANIMATION_DURATION;
    private boolean animationStarted = true;

    private float pageWidth = 0.6f;
    private int paddingBetweenItem = DEFAULT_PADDING_BETWEEN_ITEM;
    private SpeedScroller mScroller = null;

    public CarouselViewPager(Context context) {
        super(context);
        postInitViewPager();
    }

    public CarouselViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        postInitViewPager();
    }

    private void postInitViewPager() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = viewpager.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new SpeedScroller(getContext(), (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        } catch (Exception e) {
            Log.e("postInitViewPager", e.getMessage());
        }
    }

    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        try {
            Method method = ViewPager.class.getDeclaredMethod("setCurrentItemInternal", int.class, boolean.class, boolean.class, int.class);
            method.setAccessible(true);
            method.invoke(this, item, smoothScroll, false, 1500);
        } catch (Exception e) {
            e.printStackTrace();
            super.setCurrentItem(item, smoothScroll);
        }
    }

    public void setCurrentItemWithoutScrolling(int item) {
        try {
            Field mCurItem = ViewPager.class.getDeclaredField("mCurItem");
            mCurItem.setAccessible(true);
            mCurItem.setInt(this, item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scrollingToPage(int offset) {
        try {
            Method pageScrolled = ViewPager.class.getDeclaredMethod("pageScrolled", int.class);
            pageScrolled.setAccessible(true);
            pageScrolled.invoke(this, offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startAnimation(boolean arrived, Animation.AnimationListener listener) {
        animationStarted = false;
        int desiredPosition = (int) (getChildAt(0).getWidth() / 1.5f * (getChildCount()));
        if (arrived) {
            animation = new ScrollToAnimation(this, arrived, 0, desiredPosition, animationDurationInMs);
        } else {
            animation = new ScrollToAnimation(this, arrived, desiredPosition, 0, animationDurationInMs);
        }
        animation.setAnimationListener(listener);
        invalidate();
    }

    private Canvas enterAnimation(final Canvas c) {
        animationStarted = true;
        startAnimation(animation);
        return c;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!animationStarted) {
            canvas = enterAnimation(canvas);
        }
        super.onDraw(canvas);
    }

    public float getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(float pageWidth) {
        this.pageWidth = pageWidth;
    }

    public int getPaddingBetweenItem() {
        return paddingBetweenItem;
    }

    public void setPaddingBetweenItem(int paddingBetweenItem) {
        this.paddingBetweenItem = paddingBetweenItem;
    }
}