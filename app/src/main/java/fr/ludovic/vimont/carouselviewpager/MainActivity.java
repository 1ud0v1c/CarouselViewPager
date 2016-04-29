package fr.ludovic.vimont.carouselviewpager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private CarouselViewPager carousel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Entity> entities = buildData();

        carousel = (CarouselViewPager) findViewById(R.id.carousel);
        CarouselAdapter carouselAdapter = new CarouselAdapter(this, carousel, getSupportFragmentManager(), entities);
        carousel.setAdapter(carouselAdapter);
        carousel.addOnPageChangeListener(carouselAdapter);
        carousel.setOffscreenPageLimit(entities.size());
        carousel.setClipToPadding(false);
    }

    private ArrayList<Entity> buildData() {
        ArrayList<Entity> entities = new ArrayList<>();

        entities.add(new Entity(R.drawable.americano, "Americano"));
        entities.add(new Entity(R.drawable.cappucino, "Cappucino"));
        entities.add(new Entity(R.drawable.latte, "Latte"));
        entities.add(new Entity(R.drawable.mocha, "Mocha"));
        entities.add(new Entity(R.drawable.ristretto, "Ristretto"));
        entities.add(new Entity(R.drawable.short_coffee, "Short Coffee"));
        entities.add(new Entity(R.drawable.vienna, "Vienna"));

        return entities;
    }
}
