package cn.feicui.com.videoplayer.retrofit;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.feicui.com.videoplayer.IPlayerVideoCallBack;
import cn.feicui.com.videoplayer.R;
import cn.feicui.com.videoplayer.VideoInfoRecyclerAdapter;
import cn.feicui.com.videoplayer.anotation.ViewInjectProxy;
import cn.feicui.com.videoplayer.data.VideoInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitMainActivity extends AppCompatActivity implements IPlayerVideoCallBack {

    private static final String TAG = "RetrofitMainActivity";
    @Bind(R.id.exo_player)
    SimpleExoPlayerView simpleExoPlayerView;
    private List<VideoInfo> data = new ArrayList<>();
    private VideoInfoRecyclerAdapter adapter;
    private MediaSource videoSource;
    private SimpleExoPlayer player;
    private DefaultBandwidthMeter bandwidthMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ViewInjectProxy.bind(this);
        initExoPlayer();//初始化EXOplayer播放器
        //初始化 recyclerView
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        adapter = new VideoInfoRecyclerAdapter(this, data);
        adapter.setItemClickListener(new VideoInfoRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(VideoInfoRecyclerAdapter.MyViewHolder holder, int position) {
                //TODO 点击条目的时候，回调这个方法
                Toast.makeText(RetrofitMainActivity.this, ""+position, Toast.LENGTH_SHORT).show();
            }
        });
        rv.setAdapter(adapter);
        //初始化toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //给图片设置的点击事件
        }

        //默认进入当前页面时，加载数据
        asyncLoadData("3");
    }

    private void asyncLoadData(String id) {
        Call<List<VideoInfo>> videoInfos = BombClient.getsInstance().getVideoInfoApi().getVideoInfos(id);
        videoInfos.enqueue(new Callback<List<VideoInfo>>() {
            @Override
            public void onResponse(Call<List<VideoInfo>> call, Response<List<VideoInfo>> response) {
                //解析返回字符串
                List<VideoInfo> infos = response.body();
                adapter.setBigNewses(infos);
                adapter.notifyDataSetChanged();
                //刷新数据后，默认播放第一条视频
//                String url = infos.get(0).getUrl();
//                play(url);
                //播放视频   url 视频路径
                String url = "http://o9ve1mre2.bkt.clouddn.com/raw_%E6%B8%A9%E7%BD%91%E7%94%B7%E5%8D%95%E5%86%B3%E8%B5%9B.mp4";
                play(url);
            }

            @Override
            public void onFailure(Call<List<VideoInfo>> call, Throwable t) {
            }
        });
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        旅行：接口地址：http://newapi.meipai.com/output/channels_topics_timeline.json?id=3

        搞笑：接口地址：http://newapi.meipai.com/output/channels_topics_timeline.json?id=13

        明星：接口地址：http://newapi.meipai.com/output/channels_topics_timeline.json?id=16
         */
        String id = "";
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.lvXin://旅行
                id = "3";
                break;
            case R.id.mingXin://明星
                id = "16";
                break;
            case R.id.fan://搞笑
                id = "13";
                break;
        }
        asyncLoadData(id);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
    这个方法需要被主动调用才会执行
     */
    @Override
    public void play(String url) {
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)), bandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        if (videoSource != null) {
            videoSource.releaseSource();
            videoSource.prepareSource(new ExtractorMediaSource(
                    Uri.parse(url),
                    dataSourceFactory,
                    extractorsFactory,
                    null,
                   null
            ));
        } else {
            videoSource = new ExtractorMediaSource(Uri.parse(url),
                    dataSourceFactory, extractorsFactory, null, null);
        }
        // Prepare the player with the source.
        player.prepare(videoSource);
    }

    @Override
    public void pause() {

    }
}
