/*
This is an add student class in which student will enter the name and roll no
Data will be saved using save button and then it will be shown on the main activity
  */

package com.example.studentmanagementsystem.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.fragment.AddStudentFragment;
import com.example.studentmanagementsystem.util.Communication;

public class AddStudentActivity extends AppCompatActivity implements Communication {
    private AddStudentFragment mAddStudentFragment;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_fragment);
        bundle=getIntent().getExtras();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        mAddStudentFragment=new AddStudentFragment();
        Log.d("2nd activity", "onCreate: agya");
        fragmentTransaction.add(R.id.frag_container, mAddStudentFragment);
        mAddStudentFragment.setArguments(bundle);
        fragmentTransaction.commit();
    }

  /*  @Override
    protected void onStart() {
        super.onStart();
        Log.d("2nd activity", "onCreate: gya");

        mAddStudentFragment.update(bundle);
        //Log.d("2nd activity", "onCreate: gya");

    }*/

    @Override
    public void communication(Bundle bundle) {
    }
}
