/*
SortRoll class to sort the student on the basis of name using the comparator
 */
package com.example.studentmanagementsystem.comparator;

import com.example.studentmanagementsystem.model.Student;

import java.util.Comparator;

public class SortName {
    public Comparator<Student> comparator=new Comparator<Student>()
    {
        /*
         * compare method to compare on the basis of name
         * @return customer name in sorted order
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Student student1, Student student2)
        {
            return student1.getmName().compareTo(student2.getmName());
        }
    };
}
