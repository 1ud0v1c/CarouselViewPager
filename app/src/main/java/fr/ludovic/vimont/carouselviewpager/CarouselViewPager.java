package fr.ludovic.vimont.carouselviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.lang.reflect.Method;

public class CarouselViewPager extends ViewPager {
    public CarouselViewPager(Context context) {
        super(context);
    }

    public CarouselViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
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