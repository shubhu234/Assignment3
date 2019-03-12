/*
MainActivity class which controls all the app
It shows the name and rollno of the student in a list
On clicking the item in the list various options appears in dialog box to view,edit and delete the student record
There is a menu option on the top of screen which sort student record on basis of name and rollno
There is a switch button which toggles between the grid view and the list view
 */
package com.example.studentmanagementsystem.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.studentmanagementsystem.constant.Constant;
import com.example.studentmanagementsystem.listener.ItemClickListener;
import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.comparator.SortName;
import com.example.studentmanagementsystem.comparator.SortRoll;
import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.adapter.StudentAdapter;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final int VIEW=0;
    private static final int EDIT=1;
    private static final int DELETE=2;
    private Button mButtonAddStudent;
    private RecyclerView mRecyclerView;
    private StudentAdapter mAdapter;
    private Student student;
    private TextView mTextViewNoStudent;
    private Intent mIntentAddStudentDetail;
    private int position1;
    private static final String itemDialog[]={Constant.VIEW,Constant.EDIT,Constant.DELETE};
    private ArrayList<Student> studentArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();

        recyclerViewHandler();


        mButtonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntentAddStudentDetail= createIntent(AddStudentActivity.class);
                mIntentAddStudentDetail.putExtra(getString(R.string.Constant_ArrayList),studentArrayList);
                startActivityForResult(mIntentAddStudentDetail, Constant.REQUESTCODE_SETDATA);
            }
        });

}
    //method for initialization of the variables
    private void init()
    {
        mTextViewNoStudent = findViewById(R.id.tv_no_student);
        mButtonAddStudent =  findViewById(R.id.btn_addStudent);
        mRecyclerView =  findViewById(R.id.rv_recyclerView);
    }
    /*
    method- recyclerViewHandler
    It lists the student name and roll no in the list format
    On clicking the item a dialog box appears to view,edit and delete the student record
     */
    private void recyclerViewHandler()
    {
        mAdapter = new StudentAdapter(studentArrayList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new ItemClickListener(MainActivity .this, mRecyclerView, new ItemClickListener.ClickListener() {
        @Override
        public void onClick (View view,final int position){
            student = studentArrayList.get(position);
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            mBuilder.setTitle(R.string.Constant_Operations);
            mBuilder.setItems(itemDialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case VIEW:
                            view(which);
                            break;
                        case EDIT:
                            edit(which,position);
                            break;
                        case DELETE:
                            delete(which,position);
                            break;
                    }
                    dialog.dismiss();
                }
            });
            mBuilder.setNeutralButton(Constant.CANCEL, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog newDialog = mBuilder.create();
            newDialog.show();

        }
    }));
    }

    //method-view to view the existing details of the student
    //@param-parameter which to select which mode it is
    private void view(final int which)
    {
        mIntentAddStudentDetail = createIntent(AddStudentActivity.class);
        mIntentAddStudentDetail.putExtra(Constant.MODE,Constant.VIEW);
        Toast.makeText(MainActivity.this, getString(R.string.Constant_Choose) + " "+itemDialog[which], Toast.LENGTH_LONG).show();
        mIntentAddStudentDetail.putExtra(Constant.VIEW_NAME, student.getmName());
        mIntentAddStudentDetail.putExtra(Constant.VIEW_ROLL, student.getRollNo());
        startActivity(mIntentAddStudentDetail);
    }
    /*method-edit to update the details of the student
    @param-which for selecting the mode
    @param-position for selecting the position to update
     */
    private void edit(final int which,final int position)
    {
        mIntentAddStudentDetail = createIntent(AddStudentActivity.class);
        mIntentAddStudentDetail.putExtra(Constant.MODE,Constant.EDIT);
        mIntentAddStudentDetail.putExtra(getString(R.string.Constant_ArrayList),studentArrayList);
        position1 = position;
        Toast.makeText(MainActivity.this, getString(R.string.Constant_Choose) + " "+itemDialog[which], Toast.LENGTH_LONG).show();
        mAdapter.notifyItemRemoved(position);
        startActivityForResult(mIntentAddStudentDetail, Constant.REQUESTCODE_EDIT);
    }
    /*
    method-delete to delete the student record from the student arraylist
    @param which-for selecting the mode
    @param position-student record at a particular position is deleted
    dialog box appears when you choose the delete operation prompting that you have to delete this record
     */
    private void delete(int which, final int position)
    {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        alertBuilder.setTitle(R.string.Constant_DeletedSuccesfully);
        Toast.makeText(MainActivity.this, getString(R.string.Constant_Choose) + " "+itemDialog[which], Toast.LENGTH_LONG).show();
        alertBuilder.setPositiveButton(Constant.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                studentArrayList.remove(position);
                Toast.makeText(MainActivity.this, getString(R.string.Constant_Deleted), Toast.LENGTH_LONG).show();
                if (studentArrayList.size() == Constant.NULL) {
                    mTextViewNoStudent.setVisibility(View.VISIBLE);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        alertBuilder.setNegativeButton(Constant.NO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialoginterface, int which) {
                dialoginterface.cancel();
                dialoginterface.dismiss();
            }
        });
        AlertDialog mDialog = alertBuilder.create();
        mDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         switch (requestCode){
             // check if the request code is same as what is passed  here it is
             case Constant.REQUESTCODE_SETDATA:
                 if (resultCode==RESULT_OK) {
                     String name = data.getStringExtra(Constant.NAME);
                     String rollno = data.getStringExtra(Constant.ROLLNO);
                     Student student=new Student(name,rollno);
                     studentArrayList.add(student);
                     if(mTextViewNoStudent.getVisibility()==View.VISIBLE)
                     {
                         mTextViewNoStudent.setVisibility(View.INVISIBLE);

                     }
                     mAdapter.notifyDataSetChanged();
                 }
                 break;
             case Constant.REQUESTCODE_EDIT:
                 if(resultCode==RESULT_OK)
                 {
                     String nameupdate=data.getStringExtra(Constant.UPDATE_NAME);
                     String rollupadte=data.getStringExtra(Constant.UPDATE_ROLLNO);
                     Student infoupdate=new Student(nameupdate,rollupadte);
                     studentArrayList.remove(position1);
                     studentArrayList.add(position1,infoupdate);
                     mAdapter.notifyItemChanged(position1);
                 }
                 break;
         }
    }

    /*
    method-onCreateOptionsMenu
    @param menu-a menu will be displayed on top of screen
    toggle switch-to switch between the grid and linear layout
    dropdown to select options to sort on basis of roll no or name
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        MenuItem item=menu.findItem(R.id.switch1);
       MenuItem sortName=menu.findItem(R.id.item_sortbyname);
        MenuItem sortRoll=menu.findItem(R.id.item_sortbyrollno);

        sortName.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(studentArrayList, (new SortName().comparator));
                mAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,Constant.MENU,Toast.LENGTH_LONG).show();
                return true;
            }
        });
        sortRoll.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(studentArrayList, (new SortRoll().comparator));
                mAdapter.notifyDataSetChanged();

                return true ;
            }
        });
        item.setActionView(R.layout.switch_layout);
         Switch simpleSwitch = menu.findItem(R.id.switch1).getActionView().findViewById(R.id.switchButton);
         simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked)
                 {

                     mRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                 }
                 else
                 {
                     mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                 }
             }
         });
         return true;
    }
    /*
    method createIntent for creating the intent
    @param-activity class name
    @return intent
     */
    public Intent createIntent(Class<?> addStudentActivityClass)
    {
        Intent intent=new Intent(this,addStudentActivityClass);
        return intent;


    }
}