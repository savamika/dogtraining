package com.healthcare.dogtraining.ui.MYPROFILE;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.healthcare.dogtraining.ui.Completed.CompletedFragment;
import com.healthcare.dogtraining.ui.Pending.PendingFragment;
import com.healthcare.dogtraining.ui.Ship.ShipFragment;
import com.healthcare.dogtraining.ui.Upcoming.UpcomingFragment;

public class TabsAdapter extends FragmentPagerAdapter {
    int totalTabs;
    private Context context;
    String user_id;

    public TabsAdapter(FragmentManager fm, Context context, int totalTabs, String user_id) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
        this.user_id=user_id;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                PendingFragment fragment22 = new PendingFragment();
                return fragment22;
            case 1:
                ShipFragment fragment33 = new ShipFragment();
                return fragment33;
            case 2:
                CompletedFragment fragment44 = new CompletedFragment();
                return fragment44;
            default:
                return null;
        }
        }
    @Override
    public int getCount() {
        return totalTabs;
    }
}
