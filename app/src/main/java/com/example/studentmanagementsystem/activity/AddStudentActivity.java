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

public class AddStudentActivity extends AppCompatActivity {
    //variables of AddStudent class
    private  Button mButtonSaveDetails;
    private EditText mEditTextName;
    private EditText mEditTextRollno;
    private TextView mTextviewAddStudent;
    private Intent mMainActivityDetail;
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
            mButtonSaveDetails.setVisibility(View.INVISIBLE);
            mEditTextName.setText(intent.getStringExtra(Constant.VIEW_NAME));
            mEditTextRollno.setText(intent.getStringExtra(Constant.VIEW_ROLL));
            mEditTextName.setEnabled(false);
            mEditTextRollno.setEnabled(false);
            mButtonSaveDetails.setVisibility(View.GONE);
            mTextviewAddStudent.setText(getString(R.string.Constant_StudentDetailsView));
        }
        else if(intent.getStringExtra(Constant.MODE)!=null && intent.getStringExtra(Constant.MODE).contains(Constant.EDIT))
        {
            mTextviewAddStudent.setText(getString(R.string.Constant_StudentDetailsEdit));
            editMode();
        }
        else
        {
            setTitle(R.string.Constant_studentDetailsAdd);
            mButtonSaveDetails.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean error=true;
                    //various validation methods to validate the name,roll no and unique roll no
                    if (!Validate.isValidateName(mEditTextName.getText().toString())) {
                        Toast.makeText(AddStudentActivity.this, getString(R.string.Constant_ValidName),
                                Toast.LENGTH_SHORT).show();
                        error=false;
                    }
                    if (!Validate.isValidateRollNo(mEditTextRollno.getText().toString())) {
                        Toast.makeText(AddStudentActivity.this, getString(R.string.Constant_ValidRollNo),
                                Toast.LENGTH_SHORT).show();
                        error=false;
                    }
                    if (!Validate.isUniqueNo((ArrayList<Student>) getIntent().getSerializableExtra(getString(R.string.Constant_ArrayList)),mEditTextRollno.getText().toString())) {
                        Toast.makeText(AddStudentActivity.this, getString(R.string.Constant_UniqueRollNo),
                                Toast.LENGTH_SHORT).show();
                        error=false;
                    }

                    if(error){
                        mMainActivityDetail=createIntent(MainActivity.class);
                        mMainActivityDetail.putExtra(Constant.NAME, mEditTextName.getText().toString());
                        mMainActivityDetail.putExtra(Constant.ROLLNO, mEditTextRollno.getText().toString());
                    setResult(RESULT_OK, mMainActivityDetail);
                    finish();
                    }
                }
            });
        }
    }
    //initialization of the variables
    private void init() {
        mButtonSaveDetails=(Button)findViewById(R.id.btn_save);
        mEditTextName=(EditText) findViewById(R.id.et_name);
        mEditTextRollno=(EditText) findViewById(R.id.et_rollno);
        mTextviewAddStudent=(TextView)findViewById(R.id.tv_addstudent);
    }
    //method editMode- It will update the name and rollno of the existing student
    private void editMode()
    {
        setTitle(R.string.Constant_EditDetails);
        mButtonSaveDetails.setText(getString(R.string.Constant_Update));
        mButtonSaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error=true;
                if (!Validate.isValidateName(mEditTextName.getText().toString())) {
                    Toast.makeText(AddStudentActivity.this, R.string.Constant_ValidName,
                            Toast.LENGTH_SHORT).show();
                    error=false;
                }
                if (!Validate.isValidateRollNo(mEditTextRollno.getText().toString())) {
                    Toast.makeText(AddStudentActivity.this, R.string.Constant_ValidRollNo,
                            Toast.LENGTH_SHORT).show();
                    error=false;
                }
                if (!Validate.isUniqueNo((ArrayList<Student>) getIntent().getSerializableExtra(getString(R.string.Constant_ArrayList)),mEditTextRollno.getText().toString())) {
                    Toast.makeText(AddStudentActivity.this, R.string.Constant_UniqueRollNo,
                            Toast.LENGTH_SHORT).show();
                    error=false;
                }
                else if(error) {
                    mMainActivityDetail= createIntent(MainActivity.class);
                    mMainActivityDetail.putExtra(Constant.UPDATE_NAME, mEditTextName.getText().toString());
                    mMainActivityDetail.putExtra(Constant.UPDATE_ROLLNO, mEditTextRollno.getText().toString());
                    setResult(RESULT_OK, mMainActivityDetail);
                    finish();
                }
            }
        });
    }
    /*
    method createIntent for creating the intent
    @param-activity class name
    @return intent
     */
    public Intent createIntent(Class<?> mainActivityClass)
    {
        Intent intent=new Intent(this,mainActivityClass);
        return intent;
    }
}
