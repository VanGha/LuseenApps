package com.luseen.vanik.luseenapp.Activities.Fragments.Adapters;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.luseen.vanik.luseenapp.Activities.Fragments.LogRegFragments.LoginFragment;
import com.luseen.vanik.luseenapp.Activities.Fragments.LogRegFragments.RecoveryFragment;
import com.luseen.vanik.luseenapp.Activities.Fragments.LogRegFragments.RegisterFragment;
import com.luseen.vanik.luseenapp.Interfaces.AppConstants;

public class LogRegPageAdapter extends FragmentPagerAdapter {

    private Context context;
    private String[] tabs;

    public LogRegPageAdapter(Context context, FragmentManager fm) {
        super(fm);

        this.context = context;

        tabs = new String[]{AppConstants.TITTLE_LOGIN, AppConstants.TITTLE_REGISTER,
                AppConstants.TITTLE_RECOVERY_PASSWORD};

    }

    @Override
    public Fragment getItem(int position) {

        switch (tabs[position]) {

            case AppConstants.TITTLE_LOGIN: {
                return LoginFragment.newInstance();
            }

            case AppConstants.TITTLE_REGISTER: {
                return RegisterFragment.newInstance();
            }

            case AppConstants.TITTLE_RECOVERY_PASSWORD: {
                return RecoveryFragment.newInstance();
            }

            default: {
                return LoginFragment.newInstance();
            }

        }

    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
