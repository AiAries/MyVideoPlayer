package cn.feicui.com.videoplayer;

/**
 * Created by Administrator on 2016/10/23 0023.
 */

public class VideoInfo {
   private String id;
   private String url;
   private String cover_pic;
   private String screen_name;
   private String caption;
   private String avatar;

    private int plays_count;
    private int comments_count;
    private int likes_count;

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public VideoInfo(String id, String url, String cover_pic, String screen_name, String caption, String avatar, int plays_count, int comments_count) {
        this.id = id;
        this.url = url;
        this.cover_pic = cover_pic;
        this.screen_name = screen_name;
        this.caption = caption;
        this.avatar = avatar;
        this.plays_count = plays_count;
        this.comments_count = comments_count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getPlays_count() {
        return plays_count;
    }

    public void setPlays_count(int plays_count) {
        this.plays_count = plays_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

}
