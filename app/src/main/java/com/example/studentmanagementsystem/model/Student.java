/*
Student class to get the details of the student and stored it in the arraylist
 */

package com.example.studentmanagementsystem.model;

public class Student {
  //variables of Student class
  private  String RollNo;
    private   String  Name;

    /*
    Constructor of student class
    @param name-name of student
    @param RollNo- rollno of the student
     */
    public Student(String Name,String RollNo)
    {
        this.Name=Name;
        this.RollNo=RollNo;
    }
    //method getmName-get the name of student
    //@return name of student
    public   String getmName()
    {
        return  Name;
    }
    //method getRollNo
    //@return roll no of student
    public   String getRollNo()
    {
        return  RollNo;
    }

    public void setmName(String Name)
    {
        this.Name=Name;
    }
    public void setmRollNo(String RollNo)
    {
        this.RollNo=RollNo;
    }

}
