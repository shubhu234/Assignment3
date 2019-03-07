package com.example.studentmanagementsystem.adapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.R;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {
    private ArrayList<Student> studentArrayList;


    public class MyViewHolder extends RecyclerView.ViewHolder   {
        public TextView mName,mRollNo;

        public MyViewHolder(View view) {
            super(view);
            mName = (TextView) view.findViewById(R.id.tv_studentName);
            mRollNo = (TextView) view.findViewById(R.id.tv_studentRollno);
        }

    }

    public StudentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_student, parent, false);

        return new MyViewHolder(itemView);

    }
    public StudentAdapter( ArrayList<Student> studentArrayList){
        this.studentArrayList=studentArrayList;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.MyViewHolder viewHolder, int position) {
        viewHolder.getAdapterPosition();
        Student student = studentArrayList.get(position);
        //MyViewHolder Holder=(MyViewHolder)viewHolder;
        viewHolder.mName.setText(student.getmName());
        //viewHolder.mAge.setText(student.getmAge());
        viewHolder.mRollNo.setText(student.getRollNo());
    }

    @Override
    public int getItemCount() {
        return studentArrayList.size();
    }

}