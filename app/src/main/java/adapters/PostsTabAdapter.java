package adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.tanawar.tanawarandroidapp.R;
import com.tanawar.tanawarandroidapp.ShowPostImageInFullScreen;

import java.util.List;
import tanawar_objects.Post;
/**
 *
 */
public class PostsTabAdapter extends RecyclerView.Adapter<PostsTabAdapter.ViewHolder>{
    /**
     *  Represent the post items and components.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView postTitle;
        TextView postDescription;
        TextView postCategory;
        TextView teacher;
        TextView postPublishDate;
        TextView postPublishTime;
        TextView source;
        ImageView img;
        String imgPath;
        String sourceURL; // to learn more (files, videos link)
        View myLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myLayout =  itemView;
            this.postTitle = itemView.findViewById(R.id.title_post);
            this.postDescription = itemView.findViewById(R.id.description_post);
            this.postCategory = itemView.findViewById(R.id.category);
            this.teacher = itemView.findViewById(R.id.teacher);
            this.postPublishDate = itemView.findViewById(R.id.date);
            this.postPublishTime = itemView.findViewById(R.id.time);
            this.source = itemView.findViewById(R.id.data_source);
            this.img = itemView.findViewById(R.id.imageID);
        }
        /**
         *
         * @param context
         */
        private void showURLInBrowser(Context context) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(sourceURL));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        }
        /**
         *
         * @param context
         */
        public void addListeners(final Context context) {
            this.source.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //helper method to show or download data files
                    //from chrome browser
                    showURLInBrowser(context);
                }
            });
            this.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image_in_full_screen_mode();
                }
            });
        }
        /**
         *
         */
        public void image_in_full_screen_mode(){
            Intent i = new Intent(myLayout.getContext(), ShowPostImageInFullScreen.class);
            i.putExtra("IMG_URL",imgPath);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myLayout.getContext().startActivity(i);
        }
    }
    private Context context;
    private List<Post> posts;
    public PostsTabAdapter(Context c, List<Post> postsList){
        this.context = c ;
        this.posts = postsList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.post_layout, parent,
                false);
        return new ViewHolder(v);
    }
    /**
     * A method called when doing up or down movements in the recycle view, like facebook posts
     *   recyclerview.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post p = posts.get(position);
        holder.postTitle.setText(p.getTitle());
        holder.postDescription.setText(p.getDescription());
        holder.postCategory.setText(p.getCategory());
        holder.teacher.setText(p.getPostOwnerName());
        holder.postPublishDate.setText(p.getPost_publish_date());
        holder.postPublishTime.setText(p.getPost_publish_time());
        if(p.getData_link() == null || p.getData_link().isEmpty()){
            holder.source.setVisibility(LinearLayout.GONE);
        }else{
            holder.source.setVisibility(LinearLayout.VISIBLE);
            holder.source.setText(Html.fromHtml("<u>Click to learn more.</u>"));
            holder.sourceURL = p.getData_link();
        }
        holder.imgPath = p.getImgURL();
        Glide.with(context).load(p.getImgURL()).into(holder.img);
        holder.addListeners(context);
    }
    @Override
    public int getItemCount() {
        return posts.size();
    }
}
