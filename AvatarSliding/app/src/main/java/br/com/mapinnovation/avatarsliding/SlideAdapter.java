package br.com.mapinnovation.avatarsliding;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    private ImageView slideImageView;

    public SlideAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.cap_1,
            R.drawable.cap_2,
            R.drawable.cap_3,
            R.drawable.cap_4,
            R.drawable.cap_5,
            R.drawable.cap_7,
            R.drawable.cap_8,
            R.drawable.cap_9,
            R.drawable.cap_10,
            R.drawable.cap_11,
            R.drawable.cap_12,
            R.drawable.cap_13,
            R.drawable.cap_14,
            R.drawable.cap_15,
            R.drawable.cap_15_1,
            R.drawable.cap_16,
            R.drawable.cap_17,
            R.drawable.cap_18,
            R.drawable.cap_19,
            R.drawable.cap_20,
            R.drawable.cap_21,
            R.drawable.cap_22,
            R.drawable.cap_23,
            R.drawable.cap_24,
            R.drawable.cap_25,
            R.drawable.cap_26,
            R.drawable.cap_27,
            R.drawable.cap_28,
            R.drawable.cap_29,
            R.drawable.cap_30,
            R.drawable.cap_31
    };


    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        slideImageView = view.findViewById(R.id.slide_image);
        slideImageView.setImageResource(slide_images[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);

    }
}
