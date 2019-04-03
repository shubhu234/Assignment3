/*
This class is used for setting the data through the Async task
 */
package com.example.studentmanagementsystem.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.studentmanagementsystem.constant.Constant;
import com.example.studentmanagementsystem.database.StudentDBHelper;

public class BackgroundSetData extends AsyncTask<String,Void, String> {
    private Context mContext;
    private SendData sendData;


    public BackgroundSetData(Context mContext,SendData sendData) {
        this.sendData=sendData;
        this.mContext=mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String out) {
        sendData.callBack(out);
    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }

    @Override
    protected String doInBackground(String... params) {
        StudentDBHelper dbHelper=new StudentDBHelper(mContext);
        String method=params[0];
        String name=params[1];
        String rollno=params[2];
        switch (method){
            case Constant.ADD:
                dbHelper.insertStudent(name,rollno);
                return Constant.ADD_TOAST;
            case Constant.EDIT:
                dbHelper.updateStudent(name,rollno);
                return Constant.UPDATE_TOAST;
        }
        return null;

    }
    public interface SendData{
        public void callBack(String str);
    }
}