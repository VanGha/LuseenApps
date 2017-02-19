package com.luseen.vanik.luseenapp.Activities;

import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.luseen.vanik.luseenapp.Activities.Fragments.Adapters.LogRegPageAdapter;
import com.luseen.vanik.luseenapp.R;

public class LogRegActivity extends AppCompatActivity {

    static ViewPager tabs;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_reg);

        TabLayout logRegTabs = (TabLayout) findViewById(R.id.reg_log_tabs);
        tabs = (ViewPager) findViewById(R.id.reg_log_view_pager);
        LogRegPageAdapter logRegPageAdapter = new LogRegPageAdapter(this, getSupportFragmentManager());


        tabs.setAdapter(logRegPageAdapter);
        logRegTabs.setupWithViewPager(tabs);

        logRegTabs.setTabTextColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(android.R.color.black));

    }

    public static void scrollTabToPosition(int position) {
        tabs.setCurrentItem(position, true);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.question_want_to_leave_the_app);
        builder.setPositiveButton(R.string.sign_out, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
                System.exit(0);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.create().show();

    }
}
