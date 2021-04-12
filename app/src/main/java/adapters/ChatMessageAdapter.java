package adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.tanawar.tanawarandroidapp.Homepage;
import com.tanawar.tanawarandroidapp.R;

import java.util.List;
import tanawar_objects.ChatItem;
import utils.AccountType;
import utils.TanawarCalendar;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder>{
    /**
     *  Represent the Diary items and components.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView message;
        View myLayout;
        TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myLayout =  itemView;
            this.message = itemView.findViewById(R.id.chatMessageTV_chatActivity);
            this.time = itemView.findViewById(R.id.datetime_chatActivity);
        }
    }
    private Context context;
    private List<ChatItem> messages;
    public ChatMessageAdapter(Context c, List<ChatItem> list){
        this.context = c ;
        this.messages = list;
    }
    @NonNull
    @Override
    public ChatMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.chat_message_item, parent,
                false);
        return new ChatMessageAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageAdapter.ViewHolder holder, int position) {
        ChatItem p = messages.get(position);
        holder.message.setText(p.getMessage());
        holder.message.setTextColor(Color.BLACK);
        String temp = TanawarCalendar.dateTimeAsString(p.getTime());
        String time = temp.substring(0, temp.lastIndexOf(":"));
        holder.time.setText(time);
            if(Homepage.userType == AccountType.TEACHER){
//                holder.message.setBackgroundColor(
//                        ContextCompat.getColor(context, R.color.post_layout_background));
                holder.message.setTextColor(Color.parseColor("#dfe9ec"));
                holder.message.setGravity(Gravity.RIGHT);
                if(!p.getRecipientEmail().equals(Homepage.myEmail)){
                    // student is recipient
                    holder.message.setTextColor(Color.parseColor("#eff4f5"));
                    holder.message.setGravity(Gravity.LEFT);
                }
            }else{
                holder.message.setGravity(Gravity.LEFT);
                holder.message.setTextColor(Color.parseColor("#eff4f5"));
                if(!p.getRecipientEmail().equals(Homepage.myEmail)){
                    // teacher is recipient
                    holder.message.setTextColor(Color.parseColor("#dfe9ec"));
                    holder.message.setGravity(Gravity.RIGHT);
                }
            }


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
