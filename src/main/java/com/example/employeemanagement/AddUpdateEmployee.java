package com.example.employeemanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddUpdateEmployee extends AppCompatActivity implements DatePickerFragment.DateDialogListener{
    private static final String EXTRA_EMP_ID = "com.androidtutorialpoint.empId";
    private static final String EXTRA_ADD_UPDATE = "com.androidtutorialpoint.add_update";
    private static final String DIALOG_DATE = "DialogDate";
    private ImageView calendarImage;
    private CircleImageView circleImage_profile;
    private RadioGroup radioGroup;
    private RadioButton maleRadioButton,femaleRadioButton,checkradio;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText deptEditText;
    private EditText hireDateEditText;
    private Button addUpdateButton;
    private Employee newEmployee;
    private Employee oldEmployee;
    private String mode;
    private long empId;
    private EmployeeOperations employeeData;

    private Uri imageFilePath;
    private Bitmap imageToStore;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_employee);
        newEmployee = new Employee();
        oldEmployee = new Employee();
        circleImage_profile = (CircleImageView)findViewById(R.id.circular_image);
        firstNameEditText = (EditText)findViewById(R.id.edit_text_first_name);
        lastNameEditText = (EditText)findViewById(R.id.edit_text_last_name);
        hireDateEditText = (EditText) findViewById(R.id.edit_text_hire_date);
        radioGroup = (RadioGroup) findViewById(R.id.radio_gender);
        maleRadioButton = (RadioButton) findViewById(R.id.radio_male);
        femaleRadioButton = (RadioButton) findViewById(R.id.radio_female);
        calendarImage = (ImageView)findViewById(R.id.image_view_hire_date);
        deptEditText = (EditText)findViewById(R.id.edit_text_dept);
        addUpdateButton = (Button)findViewById(R.id.button_add_update_employee);

        employeeData = new EmployeeOperations(this);
        employeeData.open();

        mode = getIntent().getStringExtra(EXTRA_ADD_UPDATE);
        if(mode.equals("Update")){

            addUpdateButton.setText("Update Employee");
            empId = getIntent().getLongExtra(EXTRA_EMP_ID,0);

            initializeEmployee(empId);

        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.radio_male) {
                    newEmployee.setGender("M");
                    if(mode.equals("Update")){
                        oldEmployee.setGender("M");
                    }
                } else if (checkedId == R.id.radio_female) {
                    newEmployee.setGender("F");
                    if(mode.equals("Update")){
                        oldEmployee.setGender("F");
                    }

                }
            }

        });

        calendarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getSupportFragmentManager();
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.show(manager, DIALOG_DATE);
            }
        });

        circleImage_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage(view);
            }
        });



        addUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Employee employee = new Employee();
                Bitmap image =imageToStore;
// convert bitmap to byte
                /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte imageInByte[] = stream.toByteArray(); */

                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String hireDate = hireDateEditText.getText().toString();
                String dept = deptEditText.getText().toString();
            if (checkifValid(firstName) && checkifValid(lastName )
            && checkifValid(dept) && checkifValid(hireDate) &&
                    circleImage_profile.getDrawable()!=null && image!=null
                    && (maleRadioButton!=null || femaleRadioButton!=null))
            {
                if(mode.equals("Add")) {
                    newEmployee.setImage(image);
                    newEmployee.setFirstname(firstNameEditText.getText().toString());
                    newEmployee.setLastname(lastNameEditText.getText().toString());
                    newEmployee.setHiredate(hireDateEditText.getText().toString());
                    newEmployee.setDept(deptEditText.getText().toString());

                    //newEmployee.setGender();
                    employeeData.addEmployee(newEmployee);
                    Toast t = Toast.makeText(AddUpdateEmployee.this, "Employee "+ newEmployee.getFirstname() + " has been added successfully !", Toast.LENGTH_SHORT);
                    t.show();
                    Intent i = new Intent(AddUpdateEmployee.this,MainActivity.class);
                    startActivity(i);
                }else {
                  /*  Bitmap bitmap =BitmapFactory.decodeResource
                            (getApplicationContext().getResources(),circleImage_profile.getId());
                    */
                  Bitmap bitmap = imageToStore;
                oldEmployee.setImage(bitmap);
                    oldEmployee.setFirstname(firstNameEditText.getText().toString());
                    oldEmployee.setLastname(lastNameEditText.getText().toString());
                    oldEmployee.setHiredate(hireDateEditText.getText().toString());
                    oldEmployee.setDept(deptEditText.getText().toString());
                    oldEmployee.setGender(checkradio.getText().subSequence(0,1).toString());
                    employeeData.updateEmployee(oldEmployee);
                    Toast t = Toast.makeText(AddUpdateEmployee.this, "Employee "+ oldEmployee.getFirstname() + " has been updated successfully !", Toast.LENGTH_SHORT);
                    t.show();
                    Intent i = new Intent(AddUpdateEmployee.this,MainActivity.class);
                    startActivity(i);

                }


            }
            else
                Toast.makeText(AddUpdateEmployee.this,"Please Fill All The Fields",Toast.LENGTH_SHORT).show();
        }});



    }

    public void chooseImage(View view){
    try {
        Intent objectintent = new Intent();
        objectintent.setType("image/*");

        objectintent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(objectintent,PICK_IMAGE);
    }
    catch (Exception e){
        Toast.makeText(AddUpdateEmployee.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
    }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                    && data!=null &&data.getData()!=null){
 imageFilePath = data.getData();
 imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(),imageFilePath);
 circleImage_profile.setImageBitmap(imageToStore);
            }
        }
        catch (Exception e){
            Toast.makeText(AddUpdateEmployee.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeEmployee(long empId) {
        oldEmployee = employeeData.getEmployee(empId);
        if(oldEmployee == null){
            Toast.makeText(AddUpdateEmployee.this,"There is No Id To Display",Toast.LENGTH_SHORT).show();
            return;
        }
        circleImage_profile.setImageBitmap(oldEmployee.getImage());
        firstNameEditText.setText(oldEmployee.getFirstname());
        lastNameEditText.setText(oldEmployee.getLastname());
        hireDateEditText.setText(oldEmployee.getHiredate());
       if(oldEmployee.getGender().equals("M")){
            checkradio=maleRadioButton;
        }
        else {
            checkradio = femaleRadioButton;
        }

        radioGroup.check(checkradio.getId());
                //oldEmployee.getGender().equals("M") ? R.id.radio_male : R.id.radio_female);
        deptEditText.setText(oldEmployee.getDept());
    }


    @Override
    public void onFinishDialog(Date date) {
        hireDateEditText.setText(formatDate(date));

    }

    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String hireDate = sdf.format(date);
        return hireDate;
    }

    private boolean checkifValid(String string)
    {
    if (string.isEmpty()) {
        //editText.setError("Required Field");
        return false;
    }
        else
            return true;
    }
}