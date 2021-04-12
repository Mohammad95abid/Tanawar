package tanawar_objects;

import androidx.annotation.NonNull;

public class VideoPost {
    String title;
    String description;
    String videoURL;
    String Category;
    String video_publish_date;
    String resources;

    public VideoPost(String title, String description, String videoURL, String Category,
                     String video_publish_date,String resources) {
        this.title = title;
        this.description = description;
        this.videoURL = videoURL;
        this.Category = Category;
        this.video_publish_date = video_publish_date;
        this.resources = resources;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String video) {
        this.videoURL = video;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public String getVideo_publish_date() {
        return video_publish_date.split(" ")[0];
    }
    public String getVideo_publish_time() {
        return video_publish_date.split(" ")[1];
    }
    public void setVideo_publish_date(String video_publish_date) {
        this.video_publish_date = video_publish_date;
    }
    @NonNull
    @Override
    public String toString() {
        return "Video ["+this.title+" , "+this.Category+" , "+this.videoURL+"]";
    }
}
