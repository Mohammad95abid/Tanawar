package adapters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.tanawar.tanawarandroidapp.R;import com.tanawar.tanawarandroidapp.ChatActivity;
import com.tanawar.tanawarandroidapp.Homepage;
import java.util.Calendar;
import java.util.List;
import apis.FirebaseConstants;
import apis.PrivateLessonsAPI;
import tanawar_objects.PrivateLessonsDiaryItem;
import utils.AccountType;
import utils.PrivateLessonType;
import utils.TanawarCalendar;

/**
 *
 */
public class MyDiaryAdapter extends RecyclerView.Adapter<MyDiaryAdapter.ViewHolder>{
    /**
     *  Represent the Diary items and components.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView subjectTV;
        TextView participantsTV;
        TextView timeTV;
        String imgPath;
        View myLayout;
        ImageView zoomIC;
        ImageView messages;
        ImageView setting;
        Context context;
        String oldTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myLayout =  itemView;
            this.subjectTV = itemView.findViewById(R.id.lessonSubject_diaryItem);
            this.participantsTV = itemView.findViewById(R.id.lessonParticipantName_diaryItem);
            this.timeTV = itemView.findViewById(R.id.lessonTime_diaryItem);
            this.img = itemView.findViewById(R.id.private_lesson_img_diaryItem);
            this.zoomIC = itemView.findViewById(R.id.zoomIC_diaryItem);
            this.messages = itemView.findViewById(R.id.messages_diaryItem);
            this.setting = itemView.findViewById(R.id.set_diaryItem);
            this.setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePicker(v);
                }
            });
        }
        /**
         * a method to show the date picker dialog.
         */
        private void showDatePicker(final View v) {
            // Get Current Date
            final Calendar datetime = Calendar.getInstance();
            int mYear = datetime.get(Calendar.YEAR);
            int mMonth = datetime.get(Calendar.MONTH);
            int mDay = datetime.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    v.getRootView().getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            datetime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            datetime.set(Calendar.MONTH, monthOfYear);
                            datetime.set(Calendar.YEAR, year);
                            showTimePicker(datetime, v);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            datePickerDialog.show();
        }
        /**
         * a method to show the date picker dialog.
         * @param datetime
         */
        private void showTimePicker(final Calendar datetime, final View v) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    v.getRootView().getContext(),
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            datetime.set(Calendar.MINUTE, minute);
                            Calendar current = Calendar.getInstance();
                            if(current.getTimeInMillis() >= datetime.getTimeInMillis()){
                                Toast.makeText(context, "Invalid Time!",
                                        Toast.LENGTH_LONG).show();
                                showTimePicker(datetime, v);
                                return;
                            }
                            String temp = TanawarCalendar.getInstance().getDateTimeAsString(
                                    datetime, " ");
                            String stdName = participantsTV.getText().toString();
                            stdName = stdName.substring(stdName.indexOf(":")+2);
                            String time = temp.substring(0, temp.lastIndexOf(":"));
                            PrivateLessonsAPI.getInstance().updatePrivateLessonDetails(
                                    Homepage.myEmail, stdName , oldTime, datetime);
                            oldTime = time;
                            timeTV.setText("Lesson Time: "+time);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    }
    private Context context;
    private List<PrivateLessonsDiaryItem> lessons;
    public MyDiaryAdapter(Context c, List<PrivateLessonsDiaryItem> list){
        this.context = c ;
        this.lessons = list;
    }
    @NonNull
    @Override
    public MyDiaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.diary_item, parent,
                false);
        return new MyDiaryAdapter.ViewHolder(v);
    }
    /**
     * A method called when doing up or down movements in the recycle view, like facebook posts
     *   recyclerview.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyDiaryAdapter.ViewHolder holder, int position) {
        final PrivateLessonsDiaryItem p = lessons.get(position);
        holder.subjectTV.setText("Subject: "+p.getSubject());
        holder.context = context;
        if(Homepage.userType == AccountType.TEACHER){
            holder.participantsTV.setText("Student: "+p.getStudentsName());
            holder.setting.setVisibility(View.VISIBLE);
        }else if(Homepage.userType == AccountType.STUDENT){
            holder.participantsTV.setText("Teacher: "+p.getTeacherName());
            holder.setting.setVisibility(View.GONE);
        }
        String temp = TanawarCalendar.getInstance().getDateTimeAsString(
                p.getTime(), " ");
        holder.oldTime = temp.substring(0, temp.lastIndexOf(":"));
        String time = temp.substring(0, temp.lastIndexOf(":"));
        holder.timeTV.setText(time);
        String imgPath = p.getLessonType().getImgPath();
        holder.imgPath = imgPath;
        Glide.with(context).load(imgPath).into(holder.img);
        if(p.getLessonType() == PrivateLessonType.FACE_TO_FACE){
            // holder.zoomIC.setVisibility(View.GONE);
        }else{
            holder.zoomIC.setVisibility(View.VISIBLE);
            String zoomICPath = "https://firebasestorage.googleapis.com/v0/b/tanawar-mr.appspot.com/o/Images%2Fabstract%20images%2FzoomIC.jpg?alt=media&token=d9f92468-9597-4ad5-9102-88feaad3728a";
            Glide.with(context).load(zoomICPath).into(holder.zoomIC);
        }
        holder.zoomIC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(p.getMeetingURL()));
                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(browserIntent);
            }
        });
        holder.messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChatActivity.class);
                i.putExtra(FirebaseConstants.Collections.PrivateLesson.TEACHER_NAME,
                        p.getTeacherName());
                i.putExtra(FirebaseConstants.Collections.PrivateLesson.TEACHER_EMAIL,
                        p.getTeacherEmail());
                i.putExtra(FirebaseConstants.Collections.PrivateLesson.STUDENT_NAME,
                        p.getStudentsName());
                i.putExtra(FirebaseConstants.Collections.PrivateLesson.STUDENT_EMAIL,
                        p.getStudentsEmail());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return lessons.size();
    }
}
