package fr.ludovic.vimont.carouselviewpager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CarouselFragment extends Fragment {
    private int paddingBetweenItem = 8;

    public static Fragment newInstance(Context context, Entity entity, int position, float scale) {
        Bundle b = new Bundle();
        b.putInt("position", position);
        b.putInt("image", entity.imageResId);
        b.putString("title", entity.titleRes);
        b.putFloat("scale", scale);
        return Fragment.instantiate(context, CarouselFragment.class.getName(), b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        final ScaledFrameLayout root = (ScaledFrameLayout) inflater.inflate(R.layout.item_carousel, container, false);
        root.setScaleBoth(getArguments().getFloat("scale"));
        root.setTag("view" + getArguments().getInt("position"));
        root.post(new Runnable() {
            @Override
            public void run() {
                CarouselViewPager carousel = (CarouselViewPager) getActivity().findViewById(R.id.carousel);
                int width = root.getWidth();
                int paddingWidth = (int) (width * 0.2);
                root.setPadding(paddingWidth, 0, paddingWidth, 0);
                carousel.setPageMargin(-(paddingWidth - paddingBetweenItem) * 2);
            }
        });

        final ImageView imageView = (ImageView) root.findViewById(R.id.image);
        imageView.setImageResource(getArguments().getInt("image"));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("onClick", "tag : "+"view" + getArguments().getInt("position")+", position : "+getArguments().getInt("position"));
                CarouselViewPager carousel = (CarouselViewPager) getActivity().findViewById(R.id.carousel);
                carousel.setCurrentItem(getArguments().getInt("position"), true);
            }
        });

        final TextView labelView = (TextView) root.findViewById(R.id.label);
        labelView.setText(getArguments().getString("title"));

        return root;
    }
}