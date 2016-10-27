package cn.feicui.com.videoplayer.retrofit;

import java.util.List;

import cn.feicui.com.videoplayer.data.VideoInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/10/27 0027.
 */

public interface VideoInfoApi {

    @GET("output/channels_topics_timeline.json")
    Call<List<VideoInfo>> getVideoInfos(@Query("id") String id);
}
