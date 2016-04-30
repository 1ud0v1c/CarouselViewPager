package fr.ludovic.vimont.carouselviewpager;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import java.util.Calendar;

public class ScrollToAnimation extends Animation {
    private int currentIndex = 0, nbChilds = -1, deltaT = 0;
    private float fromX, toX;
    private long animationStart;
    private CarouselViewPager viewpager;
    private boolean endOfQuestion;

    public ScrollToAnimation(final CarouselViewPager viewpager, boolean endOfQuestion, float fromX, float toX, int duration) {
        this.viewpager = viewpager;
        this.fromX = fromX;
        this.toX = toX;
        this.endOfQuestion = endOfQuestion;

        nbChilds = viewpager.getChildCount();
        deltaT = duration / nbChilds;

        setDuration(duration);
        animationStart = Calendar.getInstance().getTimeInMillis();
        setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        int offset = (endOfQuestion) ? (int) (-toX * interpolatedTime) : (int) (-fromX * interpolatedTime + fromX);

        if(!endOfQuestion) {
            long animationProgression = Calendar.getInstance().getTimeInMillis() - animationStart;
            currentIndex = (int) (animationProgression/deltaT);
            if(currentIndex != viewpager.getCurrentItem()) {
                viewpager.setCurrentItemWhitoutScrolling(offset);
            }
        }
        viewpager.scrollingToPage(offset);
        viewpager.scrollTo(offset, 0);
    }
}