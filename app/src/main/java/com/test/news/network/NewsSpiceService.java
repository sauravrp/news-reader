package com.test.news.network;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.test.news.MediaList;
import com.test.news.model.MediaMetaData;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.converter.GsonConverter;

/**
 * Created by sauravpandey on 2/23/15.
 */
public class NewsSpiceService extends RetrofitGsonSpiceService
{

    private final static String BASE_URL = "http://api.nytimes.com";
    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(NewsRestService.class);
    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }


    @Override
    protected retrofit.converter.Converter createConverter()
    {
        return new GsonConverter(getGsonBuilder().create());
    }

    public static GsonBuilder getGsonBuilder()
    {
        GsonBuilder gb = new GsonBuilder();
        gb.excludeFieldsWithoutExposeAnnotation();

        gb.registerTypeAdapter(MediaList.class, new JsonDeserializer<MediaList>() {
            public MediaList deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
            {

                if(json!= null && json.isJsonArray())
                {
                    JsonArray mediaArray = json.getAsJsonArray();
                    if(mediaArray != null)
                    {
                        for(JsonElement mediaElem: mediaArray)
                        {
                            JsonObject mediaElemObj = mediaElem.getAsJsonObject();
                            JsonElement metadataElements = mediaElemObj.get("media-metadata");
                            if(metadataElements != null && metadataElements.isJsonArray())
                            {
                                MediaList mediaList = new MediaList();

                                JsonArray metaDataArrays = metadataElements.getAsJsonArray();

                                for(JsonElement metaDataElem: metaDataArrays)
                                {
                                    JsonObject metaDataElemObj = metaDataElem.getAsJsonObject();
                                    MediaMetaData metaDataModel = new MediaMetaData();
                                    if(metaDataElemObj.has("url"))
                                    {
                                        metaDataModel.url = metaDataElemObj.get("url").getAsString();
                                    }
                                    mediaList.add(metaDataModel);
                                }

                                return mediaList;

                            }

                        }
                    }
                    else
                    {
                        return null;
                    }

                }
                Log.d("NewsSpiceService", "deserializing MediaList");

                return null;
            }
        });

        return gb;
    }

}