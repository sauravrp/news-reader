package com.test.news.ui;

import android.widget.AbsListView;

/**
 * Created by sauravpandey on 2/23/15.
 */
public abstract class InfiniteScrollListener implements AbsListView.OnScrollListener
{
  

    private int mBufferItemCount = 10;
    private int mCurrentPage = 0;
    private int mItemCount = 0;
    private boolean mIsLoading = true;



    private int mFirstVisibleItem = 0;
    private int mLastVisibleItem = 0;


    public InfiniteScrollListener(int bufferItemCount)
    {
        mBufferItemCount = bufferItemCount;
    }

    public abstract void loadMore(int page, int totalItemsCount);


    public int getFirstVisibleItem()
    {
        return mFirstVisibleItem;
    }

    public int getLastVisibleItem()
    {
        return mLastVisibleItem;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        mFirstVisibleItem = firstVisibleItem;
        mLastVisibleItem = firstVisibleItem + visibleItemCount - 1;
        if(totalItemCount < mItemCount)
        {
            this.mItemCount = totalItemCount;

            if (totalItemCount == 0) {
                this.mIsLoading = true; }
        }

        /**
         * if loading and total exceeds item count
         */

        if (mIsLoading && (totalItemCount > mItemCount)) {
            mIsLoading = false;
            mItemCount = totalItemCount;
            mCurrentPage++;
        }

        if (!mIsLoading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + mBufferItemCount)) {
            loadMore(mCurrentPage, totalItemCount);
            mIsLoading = true;
        }
    }
}
