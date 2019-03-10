/*
Validation class to validate the name, roll no
 */
package com.example.studentmanagementsystem.validation;

import com.example.studentmanagementsystem.model.Student;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validate {
     //method- validate name to validate the name
     //@param name
     public static boolean validateName(String name)
    {
        String regex="^[a-zA-Z\\s]+$";
        Pattern pattern=Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(name);
        return matcher.find();
    }
    //method - validate the roll no
    //@param - roll no
    public static  boolean validateRollNo(String rollno)
    {
        String regex="[+-]?[0-9][0-9]*";
        Pattern pattern=Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(rollno);
        return matcher.find();
    }
    //method isUnique- to validate the unique roll no or not
    //@param-rollno
    public static boolean isUnique(ArrayList<Student> studentArrayList ,String rollno)
    {
        for(Student student:studentArrayList)
        {
            if(student.getRollNo().equals(rollno)){
                return false;
            }
        }
        return true;
    }

}
