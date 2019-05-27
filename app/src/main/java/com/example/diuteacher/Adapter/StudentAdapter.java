package com.example.diuteacher.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.diuteacher.PoJo.StudentPoJo;
import com.example.diuteacher.R;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private Context context;
    private ArrayList<StudentPoJo> studentPoJos;

    public StudentAdapter(Context context, ArrayList<StudentPoJo> studentPoJos) {
        this.context = context;
        this.studentPoJos = studentPoJos;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.student_single_layout,parent,false);

        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {

        StudentPoJo studentPoJo = studentPoJos.get(position);
        holder.student_id_tv.setText("Id : "+studentPoJo.getId());
        holder.student_name_tv.setText("Name : "+studentPoJo.getName());

    }

    @Override
    public int getItemCount() {
        return studentPoJos.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView student_id_tv,student_name_tv;

        public StudentViewHolder(View itemView) {
            super(itemView);
            student_id_tv = itemView.findViewById(R.id.student_id_tv);
            student_name_tv = itemView.findViewById(R.id.student_name_tv);

        }
    }
}
