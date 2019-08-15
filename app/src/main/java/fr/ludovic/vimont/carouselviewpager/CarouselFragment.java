package fr.ludovic.vimont.carouselviewpager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.ludovic.vimont.carouselviewpager.model.Entity;
import fr.ludovic.vimont.carouselviewpager.view.CarouselViewPager;
import fr.ludovic.vimont.carouselviewpager.view.ScaledFrameLayout;

public class CarouselFragment extends Fragment {
    private static final int OPACITY_ANIMATION_DURATION = 750;

    public static Fragment newInstance(Context context, Entity entity, int position, float scale) {
        Bundle b = new Bundle();
        b.putInt("image", entity.imageRes);
        b.putInt("position", position);
        b.putFloat("scale", scale);
        b.putString("title", entity.titleRes);
        b.putString("description", entity.description);
        return Fragment.instantiate(context, CarouselFragment.class.getName(), b);
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        final ScaledFrameLayout root = (ScaledFrameLayout) inflater.inflate(R.layout.item_carousel, container, false);
        root.setScaleBoth(getArguments().getFloat("scale"));
        root.setTag("view" + getArguments().getInt("position"));
        computePadding(root);

        final ImageView imageView = root.findViewById(R.id.image_view);
        imageView.setImageResource(getArguments().getInt("image"));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarouselViewPager carousel = getActivity().findViewById(R.id.carousel);
                carousel.setCurrentItem(getArguments().getInt("position"), true);
            }
        });

        final TextView labelView = root.findViewById(R.id.label);
        labelView.setText(getArguments().getString("title"));

        final RelativeLayout descriptionLayout = root.findViewById(R.id.description_layout);
        final ImageButton backButton = root.findViewById(R.id.back_button);
        final TextView descriptionView = root.findViewById(R.id.description_view);

        final ImageButton infoButton = root.findViewById(R.id.info_button);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View infoView) {
                opacityAnimation(descriptionLayout, 0.0f, 1.0f, OPACITY_ANIMATION_DURATION, true, new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        descriptionView.setText(getArguments().getString("description"));
                        backButton.setEnabled(true);
                        infoView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        backButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View backView) {
                                opacityAnimation(descriptionLayout, 1.0f, 0.0f, OPACITY_ANIMATION_DURATION, true, new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        backView.setEnabled(false);
                                        infoView.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        });

        return root;
    }

    private void computePadding(final ViewGroup rootLayout) {
        rootLayout.post(new Runnable() {
            @Override
            public void run() {
                Activity activity = getActivity();
                if (activity != null) {
                    CarouselViewPager carousel = getActivity().findViewById(R.id.carousel);
                    int width = rootLayout.getWidth();
                    int paddingWidth = Math.round(width * (1 - carousel.getPageWidth()) / 2);
                    int paddingSmallScaleWith = Math.round(paddingWidth * CarouselAdapter.getSmallScale());
                    rootLayout.setPadding(paddingSmallScaleWith, 0, paddingSmallScaleWith, 0);
                    int marginInPixels = Math.round(-(paddingWidth - carousel.getPaddingBetweenItem()) * 2);
                    carousel.setPageMargin(marginInPixels);
                }
            }
        });
    }

    private void opacityAnimation(final View view, float fromAlpha, float toAlpha, int duration, boolean keepResult, Animation.AnimationListener listener) {
        Animation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        if (keepResult) {
            alphaAnimation.setFillAfter(true);
        }
        alphaAnimation.setDuration(duration);
        alphaAnimation.setAnimationListener(listener);
        view.startAnimation(alphaAnimation);
    }
}