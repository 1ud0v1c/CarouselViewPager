package fr.ludovic.vimont.carouselviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

public class CarouselAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
    private float scale;
    private MainActivity context;
    private CarouselViewPager carousel;
    private ArrayList<Entity> entities;

    public final static float BIG_SCALE = 1.0f, SMALL_SCALE = 0.90f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    private FragmentManager fragmentManager;
    private ScaledFrameLayout cur = null, next = null;

    public CarouselAdapter(MainActivity context, CarouselViewPager carousel, FragmentManager fragmentManager, ArrayList<Entity> mData) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.carousel = carousel;
        this.entities = mData;
    }

    @Override
    public Fragment getItem(int position) {
        scale = (position == 0) ? BIG_SCALE : SMALL_SCALE;
        return  CarouselFragment.newInstance(context, entities.get(position), position, scale);
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset >= 0f && positionOffset <= 1f) {
            cur = getRootView(position);
            cur.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset);

            if (position < entities.size()-1) {
                next = getRootView(position + 1);
                next.setScaleBoth(SMALL_SCALE + DIFF_SCALE * positionOffset);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onPageSelected(int position) { }

    @Override
    public void onPageScrollStateChanged(int state) { }

    private ScaledFrameLayout getRootView(int position) {
        return (ScaledFrameLayout) fragmentManager.findFragmentByTag(this.getFragmentTag(position)).getView().findViewById(R.id.rootItem);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + carousel.getId() + ":" + position;
    }
}