package com.luseen.vanik.luseenapp.Activities.Fragments.Adapters;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.luseen.vanik.luseenapp.Activities.Fragments.LogRegFragments.LoginFragment;
import com.luseen.vanik.luseenapp.Activities.Fragments.LogRegFragments.RecoveryFragment;
import com.luseen.vanik.luseenapp.Activities.Fragments.LogRegFragments.RegisterFragment;
import com.luseen.vanik.luseenapp.Interfaces.AppConstants;
import com.luseen.vanik.luseenapp.R;

public class LogRegPageAdapter extends FragmentPagerAdapter {

    private String[] tabs;

    private String tittleLogin;
    private String tittleRegister;
    private String tittleRecoveryPassword;

    public LogRegPageAdapter(Context context, FragmentManager fm) {
        super(fm);

        tittleLogin = context.getResources().getString(R.string.login_tittle);
        tittleRegister = context.getResources().getString(R.string.register_tittle);
        tittleRecoveryPassword = context.getResources().getString(R.string.recovery_tittle);

        tabs = new String[]{tittleLogin, tittleRegister, tittleRecoveryPassword};

    }

    @Override
    public Fragment getItem(int position) {

        if (tabs[position].equals(tittleLogin)) {
            return LoginFragment.newInstance();
        } else if (tabs[position].equals(tittleRegister)) {
            return RegisterFragment.newInstance();
        } else if (tabs[position].equals(tittleRecoveryPassword)) {
            return RecoveryFragment.newInstance();
        } else {
            return LoginFragment.newInstance();
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
