package fr.ludovic.vimont.carouselviewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CarouselViewPager extends ViewPager {
    private int duration = 2250;
    private boolean animationNotStarted = true;

    private Animation animation;
    private DisplayMetrics metrics;
    private SpeedScroller mScroller = null;

    public CarouselViewPager(Context context) {
        super(context);
        postInitViewPager();
        metrics = getContext().getResources().getDisplayMetrics();
    }

    public CarouselViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        postInitViewPager();
        metrics = getContext().getResources().getDisplayMetrics();
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







    public void setCurrentItemWhitoutScrolling(int item) {
        try {
            Field mCurItem = ViewPager.class.getDeclaredField("mCurItem");
            mCurItem.setAccessible(true);
            mCurItem.setInt(this, item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scrollingToPage(int offset)  {
        try {
            Method pageScrolled = ViewPager.class.getDeclaredMethod("pageScrolled", int.class);
            pageScrolled.setAccessible(true);
            pageScrolled.invoke(this, offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startAnimation(boolean endOfQuestion, Animation.AnimationListener listener) {
        animationNotStarted = false;
        int chosenX = (metrics.widthPixels+400)*3;
        animation = (endOfQuestion) ? new ScrollToAnimation(this, endOfQuestion, 0, chosenX, duration) : new ScrollToAnimation(this, endOfQuestion, chosenX * 0.8f, 0, duration);
        animation.setAnimationListener(listener);
        invalidate();
    }

    private Canvas enterAnimation(final Canvas c) {
        animationNotStarted = true;
        startAnimation(animation);
        return c;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!animationNotStarted) {
            canvas = enterAnimation(canvas);
        }
        super.onDraw(canvas);
    }
}