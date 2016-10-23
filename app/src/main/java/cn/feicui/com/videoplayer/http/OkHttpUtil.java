package cn.feicui.com.videoplayer.http;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2016/9/3 0003.
 */
public class OkHttpUtil {


    /**
     * 获取字符串数据
     *
     * @param url 数据的url路径
     * @return 获取成功返回字符串内容，失败返回null
     */
    public static String getString(String url) {
        ResponseBody responseBody = getResponseBody(url);
        try {
            if (responseBody != null) {
                return responseBody.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ResponseBody getResponseBody(String url) {

//        int cacheSize = 10 * 1024 * 1024; // 10 MiB
//        Cache cache = new Cache(new File("data/data/com.feicui.edu.highpart/files"), cacheSize);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = builder
//                .cache(cache)
                .build();

        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static final MediaType FILE
            = MediaType.parse("application/octet-stream; charset=utf-8");

    public static String postFile(String url, File file, String token) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("token", token);

        RequestBody body = RequestBody.create(FILE, file);
        builder.addFormDataPart("portrait", file.getName(), body);

        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String postString(String url, Map<String, String> map) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        Set<Map.Entry<String, String>> entries = map.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value = next.getValue();
            builder.addFormDataPart(key, value);
        }

        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}
