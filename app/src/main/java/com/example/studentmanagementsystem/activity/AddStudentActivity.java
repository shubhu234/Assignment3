/*
This is an add student class in which student will enter the name and roll no
Data will be saved using save button and then it will be shown on the main activity
  */

package com.example.studentmanagementsystem.activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.studentmanagementsystem.model.BackgroundIntentService;
import com.example.studentmanagementsystem.model.BackgroundService;
import com.example.studentmanagementsystem.model.BackgroundSetData;
import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.validation.Validate;
import java.util.ArrayList;

import static com.example.studentmanagementsystem.constant.Constant.NAME;
import static com.example.studentmanagementsystem.constant.Constant.REQUESTCODE_EDIT;
import static com.example.studentmanagementsystem.constant.Constant.ROLLNO;
import static com.example.studentmanagementsystem.constant.Constant.UPDATE_NAME;
import static com.example.studentmanagementsystem.constant.Constant.UPDATE_ROLLNO;

public class AddStudentActivity extends AppCompatActivity {
    //variables of AddStudent class
    private  Button mButtonSaveDetails;
    private EditText mEditTextName;
    private EditText mEditTextRollno;
    private TextView mTextviewAddStudent;
    private Intent mMainActivityDetail;
    private int selectButtonOperation;
    private static final String itemDialog[]={Constant.AYNC_TASK,Constant.SERVICE,Constant.INTENT_SERVICE};
    public static final int ASYNC_TASK=0;
    public static final int SERVICE=1;
    public static final int INTENT_SERVICE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstudent);

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
            setTitle(R.string.constant_studentdetails);
            mButtonSaveDetails.setVisibility(View.INVISIBLE);
            mEditTextName.setText(intent.getStringExtra(Constant.VIEW_NAME));
            mEditTextRollno.setText(intent.getStringExtra(Constant.VIEW_ROLL));
            mEditTextName.setEnabled(false);
            mEditTextRollno.setEnabled(false);
            mButtonSaveDetails.setVisibility(View.GONE);
            mTextviewAddStudent.setText(getString(R.string.constant_studentdetailsview));
        }
        else if(intent.getStringExtra(Constant.MODE)!=null && intent.getStringExtra(Constant.MODE).contains(Constant.EDIT))
        {
            mTextviewAddStudent.setText(getString(R.string.constant_studentdetailsedit));
            mEditTextRollno.setText(getIntent().getStringExtra(Constant.UPDATE_ROLLNO));
            mEditTextRollno.setEnabled(false);
            editMode();
        }
        else
        {
            setTitle(R.string.constant_studentdetailsadd);
            mButtonSaveDetails.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    boolean error=true;
                    //various validation methods to validate the name,roll no and unique roll no
                    if (!Validate.isValidateName(mEditTextName.getText().toString())) {
                        Toast.makeText(AddStudentActivity.this, getString(R.string.constant_validname),
                                Toast.LENGTH_SHORT).show();
                        error=false;
                    }
                    if (!Validate.isValidateRollNo(mEditTextRollno.getText().toString())) {
                        Toast.makeText(AddStudentActivity.this, getString(R.string.constant_validrollno),
                                Toast.LENGTH_SHORT).show();
                        error=false;
                    }
                    if (!Validate.isUniqueNo((ArrayList<Student>) getIntent().getSerializableExtra(getString(R.string.constant_arrayList)),mEditTextRollno.getText().toString())) {
                        Toast.makeText(AddStudentActivity.this, getString(R.string.constant_uniquerollno),
                                Toast.LENGTH_SHORT).show();
                        error=false;
                    }

                    if(error){
                        mMainActivityDetail=createIntent(MainActivity.class);
                        mMainActivityDetail.putExtra(Constant.NAME, mEditTextName.getText().toString());
                        mMainActivityDetail.putExtra(Constant.ROLLNO, mEditTextRollno.getText().toString());
                    setResult(RESULT_OK, mMainActivityDetail);
                        generateDialogBox(mEditTextName.getText().toString(),mEditTextRollno.getText().toString(),Constant.ADD);
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
        setTitle(R.string.constant_editdetails);
        mButtonSaveDetails.setText(getString(R.string.constant_update));
        mButtonSaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error=true;
                if (!Validate.isValidateName(mEditTextName.getText().toString())) {
                    Toast.makeText(AddStudentActivity.this, R.string.constant_validname,
                            Toast.LENGTH_SHORT).show();
                    error=false;
                }
                else if(error) {
                    mMainActivityDetail= createIntent(MainActivity.class);
                    mMainActivityDetail.putExtra(Constant.UPDATE_NAME, mEditTextName.getText().toString());
                    mMainActivityDetail.putExtra(Constant.UPDATE_ROLLNO, mEditTextRollno.getText().toString());
                    setResult(RESULT_OK, mMainActivityDetail);
                    generateDialogBox(mEditTextName.getText().toString(),mEditTextRollno.getText().toString(),Constant.EDIT);
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

    private void generateDialogBox(final String name, final String rollNo, final String typeOfOperation){

        final AlertDialog.Builder mBuilder=new AlertDialog.Builder(AddStudentActivity.this);
        if(selectButtonOperation==REQUESTCODE_EDIT)
            mBuilder.setTitle(R.string.updated);
        else
            mBuilder.setTitle(R.string.added_by);


        mBuilder.setSingleChoiceItems(itemDialog, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                switch (which){
                    case ASYNC_TASK:
                        (new BackgroundSetData(AddStudentActivity.this)).execute(typeOfOperation,name,rollNo);
                        finish();
                        break;
                    case SERVICE:
                        mMainActivityDetail=createIntent(BackgroundService.class);
                        mMainActivityDetail.putExtra(Constant.MODE,typeOfOperation);
                        mMainActivityDetail.putExtra(Constant.NAME,name);
                        mMainActivityDetail.putExtra(Constant.ROLLNO,rollNo);
                        startService(mMainActivityDetail);
                        finish();
                        break;
                    case INTENT_SERVICE:
                        mMainActivityDetail=createIntent(BackgroundIntentService.class);
                        mMainActivityDetail.putExtra(Constant.MODE,typeOfOperation);
                        mMainActivityDetail.putExtra(Constant.NAME,name);
                        mMainActivityDetail.putExtra(Constant.ROLLNO,rollNo);
                        startService(mMainActivityDetail);
                        finish();
                        break;
                }
            }
        });
        mBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog mDialog=mBuilder.create();
        mDialog.show();

    }

}
