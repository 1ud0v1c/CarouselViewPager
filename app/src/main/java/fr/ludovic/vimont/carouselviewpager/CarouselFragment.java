package fr.ludovic.vimont.carouselviewpager;

import android.content.Context;
import android.os.Bundle;
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

public class CarouselFragment extends Fragment {
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        final ScaledFrameLayout root = (ScaledFrameLayout) inflater.inflate(R.layout.item_carousel, container, false);
        root.setScaleBoth(getArguments().getFloat("scale"));
        root.setTag("view" + getArguments().getInt("position"));
        computePadding(root);

        final ImageView imageView = (ImageView) root.findViewById(R.id.image);
        imageView.setImageResource(getArguments().getInt("image"));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarouselViewPager carousel = (CarouselViewPager) getActivity().findViewById(R.id.carousel);
                carousel.setCurrentItem(getArguments().getInt("position"), true);
            }
        });

        final TextView labelView = (TextView) root.findViewById(R.id.label);
        labelView.setText(getArguments().getString("title"));

        final RelativeLayout descriptionLayout = (RelativeLayout) root.findViewById(R.id.description_layout);
        final ImageButton backButton = (ImageButton) root.findViewById(R.id.back_button);
        final TextView descriptionView = (TextView) root.findViewById(R.id.description_view);

        final ImageButton infoButton = (ImageButton) root.findViewById(R.id.info_button);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View infoView) {
                opacityAnimation(descriptionLayout, 0.0f, 1.0f, 750, true, new Animation.AnimationListener() {
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
                                opacityAnimation(descriptionLayout, 1.0f, 0.0f, 750, true, new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {}

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        backView.setEnabled(false);
                                        infoView.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {}
                                });
                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
            }
        });

        return root;
    }

    private void computePadding(final ViewGroup rootLayout) {
        rootLayout.post(new Runnable() {
            @Override
            public void run() {
                CarouselViewPager carousel = (CarouselViewPager) getActivity().findViewById(R.id.carousel);
                int width = rootLayout.getWidth();
                int paddingWidth = (int) (width * (1-carousel.getPageWidth())/2);
                rootLayout.setPadding(paddingWidth, 0, paddingWidth, 0);
                carousel.setPageMargin(-(paddingWidth - carousel.getPaddingBetweenItem()) * 2);
            }
        });
    }

    private void opacityAnimation(final View view, float fromAlpha, float toAlpha, int duration, boolean keepResult, Animation.AnimationListener listener){
        Animation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        if(keepResult) alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setAnimationListener(listener);
        view.startAnimation(alphaAnimation);
    }
}