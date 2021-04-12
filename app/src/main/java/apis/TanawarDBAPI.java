package apis;

import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tanawar.tanawarandroidapp.tabs.TanawarDatabaseTab;

import java.util.Calendar;
import java.util.Date;
import helper_tools.TanawarAlertDialog;
import tanawar_objects.DBItem;

public class TanawarDBAPI {
    private FirebaseFirestore db;
    private StorageReference mStorageRef;
    /**
     * A variable to use in a singleton pattern.
     */
    private static TanawarDBAPI instance = null;
    public static TanawarDBAPI getInstance(){
        if(instance == null){
            instance = new TanawarDBAPI();
        }
        return instance;
    }
    private TanawarDBAPI(){
        this.mStorageRef = FirebaseStorage.getInstance().getReference();
        this.db = FirebaseFirestore.getInstance();
    }

    public void getAllDBItemsOrderByPublishedTime(String category, String language,
                                                  TanawarDatabaseTab tab) {
            if("All".equals(language)){ // items in all languages
                loadAllItemssByCategory(category, tab);
            }else{ // items in specific language
                loadItemsByCategoryAndLanguage(category, language, tab);
            }
    }
    /**
     *
     * @param category
     * @param tab
     */
    private void loadAllItemssByCategory(String category, final TanawarDatabaseTab tab) {
        this.db.collection(FirebaseConstants.Collections.TanawarDB.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.TanawarDB.CATEGORY,category)
                .orderBy(FirebaseConstants.Collections.TanawarDB.DATETIME,
                        Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        Date date = doc.getDate(FirebaseConstants.Collections.TanawarDB.DATETIME);
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        String t = c.get(Calendar.HOUR_OF_DAY)
                                +":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                        String dateSTR = c.get(Calendar.DAY_OF_MONTH)+"/"
                                +(c.get(Calendar.MONTH)+1)+"/"
                                +c.get(Calendar.YEAR);
                        DBItem item = new DBItem(dateSTR+" "+t,(String)doc.get(
                                FirebaseConstants.Collections.TanawarDB.TITLE),
                                (String)doc.get(FirebaseConstants.Collections
                                        .TanawarDB.DESCRIPTION)
                                ,(String)doc.get(FirebaseConstants.Collections
                                        .TanawarDB.DATA_FILE_URL));
                        tab.addDBItemToList(item);
                    }
                    tab.notifyDataSetChanged();
                }else{ // task failed
                    new TanawarAlertDialog().showSimpleDialog(
                            "Loading Database Files was Failed",
                            "The Error: "+task.getException().getMessage(),
                            tab.getActivity()
                    ).show();
                    Log.e("DatabaseFilesError",task.getException().getLocalizedMessage());
                }
            }
        });
    }
    /**
     *
     * @param language
     * @param category
     * @param tab
     */
    private void loadItemsByCategoryAndLanguage(String category, String language,
                                                final TanawarDatabaseTab tab) {
        this.db.collection(FirebaseConstants.Collections.TanawarDB.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.TanawarDB.CATEGORY,category)
                .whereEqualTo(FirebaseConstants.Collections.TanawarDB.LANGUAGE,language)
                .orderBy(FirebaseConstants.Collections.TanawarDB.DATETIME,
                        Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        Date date = doc.getDate(FirebaseConstants.Collections.TanawarDB.DATETIME);
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        String t = c.get(Calendar.HOUR_OF_DAY)
                                +":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                        String dateSTR = c.get(Calendar.DAY_OF_MONTH)+"/"
                                +(c.get(Calendar.MONTH)+1)+"/"
                                +c.get(Calendar.YEAR);
                        DBItem item = new DBItem(dateSTR+" "+t,(String)doc.get(
                                FirebaseConstants.Collections.TanawarDB.TITLE),
                                (String)doc.get(FirebaseConstants.Collections
                                        .TanawarDB.DESCRIPTION)
                                ,(String)doc.get(FirebaseConstants.Collections
                                .TanawarDB.DATA_FILE_URL));
                        tab.addDBItemToList(item);
                    }
                    tab.notifyDataSetChanged();
                }else{ // task failed
                    new TanawarAlertDialog().showSimpleDialog(
                            "Loading Database Files was Failed",
                            "The Error: "+task.getException().getMessage(),
                            tab.getActivity()
                    ).show();
                    Log.e("DatabaseFilesError",task.getException().getLocalizedMessage());
                }
            }
        });
    }
}
