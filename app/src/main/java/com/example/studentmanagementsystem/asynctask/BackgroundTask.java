package com.example.studentmanagementsystem.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.studentmanagementsystem.database.StudentDBHelper;
import com.example.studentmanagementsystem.model.Student;

import java.util.ArrayList;

public class BackgroundTask  extends AsyncTask<String,Void, ArrayList<Student>> {
    private Context mContext;
    Callback callback;

    public BackgroundTask(Context mContext,Callback callback) {
        this.mContext=mContext;
        this.callback=callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Student> out) {
         callback.getOutput(out);
    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }

    @Override
    protected ArrayList<Student> doInBackground(String... params) {
        StudentDBHelper dbHelper=new StudentDBHelper(mContext);

        return dbHelper.getStudent();

    }
    //interface CallBack having a method getOutput
    public interface Callback{
        void getOutput(ArrayList<Student> out);
    }
}
