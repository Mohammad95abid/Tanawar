package apis;
import android.app.Activity;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tanawar.tanawarandroidapp.tabs.TeacherVideosTab;

import apis.FirebaseConstants.Collections.MyVideos;
import java.util.Calendar;
import java.util.Date;
import helper_tools.TanawarAlertDialog;
import tanawar_objects.VideoPost;

public class VideosAPI {
    private FirebaseFirestore db;
    /**
     * A variable to use in a singleton pattern.
     */
    private static VideosAPI instance = null;
    public static VideosAPI getInstance(){
        if(instance == null){
            instance = new VideosAPI();
        }
        return instance;
    }
    private VideosAPI(){
          this.db = FirebaseFirestore.getInstance();
    }
    public void getVideosOrderByPublishedTime(final Activity activity, String category,
                                              String language, final TeacherVideosTab tab){
        if("All".equals(language)){ // videos in all languages
            loadAllVideosByCategory(activity, category, tab);
        }else{ // videos in specific language
            loadVideosByCategoryAndLanguage(activity, category, language, tab);
        }
    }

    private void loadVideosByCategoryAndLanguage(final Activity activity, String category,
                                                 String language, final TeacherVideosTab tab) {
        this.db.collection(MyVideos.COLLECTION_NAME)
                .whereEqualTo(MyVideos.CATEGORY,category)
                .whereEqualTo(MyVideos.LANGUAGE,language)
                .orderBy(MyVideos.DATETIME, Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        Date date = doc.getDate(MyVideos.DATETIME);
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        String t = c.get(Calendar.HOUR_OF_DAY)
                                +":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                        String time = c.get(Calendar.DAY_OF_MONTH)+"/"
                                +(c.get(Calendar.MONTH)+1)+"/"
                                +c.get(Calendar.YEAR)+" "+t;
                        VideoPost v = new VideoPost((String)doc.get(MyVideos.TITLE),
                                (String)doc.get(MyVideos.DESCRIPTION),
                                (String)doc.get(MyVideos.VIDEO_URL)
                                ,(String)doc.get(MyVideos.CATEGORY), time
                                ,(String)doc.get(MyVideos.RESOURCES));
                        tab.addVideoToList(v);
                    }
                    tab.notifyDataSetChanged();
                }else{ // task failed
                    new TanawarAlertDialog().showSimpleDialog(
                            "Loading Videos was Failed",
                            "The Error: "+task.getException().getMessage(), activity
                    ).show();
                    Log.e("VideoError",task.getException().getLocalizedMessage());
                }
            }
        });
    }

    private void loadAllVideosByCategory(final Activity activity, String category,
                                         final TeacherVideosTab tab) {
        this.db.collection(MyVideos.COLLECTION_NAME)
                .whereEqualTo(MyVideos.CATEGORY,category)
                .orderBy(MyVideos.DATETIME, Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        Date date = doc.getDate(MyVideos.DATETIME);
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        String t = c.get(Calendar.HOUR_OF_DAY)
                                +":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                        String time = c.get(Calendar.DAY_OF_MONTH)+"/"
                                +(c.get(Calendar.MONTH)+1)+"/"
                                +c.get(Calendar.YEAR)+" "+t;
                        VideoPost v = new VideoPost((String)doc.get(MyVideos.TITLE),
                                (String)doc.get(MyVideos.DESCRIPTION),
                                (String)doc.get(MyVideos.VIDEO_URL)
                                ,(String)doc.get(MyVideos.CATEGORY), time
                                ,(String)doc.get(MyVideos.RESOURCES));
                        tab.addVideoToList(v);
                    }
                    tab.notifyDataSetChanged();
                }else{ // task failed
                    new TanawarAlertDialog().showSimpleDialog(
                            "Loading Videos was Failed",
                            "The Error: "+task.getException().getMessage(), activity
                    ).show();
                    Log.e("VideoError",task.getException().getLocalizedMessage());
                }
            }
        });
    }
}
