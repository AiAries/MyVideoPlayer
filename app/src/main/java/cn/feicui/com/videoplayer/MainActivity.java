package cn.feicui.com.videoplayer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.feicui.com.videoplayer.http.OkHttpUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";
    private String url;
    private List<VideoInfo> data = new ArrayList<>();
    private VideoInfoRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        new LoadTask().execute(url);
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
        if (!TextUtils.isEmpty(url)) {
            new LoadTask().execute(url);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
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
