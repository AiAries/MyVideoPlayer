package cn.feicui.com.videoplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class VideoInfoRecyclerAdapter extends RecyclerView.Adapter<VideoInfoRecyclerAdapter.MyViewHolder> {

    private final Context context;

    public List<VideoInfo> getBigNewses() {
        return bigNewses;
    }

    public void setBigNewses(List<VideoInfo> bigNewses) {
        this.bigNewses = bigNewses;
    }

    private List<VideoInfo> bigNewses;
    private IPlayerVideoCallBack playerVideoCallBack;


    public VideoInfoRecyclerAdapter(Context context, List<VideoInfo> bigNewses) {

        if (context instanceof IPlayerVideoCallBack) {
            playerVideoCallBack = (IPlayerVideoCallBack) context;
        }
        this.context = context;
        this.bigNewses = bigNewses;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //确定展示条目的布局
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_list_pic, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //给itemView绑定数据
        final VideoInfo bigNews = bigNewses.get(position);
        Glide.with(context)
                .load(bigNews.getCover_pic()).centerCrop()
                .into(holder.iv);
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8";
                playerVideoCallBack.play(url/*bigNews.getUrl()*/);
            }
        });
        Glide.with(context)
                .load(bigNews.getAvatar()).centerCrop()
                .into(holder.header);
        holder.author.setText(bigNews.getScreen_name());
        holder.video_intro.setText(bigNews.getCaption());

    }

    @Override
    public int getItemCount() {
        return bigNewses == null ? 0 : bigNewses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cover_pic)
        ImageView iv;
        @Bind(R.id.author)
        TextView author;
        @Bind(R.id.header)
        ImageView header;
        @Bind(R.id.video_intro)
        TextView video_intro;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
