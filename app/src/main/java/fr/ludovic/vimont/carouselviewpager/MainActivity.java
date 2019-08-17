package fr.ludovic.vimont.carouselviewpager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;

import java.util.ArrayList;

import fr.ludovic.vimont.carouselviewpager.model.Entity;
import fr.ludovic.vimont.carouselviewpager.view.CarouselViewPager;

public class MainActivity extends AppCompatActivity {
    private CarouselViewPager carouselViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carouselViewPager = findViewById(R.id.carousel);
        ArrayList<Entity> entities = buildData();
        final CarouselAdapter carouselAdapter = new CarouselAdapter(this, carouselViewPager, getSupportFragmentManager(), entities);

        carouselViewPager.setAdapter(carouselAdapter);
        carouselViewPager.addOnPageChangeListener(carouselAdapter);
        carouselViewPager.setOffscreenPageLimit(entities.size());
        carouselViewPager.setClipToPadding(false);

        carouselViewPager.setScrollDurationFactor(1.5f);
        // Used for the animation we hide the ViewPager until the animation start
        carouselViewPager.setAlpha(0.0f);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // As soon as, we have the focus we launch the animation
            carouselViewPager.startAnimation(false, new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // And we display the widget to see it
                    carouselViewPager.setAlpha(1.0f);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    private ArrayList<Entity> buildData() {
        ArrayList<Entity> entities = new ArrayList<>();
        entities.add(new Entity(R.drawable.americano, getString(R.string.americano_title), getString(R.string.americano_description)));
        entities.add(new Entity(R.drawable.cappucino, getString(R.string.cappucino_title), getString(R.string.cappucino_description)));
        entities.add(new Entity(R.drawable.latte, getString(R.string.latte_title), getString(R.string.latte_description)));
        entities.add(new Entity(R.drawable.mocha, getString(R.string.mocha_title), getString(R.string.mocha_description)));
        entities.add(new Entity(R.drawable.ristretto, getString(R.string.ristretto_title), getString(R.string.ristretto_description)));
        entities.add(new Entity(R.drawable.short_coffee, getString(R.string.short_coffee_title), getString(R.string.short_coffee_description)));
        entities.add(new Entity(R.drawable.vienna, getString(R.string.vienna_title), getString(R.string.vienna_description)));
        return entities;
    }
}