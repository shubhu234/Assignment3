/*
This is an add student class in which student will enter the name and roll no
Data will be saved using save button and then it will be shown on the main activity
  */


package com.example.studentmanagementsystem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.studentmanagementsystem.constant.Constant;
import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.validation.Validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddStudent extends AppCompatActivity {
    //variables of AddStudent class
    private  Button mSaveDetails;
    private EditText mName;
    private EditText mRollno;
    private TextView mAddStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
         //initialization of the variables
         mSaveDetails=(Button)findViewById(R.id.btn_save);
         mName=(EditText) findViewById(R.id.et_name);
         mRollno=(EditText) findViewById(R.id.et_rollno);
         mAddStudent=(TextView)findViewById(R.id.tv_addstudent);
         Intent intent=getIntent();

        /*
        Conditions to check different mode_type- view,edit,delete
        View- details can only be viewed
        Edit-Student details can be edited
        Delete- Student details will be deleted
         */
        if (intent.getStringExtra(Constant.MODE) != null && intent.getStringExtra(Constant.MODE).contains(Constant.VIEW))
        {
            setTitle("Details of the student");
            mSaveDetails.setVisibility(View.INVISIBLE);
            mName.setText(intent.getStringExtra(Constant.VIEW_NAME));
            mRollno.setText(intent.getStringExtra(Constant.VIEW_ROLL));
            mName.setEnabled(false);
            mRollno.setEnabled(false);
            mSaveDetails.setVisibility(View.GONE);
            mAddStudent.setText("Existing details of the student to view");
        }
        else if(intent.getStringExtra(Constant.MODE)!=null && intent.getStringExtra(Constant.MODE).contains(Constant.EDIT))
        {
            mAddStudent.setText("Edit the details of Existing student");
            editMode();
        }
        else
        {
            setTitle("Add the details of the  Student");
            mSaveDetails.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean error=true;
                    if (!Validate.validateName(mName.getText().toString())) {
                        Toast.makeText(AddStudent.this, "Please enter a valid name",
                                Toast.LENGTH_SHORT).show();
                        error=false;
                    }
                    if (!Validate.validateRollNo(mRollno.getText().toString())) {
                        Toast.makeText(AddStudent.this, "Please enter a valid roll no",
                                Toast.LENGTH_SHORT).show();
                        error=false;
                    }
                    if (!Validate.isUnique(mRollno.getText().toString())) {
                        Toast.makeText(AddStudent.this, "Please enter a unique roll no",
                                Toast.LENGTH_SHORT).show();
                        error=false;
                    }

                    if(error){
                    Intent intent = new Intent();
                    intent.putExtra(Constant.NAME, mName.getText().toString());
                    intent.putExtra(Constant.ROLLNO, mRollno.getText().toString());

                    setResult(RESULT_OK, intent);
                    finish();
                    }

                }

            });

        }





    }

    public void editMode()
    {
        setTitle("Edit details of the student");
        mSaveDetails.setText("Update");
        mSaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error=true;
                if (!Validate.validateName(mName.getText().toString())) {
                    Toast.makeText(AddStudent.this, "Please enter a valid name",
                            Toast.LENGTH_SHORT).show();
                    error=false;
                }
                if (!Validate.validateRollNo(mRollno.getText().toString())) {
                    Toast.makeText(AddStudent.this, "Please enter a valid roll no",
                            Toast.LENGTH_SHORT).show();
                    error=false;
                }
                if (!Validate.isUnique(mRollno.getText().toString())) {
                    Toast.makeText(AddStudent.this, "Please enter a unique roll no",
                            Toast.LENGTH_SHORT).show();
                    error=false;
                }
                else if(error) {
                    Intent newIntent = new Intent();
                    newIntent.putExtra(Constant.UPDATE_NAME, mName.getText().toString());
                    newIntent.putExtra(Constant.UPDATE_ROLLNO, mRollno.getText().toString());
                    setResult(RESULT_OK, newIntent);
                    finish();
                }
            }
        });
    }
}
