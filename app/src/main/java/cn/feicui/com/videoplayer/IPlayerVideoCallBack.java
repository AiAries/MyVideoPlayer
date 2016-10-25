package cn.feicui.com.videoplayer;

/**
 * Created by Administrator on 2016/10/25 0025.
 */

public interface IPlayerVideoCallBack {
    /**
     * 播放网络视频视频
     * @param url 视频地址路径
     */
    void play(String url);

    /**
     * 暂停播放
     */
    void pause();
}
