/*
Validation class to validate the name, roll no
 */
package com.example.studentmanagementsystem.validation;

import com.example.studentmanagementsystem.model.Student;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validate {
     /*method- isValidateName to validate the name
     @param name of the student
     return true if name is valid else return false
     */
     public static boolean isValidateName(String name)
    {
        String regex="^[a-zA-Z\\s]+$";
        Pattern pattern=Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(name);
        return matcher.find();
    }
    /*method- isValidateRollno to validate the rollno
     @param roll no of the student
     return true if rollno is valid else return false
     */
    public static  boolean isValidateRollNo(String rollno)
    {
        String regex="[+-]?[0-9][0-9]*";
        Pattern pattern=Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(rollno);
        return matcher.find();
    }
    /*method- isUniqueNo is to validate the unique roll no
     @param name of the student
     return true if entered roll no is valid else return false
     */
    public static boolean isUniqueNo(ArrayList<Student> studentArrayList ,String rollno)
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
