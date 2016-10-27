package cn.feicui.com.videoplayer;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.feicui.com.videoplayer.anotation.ViewInjectProxy;
import cn.feicui.com.videoplayer.base.OkHttpUtil;
import cn.feicui.com.videoplayer.data.VideoInfo;

public class MainActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, IPlayerVideoCallBack {

    private static final String TAG = "Main";
    @Bind(R.id.exo_player)
    SimpleExoPlayerView simpleExoPlayerView;
    private String url;
    private List<VideoInfo> data = new ArrayList<>();
    private VideoInfoRecyclerAdapter adapter;
    private AsyncTask<String, Void, String> loadTask;
    private MediaSource videoSource;
    private DataSource.Factory dataSourceFactory;
    private ExtractorsFactory extractorsFactory;
    private SimpleExoPlayer player;
    private DefaultBandwidthMeter bandwidthMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ViewInjectProxy.bind(this);
        initExoPlayer();

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        adapter = new VideoInfoRecyclerAdapter(this, data);
        adapter.setItemClickListener(new VideoInfoRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(VideoInfoRecyclerAdapter.MyViewHolder holder, int position) {
                //TODO 点击条目的时候，回调这个方法
            }
        });
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

    private void initExoPlayer() {
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);

        // 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        // 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

        // Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);
        //播放视频   url 视频路径
        String url = "http://o9ve1mre2.bkt.clouddn.com/raw_%E6%B8%A9%E7%BD%91%E7%94%B7%E5%8D%95%E5%86%B3%E8%B5%9B.mp4";
        playVideo(url);
    }

    private void playVideo(String url) {

        // Produces DataSource instances through which media data is loaded.
        dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)), bandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        if (videoSource != null) {
            videoSource.releaseSource();
            videoSource.prepareSource(new ExtractorMediaSource(
                    Uri.parse(url),
                    dataSourceFactory,
                    extractorsFactory,
                    new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message message) {
                            Toast.makeText(MainActivity.this, "" + message.toString(), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }),
                    new ExtractorMediaSource.EventListener() {
                        @Override
                        public void onLoadError(IOException error) {
                            Toast.makeText(MainActivity.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ));
        } else {
            videoSource = new ExtractorMediaSource(Uri.parse(url),
                    dataSourceFactory, extractorsFactory, null, null);
        }
        // Prepare the player with the source.
        player.prepare(videoSource);
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
            //解析返回字符串
            List<VideoInfo> videoInfos = parseInfo(s);
            String url = videoInfos.get(0).getUrl();
            //
            Log.d(TAG, "onPostExecute: " + url);//视频给的路径地址有问题
            adapter.setBigNewses(videoInfos);
            adapter.notifyDataSetChanged();
            play(url);
        }
    }

    private List<VideoInfo> parseInfo(String s) {
        Gson g = new Gson();
        Type t = new TypeToken<List<VideoInfo>>() {
        }.getType();
        return g.fromJson(s, t);
    }
}
