package apis;
/**
 *
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import apis.FirebaseConstants.Collections.Users;
import apis.FirebaseConstants.Collections.StudentLookingFor;
import apis.FirebaseConstants.Collections.TeachersOnMap;
import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tanawar.tanawarandroidapp.AddAskHelpOnMap;
import com.tanawar.tanawarandroidapp.ChatActivity;
import com.tanawar.tanawarandroidapp.Homepage;
import com.tanawar.tanawarandroidapp.Login;
import com.tanawar.tanawarandroidapp.ShareTeacherProfileOnMap;
import com.tanawar.tanawarandroidapp.tabs.TanawarMabsTab;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import helper_tools.TanawarAlertDialog;
import tanawar_objects.TeacherOnMap;
import tanawar_objects.User;
import tanawar_objects.UserOnMap;
import utils.AccountType;
import utils.SharedPreferencesDB;
import utils.TanawarCalendar;

/**
 *
 */
public class UserAPI {
    /**
     * A variable to use in a singleton pattern.
     */
    private static UserAPI userAPI;
    /**
     *
     */
    private FirebaseFirestore db;
    /**
     *
     * @return
     */
    public static UserAPI getInstance(){
        if(userAPI == null)
            userAPI = new UserAPI();
        return userAPI;
    }
    /**
     *
     */
    private UserAPI(){
        this.db = FirebaseFirestore.getInstance();
    }
    /**
     *
     * @param user
     * @param activity
     */
    public void addUser(final User user, final Activity activity){
        FirestoreInterfaces.UsersCallback collback = new FirestoreInterfaces.UsersCallback(){
            @Override
            public void userExists(String title,String message) {
                new TanawarAlertDialog().showSimpleDialog(title,
                        message, activity).show();
            }
            @Override
            public void userNotExists() {
                addNewUser(user,activity, this);
            }
            @Override
            public void queryFailure(Exception e) {
                new TanawarAlertDialog().showSimpleDialog(
                        "Register Failed",
                        "New account registration failed, Try again!"+
                                "The Error: "+e.getMessage(), activity
                ).show();
            }
            @Override
            public void loginSuccessfully(User user) {

            }
            @Override
            public void loginFailure(String title, String message) {

            }
        };
        isUserExist(user , collback);
    }
    /**
     *
     * @param user
     * @param activity
     */
    private void addNewUser(final User user, final Activity activity,
                            final FirestoreInterfaces.UsersCallback collback){
        final String[] userID = new String[1];
        this.db.collection(FirebaseConstants.Collections.Users.COLLECTION_NAME).
                add(user.getMapCollection())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        userID[0] = documentReference.getId();
                        new TanawarAlertDialog().showSimpleDialogWithOKButton(
                                "Register Successfully",
                                "The user - "
                                        +"Email: "+user.getEmail()
                                        +" - added Successfully.", activity
                        , new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(activity, Login.class);
                                i.putExtra("email",user.getEmail());
                                i.putExtra("password",user.getPassword());
                                i.putExtra("userID", userID[0]);
                                activity.startActivity(i);
                            }
                        }).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       collback.queryFailure(e);
                    }
                });
    }
    /**
     *
     * @param user
     * @param collback
     */
    private void isUserExist(User user , final FirestoreInterfaces.UsersCallback collback) {
        CollectionReference users = db.collection(FirebaseConstants.Collections.Users.COLLECTION_NAME);
        Query isEmailExist = users.whereEqualTo("Email" , user.getEmail());
        final Query isPhoneExist = users.whereEqualTo("Phone" , user.getPhone());
        isEmailExist.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // QuerySnapshot an object contains all the documents as
                //      list after execute the query.
                // A QuerySnapshot contains the results of a query. it can contain
                //      zero or more DocumentSnapshot objects.
                if(queryDocumentSnapshots.isEmpty()){ // no results - Email not exists.
                    isPhoneExist.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots.isEmpty()){ // no results - Phone not exists.
                                collback.userNotExists();
                            }else{
                                collback.userExists("Faild to register",
                                        "This phone number exist in the system, " +
                                                "someone take it before you.");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) { collback.queryFailure(e);
                        }
                    });
                }else{ // there are results - user exists.
                    collback.userExists("Faild to register",
                            "This email exist in the system, someone take it before you.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                collback.queryFailure(e);
            }
        });
    }
    /**
     *
     * @param collback
     * @return
     */
    private void getUserByEmail(final String email, final String password,
                                final FirestoreInterfaces.UsersCallback collback,
                                final Activity activity){
        CollectionReference users = db.collection(
                FirebaseConstants.Collections.Users.COLLECTION_NAME);
        Query isEmailExist = users.whereEqualTo(
                FirebaseConstants.Collections.Users.EMAIL , email)
                .whereEqualTo(Users.PASSWORD,password);
        //final Query isPhoneExist = users.whereEqualTo("Phone" , email);
        isEmailExist.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int size = task.getResult().size();
                    if(size == 1){
                        User user = null;
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            Calendar birthday = TanawarCalendar.getDateAsCalendar(
                                    ""+doc.get(FirebaseConstants.Collections.
                                            Users.BIRTHDAY)
                            );
                            Calendar register = TanawarCalendar.getDateTimeAsCalendar(
                                    ""+doc.get(FirebaseConstants.Collections.
                                            Users.REGISTER_DATE)
                            );
                            user = new User(
                                    doc.get(FirebaseConstants.Collections.
                                            Users.FIRST_NAME)+"",
                                    doc.get(FirebaseConstants.Collections.
                                            Users.LAST_NAME)+"",
                                    doc.get(FirebaseConstants.Collections.Users.EMAIL)+"",
                                    doc.get(FirebaseConstants.Collections.Users.PASSWORD)+"",
                                    birthday, AccountType.getAccounType(
                                            doc.get(FirebaseConstants.Collections.
                                                 Users.ACCOUNT_TYPE)+""
                                       ),
                                    doc.get(FirebaseConstants.Collections.Users.PHONE)+"",
                                    register, doc.get(Users.ADDRESS)+""
                                    ,Double.parseDouble((String) doc.get(Users.LATITUDE)),
                                    Double.parseDouble((String) doc.get(Users.LONGITUDE)),
                                    (String) doc.get(Users.PROFILE_IMAGE_URI)
                            );
                        }
                        if(user == null){
                            collback.loginSuccessfully(new User(email,"",password));
                        }else{
                            collback.loginSuccessfully(user);
                            SharedPreferencesDB.getInstance().saveUser(user.getEmail(),
                                    user.getPassword(),user.getFname(),user.getLname()
                                    ,user.getAccountType().getValue(),
                                    TanawarCalendar.dateTimeAsString(user.getBirthday()),
                                    user.getPhone(), user.getLatitude()+"",
                                    user.getLongitude()+"", user.getProfileImGURL()
                                    ,activity.getApplicationContext());
                        }
                    }else{
                        collback.loginFailure("Login Failed",
                                "The username (Phone, Email) or the password is incorrect, " +
                                        "if you are new register and then lod in.");
                    }
                }else{
                    collback.queryFailure(task.getException());
                }
            }
        });
    }
    /**
     *
     * @param username
     * @param password
     */
    public void login(String username, String password, final Activity activity){
        FirestoreInterfaces.UsersCallback collback = new FirestoreInterfaces.UsersCallback() {
            @Override
            public void userExists(String title, String message) {

            }
            @Override
            public void userNotExists() {

            }
            @Override
            public void queryFailure(Exception e) {
                new TanawarAlertDialog().showSimpleDialog(
                        "Login Failed",
                        "Your try to Login Failed, Try again!"+
                                "The Error: "+e.getMessage(), activity
                ).show();
            }
            @Override
            public void loginSuccessfully(User user) {
                // User verified successfully, go to Homepage.
                Intent i = new Intent(activity, Homepage.class);
                i.putExtra(Users.FIRST_NAME,user.getFname());
                i.putExtra(Users.LAST_NAME,user.getLname());
                i.putExtra(Users.EMAIL,user.getEmail());
                i.putExtra(Users.ACCOUNT_TYPE,user.getAccountType().getValue());
                i.putExtra(Users.BIRTHDAY,TanawarCalendar.getDateTimeAsString(user.getBirthday()));
                i.putExtra(Users.PHONE,user.getPhone());
                i.putExtra(Users.PROFILE_IMAGE_URI,user.getProfileImGURL());
                i.putExtra(Users.LATITUDE, user.getLatitude()+"");
                i.putExtra(Users.LONGITUDE, user.getLongitude()+"");
                activity.startActivity(i);
                activity.finish();
            }
            @Override
            public void loginFailure(String title, String message) {
                new TanawarAlertDialog().showSimpleDialog(
                        title,
                        message, activity
                ).show();
            }
        };
        getUserByEmail(username,password,collback, activity);
    }

    /**
     *
     * @param accountType
     * @param tab
     * @return
     */
    public void getAllUsersOnMap(AccountType accountType, TanawarMabsTab tab) {
        if(AccountType.TEACHER.equals(accountType)){
            getAllStudentsLookingFor(tab);
        }else if(AccountType.STUDENT.equals(accountType)){
            getAllTeachers(tab);
        }else{ // Parents
            // get All Parents Implementation.
        }
    }
    /**
     *
     * @param tab
     */
    private void getAllStudentsLookingFor(final TanawarMabsTab tab) {
        final ArrayList<UserOnMap> res = new ArrayList<>();
        this.db.collection(FirebaseConstants.Collections.StudentLookingFor.COLLECTION_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot doc : task.getResult()) {
                                String temp = (String)doc.get(StudentLookingFor.DATETIME);
                                Calendar c = TanawarCalendar.dateTimeAsCalendar(temp);
                                String time = c.get(Calendar.HOUR_OF_DAY)
                                        + ":" + c.get(Calendar.MINUTE) + ":"
                                        + c.get(Calendar.SECOND);
                                String dateAsString = c.get(Calendar.DAY_OF_MONTH) + "/"
                                        + (c.get(Calendar.MONTH) + 1) + "/"
                                        + c.get(Calendar.YEAR);
                                String name = (String)doc.get(StudentLookingFor.DISPLAY_NAME);
                                String add = (String) doc.get(StudentLookingFor.ADDRESS);
                                String email = (String) doc.get(StudentLookingFor.EMAIL);
                                String phone = (String) doc.get(StudentLookingFor.PHONE);
                                String lat = (String) doc.get(StudentLookingFor.LATITUDE);
                                String lng = (String) doc.get(StudentLookingFor.LONGITUDE);
                                double latitude = Double.parseDouble(lat);
                                double longitude = Double.parseDouble(lng);
                                String desc = (String) doc.get(StudentLookingFor.DESCRIPTION);
                                UserOnMap user = new UserOnMap(AccountType.STUDENT,
                                        add,latitude,longitude,name,email,phone,
                                        desc,dateAsString,time);
                                res.add(user);
                            }
                            tab.setUsersList(res);
                        }else{
                            new TanawarAlertDialog().showSimpleDialog(
                                    "Loading Failed",
                                    "Loading the Students Looking For was Failed: "+
                                            "The Error: "+task.getException().getMessage(),
                                    tab.getActivity()).show();
                        }
                    }
                });
    }
    public void getStudentByEmail(String email, final AddAskHelpOnMap activity){
        CollectionReference users = db.collection(
                FirebaseConstants.Collections.Users.COLLECTION_NAME);
        Query isEmailExist = users.whereEqualTo(
                FirebaseConstants.Collections.Users.EMAIL , email);
        //final Query isPhoneExist = users.whereEqualTo("Phone" , email);
        isEmailExist.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int size = task.getResult().size();
                    if(size == 1){
                        User user = null;
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            user = new User(
                                    doc.get(FirebaseConstants.Collections.
                                            Users.FIRST_NAME)+"",
                                    doc.get(FirebaseConstants.Collections.
                                            Users.LAST_NAME)+"",
                                    doc.get(FirebaseConstants.Collections.Users.EMAIL)+"",
                                    "",
                                    null, AccountType.getAccounType(
                                    doc.get(FirebaseConstants.Collections.
                                            Users.ACCOUNT_TYPE)+""
                            ),
                                    doc.get(FirebaseConstants.Collections.Users.PHONE)+"",
                                    null, doc.get(Users.ADDRESS)+""
                                    ,Double.parseDouble((String) doc.get(Users.LATITUDE)),
                                    Double.parseDouble((String) doc.get(Users.LONGITUDE)),
                                    (String) doc.get(Users.PROFILE_IMAGE_URI)
                            );
                        }
                        activity.setUserData(user);
                    }
                }
            }
        });
    }

    /**
     * a method to add new request into Tanawar's Map.
     * @param m
     * @param activity
     */
    public void addAskHelpForStudent(final UserOnMap m, final AddAskHelpOnMap activity){
        this.db.collection(FirebaseConstants.Collections.StudentLookingFor.COLLECTION_NAME)
                .whereEqualTo(FirebaseConstants.Collections.StudentLookingFor.EMAIL,m.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().isEmpty()){
                        String docID = db.collection(FirebaseConstants.Collections
                                .StudentLookingFor.COLLECTION_NAME)
                                .document().getId();
                        addNewHelpRequest(docID, m, activity);
                        return;
                    }
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        String docID = doc.getId();
//                        String docID = db.collection(
//                                FirebaseConstants.Collections.StudentLookingFor.COLLECTION_NAME)
//                                .document().getId();
                        addNewHelpRequest(docID, m, activity);
                    }
                }else{ // task failed
                    Log.e("PostsError",task.getException().getLocalizedMessage());
                }
            }
        });
    }

    private void addNewHelpRequest(String docID, UserOnMap m, final AddAskHelpOnMap activity) {
        Calendar c = Calendar.getInstance();
        String time = TanawarCalendar.dateTimeAsString(c);
        Map<String,String> doc = new HashMap<>();
        doc.put(StudentLookingFor.ADDRESS,m.getAddress());
        doc.put(StudentLookingFor.DATETIME,time);
        doc.put(StudentLookingFor.DESCRIPTION,m.getDescription());
        doc.put(StudentLookingFor.EMAIL,m.getEmail());
        doc.put(StudentLookingFor.LATITUDE,m.getLatitude()+"");
        doc.put(StudentLookingFor.LONGITUDE,m.getLongitude()+"");
        doc.put(StudentLookingFor.DISPLAY_NAME,m.getDisplayName());
        doc.put(StudentLookingFor.PHONE,m.getPhone());
        db.collection(FirebaseConstants.Collections.StudentLookingFor.COLLECTION_NAME)
                .document(docID).set(doc).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                new TanawarAlertDialog().showSimpleDialog(
                        "Adding Ask Help On Map",
                        "Adding Succeed!",
                        activity
                ).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new TanawarAlertDialog().showSimpleDialog(
                        "Adding Ask Help On Map",
                        "Adding Failed",
                        activity
                ).show();
            }
        });
    }

    /**
     *
     * @param tab
     */
    private void getAllTeachers(final TanawarMabsTab tab) {
        final ArrayList<UserOnMap> res = new ArrayList<>();
        this.db.collection(TeachersOnMap.COLLECTION_NAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot doc : task.getResult()) {
//                                Date date = doc.getDate(
//                                        TeachersOnMap.DATETIME);
                                String temp = doc.getString(TeachersOnMap.DATETIME);
                                Calendar c = TanawarCalendar.dateTimeAsCalendar(temp);
//                                Calendar c = Calendar.getInstance();
//                                c.setTime(date);
                                String time = c.get(Calendar.HOUR_OF_DAY)
                                        + ":" + c.get(Calendar.MINUTE) + ":"
                                        + c.get(Calendar.SECOND);
                                String dateAsString = c.get(Calendar.DAY_OF_MONTH) + "/"
                                        + (c.get(Calendar.MONTH) + 1) + "/"
                                        + c.get(Calendar.YEAR);
                                String name = (String)doc.get(TeachersOnMap.DISPLAY_NAME);
                                String add = (String) doc.get(TeachersOnMap.ADDRESS);
                                String email = (String) doc.get(TeachersOnMap.EMAIL);
                                String phone = (String) doc.get(TeachersOnMap.PHONE);
                                String lat = (String) doc.get(TeachersOnMap.LATITUDE);
                                String lng = (String) doc.get(TeachersOnMap.LONGITUDE);
                                double latitude = Double.parseDouble(lat);
                                double longitude = Double.parseDouble(lng);
                                String desc = (String) doc.get(TeachersOnMap.DESCRIPTION);
                                boolean gender =
                                        Boolean.valueOf(doc.getString(TeachersOnMap.GENDER));
                                // boolean gender = (boolean) doc.get(TeachersOnMap.GENDER);
                                TeacherOnMap user =new TeacherOnMap(AccountType.TEACHER,
                                        add,latitude,longitude,name,email,phone,
                                        desc,dateAsString,time,gender);
                                res.add(user);
                            }
                            tab.setUsersList(res);
                        }else{
                            new TanawarAlertDialog().showSimpleDialog(
                                    "Loading Failed",
                                    "Loading the 'Teachers On Map' was Failed: "+
                                            "The Error: "+task.getException().getMessage(),
                                    tab.getActivity()).show();
                            Log.i("TeachersOnMapError","Loading Teachers: "
                                    +task.getException().getMessage());
                        }
                    }
                });
    }
    /**
     *
     * @return
     */
    public void getUsernameByEmail(final String email, final ChatActivity activity){
        CollectionReference users = db.collection(
                FirebaseConstants.Collections.Users.COLLECTION_NAME);
        Query isEmailExist = users.whereEqualTo(
                FirebaseConstants.Collections.Users.EMAIL , email);
        //final Query isPhoneExist = users.whereEqualTo("Phone" , email);
        isEmailExist.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int size = task.getResult().size();
                    if(size == 1) {
                        User user = null;
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String username = doc.get(FirebaseConstants.Collections.
                                    Users.FIRST_NAME) + " " +
                                    doc.get(FirebaseConstants.Collections.
                                            Users.LAST_NAME);
                             activity.updateRecipientName(username);
                        }
                    }

                }
            }
        });
    }
    /**
     *
     * @return
     */
    public void getUsernameByEmail(final String email, final TanawarMabsTab tab, final Marker marker){
        CollectionReference users = db.collection(
                FirebaseConstants.Collections.Users.COLLECTION_NAME);
        Query isEmailExist = users.whereEqualTo(
                FirebaseConstants.Collections.Users.EMAIL , email);
        //final Query isPhoneExist = users.whereEqualTo("Phone" , email);
        isEmailExist.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int size = task.getResult().size();
                    if(size == 1) {
                        User user = null;
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String username = doc.get(FirebaseConstants.Collections.
                                    Users.FIRST_NAME) + " " +
                                    doc.get(FirebaseConstants.Collections.
                                            Users.LAST_NAME);
                            tab.setUserName(username, AccountType.getAccounType(
                                    doc.get(FirebaseConstants.Collections.Users.ACCOUNT_TYPE)+""),
                                    marker);
                        }
                    }
                }
            }
        });
    }

    public void getTeacherByEmail(final String des, String myEmail,
                                  final ShareTeacherProfileOnMap shareTeacherProfileOnMap) {
        // GoogleMapAPI.getInstance().addNewTeacherDescription(
        //                    Homepage.myEmail, des, this);
        CollectionReference users = db.collection(
                FirebaseConstants.Collections.Users.COLLECTION_NAME);
        Query isEmailExist = users.whereEqualTo(
                FirebaseConstants.Collections.Users.EMAIL , myEmail);
        isEmailExist.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int size = task.getResult().size();
                    if(size == 1){
                        User user = null;
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            user = new User(
                                    doc.get(FirebaseConstants.Collections.
                                            Users.FIRST_NAME)+"",
                                    doc.get(FirebaseConstants.Collections.
                                            Users.LAST_NAME)+"",
                                    doc.get(FirebaseConstants.Collections.Users.EMAIL)+"",
                                    doc.get(FirebaseConstants.Collections.Users.PASSWORD)+"",
                                    null, AccountType.getAccounType(
                                    doc.get(FirebaseConstants.Collections.
                                            Users.ACCOUNT_TYPE)+""
                            ),
                                    doc.get(FirebaseConstants.Collections.Users.PHONE)+"",
                                    null, doc.get(Users.ADDRESS)+""
                                    ,Double.parseDouble((String) doc.get(Users.LATITUDE)),
                                    Double.parseDouble((String) doc.get(Users.LONGITUDE)),
                                    (String) doc.get(Users.PROFILE_IMAGE_URI)
                            );
                            GoogleMapAPI.getInstance().addNewTeacherDescription(user,
                                    des, shareTeacherProfileOnMap);
                            return;
                        }

                    }else{

                    }
                }else{

                }
            }
        });
    }
}
