package apis;

import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tanawar.tanawarandroidapp.ChatActivity;
import com.tanawar.tanawarandroidapp.MyChatsActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import helper_tools.TanawarAlertDialog;
import tanawar_objects.ChatItem;
import tanawar_objects.MyChatsItem;
import utils.TanawarCalendar;

public class ChatAPI {
    private FirebaseFirestore db;
    private static ChatAPI instance;
    private ChatAPI(){this.db = FirebaseFirestore.getInstance();}
    public static ChatAPI getInstance(){
        if (instance == null)
            instance = new ChatAPI();
        return instance;
    }
    /**
     * a method to add private lesson into the firestore.
     * @param sender
     * @param recipient
     */
    public void sendMessage(String sender, String recipient, String message,
                            final ChatActivity activity) {
        String docID = db.collection(FirebaseConstants.Collections.ChatDB.COLLECTION_NAME)
                .document().getId();
        Calendar c = Calendar.getInstance();
        String time = TanawarCalendar.dateTimeAsString(c);
        final ChatItem itm = new ChatItem(message, sender, recipient, c);
        Map<String,Object> doc = new HashMap<>();
        doc.put(FirebaseConstants.Collections.ChatDB.SENDER_EMAIL,sender);
        doc.put(FirebaseConstants.Collections.ChatDB.RECIPIENT_EMAIL,recipient);
        doc.put(FirebaseConstants.Collections.ChatDB.MESSAGE,message);
        doc.put(FirebaseConstants.Collections.ChatDB.DATETIME, time);
        db.collection(FirebaseConstants.Collections.ChatDB.COLLECTION_NAME).document(docID)
                .set(doc).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                activity.addDBItemToList(itm);
                activity.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new TanawarAlertDialog().showSimpleDialog(
                        "Adding Private Lesson was Failed",
                        "The Error: "+e.getMessage(),
                        activity
                ).show();
            }
        });
    }
    /**
     * @param sender
     * @param recipient
     * @param activity
     */
    public void loadAllChatMessages(final String sender, final String recipient,
                                    final ChatActivity activity) {
        this.db.collection(FirebaseConstants.Collections.ChatDB.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.ChatDB.SENDER_EMAIL,
                        sender)
                .whereEqualTo(FirebaseConstants.Collections.ChatDB.RECIPIENT_EMAIL,
                        recipient)
                .orderBy(FirebaseConstants.Collections.ChatDB.DATETIME,
                        Query.Direction.ASCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        String date = (String) doc.get(
                                FirebaseConstants.Collections.ChatDB.DATETIME);
                        Calendar c = TanawarCalendar.dateTimeAsCalendar(date);
                        ChatItem item = new ChatItem
                                ((String)doc.get(FirebaseConstants.Collections
                                        .ChatDB.MESSAGE),
                                        (String)doc.get(FirebaseConstants.Collections
                                                .ChatDB.SENDER_EMAIL)
                                        ,(String)doc.get(FirebaseConstants.Collections
                                        .ChatDB.RECIPIENT_EMAIL), c);
                        activity.addDBItemToList(item);
                    }
                    activity.notifyDataSetChanged();
                }else{ // task failed
                    new TanawarAlertDialog().showSimpleDialog(
                            "Loading Chats was Failed",
                            "The Error: "+task.getException().getMessage(),
                            activity
                    ).show();
                }
            }
        });
    }

    public void createConnection(final String senderEmail, final String senderName,
                                 final String recEmail, final String recName){
        this.db.collection(FirebaseConstants.Collections.MyChatsDB.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.MyChatsDB.SENDER_EMAIL,
                        senderEmail)
                .whereEqualTo(FirebaseConstants.Collections.MyChatsDB.RECIPIENT_EMAIL,
                        recEmail)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().isEmpty()){
                        checkReverseCondition(senderEmail, senderName, recEmail, recName);
                    }
                }else{ // task failed

                }
            }
        });
    }
    private void checkReverseCondition(final String senderEmail, final String senderName, final String recEmail,
                                       final String recName){
        this.db.collection(FirebaseConstants.Collections.MyChatsDB.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.MyChatsDB.SENDER_EMAIL,
                        recEmail)
                .whereEqualTo(FirebaseConstants.Collections.MyChatsDB.RECIPIENT_EMAIL,
                        senderEmail)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().isEmpty()){
                        addNewConnection(senderEmail,senderName,recEmail,recName);
                    }
                }else{ // task failed

                }
            }
        });
    }
    private void addNewConnection(String senderEmail, String senderName, String recEmail,
                                  String recName) {
        String docID = db.collection(FirebaseConstants.Collections.MyChatsDB.COLLECTION_NAME)
                .document().getId();
        Map<String,Object> doc = new HashMap<>();
        doc.put(FirebaseConstants.Collections.MyChatsDB.SENDER_EMAIL,senderEmail);
        doc.put(FirebaseConstants.Collections.MyChatsDB.RECIPIENT_EMAIL,recEmail);
        doc.put(FirebaseConstants.Collections.MyChatsDB.SENDER_NAME,senderName);
        doc.put(FirebaseConstants.Collections.MyChatsDB.RECIPIENT_NAME, recName);
        db.collection(FirebaseConstants.Collections.MyChatsDB.COLLECTION_NAME).document(docID)
                .set(doc).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void getAllConnections(final String email, final MyChatsActivity activity){
        this.db.collection(FirebaseConstants.Collections.MyChatsDB.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.MyChatsDB.SENDER_EMAIL,
                        email)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().isEmpty()) {
//                        new TanawarAlertDialog().showSimpleDialog("Your Chats",
//                                "No Chats Found!",
//                                activity).show();
//                        return;
                    }
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String se =
                                doc.getString(FirebaseConstants.Collections.MyChatsDB.SENDER_EMAIL);
                        String sn =
                                doc.getString(FirebaseConstants.Collections.MyChatsDB.SENDER_NAME);
                        String re =
                                doc.getString(FirebaseConstants.Collections.MyChatsDB.RECIPIENT_EMAIL);
                        String rn =
                                doc.getString(FirebaseConstants.Collections.MyChatsDB.RECIPIENT_NAME);
                        MyChatsItem i = new MyChatsItem(se,re,sn,rn);
                        activity.addDBItemToList(i);
                    }
                    loadReverseConnections(email ,activity);
                }else{ // task failed
                    new TanawarAlertDialog().showSimpleDialog("Wrong Action",
                            "Please Try Again!, cannot load your chat connections.",
                            activity).show();
                }
            }
        });
    }

    private void loadReverseConnections(final String email, final MyChatsActivity activity) {
        this.db.collection(FirebaseConstants.Collections.MyChatsDB.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.MyChatsDB.RECIPIENT_EMAIL,
                        email)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().isEmpty()) {
//                        new TanawarAlertDialog().showSimpleDialog("Your Chats",
//                                "No Chats Found!",
//                                activity).show();
//                        return;
                    }
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String se =
                                doc.getString(FirebaseConstants.Collections.MyChatsDB.SENDER_EMAIL);
                        String sn =
                                doc.getString(FirebaseConstants.Collections.MyChatsDB.SENDER_NAME);
                        String re =
                                doc.getString(FirebaseConstants.Collections.MyChatsDB.RECIPIENT_EMAIL);
                        String rn =
                                doc.getString(FirebaseConstants.Collections.MyChatsDB.RECIPIENT_NAME);
                        MyChatsItem i = new MyChatsItem(se,re,sn,rn);
                        activity.addDBItemToList(i);
                    }
                    activity.loadAllChats();
                }else{ // task failed
                    new TanawarAlertDialog().showSimpleDialog("Wrong Action",
                            "Please Try Again!, cannot load your chat connections.",
                            activity).show();
                }
            }
        });
    }
}
