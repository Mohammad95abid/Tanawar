package com.tanawar.tanawarandroidapp;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import apis.FirebaseConstants;
import apis.PrivateLessonsAPI;
import apis.TeacherConnectionAPI;
import helper_tools.TanawarAlertDialog;
import interfaces.InitializeComponents;
import utils.PrivateLessonType;
import utils.TanawarCalendar;

public class AddPrivateLesson extends AppCompatActivity implements InitializeComponents {
    private EditText subject;
    private Spinner lessonType;
    private EditText studentEmail;
    private EditText studentName;
    private TextView date;
    private TextView time;
    private Button add;
    private Calendar datetime;
    private String teacherEmail;
    private String teacherName;
    private EditText zoomLink;
    private Spinner student_list;
    private TextView tvZoomLinkLabel_AddPLesson;
    private HashMap<String, String> knownStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_private_lesson);
        getDataFromIntent();
        initializeActivityComponents();
        initializeComponentsListeners();
    }
    private void getDataFromIntent() {
        Intent i = getIntent();
        teacherEmail = i.getStringExtra(FirebaseConstants.Collections.Users.EMAIL);
        teacherName = i.getStringExtra(FirebaseConstants.Collections.Users.DISPLAY_NAME);
    }
    @Override
    public void initializeComponentsListeners() {
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
            /**
             * a method to show the date picker dialog.
             */
            private void showDatePicker() {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddPrivateLesson.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                datetime = Calendar.getInstance();
                                datetime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                datetime.set(Calendar.MONTH, monthOfYear);
                                datetime.set(Calendar.YEAR, year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
            /**
             * a method to show the date picker dialog.
             */
            private void showDatePicker() {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddPrivateLesson.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                if(datetime == null){
                                    datetime = Calendar.getInstance();
                                }
                                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                datetime.set(Calendar.MINUTE, minute);
                                String t = TanawarCalendar.dateTimeAsString(datetime);
                                time.setText(t.split("  ")[1]);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });
        lessonType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PrivateLessonType t = (PrivateLessonType) lessonType.getSelectedItem();
                if(t == PrivateLessonType.FACE_TO_FACE){
                    zoomLink.setVisibility(View.GONE);
                    tvZoomLinkLabel_AddPLesson.setVisibility(View.GONE);
                }else if(t == PrivateLessonType.ONLINE_MEETING){
                    zoomLink.setVisibility(View.VISIBLE);
                    tvZoomLinkLabel_AddPLesson.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectStr = subject.getText().toString();
                String studentEmailStr = studentEmail.getText().toString();
                String studentNameStr = studentName.getText().toString();
                String lessonTypeStr = lessonType.getSelectedItem().toString();
                String zoom = zoomLink.getText().toString();
                if(subjectStr == null || subjectStr.isEmpty()){
                    new TanawarAlertDialog().showSimpleDialog(
                            "Invalid Parameters - Add Private Lesson",
                            "Please enter a lesson subject...",
                            AddPrivateLesson.this).show();
                }else if(studentEmailStr == null || studentEmailStr.isEmpty()){
                    new TanawarAlertDialog().showSimpleDialog(
                            "Invalid Parameters - Add Private Lesson",
                            "Please enter a Student Email...",
                            AddPrivateLesson.this).show();
                }else if (studentNameStr == null || studentNameStr.isEmpty()){
                    new TanawarAlertDialog().showSimpleDialog(
                            "Invalid Parameters - Add Private Lesson",
                            "Please enter a Student Name...",
                            AddPrivateLesson.this).show();
                }else if (lessonTypeStr == null || lessonTypeStr.isEmpty()){
                    new TanawarAlertDialog().showSimpleDialog(
                            "Invalid Parameters - Add Private Lesson",
                            "Please enter a Lesson Type...",
                            AddPrivateLesson.this).show();
                }else if(lessonType.getSelectedItem() == PrivateLessonType.ONLINE_MEETING
                            && (zoom == null || zoom.isEmpty())){
                    new TanawarAlertDialog().showSimpleDialog(
                            "Invalid Parameters - Add Private Lesson",
                            "Please Enter the Lesson Zoom Link...",
                            AddPrivateLesson.this).show();
                }
                else if (datetime == null){
                    new TanawarAlertDialog().showSimpleDialog(
                            "Invalid Parameters - Add Private Lesson",
                            "Please choose the Lesson date and time...",
                            AddPrivateLesson.this).show();
                }else{
                    // all the fields typed, add private lesson type into the database.
                    PrivateLessonsAPI.getInstance().checkIfTeacherAndStudentHaveLessonInGivenTime(
                            teacherEmail, studentEmailStr, datetime, teacherName, subjectStr,
                            studentNameStr, lessonTypeStr, zoom ,AddPrivateLesson.this);
                }
            }
        });
        student_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String temp = (String) parent.getItemAtPosition(position);
                String name = temp.split("  -  ")[0];
                String email = temp.split("  -  ")[1];
                studentName.setText(name);
                studentEmail.setText(email);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void initializeActivityComponents() {
        subject = findViewById(R.id.edtSubject);
        lessonType = findViewById(R.id.spnLessonType);
        initSpinner();
        studentName = findViewById(R.id.edtStudentName);
        studentEmail = findViewById(R.id.edtStudentEmail);
        date = findViewById(R.id.dpLessonDate);
        time = findViewById(R.id.dpLessonTime);
        add = findViewById(R.id.addPrivateLesson);
        zoomLink = findViewById(R.id.edtZoomLink);
        student_list = findViewById(R.id.students_list_add_private_lesson);
        tvZoomLinkLabel_AddPLesson = findViewById(R.id.tvZoomLinkLabel_AddPLesson);
        loadAllKnownStudentsByTeacher();
    }

    private void loadAllKnownStudentsByTeacher() {
        TeacherConnectionAPI.getInstance()
                .loadAllKnownStudentsByTeacher(Homepage.myEmail, this);
    }

    public void initStudentsList(HashMap<String, String> students) {
        this.knownStudents = students;
        ArrayList<String> data = new ArrayList<>();
        for (String email : students.keySet())
            if(email!=null)
                data.add(students.get(email)+"  -  "+email);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_spinner_item,data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        student_list.setAdapter(adapter);
    }

    /**
     * a method to initialize the lesson types
     */
    private void initSpinner() {
        ArrayAdapter<PrivateLessonType> adapt = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, PrivateLessonType.values());
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lessonType.setAdapter(adapt);
    }

    public void goBack() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
    /**
     *
     * @param message
     */
    public void conflict(String message) {
        new TanawarAlertDialog().showSimpleDialog("Private Lesson - Conflict Time",message,
                this).show();
    }

    public void addNewLesson(
            String teacherEmail, String teacherName, String subjectStr, String studentEmailStr,
            String studentNameStr, String lessonTypeStr, Calendar datetime, String zoom,
            AddPrivateLesson activity) {
        PrivateLessonsAPI.getInstance().addPrivateLesson(teacherEmail, teacherName,
                subjectStr, studentEmailStr,
                studentNameStr, lessonTypeStr, datetime,
                zoom ,AddPrivateLesson.this);
    }
}
