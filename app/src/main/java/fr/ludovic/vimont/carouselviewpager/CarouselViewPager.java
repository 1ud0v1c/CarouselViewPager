package fr.ludovic.vimont.carouselviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CarouselViewPager extends ViewPager {
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
}