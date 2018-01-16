package com.olhu.redditclient.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.olhu.redditclient.model.Image;
import com.olhu.redditclient.model.Topic;
import com.olhu.redditclient.network.request.GetTopTopicsRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public final class RedditApiClient {
    private RedditApi api;

    public RedditApiClient(RedditApi api) {
        this.api = api;
    }

    public Observable<List<Topic>> top(@NonNull GetTopTopicsRequest request) {
        return api.getTopTopics(request.getAfter(), request.getBefore(), request.getLimit())
                .flatMap(responseBody -> Observable.just(convertJsonToTopics(responseBody.string())));
    }

    @NonNull
    private List<Topic> convertJsonToTopics(String jsonString) throws JSONException {
        List<Topic> topics = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONArray children = data.getJSONArray("children");
        for (int i = 0; i < children.length(); i++) {
            JSONObject child = children.getJSONObject(i).getJSONObject("data");
            Topic topic = Topic.builder()
                    .name(child.getString("name"))
                    .title(child.getString("title"))
                    .author(child.getString("author"))
                    .commentsNumber(child.getInt("num_comments"))
                    .createdUTC(child.getLong("created_utc") * 1000) //convert to milliseconds
                    .thumbnail(getThumbnail(child))
                    .sourceImage(getSourceImage(child))
                    .build();

            topics.add(topic);
        }
        return topics;
    }

    @NonNull
    private Image getThumbnail(JSONObject child) throws JSONException {
        int thumbnailHeight = 0;
        int thumbnailWidth = 0;
        try {
            thumbnailHeight = child.getInt("thumbnail_height");
            thumbnailWidth = child.getInt("thumbnail_width");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Image.builder()
                .height(thumbnailHeight)
                .width(thumbnailWidth)
                .url(child.getString("thumbnail"))
                .build();
    }

    @Nullable
    private Image getSourceImage(JSONObject child) {
        try {
            JSONObject imageJsonObject = child.getJSONObject("preview").getJSONArray("images").getJSONObject(0).getJSONObject("source");
            return Image.builder()
                    .height(imageJsonObject.getInt("height"))
                    .width(imageJsonObject.getInt("width"))
                    .url(imageJsonObject.getString("url"))
                    .build();

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
