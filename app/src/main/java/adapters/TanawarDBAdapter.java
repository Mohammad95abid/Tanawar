package adapters;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tanawar.tanawarandroidapp.R;
import java.util.List;
import tanawar_objects.DBItem;

public class TanawarDBAdapter extends RecyclerView.Adapter<TanawarDBAdapter.ViewHolder>{
    /**
     *  Represent the post items and components.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtPublishTime;
        TextView txtTitle;
        TextView txtDesc;
        ImageView imgFileIC;
        View myLayout;
        String fileURL;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myLayout =  itemView;
            this.txtPublishTime = itemView.findViewById(R.id.db_item_time);
            this.txtTitle = itemView.findViewById(R.id.db_item_title);
            this.txtDesc = itemView.findViewById(R.id.db_item_desc);
            this.imgFileIC = itemView.findViewById(R.id.db_item_fileIC);

        }
        /**
         *
         * @param context
         */
        private void showURLInBrowser(Context context) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(fileURL));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        }
        /**
         *
         * @param context
         */
        public void addListeners(final Context context) {
            this.imgFileIC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showURLInBrowser(context);
                }
            });
        }
    }
    private Context context;
    private List<DBItem> items;
    public TanawarDBAdapter(Context c, List<DBItem> items){
        this.context = c ;
        this.items = items;
    }
    @NonNull
    @Override
    public TanawarDBAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.db_item_layout, parent,
                false);
        return new TanawarDBAdapter.ViewHolder(v);
    }
    /**
     * A method called when doing up or down movements in the recycle view, like facebook posts
     *   recyclerview.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull TanawarDBAdapter.ViewHolder holder, int position) {
        DBItem item = items.get(position);
        holder.txtPublishTime.setText(item.getPublishTime());
        holder.txtTitle.setText(item.getTitle());
        holder.txtDesc.setText(item.getDescription());
        holder.fileURL= item.getFileURL();
        holder.addListeners(context);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
}
