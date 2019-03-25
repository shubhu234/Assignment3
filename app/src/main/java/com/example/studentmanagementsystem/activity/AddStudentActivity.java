/*
This is an add student class in which student will enter the name and roll no
Data will be saved using save button and then it will be shown on the main activity
  */

package com.example.studentmanagementsystem.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.studentmanagementsystem.constant.Constant;
import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.fragment.AddStudentFragment;
import com.example.studentmanagementsystem.model.BackgroundIntentService;
import com.example.studentmanagementsystem.model.BackgroundService;
import com.example.studentmanagementsystem.model.BackgroundSetData;
import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.util.Communication;
import com.example.studentmanagementsystem.validation.Validate;
import java.util.ArrayList;

import static com.example.studentmanagementsystem.constant.Constant.NAME;
import static com.example.studentmanagementsystem.constant.Constant.REQUESTCODE_EDIT;
import static com.example.studentmanagementsystem.constant.Constant.ROLLNO;
import static com.example.studentmanagementsystem.constant.Constant.UPDATE_NAME;
import static com.example.studentmanagementsystem.constant.Constant.UPDATE_ROLLNO;

public class AddStudentActivity extends AppCompatActivity implements Communication {
    private AddStudentFragment mAddUpdateFragment;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_fragment);

        bundle=getIntent().getExtras();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        mAddUpdateFragment=new AddStudentFragment();
        fragmentTransaction.add(R.id.frag_container,mAddUpdateFragment,"HEloo");
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAddUpdateFragment.update(bundle);
    }

    @Override
    public void communication(Bundle bundle) {

    }
}
