package cn.feicui.com.videoplayer.vitamio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.feicui.com.videoplayer.R;
import cn.feicui.com.videoplayer.data.VideoURL;
import io.vov.vitamio.widget.VideoView;

public class VitamioViedeoViewActivity extends AppCompatActivity {

    @Bind(R.id.videoView)
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitamio_viedeo_view);
        ButterKnife.bind(this);
        videoView.setVideoPath(VideoURL.url1);

        videoView.start();
    }
}
