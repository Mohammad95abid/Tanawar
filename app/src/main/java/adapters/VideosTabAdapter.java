package adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tanawar.tanawarandroidapp.R;
import com.tanawar.tanawarandroidapp.ShowVideoInFullScreen;

import java.util.List;
import tanawar_objects.VideoPost;

public class VideosTabAdapter extends RecyclerView.Adapter<VideosTabAdapter.ViewHolder> {
    /**
     *  Represent the post items and components.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView videoTitle;
        TextView videoDescription;
        VideoView videoView;
        TextView videoCategory;
        TextView videoDate;
        TextView videoTime;
        View myLayout;
        String videoURL;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myLayout = itemView;
            this.videoTitle = itemView.findViewById(R.id.title_video);
            this.videoDescription = itemView.findViewById(R.id.description_video);
            this.videoView = itemView.findViewById(R.id.videoID);
            this.videoCategory = itemView.findViewById(R.id.category);
            this.videoDate = itemView.findViewById(R.id.date);
            this.videoTime = itemView.findViewById(R.id.time);
        }
        public void addListeners(){
            this.videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(myLayout.getContext(), ShowVideoInFullScreen.class);
                    i.putExtra("Video_URL",videoURL);
                    myLayout.getContext().startActivity(i);
                }
            });
        }
//        private void videoInFullScreen(){
//            DisplayMetrics metrics = new DisplayMetrics();
//            ((Activity)myLayout.getContext()).getWindowManager()
//                    .getDefaultDisplay().getMetrics(metrics);
//            android.widget.LinearLayout.LayoutParams params =
//                    (android.widget.LinearLayout.LayoutParams) videoView.getLayoutParams();
//            params.width = metrics.widthPixels;
//            params.height = metrics.heightPixels;
//            params.leftMargin = 0;
//            videoView.setLayoutParams(params);
//        }
//        private void videoBackFromFullscreen(){
//            DisplayMetrics metrics = new DisplayMetrics();
//            ((Activity)myLayout.getContext()).getWindowManager()
//                    .getDefaultDisplay().getMetrics(metrics);
//            android.widget.LinearLayout.LayoutParams params =
//                    (android.widget.LinearLayout.LayoutParams) videoView.getLayoutParams();
//            params.width = (int)(300*metrics.density);
//            params.height = (int)(200*metrics.density);
//            //params.leftMargin = 30;
//            videoView.setLayoutParams(params);
//        }
    }
    private Context context;
    private List<VideoPost> videos;
    public VideosTabAdapter(Context c, List<VideoPost> videosList){
        this.context = c ;
        this.videos = videosList;
    }
    @NonNull
    @Override
    public VideosTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.video_layout, parent,false);
        return new VideosTabAdapter.ViewHolder(v);
    }

    /**
     * A method called when doing up or down movements in the recycle view, like facebook posts
     *   recyclerview.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull VideosTabAdapter.ViewHolder holder, int position) {
        VideoPost v = videos.get(position);
        holder.videoTitle.setText(v.getTitle());
        holder.videoDescription.setText(v.getDescription());
        holder.videoCategory.setText(v.getCategory());
        holder.videoDate.setText(v.getVideo_publish_date());
        holder.videoTime.setText(v.getVideo_publish_time());
        holder.videoURL = v.getVideoURL();
        showVideo(holder.videoView, v.getVideoURL());
        holder.addListeners();
    }

    private void showVideo(VideoView videoView, String videoURL) {
        Uri videoUri = Uri.parse(videoURL);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        MediaController mediaController = new MediaController(videoView.getContext());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }
}
