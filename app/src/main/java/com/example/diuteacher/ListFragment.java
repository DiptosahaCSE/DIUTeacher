package com.example.diuteacher;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diuteacher.Adapter.StudentAdapter;
import com.example.diuteacher.PoJo.StudentPoJo;

import java.util.ArrayList;


public class ListFragment extends Fragment {


    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<StudentPoJo> studentPoJos;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment getInstance(ArrayList<StudentPoJo> studentPoJos) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("student", studentPoJos);
        ListFragment listFragment = new ListFragment();
        listFragment.setArguments(bundle);
        return listFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        studentPoJos = (ArrayList<StudentPoJo>) getArguments().getSerializable("student");
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new StudentAdapter(context,studentPoJos));

        return view;
    }

}
