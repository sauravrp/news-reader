package com.test.news.model;

import com.google.gson.annotations.Expose;
import com.test.news.MediaList;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by sauravpandey on 2/23/15.
 */
public class News
{
    @Expose
    public String url;
    @Expose
    public String title;
    @Expose
    public String byline;

    @Expose
    public MediaList media;

}
