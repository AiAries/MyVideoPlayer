package cn.feicui.com.videoplayer.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import cn.feicui.com.videoplayer.data.VideoInfo;
import cn.feicui.com.videoplayer.data.source.VideoInfoDataSource;
import cn.feicui.com.videoplayer.util.schedulers.BaseSchedulerProvider;
import rx.Observable;
import rx.functions.Func1;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Administrator on 2016/10/28 0028.
 */

public class VideoInfoLocalDataSource implements VideoInfoDataSource {
    @Nullable
    private static VideoInfoLocalDataSource INSTANCE;
    private  final BriteDatabase mDatabaseHelper;
    private static String type;

    public void setType(String type) {
        VideoInfoLocalDataSource.type = type;
    }

    /**
     * 把Cusor对象的数据，转化成VideoInfo对象的功能
     */
    private Func1<Cursor, VideoInfo> mVideoInfoMapperFunction;

    private VideoInfoLocalDataSource(Context context, BaseSchedulerProvider schedulerProvider) {
        checkNotNull(context);
        checkNotNull(schedulerProvider);
        VideoDBHelper dbHelper = new VideoDBHelper(context);
        SqlBrite sqlBrite = SqlBrite.create();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, schedulerProvider.io());
        mVideoInfoMapperFunction = new Func1<Cursor, VideoInfo>() {
            @Override
            public VideoInfo call(Cursor c) {
                String itemId = c.getString(c.getColumnIndexOrThrow(VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_ENTRY_ID));
                String avatar = c.getString(c.getColumnIndexOrThrow(VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_AVATAR));
                String caption = c.getString(c.getColumnIndexOrThrow(VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_CAPTION));
                String cover_pic = c.getString(c.getColumnIndexOrThrow(VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_COVER_PIC));
                String name = c.getString(c.getColumnIndexOrThrow(VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_NAME));
                String url = c.getString(c.getColumnIndexOrThrow(VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_VIDEO_URL));
                VideoInfo videoInfo = new VideoInfo();
                videoInfo.setAvatar(avatar);
                videoInfo.setCaption(caption);
                videoInfo.setCover_pic(cover_pic);
                videoInfo.setUrl(url);
                videoInfo.setScreen_name(name);
                videoInfo.setId(itemId);
                return videoInfo;
            }
        };
    }

    public static VideoInfoLocalDataSource getInstance(
            @NonNull Context context,
            @NonNull BaseSchedulerProvider schedulerProvider
    ) {
        if (INSTANCE == null) {
            INSTANCE = new VideoInfoLocalDataSource(context, schedulerProvider);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
    @Override
    public Observable<List<VideoInfo>> getVideoInfos() {
        checkNotNull(type);
        String[] projection = {
                VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_ENTRY_ID,
                VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_AVATAR,
                VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_CAPTION,
                VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_COVER_PIC,
                VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_NAME,
                VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_VIDEO_URL
        };
        String sql = String.format(
                "SELECT %s FROM %s where %s = %s",
                TextUtils.join(",", projection),
                VideoPersistenceContract.ViedeoInfoEntry.TABLE_NAME,
                VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_VIDEO_TYPE,
                type
                );
        return mDatabaseHelper.createQuery(VideoPersistenceContract.ViedeoInfoEntry.TABLE_NAME, sql)
                .mapToList(mVideoInfoMapperFunction);
    }

    @Override
    public void saveVideoInfo(VideoInfo videoInfo) {
        ContentValues values = new ContentValues();
        values.put(VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_ENTRY_ID, videoInfo.getId());
        values.put(VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_AVATAR, videoInfo.getAvatar());
        values.put(VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_CAPTION, videoInfo.getCaption());
        values.put(VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_COVER_PIC, videoInfo.getCover_pic());
        values.put(VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_NAME, videoInfo.getScreen_name());
        values.put(VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_VIDEO_URL, videoInfo.getUrl());
        values.put(VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_VIDEO_TYPE, type);
        mDatabaseHelper.insert(VideoPersistenceContract.ViedeoInfoEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void refreshVideoInfos() {

    }

    @Override
    public void deleteVideoInfos() {

    }
}
