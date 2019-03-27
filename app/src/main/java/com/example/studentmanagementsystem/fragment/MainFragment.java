package com.example.studentmanagementsystem.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.activity.AddStudentActivity;
import com.example.studentmanagementsystem.adapter.StudentAdapter;
import com.example.studentmanagementsystem.comparator.SortName;
import com.example.studentmanagementsystem.comparator.SortRoll;
import com.example.studentmanagementsystem.constant.Constant;
import com.example.studentmanagementsystem.database.StudentDBHelper;
import com.example.studentmanagementsystem.listener.ItemClickListener;
import com.example.studentmanagementsystem.asynctask.BackgroundTask;
import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.util.Communication;

import java.util.ArrayList;
import java.util.Collections;

public class MainFragment extends Fragment {

    private Communication mListener;
    private View view;
    private static final int VIEW = 0;
    private static final int EDIT = 1;
    private static final int DELETE = 2;
    private Button mButtonAddStudent;
    private RecyclerView mRecyclerView;
    private StudentAdapter mAdapter;
    private Student student;
    private TextView mTextViewNoStudent;
    private Intent mIntentAddStudentDetail;
    private int position1;
    private static final String itemDialog[] = {Constant.VIEW, Constant.EDIT, Constant.DELETE};
    private ArrayList<Student> studentArrayList = new ArrayList<>();
    private StudentDBHelper dbHelper;
    private BackgroundTask backgroundTask;
    private ViewPager viewPager;
    private Context mContext;

    public MainFragment() {
        //empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new StudentDBHelper(getContext());
        studentArrayList = dbHelper.getStudent();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_fragment, container, false);

        //For initializing values and set up Adapter
        init();


        //set Onclick on Add btn

        setHasOptionsMenu(true);
        mButtonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.STUDENT_DATA_List, studentArrayList);
                bundle.putString(Constant.MODE, Constant.ADD);
                mListener.communication(bundle);
            }
        });
        return view;
    }

    private void init() {
        mTextViewNoStudent = (TextView)view.findViewById(R.id.tv_no_student);
        mButtonAddStudent = view.findViewById(R.id.btn_addStudent);
        mRecyclerView = view.findViewById(R.id.rv_recyclerView);

        mAdapter = new StudentAdapter(studentArrayList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        if(studentArrayList.size()==0){
            mTextViewNoStudent.setVisibility(View.VISIBLE);
        }
        else{
            mTextViewNoStudent.setVisibility(View.GONE);
        }
        recyclerViewHandler();

    }

    private void recyclerViewHandler()
    {

        mRecyclerView.addOnItemTouchListener(new ItemClickListener(mContext, mRecyclerView, new ItemClickListener.ClickListener() {
            @Override
            public void onClick (View view,final int position){
                student = studentArrayList.get(position);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setTitle(R.string.constant_operations);
                mBuilder.setItems(itemDialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case VIEW:
                                position1=position;
                                sendAnotherActivity();
                                break;
                            case EDIT:
                                Bundle bundle=new Bundle();
                                bundle.putString(Constant.MODE,Constant.EDIT);
                                bundle.putSerializable(Constant.STUDENT_DATA,studentArrayList.get(position));
                                position1=position;
                                mListener.communication(bundle);
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

    /*
    method-delete to delete the student record from the student arraylist
    @param which-for selecting the mode
    @param position-student record at a particular position is deleted
    dialog box appears when you choose the delete operation prompting that you have to delete this record
     */
    private void delete(int which, final int position)
    {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle(R.string.constant_deletedsuccesfully);
        Toast.makeText(getContext(), getString(R.string.constant_choose) + " "+itemDialog[which], Toast.LENGTH_LONG).show();
        alertBuilder.setPositiveButton(Constant.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbHelper.deleteStudent(studentArrayList.get(position).getRollNo());
                studentArrayList.remove(position);
                Toast.makeText(getContext(), getString(R.string.constant_deleted), Toast.LENGTH_LONG).show();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        if (context instanceof Communication) {
            mListener = (Communication) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener=null;
    }



    //method-view to view the existing details of the student
    //@param-parameter which to select which mode it is
    private void sendAnotherActivity(){
        Intent intent=new Intent(mContext,AddStudentActivity.class);
        intent.putExtra(Constant.STUDENT_DATA,studentArrayList.get(position1));
        intent.putExtra(Constant.MODE,Constant.VIEW);
        mContext.startActivity(intent);
    }

    /*
    method-onCreateOptionsMenu
    @param menu-a menu will be displayed on top of screen
    toggle switch-to switch between the grid and linear layout
    dropdown to select options to sort on basis of roll no or name

     */
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {

        inflater.inflate(R.menu.menu_item, menu);
        MenuItem item=menu.findItem(R.id.switch1);
        MenuItem sortName=menu.findItem(R.id.item_sortbyname);
        MenuItem sortRoll=menu.findItem(R.id.item_sortbyrollno);

        sortName.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(studentArrayList, (new SortName().comparator));
                mAdapter.notifyDataSetChanged();
                Toast.makeText(mContext,Constant.MENU,Toast.LENGTH_LONG).show();
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
                    mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,2));
                }
                else
                {
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                }
            }
        });

    }

    public void update(Bundle bundleFrom2Fragment){

        switch(bundleFrom2Fragment.getString(Constant.MODE)){
            case Constant.ADD:


                //Add Object in Student ArrayList at possition
                int possitionInsertStudent = 0;
                //get data from another Activity

                Student sudStudent = (Student) bundleFrom2Fragment.getSerializable(Constant.STUDENT_DATA);
                //mStudentDatabaseHelper.addData(sudStudent.getmId(),sudStudent.getmFirstName()+" "+sudStudent.getmLastName());
                studentArrayList.add(possitionInsertStudent, sudStudent);
                // mStudentDatabaseHelper.addData(sudStudent.getmId(),sudStudent.getmFirstName()+" "+sudStudent.getmLastName());

                //As size of ArrayList>0 setting visibility of back textView to gone
                if (mTextViewNoStudent.getVisibility() == View.VISIBLE) mTextViewNoStudent.setVisibility(View.GONE);
                mAdapter.notifyItemInserted(possitionInsertStudent);
                Toast.makeText(mContext,Constant.ADD_TOAST,Toast.LENGTH_SHORT).show();

                break;
            case Constant.EDIT:

                String fName=bundleFrom2Fragment.getString(Constant.NAME);
                Student suStudent=studentArrayList.get(position1);
                suStudent.setmName(fName);
                mAdapter.notifyItemChanged(position1);
                Toast.makeText(mContext,Constant.UPDATE_TOAST,Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
