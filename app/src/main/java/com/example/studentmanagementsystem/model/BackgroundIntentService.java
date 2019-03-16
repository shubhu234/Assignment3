package com.example.studentmanagementsystem.model;


import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.constant.Constant;
import com.example.studentmanagementsystem.database.StudentDBHelper;


public class BackgroundIntentService extends IntentService {

    public BackgroundIntentService() {
        super("BackgroundIntentService");

    }
    /*
    @param- intent for passing
    This method is used to insert and update the data through intent service as the background
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        StudentDBHelper studentDataBaseHelper=new StudentDBHelper(this);
        Toast.makeText(this,getString(R.string.from_intent_service),Toast.LENGTH_LONG).show();
        if(intent.getStringExtra(Constant.MODE).equals(Constant.ADD)){
            studentDataBaseHelper.insertStudent(intent.getStringExtra(Constant.NAME),intent.getStringExtra(Constant.ROLLNO));
        }else if(intent.getStringExtra(Constant.MODE).equals(Constant.TYPE_ACTION_FROM_ACTIVITY_EDIT)){
            studentDataBaseHelper.updateStudent(intent.getStringExtra(Constant.NAME),intent.getStringExtra(Constant.ROLLNO));
        }
    }
}