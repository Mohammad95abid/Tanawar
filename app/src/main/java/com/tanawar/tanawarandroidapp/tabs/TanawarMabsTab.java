package com.tanawar.tanawarandroidapp.tabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.tanawar.tanawarandroidapp.ChatActivity;
import com.tanawar.tanawarandroidapp.Homepage;
import com.tanawar.tanawarandroidapp.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import apis.FirebaseConstants;
import apis.UserAPI;
import helper_tools.TanawarAlertDialog;
import tanawar_objects.TeacherOnMap;
import tanawar_objects.UserOnMap;
import utils.AccountType;

/**
 *
 */
public class TanawarMabsTab extends Fragment implements OnMapReadyCallback{
    private View view;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private MarkerOptions markerOptions;
    private AccountType accountType;
    private Marker myLocationMarker;
    //
    private CustomInfoWindowAdapter customInfo;
    private ArrayList<UserOnMap> users;
    private ArrayList<UserOnMap> finalusers;
    private Activity activity;
    private String my_name;
    private String second_user_name;
    private boolean is_ready;
    private String second_user_email;
    private ProgressDialog dialog;
    public TanawarMabsTab(AccountType accountType, Activity activity){
        this.accountType = accountType;
        users = new ArrayList<>();
        finalusers = new ArrayList<>();
        this.activity = activity;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.google_map,container,false);
        dialog = new ProgressDialog(view.getContext());
        dialog.setTitle("Getting Your Location");
        dialog.setMessage("Pleas wait to load your location details!");
        dialog.show();
        //
        mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapAPI_ID);
            if(mapFragment == null){
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                mapFragment = SupportMapFragment.newInstance();
                ft.replace(R.id.mapAPI_ID,mapFragment).commit();
            }
            mapFragment.getMapAsync(this);
        //
        return view;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // to center this latlng in the center of user's mobile screen .
        LatLng latLng = new LatLng(Homepage.my_address_lat,
                Homepage.my_address_long);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // add custom information window for each marker.
        customInfo = new CustomInfoWindowAdapter(this.getContext());
        mMap.setInfoWindowAdapter(customInfo);
        // load the users on map list.
        UserAPI.getInstance().getAllUsersOnMap(accountType, TanawarMabsTab.this);
    }

    public void setUsersList(ArrayList<UserOnMap> users) {
        for (UserOnMap u: users) {
            if(u!=null){
                this.users.add(u);
            }
        }
        if(!this.users.isEmpty()){
            for (UserOnMap u:this.users) {
                if(u!=null){
                    markerStudentOnMap(u);
                }
            }
        }
    }
    /**
     *
     * @param lat
     * @param lng
     */
    public void setLocationWithoutMarker(double lat, double lng){
        LatLng latLng = new LatLng(lat,lng);
        markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        // add marker title .
        markerOptions.title("I'm Here");
        // add marker snippet .
        String snip = "My Location"+"-"+" "+"-"+" "+"-"+" "+"-"+
                " "+"-"+" ";
        markerOptions.snippet(snip);
        // to center this latlng in the center of user's mobile screen .
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5.0f));
        myLocationMarker = mMap.addMarker(markerOptions);
        dialog.dismiss();
    }

    public void markerStudentOnMap(UserOnMap user) {
        LatLng ltlng = new LatLng(user.getLatitude(),user.getLongitude());
        String snippet = getMarkerData(user);
        BitmapDescriptor bit = null;
        switch (user.getAccountType()){
            case TEACHER:
                if(user instanceof TeacherOnMap){
                    if(((TeacherOnMap)user).isMale()){ // Male Teacher
                        bit = BitmapDescriptorFactory.fromResource(R.drawable
                                .male_teacher_map_icon);
                    }else { // Female Teacher
                        bit = BitmapDescriptorFactory.fromResource(R.drawable
                                .female_teacher_map_icon);
                    }
                }
                break;
            case STUDENT:
                bit = BitmapDescriptorFactory.fromResource(R.drawable
                        .student_map_icon);
                break;
//            case PARENT:
//                bit = null;
//                break;
            default:
                bit = null;
                break;
        }
        if(bit != null){
            MarkerOptions mo = new MarkerOptions().position(ltlng).snippet(snippet).icon(bit);
            mMap.addMarker(mo);
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(final Marker marker) {
                    final AlertDialog.Builder builder =
                            new AlertDialog.Builder(getContext());
                    CharSequence itms[] = new CharSequence[]{"Call","Chat"};
                    builder.setNegativeButton("Cancel", null);
                    builder.setTitle("Select Connection Way:");
                    builder.setSingleChoiceItems(itms, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            switch (which){
                                case 0:
                                    call(marker);
                                    break;
                                case 1:
                                    chat(marker);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    builder.show();
                }
            });
        }
    }

    private void chat(Marker marker) {
        try{
            my_name = Homepage.myName;
            if(my_name == null || my_name.isEmpty()){
                UserAPI.getInstance().getUsernameByEmail(Homepage.myEmail, this, marker);
            }else if((second_user_name ==null || second_user_name.isEmpty())
                    && marker != null && marker.getSnippet() != null ) {
                String second = marker.getSnippet().split("-")[2];
                second_user_email = second;
                UserAPI.getInstance().getUsernameByEmail(second, this, marker);
            }
        }catch (Exception e){

        }

    }
    private void callChatActivity() {
        Intent i = new Intent(activity, ChatActivity.class);
        if(accountType == AccountType.TEACHER){
            i.putExtra(FirebaseConstants.Collections.PrivateLesson.TEACHER_EMAIL, Homepage.myEmail);
            i.putExtra(FirebaseConstants.Collections.PrivateLesson.TEACHER_NAME, my_name);
            i.putExtra(FirebaseConstants.Collections.PrivateLesson.STUDENT_NAME, second_user_name);
            i.putExtra(FirebaseConstants.Collections.PrivateLesson.STUDENT_EMAIL, second_user_email);
        }else if(accountType == AccountType.STUDENT){
            i.putExtra(FirebaseConstants.Collections.PrivateLesson.TEACHER_EMAIL, second_user_email);
            i.putExtra(FirebaseConstants.Collections.PrivateLesson.TEACHER_NAME, second_user_name);
            i.putExtra(FirebaseConstants.Collections.PrivateLesson.STUDENT_NAME, my_name);
            i.putExtra(FirebaseConstants.Collections.PrivateLesson.STUDENT_EMAIL, Homepage.myEmail);
        }
        second_user_name = null;
        second_user_email = null;
        startActivity(i);
    }
    private void call(Marker marker) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            String phone = marker.getSnippet().split("-")[3];
            callIntent.setData(Uri.parse("tel: "+phone));
            startActivity(callIntent);
//          if (ActivityCompat.checkSelfPermission(getContext(),
//                 Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        }catch (Exception e){
            new TanawarAlertDialog().showSimpleDialog("Calling Failed",
                    "failed to start phone dial right now," +
                            "\n error occurred, please try later.", activity);
        }
    }

    public void setUserName(String username, AccountType type, Marker marker) {
            if(type != accountType){
                //this is the second_user_name
                second_user_name = username;
            }else{
                // this is my name
                my_name = username;
            }
            if(second_user_name != null && !second_user_name.isEmpty()
                    && my_name != null && !my_name.isEmpty()){
                is_ready = true;
                callChatActivity();
            }else{
                chat(marker);
                is_ready = false;
            }
    }
    /**
     *
     */
    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View mWindow;
        private Context context;
        private TextView account_type;
        private TextView display_name;
//        private TextView address;
        private TextView email;
        private TextView phone;
        private TextView description;
        private TextView datetime;
        private ImageView call;
        private ImageView gmail;
        public CustomInfoWindowAdapter(Context context){
            this.context = context;
            mWindow = LayoutInflater.from(this.context).inflate(R.layout.custom_info_window_layout
                              ,null);
        }
        private void rendoWindowText(Marker marker, final View mWindow){
            account_type = mWindow.findViewById(R.id.info_window_account_type);
            display_name = mWindow.findViewById(R.id.info_window_display_name);
            //address = mWindow.findViewById(R.id.info_window_address);
            email = mWindow.findViewById(R.id.info_window_email);
            phone = mWindow.findViewById(R.id.info_window_phone);
            description = mWindow.findViewById(R.id.info_window_description);
            datetime = mWindow.findViewById(R.id.info_window_datetime);
            String[] data = marker.getSnippet().split("-");
            this.account_type.setText(data[0]);
            this.display_name.setText(data[1]);
            this.email.setText(data[2]);
            this.phone.setText(data[3]);
            this.description.setText(data[4]);
            this.datetime.setText(data[5]);
            call = mWindow.findViewById(R.id.call_map_icon);
            gmail = mWindow.findViewById(R.id.gmail_map_icon);
        }
        @Override
        public View getInfoWindow(Marker marker) {
            if(marker != null && marker.equals(myLocationMarker)){
                marker.setTitle("I'm Here!");
                marker.setSnippet("Your Location");
                return null;
            }else{
                rendoWindowText(marker,mWindow);
            }
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            if(marker != null && marker.equals(myLocationMarker)){
                marker.setTitle("I'm Here!");
                marker.setSnippet("Your Location");
                return null;
            }else{
                rendoWindowText(marker,mWindow);
            }
            return mWindow;
        }
    }

    /**
     *
     * @param user
     */
    private String getMarkerData(UserOnMap user) {
        String[] res = new String[7];
        switch (user.getAccountType()){
            case STUDENT:
                res[0] = "< Student >";
                break;
            case PARENT:
                res[0] = "< Parent >";
                break;
            case TEACHER:
                res[0] = "< Teacher >";
                break;
            default:
                res[0] = "< Default >";
                break;
        }
        res[1] = user.getDisplayName();
        res[2] = user.getEmail();
        res[3] = user.getPhone();
        res[4] = user.getDescription();
        String time = user.getTime().split(":")[0] +":"+user.getTime().split(":")[1];
        String htmlFormat = user.getDate()+"<br>&nbsp;"+time;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            res[5] = String.valueOf(Html.fromHtml(
                    htmlFormat, Html.FROM_HTML_MODE_COMPACT));
        } else {
            res[5] = String.valueOf(Html.fromHtml(htmlFormat));
        }
        String result = res[0]+"-"+res[1]+"-"+res[2]+"-"+res[3]+"-"+
                res[4]+"-"+res[5];
        return result;
    }
}
