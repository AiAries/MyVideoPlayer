package cn.feicui.com.videoplayer.rx;

import java.util.List;

import cn.feicui.com.videoplayer.data.VideoInfo;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/27 0027.
 */

public interface VideoInfoApi {

    @GET("output/channels_topics_timeline.json")
    Observable<List<VideoInfo>> getVideoInfos(@Query("id") String id);
}
