package cn.feicui.com.videoplayer.data.source;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.feicui.com.videoplayer.data.VideoInfo;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Administrator on 2016/10/28 0028.
 */

public class VideoInfoRepository implements VideoInfoDataSource {

    private VideoInfoDataSource mLocalVideoInfoSource;//本地数据源
    private VideoInfoDataSource mRemoteVideoInfoSource;//网络数据源

    private VideoInfoRepository(VideoInfoDataSource mLocalVideoInfoSource,
                                VideoInfoDataSource mRemoteVideoInfoSource) {
        this.mLocalVideoInfoSource = checkNotNull(mLocalVideoInfoSource);
        this.mRemoteVideoInfoSource = checkNotNull(mRemoteVideoInfoSource);
    }

    //单例设计这个类
    private static VideoInfoRepository repository;

    public static VideoInfoRepository getInstance(
            @NonNull VideoInfoDataSource mLocalVideoInfoSource,
            @NonNull VideoInfoDataSource mRemoteVideoInfoSource
    ) {

        if (repository == null) {
            repository = new VideoInfoRepository(mLocalVideoInfoSource, mRemoteVideoInfoSource);
        }
        return repository;
    }

    /**
     * Used to force {@link #getInstance(VideoInfoDataSource, VideoInfoDataSource)}
     * to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        repository = null;
    }

    private Map<String, VideoInfo> mCachedData;//memory内存缓存

    /**
     * 当从网络获取到数据的时候，把这个标记值变成true，意思就是希望
     * {@link #mCachedData} 内存数据装入新的数据
     */
    private boolean isCacheDirty = false;

    @Override
    public Observable<List<VideoInfo>> getVideoInfos() {
        if (mCachedData != null && mCachedData.size()>0&&!isCacheDirty) {
            return Observable.from(mCachedData.values()).toList();
        } else if (mCachedData == null) {
            mCachedData = new HashMap<>();
        }
        //内存中没有缓存
        //所以从服务器获取数据，并保存到本地和缓存
        Observable<List<VideoInfo>> remoteData = getAndSaveRemoteData();

        if (isCacheDirty) {
            return remoteData;
        } else {
            //获取本地数据，并缓存
            Observable<List<VideoInfo>> localData = getAndCachedLocalData();
            //如果本地数据localData不为空，就返回本地的数据，否则返回服务器获取的数据remoteData
            return Observable.concat(localData, remoteData)
                    .filter(new Func1<List<VideoInfo>, Boolean>() {
                        @Override
                        public Boolean call(List<VideoInfo> videoInfos) {
                            return !videoInfos.isEmpty();
                        }
                    }).first();
        }
    }

    private Observable<List<VideoInfo>> getAndCachedLocalData() {
       return mLocalVideoInfoSource.getVideoInfos()
               //
                .flatMap(new Func1<List<VideoInfo>, Observable<List<VideoInfo>>>() {
                    @Override
                    public Observable<List<VideoInfo>> call(List<VideoInfo> videoInfos) {
                        return Observable.from(videoInfos).doOnNext(
                                new Action1<VideoInfo>() {
                                    @Override
                                    public void call(VideoInfo videoInfo) {
                                        mCachedData.put(videoInfo.getId(), videoInfo);
                                    }
                                }
                        ).toList();
                    }

                });

    }

    private Observable<List<VideoInfo>> getAndSaveRemoteData() {
        return mRemoteVideoInfoSource.getVideoInfos()
                .flatMap(new Func1<List<VideoInfo>, Observable<List<VideoInfo>>>() {
                    @Override
                    public Observable<List<VideoInfo>> call(List<VideoInfo> videoInfos) {
                        return Observable.from(videoInfos)
                                .doOnNext(new Action1<VideoInfo>() {
                                    @Override
                                    public void call(VideoInfo videoInfo) {
                                        mLocalVideoInfoSource.saveVideoInfo(videoInfo);
                                        mCachedData.put(videoInfo.getId(), videoInfo);
                                    }
                                })
                                .doOnCompleted(new Action0() {
                                    @Override
                                    public void call() {
                                        isCacheDirty = false;
                                    }
                                }).toList();
                    }
                });
    }

    @Override
    public void saveVideoInfo(VideoInfo videoInfo) {
        // 不需要处理
    }

    @Override
    public void refreshVideoInfos() {
        isCacheDirty = true;
    }

    @Override
    public void deleteVideoInfos() {
        mLocalVideoInfoSource.deleteVideoInfos();
        if (mCachedData != null) {
            mCachedData.clear();
        }
    }
}
