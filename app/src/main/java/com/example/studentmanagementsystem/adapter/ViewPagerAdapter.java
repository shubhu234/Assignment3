package com.example.studentmanagementsystem.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.studentmanagementsystem.fragment.AddStudentFragment;
import com.example.studentmanagementsystem.fragment.MainFragment;

public class ViewPagerAdapter  extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MainFragment(); //ChildFragment1 at position 0
            case 1:
                return new AddStudentFragment(); //ChildFragment2 at position 1
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2; //two fragments
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "StudentList";
            case 1:
                return "Add/Update";
        }
        return null;
    }
}
