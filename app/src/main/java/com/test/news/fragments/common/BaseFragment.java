package com.test.news.fragments.common;

import android.app.Fragment;
import android.os.Bundle;

import com.test.news.activities.common.BaseActivity;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by sauravpandey on 2/23/15.
 */
public class BaseFragment extends Fragment
{
    BaseActivity mBaseActivity;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if(getActivity() != null && getActivity() instanceof BaseActivity)
        {
            mBaseActivity = (BaseActivity) getActivity();
        }
    }

    protected SpiceManager getSpiceManager()
    {
        if(mBaseActivity != null)
        {
            return mBaseActivity.getmSpiceManager();
        }
        else
        {
            // log error
            return null;
        }
    }

    @Override
    public void onDetach()
    {
        mBaseActivity = null;
        super.onDetach();
    }
}
