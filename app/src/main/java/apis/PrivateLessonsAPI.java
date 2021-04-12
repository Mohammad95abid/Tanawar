package apis;

import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import apis.FirebaseConstants.Collections.PrivateLesson;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tanawar.tanawarandroidapp.AddPrivateLesson;
import com.tanawar.tanawarandroidapp.Login;
import com.tanawar.tanawarandroidapp.MyDiary;
import com.tanawar.tanawarandroidapp.StudentProfile;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import helper_tools.TanawarAlertDialog;
import tanawar_objects.PrivateLessonsDiaryItem;
import utils.PrivateLessonType;
import utils.TanawarCalendar;

public class PrivateLessonsAPI {
    private FirebaseFirestore db;
    private static PrivateLessonsAPI instance;
    private PrivateLessonsAPI(){this.db = FirebaseFirestore.getInstance();}
    public static PrivateLessonsAPI getInstance(){
        if (instance == null)
            instance = new PrivateLessonsAPI();
        return instance;
    }
    /**
     *
     * @param teacherEmail
     * @param studentEmailStr
     * @param datetime
     * @param teacherName
     * @param subjectStr
     * @param studentNameStr
     * @param lessonTypeStr
     * @param zoom
     * @param activity
     */
    public void checkIfTeacherAndStudentHaveLessonInGivenTime(final String teacherEmail,
                 final String studentEmailStr, final Calendar datetime, final String teacherName,
                 final String subjectStr, final String studentNameStr, final String lessonTypeStr,
                 final String zoom, final AddPrivateLesson activity) {
        this.db.collection(PrivateLesson.COLLECTION_NAME)
                .whereEqualTo(PrivateLesson.TEACHER_EMAIL,
                        teacherEmail)
//                .whereEqualTo(PrivateLesson.STUDENT_EMAIL,
//                        studentEmailStr)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        String tempTime = doc.getString(PrivateLesson.DATETIME);
                        tempTime = tempTime.substring(0, tempTime.lastIndexOf(":"));
                        tempTime = removeSpaces(tempTime);
                        String newTime = TanawarCalendar.dateTimeAsString(datetime);
                        newTime = newTime.substring(0, newTime.lastIndexOf(":"));
                        newTime = removeSpaces(newTime);
                        if(tempTime.equals(newTime)){
                            // conflict with another lesson
                            String time = doc.getString(PrivateLesson.DATETIME);
                            String stdName = doc.getString(PrivateLesson.STUDENT_NAME);
                            time = time.substring(0, time.lastIndexOf(":"));
                            activity.conflict("You and "+stdName+" have another lesson at "+time
                                    +", Please choose another time!");
                            return;
                        }
                    }
                    // can add this new lesson
                    activity.addNewLesson(teacherEmail, teacherName,
                            subjectStr, studentEmailStr,
                            studentNameStr, lessonTypeStr, datetime,
                            zoom ,activity);
                }
            }
        });
    }

    /**
     * a method to add private lesson into the firestore.
     * @param teacherEmail
     * @param teacherName
     * @param subjectStr
     * @param studentEmailStr
     * @param studentNameStr
     * @param lessonTypeStr
     * @param datetime
     */
    public void addPrivateLesson(String teacherEmail, String teacherName, String subjectStr,
                                 String studentEmailStr, String studentNameStr,
                                 String lessonTypeStr, Calendar datetime, String zoom,
                                 final AddPrivateLesson activity) {
        String docID = db.collection(FirebaseConstants.Collections.PrivateLesson.COLLECTION_NAME)
                .document().getId();
        String temp = TanawarCalendar.dateTimeAsString(datetime);
        Map<String,Object> doc = new HashMap<>();
        doc.put(FirebaseConstants.Collections.PrivateLesson.TEACHER_EMAIL,teacherEmail);
        doc.put(FirebaseConstants.Collections.PrivateLesson.TEACHER_NAME,teacherName);
        doc.put(FirebaseConstants.Collections.PrivateLesson.DATETIME,temp);
        doc.put(FirebaseConstants.Collections.PrivateLesson.SUBJECT,subjectStr);
        doc.put(FirebaseConstants.Collections.PrivateLesson.LESSON_TYPE,lessonTypeStr);
        doc.put(FirebaseConstants.Collections.PrivateLesson.STUDENT_EMAIL,studentEmailStr);
        doc.put(FirebaseConstants.Collections.PrivateLesson.STUDENT_NAME,studentNameStr);
        doc.put(FirebaseConstants.Collections.PrivateLesson.ZOOM_LINK,zoom);
        db.collection(FirebaseConstants.Collections.PrivateLesson.COLLECTION_NAME).document(docID)
                .set(doc).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                new TanawarAlertDialog().showSimpleDialogWithOKButton(
                        "Add Private Lesson",
                        "The New Private Lesson Saved Successfully!",
                        activity, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.goBack();
                            }
                        }
                ).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new TanawarAlertDialog().showSimpleDialogWithOKButton(
                        "Adding Private Lesson was Failed",
                        "The Error: "+e.getMessage(),
                        activity, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.goBack();
                            }
                        }
                ).show();
            }
        });
    }
    public void updatePrivateLessonDetails(final String teacherEmail, final String studentName,
                                           final String oldTime,
                                           final Calendar dateTime){
        this.db.collection(FirebaseConstants.Collections.PrivateLesson.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.PrivateLesson.TEACHER_EMAIL,
                        teacherEmail)
                .whereEqualTo(FirebaseConstants.Collections.PrivateLesson.STUDENT_NAME,
                        studentName)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    HashMap<String, String> toSet = new HashMap<>();
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                            String tempTime = doc.getString(PrivateLesson.DATETIME);
                            tempTime = tempTime.substring(0, tempTime.lastIndexOf(":"));
                            tempTime = removeSpaces(tempTime);
                            String oldTimeTemp = removeSpaces(oldTime);
                            if(tempTime.equals(oldTimeTemp)){
                                String id = doc.getId();
                                String teacherEmail = doc.getString(PrivateLesson.TEACHER_EMAIL);
                                toSet.put(PrivateLesson.TEACHER_EMAIL, teacherEmail);
                                String teacherName = doc.getString(PrivateLesson.TEACHER_NAME);
                                toSet.put(PrivateLesson.TEACHER_NAME, teacherName);
                                String studentEmail = doc.getString(PrivateLesson.STUDENT_EMAIL);
                                toSet.put(PrivateLesson.STUDENT_EMAIL, studentEmail);
                                toSet.put(PrivateLesson.STUDENT_NAME, studentName);
                                String current_time = doc.getString(PrivateLesson.DATETIME);
                                String stdName = doc.getString(PrivateLesson.STUDENT_NAME);
                                toSet.put(PrivateLesson.STUDENT_NAME, stdName);
                                String subject = doc.getString(PrivateLesson.SUBJECT);
                                toSet.put(PrivateLesson.SUBJECT, subject);
                                String lessonType = doc.getString(PrivateLesson.LESSON_TYPE);
                                toSet.put(PrivateLesson.LESSON_TYPE, lessonType);
                                String zoomLink = doc.getString(PrivateLesson.ZOOM_LINK);
                                toSet.put(PrivateLesson.ZOOM_LINK, zoomLink);
                                // update current lesson founded.
                                String temp = TanawarCalendar.dateTimeAsString(dateTime);
                                updateDocumentDetails(id, dateTime, toSet);
                            }
                    }
                }
            }
        });
    }

    private String removeSpaces(String oldTime) {
        if(oldTime == null)
            return "";
        return oldTime.replaceAll(" ", "");
    }
    /**
     *
     * @param id
     * @param dateTime
     * @param toSet
     */
    private void updateDocumentDetails(String id, Calendar dateTime,
                                       HashMap<String,String> toSet) {
        String temp = TanawarCalendar.dateTimeAsString(dateTime);
        toSet.put(FirebaseConstants.Collections.PrivateLesson.DATETIME, temp);
        this.db.collection(FirebaseConstants.Collections.PrivateLesson.COLLECTION_NAME)
                .document(id).set(toSet);
    }

    /**
     *
     * @param teacherEmail
     * @param activity
     */
    public void loadAllPrivateLessonByTeacherEmail(final String teacherEmail,
                                                   final MyDiary activity) {
        this.db.collection(FirebaseConstants.Collections.PrivateLesson.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.PrivateLesson.TEACHER_EMAIL,
                        teacherEmail)
                .orderBy(FirebaseConstants.Collections.PrivateLesson.DATETIME,
                        Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        String date = (String) doc.get(
                                FirebaseConstants.Collections.PrivateLesson.DATETIME);
                        Calendar c = TanawarCalendar.dateTimeAsCalendar(date);
                        PrivateLessonType type = PrivateLessonType.getByValue((String)doc.get(
                                FirebaseConstants.Collections.PrivateLesson.LESSON_TYPE));
                        PrivateLessonsDiaryItem item = new PrivateLessonsDiaryItem
                                (teacherEmail,type, c,
                                        (String)doc.get(FirebaseConstants.Collections
                                                .PrivateLesson.ZOOM_LINK),
                                        (String)doc.get(FirebaseConstants.Collections
                                                .PrivateLesson.SUBJECT)
                                        ,(String)doc.get(FirebaseConstants.Collections
                                        .PrivateLesson.STUDENT_NAME),
                                        (String)doc.get(FirebaseConstants.Collections
                                                .PrivateLesson.STUDENT_EMAIL));
                        activity.addDBItemToList(item);
                    }
                    activity.notifyDataSetChanged();
                }else{ // task failed
                    new TanawarAlertDialog().showSimpleDialog(
                            "Loading Database Files was Failed",
                            "The Error: "+task.getException().getMessage(),
                            activity
                    ).show();
                }
            }
        });
    }

    public void loadAllPrivateLessonByStudentEmail(final String studentEmail,
                                                   final StudentProfile activity) {
        this.db.collection(FirebaseConstants.Collections.PrivateLesson.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.PrivateLesson.STUDENT_EMAIL,
                        studentEmail)
                .orderBy(FirebaseConstants.Collections.PrivateLesson.DATETIME,
                        Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        String date = (String) doc.get(
                                FirebaseConstants.Collections.PrivateLesson.DATETIME);
                        Calendar c = TanawarCalendar.dateTimeAsCalendar(date);
                        PrivateLessonType type = PrivateLessonType.getByValue((String)doc.get(
                                FirebaseConstants.Collections.PrivateLesson.LESSON_TYPE));
                        PrivateLessonsDiaryItem item = new PrivateLessonsDiaryItem
                                ((String)doc.get(FirebaseConstants.Collections
                                        .PrivateLesson.TEACHER_EMAIL),
                                        ((String)doc.get(FirebaseConstants.Collections
                                                .PrivateLesson.TEACHER_NAME)), type, c,
                                        (String)doc.get(FirebaseConstants.Collections
                                                .PrivateLesson.ZOOM_LINK),
                                        (String)doc.get(FirebaseConstants.Collections
                                                .PrivateLesson.SUBJECT)
                                        ,(String)doc.get(FirebaseConstants.Collections
                                        .PrivateLesson.STUDENT_NAME),
                                        (String)doc.get(FirebaseConstants.Collections
                                                .PrivateLesson.STUDENT_EMAIL));
                        activity.addDBItemToList(item);
                    }
                    activity.notifyDataSetChanged();
                }else{ // task failed
                    new TanawarAlertDialog().showSimpleDialog(
                            "Loading Database Files was Failed",
                            "The Error: "+task.getException().getMessage(),
                            activity
                    ).show();
                }
            }
        });
    }

}
