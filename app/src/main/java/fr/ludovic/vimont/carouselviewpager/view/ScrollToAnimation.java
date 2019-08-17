package fr.ludovic.vimont.carouselviewpager.view;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import java.util.Calendar;

public class ScrollToAnimation extends Animation {
    private boolean beginAnimation;
    private int currentIndex = 0;
    private int deltaT = 0;
    private int numberOfChild = -1;
    private float fromX, toX;
    private long animationStart;

    private CarouselViewPager viewpager;

    public ScrollToAnimation(final CarouselViewPager viewpager, boolean beginAnimation, float fromX, float toX, int duration) {
        this.viewpager = viewpager;
        this.fromX = fromX;
        this.toX = toX;
        this.beginAnimation = beginAnimation;

        numberOfChild = viewpager.getChildCount();
        deltaT = duration / numberOfChild;

        setDuration(duration);
        animationStart = Calendar.getInstance().getTimeInMillis();
        setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        int offset = (beginAnimation) ? (int) (-toX * interpolatedTime) : (int) (-fromX * interpolatedTime + fromX);

        if(!beginAnimation) {
            long animationProgression = Calendar.getInstance().getTimeInMillis() - animationStart;
            currentIndex = (int) (animationProgression/deltaT);
            if(currentIndex != viewpager.getCurrentItem()) {
                viewpager.setCurrentItemWithoutScrolling(offset);
            }
        }
        viewpager.scrollingToPage(offset);
        viewpager.scrollTo(offset, 0);
    }
}