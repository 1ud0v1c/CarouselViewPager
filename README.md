# CarouselViewPager

A simple Coverflow widget based on a ViewPager. The provided example use coffee illustrations :3.

![CarouselViewPager example](https://github.com/1ud0v1c/CarouselViewPager/raw/master/screenshots/carousel_example.png "")

![CarouselViewPager description](https://github.com/1ud0v1c/CarouselViewPager/raw/master/screenshots/carousel_description.png "")

![CarouselViewPager animation](https://github.com/1ud0v1c/CarouselViewPager/raw/master/screenshots/carousel_animation.gif "")

## Features :

- The current page is pushed forward the other items thanks to a scaling.
- An animation is available onStart of the Activity
- A padding is applied to be able to have 3 pages displayed at once.
- You can control the speed of the scrolling

## Example of initialisation :

```java
CarouselViewPager carousel = (CarouselViewPager) findViewById(R.id.carousel);
ArrayList<Entity> entities = buildData();
CarouselAdapter carouselAdapter = new CarouselAdapter(this, carousel, getSupportFragmentManager(), entities);

carousel.setAdapter(carouselAdapter);
carousel.addOnPageChangeListener(carouselAdapter);
carousel.setOffscreenPageLimit(entities.size());
carousel.setClipToPadding(false);

carousel.setScrollDurationFactor(1.5f);
carousel.setPageWidth(0.55f);
carousel.settPaddingBetweenItem(16);
```

