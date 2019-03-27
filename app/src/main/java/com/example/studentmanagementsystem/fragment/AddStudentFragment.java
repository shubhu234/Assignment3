package com.example.studentmanagementsystem.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.studentmanagementsystem.R;
import com.example.studentmanagementsystem.constant.Constant;
import com.example.studentmanagementsystem.database.StudentDBHelper;
import com.example.studentmanagementsystem.service.BackgroundIntentService;
import com.example.studentmanagementsystem.service.BackgroundService;
import com.example.studentmanagementsystem.asynctask.BackgroundSetData;
import com.example.studentmanagementsystem.model.Student;
import com.example.studentmanagementsystem.util.Communication;
import com.example.studentmanagementsystem.validation.Validate;

import java.util.ArrayList;

public class AddStudentFragment extends Fragment {

    private static final int REQUEST_CODE_EDIT =0 ;
    private static final int REQUEST_CODE_ADD =1 ;
    private Button mButtonSaveDetails;
    private EditText mEditTextName;
    private EditText mEditTextRollno;
    private TextView mTextviewAddStudent;
    private Intent mMainActivityDetail;
    private int selectButtonOperation;
    private ArrayList<Student> list=new ArrayList<Student>();
    private static final String itemDialog[]={Constant.AYNC_TASK,Constant.SERVICE,Constant.INTENT_SERVICE};
    public static final int ASYNC_TASK=0;
    public static final int SERVICE=1;
    public static final int INTENT_SERVICE=2;
    private View view;
    private StudentDBHelper dbHelper;
    private Context mContext;
    private Bundle bundle;
    public boolean error;
    private Communication mListener;
    private String typeAction;
    private Student editStudentDetail;

    public AddStudentFragment(){
        //empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.activity_add_student_fragment, container, false);
        dbHelper=new StudentDBHelper(mContext);
        bundle=new Bundle();
        bundle.putString(Constant.MODE,Constant.ADD);

        init();
        if(getArguments()!=null){
            bundle=getArguments();
            Log.d("check", "onCreateView: ");
            update(bundle);
        }

        //set click listener to button
        mButtonSaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error=true;
                switch (selectButtonOperation){
                    case REQUEST_CODE_EDIT:
                        editButton();
                        break;
                    case REQUEST_CODE_ADD:
                        addButton();
                        break;
                }
            }
        });
        return view;
    }

    private void editButton() {
        String name = mEditTextName.getText().toString().trim();
        if (!Validate.isValidateName(name)) {
            mEditTextName.setError("Enter Valid name");
            error=false;
        }

        if (error) {
            Bundle editBundle=new Bundle();
            editBundle.putString(Constant.NAME,name);
            editBundle.putString(Constant.ROLLNO,mEditTextRollno.getText().toString());
            generateDialogBox(mEditTextName.getText().toString(),mEditTextRollno.getText().toString(),Constant.EDIT,editBundle);
        }
    }

    private void addButton()
    {
        String name = mEditTextName.getText().toString().trim();
        String rollNo = mEditTextRollno.getText().toString().trim();

         error=true;
        //various validation methods to validate the name,roll no and unique roll no
        if (!Validate.isValidateName(name)) {
            mEditTextName.setError("Enter Valid name");
            error=false;
        }
        if (!Validate.isValidateRollNo(rollNo)) {
            mEditTextRollno.setError("Enter valid rollno");
            error=false;
        }
        if (!Validate.isUniqueNo(list,rollNo)) {
            mEditTextRollno.setError("Enter unique roll no");
            error=false;
        }

        if (error) {
            Student student = new Student(name.toUpperCase(),rollNo);
            Bundle addBundle=new Bundle();
            addBundle.putSerializable(Constant.STUDENT_DATA,student);
            generateDialogBox(mEditTextName.getText().toString(),mEditTextRollno.getText().toString(),Constant.ADD,addBundle);
        }
    }

    private void init() {
        mButtonSaveDetails=(Button)view.findViewById(R.id.btn_save);
        mEditTextName=(EditText)view.findViewById(R.id.et_name);
        mEditTextRollno=(EditText) view.findViewById(R.id.et_rollno);

    }
    private void setTextOfEditText(Student student){
        mEditTextName.setText(student.getmName().toUpperCase());
        mEditTextRollno.setText(student.getRollNo().toUpperCase());
    }

    private void generateDialogBox(final String name, final String rollNo, final String typeOfOperation,final Bundle sendBundle){

        final AlertDialog.Builder mBuilder=new AlertDialog.Builder(mContext);
        if(typeOfOperation==Constant.EDIT)
            mBuilder.setTitle(R.string.updated);
        else
            mBuilder.setTitle(R.string.added_by);


        mBuilder.setSingleChoiceItems(itemDialog, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                switch (which){
                    case ASYNC_TASK:
                        (new BackgroundSetData(mContext)).execute(typeOfOperation,name,rollNo);
                        break;
                    case SERVICE:
                        Intent service=new Intent(mContext, BackgroundService.class);
                        startServiceWork(service,name,rollNo,typeOfOperation);
                        break;
                    case INTENT_SERVICE:
                        Intent intentForService=new Intent(mContext, BackgroundIntentService.class);
                        startServiceWork(intentForService,name,rollNo,typeOfOperation);
                        break;
                }
                sendBundle.putString(Constant.MODE,typeOfOperation);
                mListener.communication(sendBundle);

            }
        });
        mBuilder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog mDialog=mBuilder.create();
        mDialog.show();

    }

    private void startServiceWork(Intent service, String name, String rollNo, String typeOfOperation) {
        service.putExtra(Constant.MODE,typeOfOperation);
        service.putExtra(Constant.ROLLNO,rollNo);
        service.putExtra(Constant.NAME,name);
        mContext.startService(service);
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

    public void update(Bundle bundleNew){
        bundle=bundleNew;
        typeAction=bundle.getString(Constant.MODE);
        Log.d("view", "update:message ");
        switch (typeAction){
            case Constant.ADD:
                selectButtonOperation=REQUEST_CODE_ADD;
                list=(ArrayList<Student>) bundle.getSerializable(Constant.STUDENT_DATA_List);
                mButtonSaveDetails.setText("ADD");
                mEditTextRollno.setEnabled(true);
                break;
            case Constant.EDIT:
                selectButtonOperation=REQUEST_CODE_EDIT;
                mButtonSaveDetails.setText(Constant.BTN_CHANGE_TEXT_UPDATE);
                editStudentDetail=(Student) bundle.getSerializable(Constant.STUDENT_DATA);
                mEditTextName.setText(editStudentDetail.getmName().toUpperCase());
                mEditTextRollno.setText(editStudentDetail.getRollNo().toUpperCase());
                mEditTextRollno.setEnabled(false);
                //mButtonAdd.setVisibility(View.VISIBLE);
                break;
            case Constant.VIEW:
                editStudentDetail=(Student) bundle.getSerializable(Constant.STUDENT_DATA);
                mEditTextName.setText(editStudentDetail.getmName().toUpperCase());
                mEditTextRollno.setText(editStudentDetail.getRollNo().toUpperCase());
                mEditTextRollno.setEnabled(false);
                mEditTextName.setEnabled(false);
                mButtonSaveDetails.setVisibility(View.GONE);
                break;
        }
    }


}
