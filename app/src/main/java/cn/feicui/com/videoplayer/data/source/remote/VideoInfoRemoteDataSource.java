package cn.feicui.com.videoplayer.data.source.remote;

import java.util.List;

import cn.feicui.com.videoplayer.data.VideoInfo;
import cn.feicui.com.videoplayer.data.source.VideoInfoDataSource;
import cn.feicui.com.videoplayer.retrofitRx.BombClient;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/28 0028.
 */

public class VideoInfoRemoteDataSource implements VideoInfoDataSource {
    private  String type;

    private static VideoInfoRemoteDataSource  videoInfoRemoteDataSource;

    private VideoInfoRemoteDataSource() {
    }
    public static VideoInfoRemoteDataSource getInstance() {
        if (videoInfoRemoteDataSource==null) {
            videoInfoRemoteDataSource = new VideoInfoRemoteDataSource();
        }
        return videoInfoRemoteDataSource;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Observable<List<VideoInfo>> getVideoInfos() {
        return BombClient.getsInstance().getVideoInfoApi().getVideoInfos(type);
    }

    @Override
    public void saveVideoInfo(VideoInfo videoInfo) {

    }

    @Override
    public void refreshVideoInfos() {

    }

    @Override
    public void deleteVideoInfos() {

    }
}
