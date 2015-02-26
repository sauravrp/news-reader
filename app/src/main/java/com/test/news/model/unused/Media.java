package com.test.news.model.unused;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.test.news.model.MediaMetaData;

import java.util.List;

/**
 * Created by sauravpandey on 2/23/15.
 */
public class Media
{

    @SerializedName("media-metadata")
    @Expose()
    public List<MediaMetaData> mediaMetdataList;
}
