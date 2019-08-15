package fr.ludovic.vimont.carouselviewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;

import fr.ludovic.vimont.carouselviewpager.model.Entity;
import fr.ludovic.vimont.carouselviewpager.view.CarouselViewPager;
import fr.ludovic.vimont.carouselviewpager.view.ScaledFrameLayout;

public class CarouselAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
    private final static float BIG_SCALE = 1.0f;
    private final static float SMALL_SCALE = 0.90f;
    private final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    private float scale;
    private CarouselViewPager carousel;

    private Context context;
    private FragmentManager fragmentManager;
    private ArrayList<Entity> entities;
    private ScaledFrameLayout currentLayout = null;
    private ScaledFrameLayout nextLayout = null;


    public CarouselAdapter(Context context, CarouselViewPager carousel, FragmentManager fragmentManager, ArrayList<Entity> mData) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.carousel = carousel;
        this.entities = mData;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            scale = BIG_SCALE;
        } else {
            scale = SMALL_SCALE;
        }
        return CarouselFragment.newInstance(context, entities.get(position), position, scale);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset >= 0f && positionOffset <= 1f) {
            currentLayout = getRootView(position);
            currentLayout.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset);

            if (position < entities.size() - 1) {
                nextLayout = getRootView(position + 1);
                nextLayout.setScaleBoth(SMALL_SCALE + DIFF_SCALE * positionOffset);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onPageSelected(int position) {
        Log.w("onPageSelected", "position : " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private ScaledFrameLayout getRootView(int position) {
        Fragment fragment = fragmentManager.findFragmentByTag(this.getFragmentTag(position));
        return (ScaledFrameLayout) fragment.getView().findViewById(R.id.rootItem);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + carousel.getId() + ":" + position;
    }

    public static float getSmallScale() {
        return SMALL_SCALE;
    }
}