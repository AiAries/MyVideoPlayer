package cn.feicui.com.videoplayer.data.source;

import java.util.List;

import cn.feicui.com.videoplayer.data.VideoInfo;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/28 0028.
 */

public interface VideoInfoDataSource {
    /**
     * 获取所有的视频信息
     * @return
     */
     Observable<List<VideoInfo>> getVideoInfos();

    /**
     * 保存视频信息
     * @param videoInfo
     */
    void saveVideoInfo(VideoInfo videoInfo);

    /**
     * 刷新视频信息
     */
    void refreshVideoInfos();

    /**
     * 删除所有的视频信息
     */
    void deleteVideoInfos();
}
