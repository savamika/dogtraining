/*
package com.healthcare.dogtraining.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.healthcare.dogtraining.Model.HomeSlidermodel;
import com.healthcare.dogtraining.R;
import com.healthcare.dogtraining.Session1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.healthcare.dogtraining.API.Base_Url.gethomelsiderpath;

public class HomeSlider_adapter extends PagerAdapter {
    private List<HomeSlidermodel> allProductListModels;
    private ArrayList<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    HashMap<String, String> file_maps;
    String img1,img2,img3;
    Session1 session1;
    final Handler handler = new Handler();
    public Timer swipeTimer ;
    public HomeSlider_adapter(Context context, List<HomeSlidermodel> allProductListModels) {
        this.context = context;
        this.allProductListModels = allProductListModels;
        inflater = LayoutInflater.from(context);

    }

    public void setTimer(final ViewPager myPager, int time){
        final int size =allProductListModels.size();


        final Runnable Update = new Runnable() {
            int NUM_PAGES =size;
            int currentPage = 0 ;
            public void run() {
                if (currentPage == NUM_PAGES ) {
                    currentPage = 0;
                }
                myPager.setCurrentItem(currentPage++, true);
            }
        };

        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000, time*1000);
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }



    @Override
    public int getCount() {
        return allProductListModels.size();

    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.home_slider_row, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.slider_image);
        final Session1 session1 =new Session1(context);

        Glide.with(context)
                .load(gethomelsiderpath + allProductListModels.get(position).getImage())
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






*/
