package com.test.news.activities.common;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.test.news.R;
import com.test.news.network.NewsSpiceService;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by sauravpandey on 2/23/15.
 */
public abstract class BaseActivity extends Activity
{

    private SpiceManager mSpiceManager = new SpiceManager(NewsSpiceService.class);

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null)
        {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, createFragment())
                    .commit();
        }
    }


    protected abstract Fragment createFragment();

    @Override
    protected void onStart()
    {
        if(!mSpiceManager.isStarted())
            mSpiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        if(mSpiceManager.isStarted())
            mSpiceManager.shouldStop();
        super.onStop();
    }

    public SpiceManager getmSpiceManager() {
        return mSpiceManager;
    }

}
