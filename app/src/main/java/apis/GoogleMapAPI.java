package apis;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tanawar.tanawarandroidapp.ShareTeacherProfileOnMap;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import helper_tools.TanawarAlertDialog;
import tanawar_objects.TeacherOnMap;
import tanawar_objects.User;
import utils.AccountType;
import utils.TanawarCalendar;

public class GoogleMapAPI {
    /**
     *
     */
    private FirebaseFirestore db;
    private static GoogleMapAPI instance;
    public static GoogleMapAPI getInstance(){
        if(instance == null){
            instance = new GoogleMapAPI();
        }
        return instance;
    }
    private GoogleMapAPI(){
        this.db = FirebaseFirestore.getInstance();
    }
    public void getHelpRequest(String email, final TextView tvt){
        this.db.collection(FirebaseConstants.Collections.StudentLookingFor.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.StudentLookingFor.EMAIL,email)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        String desc = doc.getString(
                                        FirebaseConstants.Collections.StudentLookingFor.DESCRIPTION);
                        tvt.setText(desc);
                    }
                }else{ // task failed
                    Log.e("PostsError",task.getException().getLocalizedMessage());
                }
            }
        });
    }

    public void loadCurrentTeacherDescription(String myEmail, final TextView tvt) {
        this.db.collection(FirebaseConstants.Collections.TeachersOnMap.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.TeachersOnMap.EMAIL, myEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot doc : task.getResult()) {
                                String desc = (String) doc.get(
                                        FirebaseConstants.Collections.TeachersOnMap.DESCRIPTION);
                                tvt.setText(desc);
                            }
                        }else{
                            Log.i("TeachersOnMapError","Loading Teachers: "
                                    +task.getException().getMessage());
                        }
                    }
                });
    }

    public void addNewTeacherDescription(final User user, final String des,
                                         final ShareTeacherProfileOnMap shareTeacherProfileOnMap) {
        this.db.collection(FirebaseConstants.Collections.TeachersOnMap.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.TeachersOnMap.EMAIL, user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty()){
                                String docID = db.collection(FirebaseConstants.Collections
                                        .TeachersOnMap.COLLECTION_NAME)
                                        .document().getId();
                                updateExistTeacherDescription(docID, user, des,
                                        shareTeacherProfileOnMap);
                                return;
                            }
                            for (final QueryDocumentSnapshot doc : task.getResult()) {
                                String id = doc.getId();
                                updateExistTeacherDescription(id, user, des,
                                        shareTeacherProfileOnMap);
                            }
                        }else{
                            Log.i("TeachersOnMapError","Loading Teachers: "
                                    +task.getException().getMessage());
                        }
                    }
                });
    }

    public void updateExistTeacherDescription(String docID ,final User user, final String des,
                       final ShareTeacherProfileOnMap activity) {
        Calendar c = Calendar.getInstance();
        String time = TanawarCalendar.dateTimeAsString(c);
        Map<String,String> doc = new HashMap<>();
        doc.put(FirebaseConstants.Collections.TeachersOnMap.ADDRESS,user.getAddress());
        doc.put(FirebaseConstants.Collections.TeachersOnMap.DATETIME,time);
        doc.put(FirebaseConstants.Collections.TeachersOnMap.DESCRIPTION,des);
        doc.put(FirebaseConstants.Collections.TeachersOnMap.EMAIL,user.getEmail());
        doc.put(FirebaseConstants.Collections.TeachersOnMap.LATITUDE,user.getLatitude()+"");
        doc.put(FirebaseConstants.Collections.TeachersOnMap.LONGITUDE,user.getLongitude()+"");
        doc.put(FirebaseConstants.Collections.TeachersOnMap.DISPLAY_NAME,user.getDisplayName());
        doc.put(FirebaseConstants.Collections.TeachersOnMap.PHONE,user.getPhone());
        doc.put(FirebaseConstants.Collections.TeachersOnMap.GENDER,"true");
        db.collection(FirebaseConstants.Collections.TeachersOnMap.COLLECTION_NAME)
                .document(docID).set(doc).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                activity.setCurrentDesc(des);
                new TanawarAlertDialog().showSimpleDialog(
                        "Share Teacher Profile On Map",
                        "Sharing Succeed",
                        activity
                ).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new TanawarAlertDialog().showSimpleDialog(
                        "Share Teacher Profile On Map",
                        "Sharing Failed",
                        activity
                ).show();
            }
        });
    }
}
