package com.tanawar.tanawarandroidapp;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

import apis.FirebaseConstants;
import apis.UserAPI;
import helper_tools.TanawarAlertDialog;
import interfaces.InitializeComponents;
import tanawar_objects.User;
import utils.AccountType;
public class Register extends AppCompatActivity implements InitializeComponents {
    private static final int CHOOSE_ADDRESS_ON_MAP_REQUEST_CODE = 2;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private ImageView datePickerIcon;
    private TextView dateField;
    private boolean is_birthday_choice = false;
    private Calendar birthday;
    private Spinner accountType;
    private ArrayAdapter<String> account_typeAdapter;
    private AccountType account_type_choice;
    private EditText first_name;
    private EditText last_name;
    private EditText email;
    private EditText password;
    private EditText verifyPassword;
    private EditText phone_number;
    private TextView edtAddress;
    private ImageView addressIMG;
    private Button submit;
    // address data variables
    private double lat;
    private double lng;
    private String addressAsString;
    private boolean isAddressChoosed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initializeActivityComponents();
        initializeComponentsListeners();
        initializeDatePickerListener();
    }
    @Override
    public void initializeActivityComponents() {
        datePickerIcon = findViewById(R.id.birthdayIMG);
        dateField = findViewById(R.id.date_field);
        accountType = findViewById(R.id.accountTypeSpinner);
        account_typeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.account_type_spinner));
        account_typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountType.setAdapter(account_typeAdapter);
        first_name = findViewById(R.id.edtfirstname);
        last_name = findViewById(R.id.edtLastname);
        email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtPassword);
        verifyPassword = findViewById(R.id.edtVerfiyPassword);
        phone_number = findViewById(R.id.edtPhoneNumber);
        submit = findViewById(R.id.btnSubmit);
        edtAddress = findViewById(R.id.edtAddress);
        addressIMG = findViewById(R.id.addressIMG);
    }



    @Override
    public void initializeComponentsListeners() {
        this.dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        datePickerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        this.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch ((int) accountType.getSelectedItemId()){
                    case 0:
                        account_type_choice = AccountType.STUDENT;
                        break;
                    case 1:
                        account_type_choice = AccountType.TEACHER;
                        break;
                    case 2:
                        account_type_choice = AccountType.PARENT;
                        break;
                    default:
                        break;
                }
                if(emptyFieldsTest() && addressIsValid() && emailIsValid() && passwordIsValid()
                        && phoneIsValid()){
//                    // go to Firebase and sign up
//                    //if successfully go to login and pass username and password
                    String fn = first_name.getText().toString();
                    String ln = last_name.getText().toString();
                    String em = email.getText().toString();
                    String pas = password.getText().toString();
                    String pn = phone_number.getText().toString();
                    Calendar c =  Calendar.getInstance();
                    User user = new User(first_name.getText().toString(),
                            last_name.getText().toString(), email.getText().toString(),
                            password.getText().toString(), birthday ,
                            account_type_choice,
                            phone_number.getText().toString(),
                            Calendar.getInstance(), addressAsString,lat,lng,""
                            );
                    UserAPI.getInstance().addUser(user,Register.this);
                }
            }
        });
        edtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAddress();
            }
        });
        addressIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAddress();
            }
        });
    }

    private void chooseAddress() {
        Intent i = new Intent(this,ChooseAddressOnMap.class);
        startActivityForResult(i,CHOOSE_ADDRESS_ON_MAP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2 && data!=null)
        {
            addressAsString=data.getStringExtra(FirebaseConstants.Collections.Users.ADDRESS);
            lat = data.getExtras().getDouble(FirebaseConstants.Collections.Users.LATITUDE);
            lng = data.getExtras().getDouble(FirebaseConstants.Collections.Users.LONGITUDE);
            isAddressChoosed = true;
            edtAddress.setText(addressAsString);
        }
    }

    /**
     *
     * @return
     */
    private boolean addressIsValid() {
        String address = edtAddress.getText().toString();
        if(address == null || address.isEmpty() || !isAddressChoosed){
            showAddressInvalidDialog();
            return false;
        }
        return true;
    }
    /**
     *
     */
    private void showAddressInvalidDialog(){
        new TanawarAlertDialog().showSimpleDialog("Address Invalid",
                "The address is invalid, please press your address then click the search icon" +
                        "and choose your address on the map.",
                Register.this).show();
    }
    /**
     *
     * @return
     */
    private boolean phoneIsValid() {
        boolean res = Patterns.PHONE.matcher(this.phone_number.getText().toString()).matches();
        if(!res){
            new TanawarAlertDialog().showSimpleDialog(getResources().getString(R.string.wrong_value),
                    getResources().getString(R.string.phone_incorrect),
                    Register.this).show();
        }
        return res;
    }
    /**
     *
     * @return
     */
    private boolean passwordIsValid() {
        String password = this.password.getText().toString();
        String[] password_messages = getResources().getStringArray(R.array.password_invalid);
        // check if password contains Upper Case
        boolean x = isContainsUpperCase(password);
        // check if password contains Lower Case
        boolean y = isContainsLowerCase(password);
        // check if password contains Numbers
        boolean z = isContainsDigit(password);
        if(!x){
            new TanawarAlertDialog().showSimpleDialog(password_messages[0] ,password_messages[1] ,
                    Register.this).show();
            return false;
        }
        if(!y){
            new TanawarAlertDialog().showSimpleDialog(password_messages[0] ,password_messages[2] ,
                    Register.this).show();
            return false;
        }
        if(!z){
            new TanawarAlertDialog().showSimpleDialog(password_messages[0] ,password_messages[3] ,
                    Register.this).show();
            return false;
        }
        // check password length.
        if(password.length()<8) {
            new TanawarAlertDialog().showSimpleDialog(password_messages[0] ,password_messages[4] ,
                    Register.this).show();
            return false;
        }
        // get the verify password .
        String verifyPass = verifyPassword.getText().toString();
        // the password is valid and correct .
        if(!password.equals(verifyPass)) {
            new TanawarAlertDialog().showSimpleDialog(password_messages[0] ,password_messages[5] ,
                    Register.this).show();
            return false;
        }
        return true;
    }
    /**
     *
     * @param password
     * @return
     */
    private boolean isContainsDigit(String password) {
        for (char ch : password.toCharArray()){
            if(Character.isDigit(ch))
                return true;
        }
        return false;
    }
    /**
     *
     * @param password
     * @return
     */
    private boolean isContainsLowerCase(String password) {
        for (char ch : password.toCharArray()){
            if(Character.isLowerCase(ch))
                return true;
        }
        return false;
    }
    /**
     *
     * @param password
     * @return
     */
    private boolean isContainsUpperCase(String password) {
        for (char ch : password.toCharArray()){
            if(Character.isUpperCase(ch))
                return true;
        }
        return false;
    }
    /**
     *
     * @return
     */
    private boolean emailIsValid() {
        String email = this.email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(emailPattern) && email.length() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
//    boolean res = Patterns.EMAIL_ADDRESS.matcher(str).matches();
//        if(!res){
//            new TanawarAlertDialog().showSimpleDialog(getResources().getString(R.string.wrong_value) ,
//                    getResources().getString(R.string.email_incorrect),
//                    Register.this).show();
//        }
//        return res;
    }
    /**
     *
     * @return
     */
    private boolean emptyFieldsTest() {
        String fname = this.first_name.getText().toString();
        String[] messages = getResources().getStringArray(R.array.register_form_fields_dialog_emptyTest);
        if(fname.isEmpty()){
            showEmptyFieldAlertDialog(messages,0);
            return false;
        }
        String lname = this.last_name.getText().toString();
        if(lname.isEmpty()){
            showEmptyFieldAlertDialog(messages,1);
            return false;
        }
        String email = this.email.getText().toString();
        if(email.isEmpty()){
            showEmptyFieldAlertDialog(messages,2);
            return false;
        }
        String password = this.password.getText().toString();
        if(password.isEmpty()){
            showEmptyFieldAlertDialog(messages,3);
            return false;
        }
        String verifyPassword = this.verifyPassword.getText().toString();
        if(verifyPassword.isEmpty()){
            showEmptyFieldAlertDialog(messages,4);
            return false;
        }
        if(!is_birthday_choice){
            showEmptyFieldAlertDialog(messages,5);
            return false;
        }
        if(account_type_choice == null){
            showEmptyFieldAlertDialog(messages,6);
            return false;
        }
        String phone = this.phone_number.getText().toString();
        if(phone.isEmpty()){
            showEmptyFieldAlertDialog(messages,7);
            return false;
        }
        return true;
    }
    /**
     *
     * @param messages
     * @param messageID
     */
    private void showEmptyFieldAlertDialog(String[] messages,int messageID){
        new TanawarAlertDialog().showSimpleDialog(getResources().getString(R.string.empty_field)
                , messages[messageID],this).show();
    }
    /**
     *
     */
    private void initializeDatePickerListener(){
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                setBirthday(year,month+1,dayOfMonth);
            }
        };
    }
    /**
     *
     * @param year
     * @param month
     * @param dayOfMonth
     */
    private void setBirthday(int year, int month, int dayOfMonth){
        this.birthday = Calendar.getInstance();
        this.birthday.set(year,month,dayOfMonth);
        this.dateField.setText(dayOfMonth+"-"+month+"-"+year);
        is_birthday_choice = true;
    }
    /**
     *
     */
    private void showDatePickerDialog(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(Register.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,
                year, month , day);
        Calendar maximumDate = Calendar.getInstance();
        maximumDate.add(Calendar.DATE, -1);
        dialog.getDatePicker().setMaxDate(maximumDate.getTimeInMillis());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
