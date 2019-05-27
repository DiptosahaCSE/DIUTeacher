package com.example.diuteacher;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TotalAttendanceFragment extends Fragment {

    DatabaseReference databaseReference;
    private Spinner semester_Sp, department_Sp, section_Sp;
    private Context context;
    private EditText subject_Name_Et, id_number_Et;
    private TextView total_attendance_tv;
    private Button find_btn, today_attendance_btn;
    ArrayList<StudentPoJo> studentPoJos;
    int count = 0;
    CheckSystem checkSystem;

    public TotalAttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        checkSystem = new CheckSystem(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_total_attendance, container, false);

        subject_Name_Et = view.findViewById(R.id.subject_Name_Et);
        id_number_Et = view.findViewById(R.id.id_number_Et);
        total_attendance_tv = view.findViewById(R.id.total_attendance_tv);
        today_attendance_btn = view.findViewById(R.id.today_attendance_btn);

        semester_Sp = view.findViewById(R.id.semester_Sp);
        department_Sp = view.findViewById(R.id.department_Sp);
        section_Sp = view.findViewById(R.id.section_Sp);

        find_btn = view.findViewById(R.id.find_btn);


        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance");

        //preparing adapter for spinner
        ArrayAdapter<CharSequence> departmentAdapter = ArrayAdapter.
                createFromResource(context, R.array.department, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> semesterAdapter = ArrayAdapter.
                createFromResource(context, R.array.semester, android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> sectionAdapter = ArrayAdapter.
                createFromResource(context, R.array.section, android.R.layout.simple_spinner_dropdown_item);

        // setting value to spinner
        department_Sp.setAdapter(departmentAdapter);
        semester_Sp.setAdapter(semesterAdapter);
        section_Sp.setAdapter(sectionAdapter);

        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkSystem.havingInternetConnection()) {
                    //having internet connect connection

                    String department = department_Sp.getSelectedItem().toString();
                    String semester = semester_Sp.getSelectedItem().toString();
                    String section = section_Sp.getSelectedItem().toString();
                    String subject = subject_Name_Et.getText().toString();
                    String studentId = id_number_Et.getText().toString();
                    if (!TextUtils.isEmpty(subject) && (!TextUtils.isEmpty(studentId))) {
                        //having all field
                        getDataFromFireBase(department, semester, section, subject, studentId);
                    } else {

                        //subject or student id not here
                        Toast.makeText(context, "All field are required", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //no internet connection
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                }


            }
        });

        today_attendance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new PresentAttendanceFragment());
            }
        });

        return view;
    }

    public void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }

    public void getDataFromFireBase(String department, String semester, String section, String subject, String sId) {

        databaseReference.child(department).child(semester).child(section).child(subject).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {
                    //data exists
                    count = 0;
                    studentPoJos = new ArrayList<>();
                    for (DataSnapshot children : dataSnapshot.getChildren()) {

                        if (children.hasChild(id_number_Et.getText().toString())) {
                            //having children
                            count++;
                        }
                    }

                    total_attendance_tv.setText("Total Class in this semester : " + String.valueOf(count));

                } else {
                    //no data
                    total_attendance_tv.setText("Id Not Found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
