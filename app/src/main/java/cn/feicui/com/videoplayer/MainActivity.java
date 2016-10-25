package cn.feicui.com.videoplayer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.feicui.com.videoplayer.anotation.ViewInject;
import cn.feicui.com.videoplayer.anotation.ViewInjectProxy;
import cn.feicui.com.videoplayer.http.OkHttpUtil;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class MainActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, IPlayerVideoCallBack {

    private static final String TAG = "Main";
    @Bind(R.id.sr)
    SwipeRefreshLayout sr;
    private String url;
    private List<VideoInfo> data = new ArrayList<>();
    private VideoInfoRecyclerAdapter adapter;
    @ViewInject(R.id.videoView)
    private VideoView videoView;
    private MediaController mediaController;
    private AsyncTask<String, Void, String> loadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ViewInjectProxy.bind(this);
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        sr.setRefreshing(true);
        sr.setOnRefreshListener(this);
//        videoView = (SimpleVideoView) findViewById(R.id.videoView);


        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        adapter = new VideoInfoRecyclerAdapter(this, data);
        rv.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //给图片设置的点击事件

        }
        url = "http://newapi.meipai.com/output/channels_topics_timeline.json?id=3";
        load();
    }

    private void load() {
        loadTask = new LoadTask().execute(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadTask != null && loadTask.isCancelled()) {
            loadTask.cancel(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        旅行：接口地址：http://newapi.meipai.com/output/channels_topics_timeline.json?id=3

        搞笑：接口地址：http://newapi.meipai.com/output/channels_topics_timeline.json?id=13

        明星：接口地址：http://newapi.meipai.com/output/channels_topics_timeline.json?id=16
         */
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.lvXin://旅行
                url = "http://newapi.meipai.com/output/channels_topics_timeline.json?id=3";
                break;
            case R.id.mingXin://明星
                url = "http://newapi.meipai.com/output/channels_topics_timeline.json?id=16";
                break;
            case R.id.fan://搞笑
                url = "http://newapi.meipai.com/output/channels_topics_timeline.json?id=13";
                break;
        }
        load();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRefresh() {
        load();
    }

    @Override
    public void play(String url) {
        if (videoView.isPlaying()) {
            videoView.stopPlayback();
        }
        videoView.setVideoPath(url);
        videoView.start();
    }

    @Override
    public void pause() {

    }

    class LoadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String json = OkHttpUtil.getString(url);
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            sr.setRefreshing(false);
            //解析返回字符串
            List<VideoInfo> videoInfos = parseInfo(s);
            String url = videoInfos.get(0).getUrl();
//            videoView.onResume();
            //http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8
            Log.d(TAG, "onPostExecute: " + url);//视频给的路径地址有问题
            play("http://o9ve1mre2.bkt.clouddn.com/raw_%E6%B8%A9%E7%BD%91%E7%94%B7%E5%8D%95%E5%86%B3%E8%B5%9B.mp4");
            adapter.setBigNewses(videoInfos);
            adapter.notifyDataSetChanged();

        }
    }

    private List<VideoInfo> parseInfo(String s) {
        Gson g = new Gson();
        Type t = new TypeToken<List<VideoInfo>>() {
        }.getType();
        return g.fromJson(s, t);
    }
}
