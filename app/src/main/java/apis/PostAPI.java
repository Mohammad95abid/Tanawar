package apis;

import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tanawar.tanawarandroidapp.Teacher_AddPost;
import com.tanawar.tanawarandroidapp.Teacher_MyPosts;
import com.tanawar.tanawarandroidapp.tabs.TanawarPostsTab;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import helper_tools.TanawarAlertDialog;
import tanawar_objects.Post;

public class PostAPI {
    /**
     * An instance of the firestore database, used to read and write data.
     */
    private FirebaseFirestore db;
    /**
     * An instance of the firebase storage , used to upload and download file.
     */
    private StorageReference mStorageRef;
    /**
     * A variable to use in a singleton pattern.
     */
    private static PostAPI instance = null;
    public static PostAPI getInstance(){
        if(instance == null){
            instance = new PostAPI();
        }
        return instance;
    }
    private PostAPI(){
        this.mStorageRef = FirebaseStorage.getInstance().getReference();
        this.db = FirebaseFirestore.getInstance();
    }
    public void getTeacherPostsOrderByPublishedTime(String category, String language,
                                                    String email, final Teacher_MyPosts tab){
        if("All".equals(language)){ // videos in all languages
            loadAllTeacherPostsByCategory(category, email, tab);
        }else{ // videos in specific language
            loadTeacherPostsByCategoryAndLanguage(category, language, email, tab);
        }
    }
    /**
     *
     * @param category
     * @param language
     * @param email
     * @param tab
     */
    private void loadTeacherPostsByCategoryAndLanguage(String category, String language,
                                                       String email, final Teacher_MyPosts tab) {
        this.db.collection(FirebaseConstants.Collections.Posts.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.Posts.OWNER_EMAIL,email)
                .whereEqualTo(FirebaseConstants.Collections.Posts.CATEGORY,category)
                .whereEqualTo(FirebaseConstants.Collections.Posts.LANGUAGE,language)
                .orderBy(FirebaseConstants.Collections.Posts.DATETIME,
                        Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        Date date = doc.getDate(FirebaseConstants.Collections.Posts.DATETIME);
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        String t = c.get(Calendar.HOUR_OF_DAY)
                                +":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                        String time = c.get(Calendar.DAY_OF_MONTH)+"/"
                                +(c.get(Calendar.MONTH)+1)+"/"
                                +c.get(Calendar.YEAR);
                        Post p = new Post((String)doc.get(
                                FirebaseConstants.Collections.Posts.TITLE),
                                (String)doc.get(FirebaseConstants.Collections.Posts.DESCRIPTION),
                                (String)doc.get(FirebaseConstants.Collections.Posts.IMAGE_URL)
                                ,(String)doc.get(FirebaseConstants.Collections.Posts.DATA_FILE_URL)
                                ,(String)doc.get(FirebaseConstants.Collections.Posts.OWNER_NAME)
                                , time , t
                                ,(String)doc.get(FirebaseConstants.Collections.Posts.OWNER_EMAIL)
                                ,(String)doc.get(FirebaseConstants.Collections.Posts.CATEGORY));
                        tab.addPostToList(p);
                    }
                    tab.notifyDataSetChanged();
                }else{ // task failed
                    new TanawarAlertDialog().showSimpleDialog(
                            "Loading TeacherOnMap Posts was Failed",
                            "The Error: "+task.getException().getMessage(), tab
                    ).show();
                    Log.e("PostsError",task.getException().getLocalizedMessage());
                }
            }
        });
    }
    /**
     *
     * @param category
     * @param email
     * @param tab
     */
    private void loadAllTeacherPostsByCategory(String category,String email,
                                               final Teacher_MyPosts tab) {
        this.db.collection(FirebaseConstants.Collections.Posts.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.Posts.OWNER_EMAIL,email)
                .whereEqualTo(FirebaseConstants.Collections.Posts.CATEGORY,category)
                .orderBy(FirebaseConstants.Collections.Posts.DATETIME,
                        Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        Date date = doc.getDate(FirebaseConstants.Collections.Posts.DATETIME);
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        String t = c.get(Calendar.HOUR_OF_DAY)
                                +":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                        String time = c.get(Calendar.DAY_OF_MONTH)+"/"
                                +(c.get(Calendar.MONTH)+1)+"/"
                                +c.get(Calendar.YEAR);
                        Post p = new Post((String)doc.get(
                                FirebaseConstants.Collections.Posts.TITLE),
                                (String)doc.get(FirebaseConstants.Collections.Posts.DESCRIPTION),
                                (String)doc.get(FirebaseConstants.Collections.Posts.IMAGE_URL)
                                ,(String)doc.get(FirebaseConstants.Collections.Posts.DATA_FILE_URL)
                                ,(String)doc.get(FirebaseConstants.Collections.Posts.OWNER_NAME)
                                , time , t
                                ,(String)doc.get(FirebaseConstants.Collections.Posts.OWNER_EMAIL)
                                ,(String)doc.get(FirebaseConstants.Collections.Posts.CATEGORY));
                        tab.addPostToList(p);
                    }
                    tab.notifyDataSetChanged();
                }else{ // task failed
                    new TanawarAlertDialog().showSimpleDialog(
                            "Loading TeacherOnMap Posts was Failed",
                            "The Error: "+task.getException().getMessage(), tab
                    ).show();
                    Log.e("PostsError",task.getException().getLocalizedMessage());
                }
            }
        });
    }
    // The follow part to get all the posts in the app DB.
    /**
     *
     * @param category
     * @param language
     * @param tab
     */
    public void getAllPostsOrderByPublishedTime(String category, String language,
                                                TanawarPostsTab tab) {
        if("All".equals(language)){ // posts in all languages
            loadAllPostsByCategory(category, tab);
        }else{ // posts in specific language
            loadPostsByCategoryAndLanguage(category, language, tab);
        }
    }
    /**
     *
     * @param category
     * @param language
     * @param tab
     */
    private void loadPostsByCategoryAndLanguage(String category, String language,
                                                final TanawarPostsTab tab) {
        this.db.collection(FirebaseConstants.Collections.Posts.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.Posts.CATEGORY,category)
                .whereEqualTo(FirebaseConstants.Collections.Posts.LANGUAGE,language)
                .orderBy(FirebaseConstants.Collections.Posts.DATETIME,
                        Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        Date date = doc.getDate(FirebaseConstants.Collections.Posts.DATETIME);
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        String t = c.get(Calendar.HOUR_OF_DAY)
                                +":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                        String time = c.get(Calendar.DAY_OF_MONTH)+"/"
                                +(c.get(Calendar.MONTH)+1)+"/"
                                +c.get(Calendar.YEAR);
                        Post p = new Post((String)doc.get(
                                FirebaseConstants.Collections.Posts.TITLE),
                                (String)doc.get(FirebaseConstants.Collections.Posts.DESCRIPTION),
                                (String)doc.get(FirebaseConstants.Collections.Posts.IMAGE_URL)
                                ,(String)doc.get(FirebaseConstants.Collections.Posts.DATA_FILE_URL)
                                ,(String)doc.get(FirebaseConstants.Collections.Posts.OWNER_NAME)
                                , time , t
                                ,(String)doc.get(FirebaseConstants.Collections.Posts.OWNER_EMAIL)
                                ,(String)doc.get(FirebaseConstants.Collections.Posts.CATEGORY));
                        tab.addPostToList(p);
                    }
                    tab.notifyDataSetChanged();
                }else{ // task failed
                    new TanawarAlertDialog().showSimpleDialog(
                            "Loading Posts was Failed",
                            "The Error: "+task.getException().getMessage(),
                             tab.getActivity()
                    ).show();
                    Log.e("PostsError",task.getException().getLocalizedMessage());
                }
            }
        });
    }
    /**
     *
     * @param category
     * @param tab
     */
    private void loadAllPostsByCategory(String category, final TanawarPostsTab tab) {
        this.db.collection(FirebaseConstants.Collections.Posts.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.Posts.CATEGORY,category)
                .orderBy(FirebaseConstants.Collections.Posts.DATETIME,
                        Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        Date date = doc.getDate(FirebaseConstants.Collections.Posts.DATETIME);
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        String t = c.get(Calendar.HOUR_OF_DAY)
                                +":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                        String time = c.get(Calendar.DAY_OF_MONTH)+"/"
                                +(c.get(Calendar.MONTH)+1)+"/"
                                +c.get(Calendar.YEAR);
                        Post p = new Post((String)doc.get(
                                FirebaseConstants.Collections.Posts.TITLE),
                                (String)doc.get(FirebaseConstants.Collections.Posts.DESCRIPTION),
                                (String)doc.get(FirebaseConstants.Collections.Posts.IMAGE_URL)
                                ,(String)doc.get(FirebaseConstants.Collections.Posts.DATA_FILE_URL)
                                ,(String)doc.get(FirebaseConstants.Collections.Posts.OWNER_NAME)
                                , time , t
                                ,(String)doc.get(FirebaseConstants.Collections.Posts.OWNER_EMAIL)
                                ,(String)doc.get(FirebaseConstants.Collections.Posts.CATEGORY));
                        tab.addPostToList(p);
                    }
                    tab.notifyDataSetChanged();
                }else{ // task failed
                    new TanawarAlertDialog().showSimpleDialog(
                            "Loading Posts was Failed",
                            "The Error: "+task.getException().getMessage(),
                            tab.getActivity()
                    ).show();
                    Log.e("PostsError",task.getException().getLocalizedMessage());
                }
            }
        });
    }
    // The follow part to save teacher post into the DB.
    public void addPost(String title, String language, String category, String imageURL,
                        String description, Timestamp datetime, String datafileURL,
                        String ownerName, String ownerEmail, final Teacher_AddPost activity){
        String docID = db.collection(FirebaseConstants.Collections.Posts.COLLECTION_NAME)
                .document().getId();
        Map<String,Object> doc = new HashMap<>();
        doc.put(FirebaseConstants.Collections.Posts.CATEGORY,category);
        doc.put(FirebaseConstants.Collections.Posts.DATA_FILE_URL,datafileURL);
        doc.put(FirebaseConstants.Collections.Posts.DATETIME,datetime);
        doc.put(FirebaseConstants.Collections.Posts.DESCRIPTION,description);
        doc.put(FirebaseConstants.Collections.Posts.IMAGE_URL,imageURL);
        doc.put(FirebaseConstants.Collections.Posts.LANGUAGE,language);
        doc.put(FirebaseConstants.Collections.Posts.OWNER_EMAIL,ownerEmail);
        doc.put(FirebaseConstants.Collections.Posts.OWNER_NAME,ownerName);
        doc.put(FirebaseConstants.Collections.Posts.TITLE,title);
        db.collection(FirebaseConstants.Collections.Posts.COLLECTION_NAME).document(docID)
                .set(doc).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                new TanawarAlertDialog().showSimpleDialog(
                        "Create Post ",
                        "Your post saved, uploaded to the database successfully.",
                         activity).show();
                activity.clearForum();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new TanawarAlertDialog().showSimpleDialog(
                        "Uploading Post was Failed",
                        "The Error: "+e.getMessage(), activity
                ).show();
                Log.e("PostsError",e.getLocalizedMessage());
            }
        });
    }

    /**
     * A method to upload an image to the storage before uploading the post to the firestore.
     * @param imageName the image name.
     * @param imgUri used to upload the image.
     * @param activity used to show dialogs.
     */
    public void uploadPostImage(String imageName, Uri imgUri, final Teacher_AddPost activity){
        final StorageReference ref = mStorageRef.child(
                FirebaseConstants.Storage.Paths.IMAGES_PARENT_PATH+imageName);
        UploadTask uploadTask = ref.putFile(imgUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,
                Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    activity.uploadPost(downloadUri.toString());
                } else {
                    new TanawarAlertDialog().showSimpleDialog(
                            "Uploading Post's Image was Failed",
                            "The Error: "+task.getException().getMessage(), activity
                    ).show();
                    Log.e("PostsError",task.getException().getLocalizedMessage());
                }
            }
        });
    }
}
