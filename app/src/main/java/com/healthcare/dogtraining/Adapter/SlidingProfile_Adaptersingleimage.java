package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.healthcare.dogtraining.API.Base_Url.categoryimagepasth;

public class SlidingProfile_Adaptersingleimage  extends PagerAdapter {

    public ArrayList<String> IMAGES;
    private ArrayList<String> ID;
    private LayoutInflater inflater;
    private Context context;
     Handler handler = new Handler();
    public Timer swipeTimer ;


    public SlidingProfile_Adaptersingleimage(Context context, ArrayList<String> IMAGES, ArrayList<String> ID) {
        this.context = context;
        this.IMAGES=IMAGES;
        this.ID=ID;
        inflater = LayoutInflater.from(context);
    }





    @Override
    public int getCount() {
        return  IMAGES.size();



    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }





    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.sliding_profile_layout, view, false);
        assert imageLayout != null;
        ImageView imageView = (ImageView) imageLayout.findViewById(R.id.slider_image);
        LinearLayout linearLayout = (LinearLayout) imageLayout.findViewById(R.id.l_layout);

        Glide.with(context)
                .load( categoryimagepasth+ IMAGES.get(position))
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .centerCrop()
                        .dontTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .skipMemoryCache(true)
                .into(imageView);
        view.addView(imageLayout, 0);

        return imageLayout;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}



