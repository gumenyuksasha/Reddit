package com.olhu.redditclient.network;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RedditApi {
    @GET("top.json")
    Observable<ResponseBody> getTopTopics(@Query("after") String after, @Query("before") String before, @Query("limit") int limit);
}
