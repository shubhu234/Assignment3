package com.example.studentmanagementsystem.model;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.constant.Constant;
import com.example.studentmanagementsystem.database.StudentDBHelper;

public class BackgroundService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
    /*
    @param- intent for passing
    This method is used to insert and update the data through  service as the background
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StudentDBHelper studentDataBaseHelper=new StudentDBHelper(this);
        Toast.makeText(this,getString(R.string.from_service),Toast.LENGTH_LONG).show();
        if(intent.getStringExtra(Constant.MODE).equals(Constant.ADD)){
            studentDataBaseHelper.insertStudent(intent.getStringExtra(Constant.NAME),intent.getStringExtra(Constant.ROLLNO));
        }else if(intent.getStringExtra(Constant.MODE).equals(Constant.TYPE_ACTION_FROM_ACTIVITY_EDIT)){
            studentDataBaseHelper.updateStudent(intent.getStringExtra(Constant.NAME),intent.getStringExtra(Constant.ROLLNO));
        }
        stopSelf();
        return  START_NOT_STICKY;
    }
}
