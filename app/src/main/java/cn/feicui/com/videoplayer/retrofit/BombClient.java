package cn.feicui.com.videoplayer.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：yuanchao on 2016/8/16 0016 11:57
 * 邮箱：yuanchao@feicuiedu.com
 */
public class BombClient {

    private static BombClient sInstance;

    public static synchronized BombClient getsInstance() {
        if (sInstance == null) {
            sInstance = new BombClient();
        }
        return sInstance;
    }

    private Retrofit retrofit;
    private VideoInfoApi videoInfoApi;

    private BombClient() {

        // 日志拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 构建OkHttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //.addInterceptor(new BombInterceptor()) // 用来统一处理bomb必要头字段的拦截器
                .addInterceptor(httpLoggingInterceptor) // 日志拦截器
                .build();

        // 让Gson能将bomb返回的时间戳自动转为date对象
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                        // bomb服务器baseurl
                .baseUrl("http://newapi.meipai.com/")
                        // Gson转换器
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }
    public VideoInfoApi getVideoInfoApi() {
        if (videoInfoApi == null) {
            videoInfoApi = retrofit.create(VideoInfoApi.class);
        }
        return videoInfoApi;
    }

}
