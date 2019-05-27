package com.example.diuteacher;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diuteacher.PoJo.StudentPoJo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class PresentAttendanceFragment extends Fragment {


    DatabaseReference databaseReference, studentDatabaseReference;
    private Spinner semester_Sp, department_Sp, section_Sp;
    private Context context;
    private EditText subject_Name_Et, date_Et;
    private TextView total_present_tv, find_present_student_tv, find_absent_student_tv, total_student_tv;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    Calendar calendar;
    Button find_btn, total_attendance_btn;
    ArrayList<StudentPoJo> presentStudentPoJos, totalStudentPoJos;

    CheckSystem checkSystem;

    public PresentAttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        checkSystem = new CheckSystem(context);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_present_attendance, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");
        studentDatabaseReference = FirebaseDatabase.getInstance().getReference("Student");


        subject_Name_Et = view.findViewById(R.id.subject_Name_Et);
        date_Et = view.findViewById(R.id.date_Et);
        find_btn = view.findViewById(R.id.find_btn);
        total_attendance_btn = view.findViewById(R.id.total_attendance_btn);
        total_present_tv = view.findViewById(R.id.total_present_tv);
        find_present_student_tv = view.findViewById(R.id.find_present_student_tv);
        find_absent_student_tv = view.findViewById(R.id.find_absent_student_tv);
        total_student_tv = view.findViewById(R.id.total_student_tv);

        semester_Sp = view.findViewById(R.id.semester_Sp);
        section_Sp = view.findViewById(R.id.section_Sp);
        department_Sp = view.findViewById(R.id.department_Sp);
        calendar = Calendar.getInstance();


        ArrayAdapter<CharSequence> departmentAdapter = ArrayAdapter.
                createFromResource(context, R.array.department, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> semesterAdapter = ArrayAdapter.
                createFromResource(context, R.array.semester, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> sectionAdapter = ArrayAdapter.
                createFromResource(context, R.array.section, android.R.layout.simple_spinner_dropdown_item);

        department_Sp.setAdapter(departmentAdapter);
        semester_Sp.setAdapter(semesterAdapter);
        section_Sp.setAdapter(sectionAdapter);

        date_Et.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, dateSetListener, year, month, dayOfMonth);

                    datePickerDialog.show();
                }
                return true;

            }
        });

        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String department = department_Sp.getSelectedItem().toString();
                String semester = semester_Sp.getSelectedItem().toString();
                String section = section_Sp.getSelectedItem().toString();

                String subject = subject_Name_Et.getText().toString();
                String date = date_Et.getText().toString();

                if (checkSystem.havingInternetConnection()) {
                    //Having internet connection
                    if (!TextUtils.isEmpty(subject) && !TextUtils.isEmpty(date)) {
                        //Having all field
                        findTotalStudent(department, semester, section);
                        findPresentStudent(department, semester, section, subject, date);
                    } else {
                        // Not having all field
                        Toast.makeText(context, "All field required", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //No internet Connection
                    Toast.makeText(context, "No internet Connection", Toast.LENGTH_SHORT).show();
                }


            }
        });

        find_present_student_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (presentStudentPoJos != null) {
                    ListFragment listFragment = ListFragment.getInstance(presentStudentPoJos);
                    changeFragment(listFragment);
                } else {
                    Toast.makeText(context, "Student Not Fixed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        find_absent_student_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<StudentPoJo> absentStudent = findTotalAbsentStudent(totalStudentPoJos, presentStudentPoJos);

                if (absentStudent.size() > 0) {
                    ListFragment listFragment = ListFragment.getInstance(absentStudent);
                    changeFragment(listFragment);
                } else {
                    Toast.makeText(context, "Student Not Fixed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        total_attendance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new TotalAttendanceFragment());
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                calendar.set(year, month, dayOfMonth);
                String finalDate = sdf.format(calendar.getTime());
                date_Et.setText(finalDate);
            }
        };
        return view;
    }

    private ArrayList<StudentPoJo> findTotalAbsentStudent(ArrayList<StudentPoJo> totalStudentPoJos, ArrayList<StudentPoJo> presentStudentPoJos) {

        ArrayList<StudentPoJo>tempAll = totalStudentPoJos;
        ArrayList<StudentPoJo>tempPresent = presentStudentPoJos;
        totalStudentPoJos.removeAll(presentStudentPoJos);

        if (totalStudentPoJos.size() > 0 && presentStudentPoJos.size() > 0) {

            for (int i = 0; i < totalStudentPoJos.size(); i++) {

                for (int j = 0; j < presentStudentPoJos.size(); j++) {
                    if (totalStudentPoJos.get(i).getId().equals(presentStudentPoJos.get(j).getId())){

                    }
                }

            }

            //absent_student_tv.setText(totalStudent.size());
            return totalStudentPoJos;
        } else {
            // absent_student_tv.setText("null");
            return null;
        }


    }

    private void findTotalStudent(String department, String semester, String section) {

        studentDatabaseReference.child(department).child(semester).child(section).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    totalStudentPoJos = new ArrayList<>();

                    for (DataSnapshot idShot : dataSnapshot.getChildren()) {

                               /* String id = idShot.getKey();
                                ids.add(id);*/

                        StudentPoJo studentPoJo = idShot.getValue(StudentPoJo.class);
                        totalStudentPoJos.add(studentPoJo);
                    }
                    //find_present_student_tv.setVisibility(TextView.VISIBLE);
                    total_student_tv.setText("Total Student : " + totalStudentPoJos.size());
                } else {
                    total_student_tv.setText("Not Found");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void findPresentStudent(String department, String semester, String section, String subject, String date) {

        databaseReference.child(department).child(semester).child(section)
                .child(subject).child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    presentStudentPoJos = new ArrayList<>();

                    for (DataSnapshot idShot : dataSnapshot.getChildren()) {

                               /* String id = idShot.getKey();
                                ids.add(id);*/

                        StudentPoJo studentPoJo = idShot.getValue(StudentPoJo.class);
                        presentStudentPoJos.add(studentPoJo);
                    }
                    total_present_tv.setText("Total Present : " + presentStudentPoJos.size());
                } else {
                    total_present_tv.setText("Not Found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }


}
