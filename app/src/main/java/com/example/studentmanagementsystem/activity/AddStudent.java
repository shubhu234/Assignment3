/*
This is an add student class in which student will enter the name and roll no
Data will be saved using save button and then it will be shown on the main activity
  */

package com.example.studentmanagementsystem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.studentmanagementsystem.constant.Constant;
import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.validation.Validate;
import java.util.ArrayList;

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
         init();
         Intent intent=getIntent();

        /*
        Conditions to check different mode_type- view,edit,delete
        View- details can only be viewed
        Edit-Student details can be edited
        Delete- Student details will be deleted
         */
        if (intent.getStringExtra(Constant.MODE) != null && intent.getStringExtra(Constant.MODE).contains(Constant.VIEW))
        {
            setTitle(R.string.Constant_StudentDetails);
            mSaveDetails.setVisibility(View.INVISIBLE);
            mName.setText(intent.getStringExtra(Constant.VIEW_NAME));
            mRollno.setText(intent.getStringExtra(Constant.VIEW_ROLL));
            mName.setEnabled(false);
            mRollno.setEnabled(false);
            mSaveDetails.setVisibility(View.GONE);
            mAddStudent.setText(getString(R.string.Constant_StudentDetailsView));
        }
        else if(intent.getStringExtra(Constant.MODE)!=null && intent.getStringExtra(Constant.MODE).contains(Constant.EDIT))
        {
            mAddStudent.setText(getString(R.string.Constant_StudentDetailsEdit));
            editMode();
        }
        else
        {
            setTitle(R.string.Constant_studentDetailsAdd);
            mSaveDetails.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean error=true;
                    if (!Validate.validateName(mName.getText().toString())) {
                        Toast.makeText(AddStudent.this, getString(R.string.Constant_ValidName),
                                Toast.LENGTH_SHORT).show();
                        error=false;
                    }
                    if (!Validate.validateRollNo(mRollno.getText().toString())) {
                        Toast.makeText(AddStudent.this, getString(R.string.Constant_ValidRollNo),
                                Toast.LENGTH_SHORT).show();
                        error=false;
                    }
                    if (!Validate.isUnique((ArrayList<Student>) getIntent().getSerializableExtra(getString(R.string.Constant_ArrayList)),mRollno.getText().toString())) {
                        Toast.makeText(AddStudent.this, getString(R.string.Constant_UniqueRollNo),
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
    //initialization of the variables
    private void init() {
        mSaveDetails=(Button)findViewById(R.id.btn_save);
        mName=(EditText) findViewById(R.id.et_name);
        mRollno=(EditText) findViewById(R.id.et_rollno);
        mAddStudent=(TextView)findViewById(R.id.tv_addstudent);
    }
    //metod editMode- It will update the name and rollno of the existing student
    private void editMode()
    {
        setTitle(R.string.Constant_EditDetails);
        mSaveDetails.setText(getString(R.string.Constant_Update));
        mSaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error=true;
                if (!Validate.validateName(mName.getText().toString())) {
                    Toast.makeText(AddStudent.this, R.string.Constant_ValidName,
                            Toast.LENGTH_SHORT).show();
                    error=false;
                }
                if (!Validate.validateRollNo(mRollno.getText().toString())) {
                    Toast.makeText(AddStudent.this, R.string.Constant_ValidRollNo,
                            Toast.LENGTH_SHORT).show();
                    error=false;
                }
                if (!Validate.isUnique((ArrayList<Student>) getIntent().getSerializableExtra(getString(R.string.Constant_ArrayList)),mRollno.getText().toString())) {
                    Toast.makeText(AddStudent.this, R.string.Constant_UniqueRollNo,
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
