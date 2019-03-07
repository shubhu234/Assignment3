package com.example.studentmanagementsystem.activity;

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
import android.util.Log;
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
    RelativeLayout mEmptyLayout;
    private Button mAddStudent;
    private TextView noStudent;
    private RecyclerView recyclerView;
    private StudentAdapter mAdapter;
    private Student student;
    private int position1;
    final String itemDialog[] = {"View", "Edit", "Delete"};
    public  static ArrayList<Student> studentArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmptyLayout = (RelativeLayout) findViewById(R.id.textview_msg);


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mAdapter = new StudentAdapter(studentArrayList);
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);


        mAddStudent = (Button) findViewById(R.id.btn_addStudent);

        mAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddStudent.class);
                startActivityForResult(intent, 1);
            }
        });

       recyclerView.addOnItemTouchListener(new ItemClickListener(MainActivity .this, recyclerView, new ItemClickListener.ClickListener() {
        @Override
        public void onClick (View view,final int position){

            student = studentArrayList.get(position);
            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
            mBuilder.setTitle("Choose the operation you want to perform");
            mBuilder.setItems(itemDialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case VIEW:
                            Intent intent = new Intent(MainActivity.this, AddStudent.class);
                            intent.putExtra(Constant.MODE,Constant.VIEW);
                            Toast.makeText(MainActivity.this, "You have choosen" + " "+itemDialog[which], Toast.LENGTH_LONG).show();
                            intent.putExtra(Constant.VIEW_NAME, student.getmName());
                            Log.i("TESTSEND", "NAME:" + student.getmName());
                            intent.putExtra(Constant.VIEW_ROLL, student.getRollNo());
                            Log.i("TESTSEND", "ROLL:" + student.getRollNo());
                            startActivity(intent);
                            dialog.dismiss();
                            break;
                        case EDIT:
                            Intent editIntent = new Intent(MainActivity.this, AddStudent.class);
                            editIntent.putExtra(Constant.MODE,Constant.EDIT);
                            position1 = position;
                            Toast.makeText(MainActivity.this, "You have choosen" + " "+itemDialog[which], Toast.LENGTH_LONG).show();
                            mAdapter.notifyItemRemoved(position);
                            dialog.dismiss();;
                            startActivityForResult(editIntent, 2);
                            break;
                        case DELETE:
                           AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                            alertBuilder.setTitle("Do you really want to delete this record");
                            Toast.makeText(MainActivity.this, "You have choosen" + " "+itemDialog[which], Toast.LENGTH_LONG).show();
                            alertBuilder.setPositiveButton(Constant.OK, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    studentArrayList.remove(position);
                                    Toast.makeText(MainActivity.this, "You have successfully deleted the record  ", Toast.LENGTH_LONG).show();
                                    if (studentArrayList.size() == 0) {
                                        mEmptyLayout.setVisibility(View.VISIBLE);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                            alertBuilder.setNegativeButton(Constant.NO, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface1, int which) {
                                    dialogInterface1.cancel();
                                    dialogInterface1.dismiss();
                                }
                            });
                            AlertDialog mDialog = alertBuilder.create();
                           mDialog.show();
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

                     recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                 }
                 else
                 {
                     recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));



                 }


             }
         });
         return true;


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mAdapter.getItemCount() == -1) {
            mEmptyLayout.setVisibility(View.VISIBLE);
        } else {
            mEmptyLayout.setVisibility(View.GONE);
        }
        // check if the request code is same as what is passed  here it is
        if (requestCode == 1 && resultCode==RESULT_OK) {
            String name = data.getStringExtra(Constant.NAME);
            String rollno = data.getStringExtra(Constant.ROLLNO);
            Student std=new Student(name,rollno);
            studentArrayList.add(std);
            mAdapter.notifyDataSetChanged();
            Log.i("NAME", "Name:" + name);
            Log.i("RollNo", "RollNo:" + rollno);

        }
        if(requestCode==2 && resultCode==RESULT_OK)
        {
            String nameupdate=data.getStringExtra(Constant.UPDATE_NAME);
            String rollupadte=data.getStringExtra(Constant.UPDATE_ROLLNO);
            Student infoupdate=new Student(nameupdate,rollupadte);
            studentArrayList.remove(position1);
            studentArrayList.add(position1,infoupdate);
            mAdapter.notifyItemChanged(position1);
        }
    }
}