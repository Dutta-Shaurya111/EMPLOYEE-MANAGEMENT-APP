package com.example.employeemanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class EmployeeOperations {
    public static final String LOGTAG = "EMP_MNGMNT_SYS";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase database;

    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;
    private static final String[] allColumns = {
            EmployeeDBHandler.COLUMN_ID,
            EmployeeDBHandler.KEY_IMAGE,
            EmployeeDBHandler.COLUMN_FIRST_NAME,
            EmployeeDBHandler.COLUMN_LAST_NAME,
            EmployeeDBHandler.COLUMN_GENDER,
            EmployeeDBHandler.COLUMN_HIRE_DATE,
            EmployeeDBHandler.COLUMN_DEPT

    };

    public EmployeeOperations(Context context){
        dbhandler = new EmployeeDBHandler(context);
    }

    public void open(){
        Log.i(LOGTAG,"Database Opened");
        database = dbhandler.getWritableDatabase();


    }
    public void close(){
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();

    }
    public Employee addEmployee(Employee employee){

        Bitmap imageToStoreBitmap = employee.getImage();
        byteArrayOutputStream = new ByteArrayOutputStream();
        imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        imageInBytes = byteArrayOutputStream.toByteArray();
        // image is in form of byte array

        ContentValues values  = new ContentValues();

        values.put(EmployeeDBHandler.KEY_IMAGE,imageInBytes);
        values.put(EmployeeDBHandler.COLUMN_FIRST_NAME,employee.getFirstname());
        values.put(EmployeeDBHandler.COLUMN_LAST_NAME,employee.getLastname());
        values.put(EmployeeDBHandler.COLUMN_GENDER, employee.getGender());
        values.put(EmployeeDBHandler.COLUMN_HIRE_DATE, employee.getHiredate());
        values.put(EmployeeDBHandler.COLUMN_DEPT, employee.getDept());
        long insertid = database.insert(EmployeeDBHandler.TABLE_EMPLOYEES,null,values);
        employee.setEmpId(insertid);
        return employee;

    }

    // Getting single Employee
    public Employee getEmployee(long id) {

        Cursor cursor = database.query(EmployeeDBHandler.TABLE_EMPLOYEES, allColumns, EmployeeDBHandler.COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor.getCount() == 0) {
            //  Toast.makeText(EmployeeOperations.this,"There is No Id To Display",Toast.LENGTH_SHORT).show();
            return null;
        } else if (cursor.getCount()!=0) {

            cursor.moveToFirst();
        }

            byte[] imageBytes = cursor.getBlob(1);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, null);

            Employee e = new Employee(Long.parseLong(cursor.getString(0)), bitmap, cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
            // return Employee
            return e;
        }

    // Getting single Employee
    public Employee getEmployeeByDept(String dept) {

        Cursor cursor = database.query(EmployeeDBHandler.TABLE_EMPLOYEES, allColumns, EmployeeDBHandler.COLUMN_DEPT + "=?", new String[]{dept}, null, null, null, null);
        if (cursor.getCount() == 0) {
            //  Toast.makeText(EmployeeOperations.this,"There is No Id To Display",Toast.LENGTH_SHORT).show();
            return null;
        } else if (cursor.getCount()!=0) {

            cursor.moveToFirst();
        }

        byte[] imageBytes = cursor.getBlob(1);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length, null);

        Employee e = new Employee(Long.parseLong(cursor.getString(0)), bitmap, cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        // return Employee
        return e;
    }


    public List<Employee> getAllEmployees() {

        Cursor cursor = database.query(EmployeeDBHandler.TABLE_EMPLOYEES,allColumns,null,null,null, null, null);

        List<Employee> employees = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Employee employee = new Employee();
                byte [] byteimage = cursor.getBlob(cursor.getColumnIndex(EmployeeDBHandler.KEY_IMAGE));
                Bitmap bitmap =BitmapFactory.decodeByteArray(byteimage,0,byteimage.length,null);
                //employee.setFirstname(cursor.getString(1));
                employee.setEmpId(cursor.getLong(cursor.getColumnIndex(EmployeeDBHandler.COLUMN_ID)));
                employee.setImage(bitmap);
                employee.setFirstname(cursor.getString(cursor.getColumnIndex(EmployeeDBHandler.COLUMN_FIRST_NAME)));
                employee.setLastname(cursor.getString(cursor.getColumnIndex(EmployeeDBHandler.COLUMN_LAST_NAME)));
                employee.setGender(cursor.getString(cursor.getColumnIndex(EmployeeDBHandler.COLUMN_GENDER)));
                employee.setHiredate(cursor.getString(cursor.getColumnIndex(EmployeeDBHandler.COLUMN_HIRE_DATE)));
                employee.setDept(cursor.getString(cursor.getColumnIndex(EmployeeDBHandler.COLUMN_DEPT)));
                employees.add(employee);
            }
        }
        // return All Employees
        return employees;
    }




    // Updating Employee
    public int updateEmployee(Employee employee) {

        ContentValues values = new ContentValues();

        Bitmap bitmap = employee.getImage();
        ByteArrayOutputStream Bytestream=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, Bytestream);
        byte [] b=Bytestream.toByteArray();
       // String temp= Base64.encodeToString(b, Base64.DEFAULT);

        values.put(EmployeeDBHandler.KEY_IMAGE ,b);
        values.put(EmployeeDBHandler.COLUMN_FIRST_NAME, employee.getFirstname());
        values.put(EmployeeDBHandler.COLUMN_LAST_NAME, employee.getLastname());
        values.put(EmployeeDBHandler.COLUMN_GENDER, employee.getGender());
        values.put(EmployeeDBHandler.COLUMN_HIRE_DATE, employee.getHiredate());
        values.put(EmployeeDBHandler.COLUMN_DEPT, employee.getDept());

        // updating row
        return database.update(EmployeeDBHandler.TABLE_EMPLOYEES, values,
                EmployeeDBHandler.COLUMN_ID + "=?",new String[] { String.valueOf(employee.getEmpId())});
    }

    // Deleting Employee
    public void removeEmployee(Employee employee) {
       /* if (employee == null){

            return;
        }  */
        database.delete(EmployeeDBHandler.TABLE_EMPLOYEES, EmployeeDBHandler.COLUMN_ID + "=" + employee.getEmpId(), null);
    }



}