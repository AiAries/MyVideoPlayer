package cn.feicui.com.videoplayer.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/10/28 0028.
 */

public class VideoDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Video.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + VideoPersistenceContract.ViedeoInfoEntry.TABLE_NAME + " (" +
                    VideoPersistenceContract.ViedeoInfoEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_AVATAR + TEXT_TYPE + COMMA_SEP +
                    VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_CAPTION + TEXT_TYPE + COMMA_SEP +
                    VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_COVER_PIC + TEXT_TYPE + COMMA_SEP +
                    VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_VIDEO_TYPE + TEXT_TYPE + COMMA_SEP +
                    VideoPersistenceContract.ViedeoInfoEntry.COLUMN_NAME_VIDEO_URL + TEXT_TYPE  +
                    " )";
    public VideoDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public VideoDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
