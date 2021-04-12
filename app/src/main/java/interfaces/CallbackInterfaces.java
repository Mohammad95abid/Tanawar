package interfaces;

import java.util.ArrayList;

import tanawar_objects.VideoPost;

public class CallbackInterfaces {
    public interface VideosLoadingCallback{
        public void getVideosAsList(ArrayList<VideoPost> videosList);
    }
}
