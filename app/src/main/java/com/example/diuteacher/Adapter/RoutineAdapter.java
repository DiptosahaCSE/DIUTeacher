package com.example.diuteacher.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.diuteacher.PoJo.ClassPoJo;
import com.example.diuteacher.R;

import java.util.ArrayList;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder> {

    private Context context;
    private ArrayList<ClassPoJo> classPoJos;

    public RoutineAdapter(Context context, ArrayList<ClassPoJo> classPoJos) {
        this.context = context;
        this.classPoJos = classPoJos;
    }

    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.routine_single_layout,parent,false);

        return new RoutineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineViewHolder holder, int position) {

        ClassPoJo classPoJo = classPoJos.get(position);
        holder.subject_Name_Tv.setText(classPoJo.getSubjectName());
        holder.teacher_Name_Tv.setText(classPoJo.getTeacherName());
        holder.time_Tv.setText(classPoJo.getStartingTime()+" To "+classPoJo.getEndingTime());

    }

    @Override
    public int getItemCount() {
        return classPoJos.size();
    }

    public class RoutineViewHolder extends RecyclerView.ViewHolder {

        private TextView subject_Name_Tv,teacher_Name_Tv,time_Tv;

        public RoutineViewHolder(View itemView) {
            super(itemView);
            subject_Name_Tv = itemView.findViewById(R.id.subject_Name_Tv);
            teacher_Name_Tv = itemView.findViewById(R.id.teacher_Name_Tv);
            time_Tv = itemView.findViewById(R.id.time_Tv);
        }
    }
    public void updateRoutine(ArrayList<ClassPoJo>classPoJos){
        this.classPoJos = classPoJos;
        notifyDataSetChanged();
    }
}
