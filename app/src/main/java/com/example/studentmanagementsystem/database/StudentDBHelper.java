/*
StudentDBHelper class creating the database and store the data
 */
package com.example.studentmanagementsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.studentmanagementsystem.model.Student;
import java.util.ArrayList;

public class StudentDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Student.db";
    private static final int DATABASE_VERSION = 1;
    public static final String STUDENT_TABLE_NAME = "student";
    public static final String STUDENT_COLUMN_ROLLNO = "_rollno";
    public static final String STUDENT_COLUMN_NAME = "name";

    /*
    Constructor of StudentDBHelper class
    @param - context
     */
    public StudentDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + STUDENT_TABLE_NAME + "(" +
                STUDENT_COLUMN_ROLLNO + " TEXT , " +
                STUDENT_COLUMN_NAME + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE_NAME);
        onCreate(db);
    }

    /*
    method- insertStudent to insert the details of student in the database
    @param- name of the student
    @param-rollno of the student
     */
    public void insertStudent(String name,String rollno) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDENT_COLUMN_NAME, name);
        contentValues.put(STUDENT_COLUMN_ROLLNO,rollno);
        db.insert(STUDENT_TABLE_NAME, null, contentValues);
    }
     /*
    method- updateStudent to update the details of student in the database
    @param- name of the student
    @param-rollno of the student
     */
     public void updateStudent( String name,String rollno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDENT_COLUMN_NAME, name);
        db.update(STUDENT_TABLE_NAME, contentValues, STUDENT_COLUMN_ROLLNO+" = ?", new String[]{rollno});
        db.close();
    }

    /*
    method getStudent to get the details of the student
    @return it will return the arrayList in which the student details will be there
     */
    public ArrayList<Student> getStudent() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + STUDENT_TABLE_NAME ,null);
        ArrayList<Student> arrayList=new ArrayList<>();
        String name,rollno;
       while(res.moveToNext())
       {
           rollno=res.getString(res.getColumnIndex(STUDENT_COLUMN_ROLLNO));
           name=res.getString(res.getColumnIndex(STUDENT_COLUMN_NAME));
           Student student=new Student(name,rollno);
           arrayList.add(student);
       }
        return arrayList;
    }

    public Cursor getAllStudent() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + STUDENT_TABLE_NAME, null );
        return res;
    }

     /*
        method- deleteStudent to delete the details of student in the database
        @param-rollno of the student
     */
    public void deleteStudent(String rollno) {
        SQLiteDatabase db = this.getWritableDatabase();
         db.delete(STUDENT_TABLE_NAME,
                STUDENT_COLUMN_ROLLNO + " = ? ",
                new String[] { String.valueOf(rollno)});
    }
}
