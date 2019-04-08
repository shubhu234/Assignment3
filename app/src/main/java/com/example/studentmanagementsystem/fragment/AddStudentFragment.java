package com.example.studentmanagementsystem.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.example.studentmanagementsystem.R.string.message_received;

public class AddStudentFragment extends Fragment implements BackgroundSetData.SendData {

    private static final int REQUEST_CODE_EDIT =0 ;
    private static final int REQUEST_CODE_ADD =1 ;
    private Button btnSaveDetails;
    private EditText etName;
    private EditText etRollno;
    private TextView tvAddStudent;
    private int selectButtonOperation;
    private ArrayList<Student> list=new ArrayList<Student>();
    private static final String itemDialog[]={Constant.AYNC_TASK,Constant.SERVICE,Constant.INTENT_SERVICE};
    public static final int ASYNC_TASK=0;
    public static final int SERVICE=1;
    public static final int INTENT_SERVICE=2;
    private View view;
    private StudentDBHelper dbHelper;
    private Context mContext;
    private Bundle sendFragmentBundle;
    private Bundle bundle;
    public boolean error;
    private Communication mListener;
    private String typeAction;
    private Student editStudentDetail;
    private BackgroundSetData.SendData mFragmentContext;
    private StudentBroadcastReceiver mStudentBroadcastReceiver = new StudentBroadcastReceiver();

    public AddStudentFragment(){
        //empty constructor
    }

    //to register broadcast receiver
    @Override
   public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(Constant.ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mStudentBroadcastReceiver,intentFilter);
    }
    //to unregister broadcast receiver
    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mStudentBroadcastReceiver);

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
            viewMode(bundle);
        }

        //set click listener to button
        btnSaveDetails.setOnClickListener(new View.OnClickListener() {
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
        if(etRollno.getText().toString().equals(""))
        {
            Toast.makeText(mContext,"No student added",Toast.LENGTH_LONG).show();
            clearEditText();
            mListener.changeFragment();
        }else {
            String name = etName.getText().toString().trim();
            if (!Validate.isValidateName(name)) {
                etName.setError("Enter Valid name");
                error = false;
            }

            if (error) {
                Bundle editBundle = new Bundle();
                editBundle.putString(Constant.NAME, name);
                editBundle.putString(Constant.ROLLNO, etRollno.getText().toString());
                generateDialogBox(etName.getText().toString(), etRollno.getText().toString(), Constant.EDIT, editBundle);
            }
        }
    }

    private void addButton()
    {
        String name = etName.getText().toString().trim();
        String rollNo = etRollno.getText().toString().trim();

         error=true;
        //various validation methods to validate the name,roll no and unique roll no
        if (!Validate.isValidateName(name)) {
            etName.setError(getString(R.string.constant_validname));
            error=false;
        }
        if (!Validate.isValidateRollNo(rollNo)) {
            etRollno.setError(getString(R.string.constant_validrollno));
            error=false;
        }
        if (!Validate.isUniqueNo(list,rollNo)) {
            etRollno.setError(getString(R.string.constant_uniquerollno));
            error=false;
        }

        if (error) {
            Student student = new Student(name.toUpperCase(),rollNo);
            Bundle addBundle=new Bundle();
            addBundle.putSerializable(Constant.STUDENT_DATA,student);
            generateDialogBox(etName.getText().toString(),etRollno.getText().toString(),Constant.ADD,addBundle);
        }
    }

    private void init() {
        btnSaveDetails=(Button)view.findViewById(R.id.btn_save);
        etName=(EditText)view.findViewById(R.id.et_name);
        etRollno=(EditText) view.findViewById(R.id.et_rollno);
    }
    /*private void setTextOfEditText(Student student){
        etName.setText(student.getmName().toUpperCase());
       etRollno.setText(student.getRollNo().toUpperCase());
    }*/

    private void generateDialogBox(final String name, final String rollNo, final String typeOfOperation,final Bundle sendBundle){
        sendBundle.putString(Constant.MODE,typeOfOperation);
        sendFragmentBundle=sendBundle;
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
                        (new BackgroundSetData(mContext,(BackgroundSetData.SendData)AddStudentFragment.this)).execute(typeOfOperation,name,rollNo);
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
        clearEditText();
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

    public void viewMode(Bundle bundle)
    {
        etName.setText(bundle.getString(Constant.VIEW_NAME));
        etRollno.setText(bundle.getString(Constant.VIEW_ROLL));
        etRollno.setEnabled(false);
        etName.setEnabled(false);
        btnSaveDetails.setVisibility(View.GONE);
    }

    public void update(Bundle bundleNew){
        bundle=bundleNew;
        typeAction=bundle.getString(Constant.MODE);
        switch (typeAction){
            case Constant.ADD:
                selectButtonOperation=REQUEST_CODE_ADD;
                list=(ArrayList<Student>) bundle.getSerializable(Constant.STUDENT_DATA_List);
                btnSaveDetails.setText(Constant.ADDING);
                etRollno.setEnabled(true);
                break;
            case Constant.EDIT:
                selectButtonOperation=REQUEST_CODE_EDIT;
                btnSaveDetails.setText(Constant.BTN_CHANGE_TEXT_UPDATE);
                editStudentDetail=(Student) bundle.getSerializable(Constant.STUDENT_DATA);
                etName.setText(editStudentDetail.getmName().toUpperCase());
                etRollno.setText(editStudentDetail.getRollNo().toUpperCase());
                etRollno.setEnabled(false);
                break;
            default:
                break;
        }
    }
    private void clearEditText()
    {
        etName.setText("");
        etRollno.setText("");
    }
    public void callBack(String str) {
        Toast.makeText(mContext,str,Toast.LENGTH_LONG).show();
        mListener.communication(sendFragmentBundle);
        clearEditText();
    }
    //Inner broadcast receiver that receives the broadcast if the services have indeed added the elements in the database.
    public class StudentBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(mContext,intent.getStringExtra(getString(message_received)),Toast.LENGTH_LONG).show();
            if(bundle!=null)
            {
                mListener.communication(sendFragmentBundle);
            }
        }
    }
}
