/*
Student class to get the details of the student and stored it in the arraylist
 */

package com.example.studentmanagementsystem.model;

import java.io.Serializable;

public class Student  implements Serializable {
    private  String rollNo;
    private   String  name;

    /**
    Constructor of student class
    @param name-name of student
    @param rollNo- rollno of the student
     */
    public Student(String name,String rollNo)
    {
        this.name=name;
        this.rollNo=rollNo;
    }
    public   String getmName()
    {
        return  name;
    }
    public   String getRollNo()
    {
        return  rollNo;
    }
    public void setmName(String Name)
    {
        this.name=Name;
    }
    public void setmRollNo(String RollNo)
    {
        this.rollNo=RollNo;
    }
}
